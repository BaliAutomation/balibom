package ac.bali.bom.parts;

import ac.bali.bom.ui.support.HasListViewController;
import ac.bali.bom.ui.support.Height;
import ac.bali.bom.ui.support.Order;
import ac.bali.bom.ui.support.RenderAsName;
import java.util.List;
import java.util.Map;
import java.util.Set;
import org.apache.polygene.api.association.Association;
import org.apache.polygene.api.association.ManyAssociation;
import org.apache.polygene.api.common.UseDefaults;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.property.Property;

@SuppressWarnings("unused")
@HasListViewController({PartsService.class})
public interface Part extends HasIdentity
{

    @RenderAsName
    @Order(1)
    Property<String> manufacturer();

    @RenderAsName
    @Order(2)
    Property<String> mpn();

    @UseDefaults
    @Order(3)
    @Height(150)
    Property<Set<String>> datasheetUrls();

    @UseDefaults
    @Order(4)
    @Height(200)
    Property<Set<String>> imageUrls();

    @UseDefaults
    @Order(10)
    ManyAssociation<Part> alternates();

    @UseDefaults
    @Order(20)
    Property<Map<String, String>> parameters();

    @UseDefaults
    @Order(30)
    Property<Map<String, Supply>> supply();

    Property<List<String>> errors();
}
