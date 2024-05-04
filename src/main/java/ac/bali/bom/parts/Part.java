package ac.bali.bom.parts;

import ac.bali.bom.suppliers.Supply;
import ac.bali.bom.view.Deleter;
import org.qi4j.library.javafx.support.HasListViewController;
import org.qi4j.library.javafx.support.Height;
import org.qi4j.library.javafx.support.Order;
import org.qi4j.library.javafx.support.RenderAsDescription;
import org.qi4j.library.javafx.support.RenderAsName;
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
    @Order(1)
    Property<String> manufacturer();

    @RenderAsName
    @Order(2)
    Property<String> mpn();

    @UseDefaults
    @RenderAsDescription
    @Order(3)
    Property<String> partIntro();

    @UseDefaults
    @Order(4)
    @Height(pref=150)
    Property<Set<String>> datasheetUrls();

    @UseDefaults
    @Order(5)
    @Height(pref=200)
    Property<Set<String>> imageUrls();

    @UseDefaults
    @Order(10)
    @Height(pref=300)
    ManyAssociation<Part> alternates();

    @UseDefaults("1")
    @Order(7)
    Property<Float> wastePercentage();

    @UseDefaults("5")
    @Order(6)
    Property<Integer> wastePieces();

    @UseDefaults
    @Order(20)
    @Height(min=300, pref=500)
    Property<Map<String, String>> parameters();

    @UseDefaults
    @Order(30)
    @Height(pref=300, min=300, max=300)
    Property<Map<String, Supply>> supply();

    @UseDefaults
    Property<List<String>> errors();
}
