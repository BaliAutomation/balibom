package ac.bali.bom.bootstrap;

import org.qi4j.library.javafx.ui.ActionBar;
import org.qi4j.library.javafx.ui.ArrayPropertyControl;
import org.qi4j.library.javafx.ui.BigDecimalPropertyControl;
import org.qi4j.library.javafx.ui.BigIntegerPropertyControl;
import org.qi4j.library.javafx.ui.BooleanPropertyControl;
import org.qi4j.library.javafx.ui.BytePropertyControl;
import org.qi4j.library.javafx.ui.CharacterPropertyControl;
import org.qi4j.library.javafx.ui.CompositeDialog;
import org.qi4j.library.javafx.ui.CompositePane;
import org.qi4j.library.javafx.ui.DoublePropertyControl;
import org.qi4j.library.javafx.ui.DurationPropertyControl;
import org.qi4j.library.javafx.ui.EntityListController;
import org.qi4j.library.javafx.support.EntityNameComparator;
import org.qi4j.library.javafx.ui.EntityPane;
import org.qi4j.library.javafx.ui.EntityReferenceControl;
import org.qi4j.library.javafx.ui.EntityReferencePropertyControl;
import org.qi4j.library.javafx.ui.EnumPropertyControl;
import org.qi4j.library.javafx.ui.FloatPropertyControl;
import org.qi4j.library.javafx.ui.IdentityPropertyControl;
import org.qi4j.library.javafx.ui.InstantPropertyControl;
import org.qi4j.library.javafx.ui.IntegerPropertyControl;
import org.qi4j.library.javafx.ui.ListPropertyControl;
import org.qi4j.library.javafx.ui.LocalDatePropertyControl;
import org.qi4j.library.javafx.ui.LocalDateTimePropertyControl;
import org.qi4j.library.javafx.ui.LocalTimePropertyControl;
import org.qi4j.library.javafx.ui.LongPropertyControl;
import org.qi4j.library.javafx.ui.MapPropertyControl;
import org.qi4j.library.javafx.ui.NameListCell;
import org.qi4j.library.javafx.ui.OffsetDateTimePropertyControl;
import org.qi4j.library.javafx.ui.PeriodPropertyControl;
import org.qi4j.library.javafx.ui.PropertyCtrlFactory;
import org.qi4j.library.javafx.ui.ShortPropertyControl;
import org.qi4j.library.javafx.ui.StringPropertyControl;
import org.qi4j.library.javafx.ui.ValueLinkControl;
import org.qi4j.library.javafx.ui.ZonedDateTimePropertyControl;
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
        module.objects(
            ActionBar.class,
            ArrayPropertyControl.class,
            BigDecimalPropertyControl.class,
            BigIntegerPropertyControl.class,
            BooleanPropertyControl.class,
            BytePropertyControl.class,
            CharacterPropertyControl.class,
            CompositeDialog.class,
            CompositePane.class,
            DoublePropertyControl.class,
            DurationPropertyControl.class,
            EntityNameComparator.class,
            EntityListController.class,
            EntityPane.class,
            EntityReferenceControl.class,
            EntityReferencePropertyControl.class,
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
            MapPropertyControl.class,
            NameListCell.class,
            OffsetDateTimePropertyControl.class,
            PeriodPropertyControl.class,
            ShortPropertyControl.class,
            StringPropertyControl.class,
            ValueLinkControl.class,
            ZonedDateTimePropertyControl.class
        );
        return module;
    }
}
