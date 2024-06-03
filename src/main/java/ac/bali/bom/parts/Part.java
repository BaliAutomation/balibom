package ac.bali.bom.parts;

import ac.bali.bom.manufacturers.Manufacturer;
import ac.bali.bom.supply.Supply;
import ac.bali.bom.view.Deleter;
import org.apache.polygene.api.association.Association;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.Height;
import org.qi4j.library.crudui.Order;
import org.qi4j.library.crudui.RenderAsDescription;
import org.qi4j.library.crudui.RenderAsName;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
@HasListViewController({PartsService.class, Deleter.class})
public interface Part extends HasIdentity
{
    @RenderAsName
    @Order(4)
    Association<Manufacturer> manufacturer();

    @RenderAsName
    @Order(5)
    Property<String> mpn();

    @UseDefaults
    @RenderAsDescription
    @Order(8)
    Property<String> partIntro();

    @UseDefaults
    @Order(10)
    @Height(pref=150)
    Property<Set<String>> datasheetUrls();

    @UseDefaults
    @Order(15)
    @Height(pref=200)
    Property<Set<String>> imageUrls();

    @UseDefaults
    @Order(20)
    @Height(pref=300)
    ManyAssociation<Part> alternates();

    @UseDefaults("1")
    @Order(30)
    Property<Float> wastePercentage();

    @UseDefaults("5")
    @Order(40)
    Property<Integer> wastePieces();

    @UseDefaults
    @Order(50)
    @Height(min=300, pref=600)
    Property<Map<String, String>> parameters();

    @UseDefaults
    @Order(60)
    @Height(pref=300, min=300, max=600)
    Property<Map<String, Supply>> supply();

    @UseDefaults
    Property<List<String>> errors();
}
