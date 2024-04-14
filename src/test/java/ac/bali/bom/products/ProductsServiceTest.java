package ac.bali.bom.products;

import java.io.File;
import org.junit.Test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.core.IsEqual.equalTo;

public class ProductsServiceTest
{
    @Test
    public void testExtractName1()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC/colibri-3-RevC-bom.csv");
        assertThat( ProductsService.Mixin.parseNameFromFile(f), equalTo("colibri-3") );
    }

    @Test
    public void testExtractName2()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/RevC/colibri-3-RevC-bom.csv");
        assertThat( ProductsService.Mixin.parseNameFromFile(f), equalTo("colibri-3") );
    }

    @Test
    public void testExtractName3()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/RevC/bom.csv");
        assertThat( ProductsService.Mixin.parseNameFromFile(f), equalTo("colibri-3") );
    }

    @Test
    public void testRevisionParsing1()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC/colibri-3-RevC-bom.csv");
        assertThat( ProductsService.Mixin.parseRevisionFromFile(f), equalTo("C") );
    }

    @Test
    public void testRevisionParsing2()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC/colibri-3-bom.csv");
        String revision = ProductsService.Mixin.parseRevisionFromFile(f);
        assertThat(revision, equalTo("C") );
    }

    @Test
    public void testRevisionParsing3()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-Rev1.2/colibri-3-RevC-bom.csv");
        assertThat( ProductsService.Mixin.parseRevisionFromFile(f), equalTo("1.2") );
    }

    @Test
    public void testRevisionParsing4()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-RevC");
        assertThat( ProductsService.Mixin.parseRevisionFromFile(f), equalTo("C") );
    }

    @Test
    public void testRevisionParsing5()
    {
        File f = new File("/home/niclas/dev/bfm/hardware/colibri-3/colibri-3-Rev1.2-bom.csv");
        String revision = ProductsService.Mixin.parseRevisionFromFile(f);
        assertThat(revision, equalTo("1.2") );
    }

}
