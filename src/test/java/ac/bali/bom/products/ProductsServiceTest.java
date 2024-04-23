package ac.bali.bom.products;

import ac.bali.bom.parts.PartsService;
import java.io.File;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.library.fileconfig.FileConfigurationService;
import org.apache.polygene.test.AbstractPolygeneTest;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProductsServiceTest extends AbstractPolygeneTest
{
    @Service
    ProductsService products;

    @Test
    public void testExtractName1()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC/colibri-3-RevC-bom.csv");
        assertThat( products.parseNameFromFile(f), equalTo("colibri-3") );
    }

    @Test
    public void testExtractName2()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/RevC/colibri-3-RevC-bom.csv");
        assertThat( products.parseNameFromFile(f), equalTo("colibri-3") );
    }

    @Test
    public void testExtractName3()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/RevC/bom.csv");
        assertThat( products.parseNameFromFile(f), equalTo("colibri-3") );
    }

    @Test
    public void testRevisionParsing1()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC/colibri-3-RevC-bom.csv");
        assertThat( products.parseRevisionFromFile(f), equalTo("C") );
    }

    @Test
    public void testRevisionParsing2()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC/colibri-3-bom.csv");
        String revision = products.parseRevisionFromFile(f);
        assertThat(revision, equalTo("C") );
    }

    @Test
    public void testRevisionParsing3()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-Rev1.2/colibri-3-RevC-bom.csv");
        assertThat( products.parseRevisionFromFile(f), equalTo("1.2") );
    }

    @Test
    public void testRevisionParsing4()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC");
        assertThat( products.parseRevisionFromFile(f), equalTo("C") );
    }

    @Test
    public void testRevisionParsing5()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-Rev1.2-bom.csv");
        String revision = products.parseRevisionFromFile(f);
        assertThat(revision, equalTo("1.2") );
    }

    @Override
    public void assemble(ModuleAssembly module) throws AssemblyException
    {
        module.services(ProductsService.class);
        module.services(PartsService.class);
        module.services(BomReader.class);
        module.services(FileConfigurationService.class);
    }
}
