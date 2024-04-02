package ac.bali.bom.bootstrap;

import ac.bali.bom.ui.*;
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
            CompositePane.class,
            DoublePropertyControl.class,
            DurationPropertyControl.class,
            EntityListController.class,
            EntityPane.class,
            NameListCell.class,
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
            OffsetDateTimePropertyControl.class,
            PeriodPropertyControl.class,
            ShortPropertyControl.class,
            StringPropertyControl.class,
            ZonedDateTimePropertyControl.class
        );
        return module;
    }
}
