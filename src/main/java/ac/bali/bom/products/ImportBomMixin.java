package ac.bali.bom.products;

import ac.bali.bom.parts.PartsService;
import java.io.File;
import org.apache.polygene.api.entity.EntityBuilder;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.identity.StringIdentity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.library.fileconfig.FileConfiguration;
import org.apache.polygene.library.fileconfig.FileConfigurationService;

@SuppressWarnings("resource")
public abstract class ImportBomMixin
    implements ProductsService
{
    @Structure
    private UnitOfWorkFactory uowf;

    @Service
    private PartsService partsService;

    @Service
    private BomReader bomReader;

    @Service
    FileConfigurationService fileConfigurationService;

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

    @Override
    public String parseNameFromFile(File bomFile)
    {
        String path = bomFile.getAbsolutePath();
        String pattern = "/";
        if (fileConfigurationService.os().equals(FileConfiguration.OS.windows))
            pattern = "\\\\";
        String[] parts = path.split(pattern);
        return parts[parts.length - 3];
    }

    @Override
    public String parseRevisionFromFile(File bomFile)
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
