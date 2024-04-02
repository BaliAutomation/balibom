package ac.bali.bom.ui;

import ac.bali.bom.support.Ignore;
import ac.bali.bom.support.PropertyOrderComparator;
import javafx.scene.Node;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.entity.EntityReference;
import org.apache.polygene.api.identity.Identity;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.apache.polygene.api.value.ValueComposite;
import org.apache.polygene.spi.PolygeneSPI;
import org.apache.polygene.spi.module.ModuleSpi;

import java.util.AbstractMap.SimpleEntry;
import java.util.*;
import java.util.stream.Collectors;

public class CompositePane<T> extends VBox
    implements Initializable
{
    static final PropertyOrderComparator PROPERTY_COMPARATOR = new PropertyOrderComparator();

    private final Map<String, PropertyControl> controls = new HashMap<>();

    @Uses
    private Class<T> compositeType;

    @Service
    PropertyCtrlFactory factory;

    @Structure
    PolygeneSPI spi;

    @Structure
    ValueBuilderFactory vbf;

    @Structure
    UnitOfWorkFactory uowf;

    @Structure
    ModuleSpi module;

    @SuppressWarnings("unchecked")
    public void updateWith(T current)
    {

        module.typeLookup().lookupValueModel(compositeType).state().properties().forEach(p ->
        {
            String name = factory.nameOf(p);
            PropertyControl ctrl = controls.get(name);
            if (ctrl != null)
            {
                Object value = spi.stateOf((ValueComposite) current).propertyFor(p.accessor()).get();
                ctrl.setValue(value);
            }
        });

    }

    public void clearForm()
    {
        controls.values().forEach(PropertyControl::clear);
    }

    private Node createForm()
    {
        this.controls.clear();
        VBox vbox = new VBox();
        EntityDescriptor entityDescriptor = module.typeLookup().lookupEntityModel(compositeType);
        entityDescriptor.state()
            .properties()
            .filter(property -> property.metaInfo(Ignore.class) == null)
            .sorted(PROPERTY_COMPARATOR)
            .map(property ->
            {
                PropertyControl control = factory.createPropertyControl(property, true);
                if( control == null )
                    return null;
                return new SimpleEntry<>(factory.nameOf(property), control);
            })
            .filter( Objects::nonNull)
            .forEach(entry ->
            {
                vbox.getChildren().add(entry.getValue());
                this.controls.put(entry.getKey(), entry.getValue());
            });
        vbox.setFillWidth(true);
        return new AnchorPane(vbox);
    }

    @Override
    public void initialize() throws Exception
    {
        getChildren().add(createForm());
    }

    public T toValue()
    {
        T value = vbf.newValueBuilderWithState(
            compositeType,
            p ->
            {
                PropertyControl control = controls.get(factory.nameOf(p));
                return control.valueOf();
            },
            a -> {
                PropertyControl control = controls.get(factory.nameOf(a));
                Object v = control.valueOf();
                if( v instanceof String)
                    return EntityReference.parseEntityReference((String) v);
                else if( v instanceof Identity )
                    return EntityReference.create((Identity) v);
                else
                    throw new InternalError("Associations should be serialized to strings");
            },
            m -> null,
            n -> null
        ).newInstance();
        return value;
    }
}
