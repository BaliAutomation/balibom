package ac.bali.bom.products;

import java.io.File;
import java.io.IOException;
import java.io.StreamTokenizer;
import java.io.StringReader;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;

@SuppressWarnings("unused")
@Mixins(BomReader.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface BomReader
{
    Bom load(String product, String revision, File bomFile) throws Exception;

    Bom load(String product, String revision, List<String> lines) throws InvalidBomException;

    abstract class Mixin
        implements BomReader
    {
        @Structure
        private ValueBuilderFactory vbf;

        public Bom load(String product, String revision, File bomFile)
            throws Exception
        {
            List<String> lines = Files.readAllLines(bomFile.toPath());
            return load(product, revision, lines);
        }

        public Bom load(String product, String revision, List<String> lines) throws InvalidBomException
        {
            Columns columns = new Columns();
            ValueBuilder<Bom> builder = vbf.newValueBuilder(Bom.class);
            Bom bomP = builder.prototype();
            bomP.product().set(product);
            bomP.revision().set(revision);
            findColumns(lines.get(0), columns);
            ValueBuilder<BomItem> builder2 = vbf.newValueBuilder(BomItem.class);
            BomItem bomItemP = builder2.prototype();
            List<BomItem> items = new ArrayList<>();
            for (int i = 1; i < lines.size(); i++)
            {
                String line = lines.get(i);
                if( line.contains("-- mixed values --"))
                    throw new InvalidBomException("BOM is invalid, containing '-- mixed values--' on line " + (i+1) + " : " + line );
                if (line != null && line.trim().length() > 0)
                {
                    try
                    {
                        String[] parts = parseLine(line);
                        validateParts(parts, columns);
                        String designator = parts[columns.designatorColumn];
                        int quantity;
                        if (columns.quantityColumn == -1)
                        {
                            quantity = countDesignators(parts[columns.designatorColumn]);
                        }
                        else
                        {
                            quantity = Integer.parseInt(parts[columns.quantityColumn]);
                        }
                        bomItemP.designator().set(designator);
                        bomItemP.quantity().set(quantity);
                        if( columns.valueColumn >= 0 )
                        {
                            String value = parts[columns.valueColumn].trim();
                            bomItemP.value().set(value);
                        }
                        if( columns.mfColumn >= 0 )
                        {
                            String mf = parts[columns.mfColumn].trim();
                            bomItemP.mf().set(mf);
                        }
                        if( columns.mpnColumn >= 0 )
                        {
                            String mpn = parts[columns.mpnColumn].trim();
                            bomItemP.mpn().set(mpn);
                        }
                        if( columns.footprintColumn >= 0)
                        {
                            String footprint = parts[columns.footprintColumn];
                            bomItemP.footprint().set(footprint);
                        }
                        Map<String,String> attributes = new HashMap<>();
                        for( Map.Entry<String, Integer> attr : columns.attributes.entrySet())
                        {
                            String attrName = attr.getKey();
                            int attrColumn = attr.getValue();
                            attributes.put(attrName, parts[attrColumn]);
                        }
                        bomItemP.attributes().set(attributes);
                        BomItem item = builder2.newInstance();
                        items.add(item);
                    } catch (Exception e)
                    {
                        bomP.errors().get().add(e.getMessage());
                    }
                }
            }
            bomP.items().set(items);
            return builder.newInstance();
        }

        private void validateParts(String[] parts, Columns columns)
        {
            int maxIndex = parts.length - 1;
            if (columns.designatorColumn > maxIndex)
                throw new IllegalArgumentException("'Designator' field is missing for BOM item: " + Arrays.toString(parts));
            if (columns.valueColumn > maxIndex)
                throw new IllegalArgumentException("'Value' field is missing for BOM item: " + Arrays.toString(parts));
            if (columns.footprintColumn > maxIndex)
                throw new IllegalArgumentException("'Footprint' field is missing for BOM item: " + Arrays.toString(parts));
            if (columns.mpnColumn > maxIndex)
                throw new IllegalArgumentException("'MPN' field is missing for BOM item: " + Arrays.toString(parts));
            if (columns.mfColumn > maxIndex)
                throw new IllegalArgumentException("MF field is missing for BOM item: " + Arrays.toString(parts));
            if (columns.quantityColumn > maxIndex)
                throw new IllegalArgumentException("'Qty' field is missing for BOM item: " + Arrays.toString(parts));
        }

        static Integer countDesignators(String designators)
        {
            return designators.split(",").length;
        }

        private String[] parseLine(String line)
        {
            List<String> words = new ArrayList<>();
            try
            {
                StreamTokenizer st = new StreamTokenizer(new StringReader(line));
                while (st.nextToken() != StreamTokenizer.TT_EOF)
                {
                    if (st.sval != null)
                    {
                        words.add(st.sval);
                    }
                }
            } catch (IOException e)
            {
                // can't happen
            }
            return words.toArray(new String[6]);
        }

        private void findColumns(String heading, Columns columns)
        {
            String[] parts = heading.split(",");
            int index = 0;
            for (String part : parts)
            {
                part = trim(part);
                if (part.equalsIgnoreCase("MF") || part.equalsIgnoreCase("Manufacturer"))
                {
                    columns.mfColumn = index;
                }
                else if (part.equalsIgnoreCase("MPN"))
                {
                    columns.mpnColumn = index;
                }
                else if (part.equalsIgnoreCase("Designator") || part.equalsIgnoreCase("Reference"))
                {
                    columns.designatorColumn = index;
                }
                else if ((part.equalsIgnoreCase("Comment") && columns.valueColumn == -1) || part.equalsIgnoreCase("Value"))
                {
                    columns.valueColumn = index;
                }
                else if (part.equalsIgnoreCase("Footprint"))
                {
                    columns.footprintColumn = index;
                }
                else if (part.equalsIgnoreCase("Qty") || part.equalsIgnoreCase("Quantity"))
                {
                    columns.quantityColumn = index;
                } else {
                    columns.attributes.put(part, index);
                }
                index++;
            }
        }

        private String trim(String part)
        {
            part = part.trim();
            if (part.startsWith("\"") || part.startsWith("'"))
                part = part.substring(1, part.length() - 1);
            return part;
        }

        private static class Columns
        {
            private int designatorColumn = -1;
            private int valueColumn = -1;
            private int footprintColumn = -1;
            private int mpnColumn = -1;
            private int mfColumn = -1;
            private int quantityColumn = -1;
            private final Map<String, Integer> attributes = new HashMap<>();
        }

    }
}
