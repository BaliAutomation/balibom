package ac.bali.bom.products;

import ac.bali.bom.parts.PartsService;
import org.qi4j.library.javafx.support.Action;
import java.io.File;
import org.apache.polygene.api.concern.Concerns;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.mixin.Mixins;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkConcern;
import org.apache.polygene.api.unitofwork.concern.UnitOfWorkPropagation;

@SuppressWarnings("unused")
@Mixins(ProductsService.Mixin.class)
@Concerns(UnitOfWorkConcern.class)
public interface ProductsService
{
    @UnitOfWorkPropagation(usecase = "Import BOM")
    @Action(label="Import BOM...")
    Product importBom(File bomFile) throws Exception;

    @SuppressWarnings("resource")
    class Mixin
        implements ProductsService
    {
        @Structure
        private UnitOfWorkFactory uowf;

        @Service
        private PartsService partsService;

        @Service
        private BomReader bomReader;

        @Override
        public Product importBom(File bomFile) throws Exception
        {
            String product = parseNameFromFile(bomFile);
            String revision = parseRevisionFromFile(bomFile);
            Bom bom = bomReader.load(product, revision, bomFile);
            UnitOfWork uow = uowf.currentUnitOfWork();
            Identity identity = StringIdentity.identityOf("product_" + product + "_" + revision);
            EntityBuilder<Product> builder = uow.newEntityBuilder(Product.class, identity);
            Product instance = builder.instance();
            instance.bomFile().set(bomFile.getAbsolutePath());
            instance.name().set(product);
            instance.revision().set(revision);
            instance.bom().set(bom);
            return builder.newInstance();
        }

        static String parseNameFromFile(File bomFile)
        {
            String path = bomFile.getAbsolutePath();
            String[] parts = path.split(File.pathSeparator);
            return parts[parts.length - 3];
        }

        static String parseRevisionFromFile(File bomFile)
        {
            String path = bomFile.getAbsolutePath();
            int pos = path.indexOf("Rev");
            if (pos < 0)
                throw new IllegalArgumentException("File structure is not followed.");
            pos = pos + 3;  // skip the "Rev" part
            //noinspection StatementWithEmptyBody
            while (!Character.isLetterOrDigit(path.charAt(pos++)))  // Skip any non alphanum such as dashes.
            {
            }
            int start = pos - 1;
            if (Character.isLetter(path.charAt(start)))
            {
                while (pos < path.length() - 1 && Character.isLetterOrDigit(path.charAt(pos)))  // take all characters as revision id
                {
                    pos++;
                }
            } else
            {
                while (pos < path.length() - 1 && (Character.isDigit(path.charAt(pos)) || path.charAt(pos) == '.'))  // take all characters as revision id
                {
                    pos++;
                }
            }
            String rev;
            if (pos == path.length())
                rev = path.substring(start);
            else
                rev = path.substring(start, pos);
            return rev;
        }
    }
}
