package ac.bali.bom.bootstrap.model;

import org.apache.polygene.api.common.Visibility;
import org.qi4j.library.crudui.FieldDescriptor;
import org.qi4j.library.crudui.javafx.ui.ActionBar;
import org.qi4j.library.crudui.javafx.ui.ArrayPropertyControl;
import org.qi4j.library.crudui.javafx.ui.AssociationControl;
import org.qi4j.library.crudui.javafx.ui.BigDecimalPropertyControl;
import org.qi4j.library.crudui.javafx.ui.BigIntegerPropertyControl;
import org.qi4j.library.crudui.javafx.ui.BooleanPropertyControl;
import org.qi4j.library.crudui.javafx.ui.BytePropertyControl;
import org.qi4j.library.crudui.javafx.ui.CharacterPropertyControl;
import org.qi4j.library.crudui.javafx.ui.CompositeListPropertyControl;
import org.qi4j.library.crudui.javafx.ui.CompositePane;
import org.qi4j.library.crudui.javafx.ui.DoublePropertyControl;
import org.qi4j.library.crudui.javafx.ui.DurationPropertyControl;
import org.qi4j.library.crudui.javafx.ui.EntityListController;
import org.qi4j.library.crudui.EntityNameComparator;
import org.qi4j.library.crudui.javafx.ui.EntityPane;
import org.qi4j.library.crudui.javafx.ui.EntityReferenceControl;
import org.qi4j.library.crudui.javafx.ui.EnumPropertyControl;
import org.qi4j.library.crudui.javafx.ui.FloatPropertyControl;
import org.qi4j.library.crudui.javafx.ui.IdentityPropertyControl;
import org.qi4j.library.crudui.javafx.ui.InstantPropertyControl;
import org.qi4j.library.crudui.javafx.ui.IntegerPropertyControl;
import org.qi4j.library.crudui.javafx.ui.ListPropertyControl;
import org.qi4j.library.crudui.javafx.ui.LocalDatePropertyControl;
import org.qi4j.library.crudui.javafx.ui.LocalDateTimePropertyControl;
import org.qi4j.library.crudui.javafx.ui.LocalTimePropertyControl;
import org.qi4j.library.crudui.javafx.ui.LongPropertyControl;
import org.qi4j.library.crudui.javafx.ui.ManyAssociationControl;
import org.qi4j.library.crudui.javafx.ui.MapPropertyControl;
import org.qi4j.library.crudui.javafx.ui.NameListCell;
import org.qi4j.library.crudui.javafx.ui.NamedAssociationControl;
import org.qi4j.library.crudui.javafx.ui.OffsetDateTimePropertyControl;
import org.qi4j.library.crudui.javafx.ui.ParametersForm;
import org.qi4j.library.crudui.javafx.ui.PeriodPropertyControl;
import org.qi4j.library.crudui.javafx.ui.PropertyCtrlFactory;
import org.qi4j.library.crudui.javafx.ui.SetPropertyControl;
import org.qi4j.library.crudui.javafx.ui.ShortPropertyControl;
import org.qi4j.library.crudui.javafx.ui.StringPropertyControl;
import org.qi4j.library.crudui.javafx.ui.ValueLinkControl;
import org.qi4j.library.crudui.javafx.ui.ZonedDateTimePropertyControl;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.ModuleAssembly;
import org.apache.polygene.bootstrap.layered.ModuleAssembler;

public class JavaFxModule
    implements ModuleAssembler
{
    public static final String NAME = "JavaFx Module";

    @Override
    public ModuleAssembly assemble(LayerAssembly layer, ModuleAssembly module) throws AssemblyException
    {
        module.defaultServices();
        module.services(PropertyCtrlFactory.class);
        module.values(FieldDescriptor.class).visibleIn(Visibility.layer);
        module.objects(
            ActionBar.class,
            ArrayPropertyControl.class,
            AssociationControl.class,
            BigDecimalPropertyControl.class,
            BigIntegerPropertyControl.class,
            BooleanPropertyControl.class,
            BytePropertyControl.class,
            CharacterPropertyControl.class,
            CompositePane.class,
            CompositeListPropertyControl.class,
            DoublePropertyControl.class,
            DurationPropertyControl.class,
            EntityNameComparator.class,
            EntityListController.class,
            EntityPane.class,
            EntityReferenceControl.class,
            EnumPropertyControl.class,
            FloatPropertyControl.class,
            IdentityPropertyControl.class,
            InstantPropertyControl.class,
            IntegerPropertyControl.class,
            ListPropertyControl.class,
            LocalDatePropertyControl.class,
            LocalDateTimePropertyControl.class,
            LocalTimePropertyControl.class,
            LongPropertyControl.class,
            ManyAssociationControl.class,
            MapPropertyControl.class,
            NamedAssociationControl.class,
            NameListCell.class,
            OffsetDateTimePropertyControl.class,
            ParametersForm.class,
            PeriodPropertyControl.class,
            SetPropertyControl.class,
            ShortPropertyControl.class,
            StringPropertyControl.class,
            ValueLinkControl.class,
            ZonedDateTimePropertyControl.class
        ).visibleIn(Visibility.layer);
        return module;
    }
}
