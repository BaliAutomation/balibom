package ac.bali.bom.bootstrap;

import org.apache.polygene.api.structure.Application;
import org.apache.polygene.bootstrap.ApplicationAssembly;
import org.apache.polygene.bootstrap.AssemblyException;
import org.apache.polygene.bootstrap.LayerAssembly;
import org.apache.polygene.bootstrap.layered.LayeredApplicationAssembler;

public class Qi4jApplicationAssembler extends LayeredApplicationAssembler
{
    public Qi4jApplicationAssembler(String name, String version, Application.Mode mode) throws AssemblyException {
        super(name, version, mode);
    }

    @Override
    protected void assembleLayers(ApplicationAssembly assembly) throws AssemblyException {
        LayerAssembly configLayer = createLayer(ConfigurationLayer.class);
        LayerAssembly infraLayer = createLayer(InfrastructureLayer.class);
        LayerAssembly modelLayer = createLayer(ModelLayer.class);
        LayerAssembly viewLayer = createLayer(ViewLayer.class);
        viewLayer.uses(modelLayer);
        viewLayer.uses(infraLayer);
        viewLayer.uses(configLayer);
        modelLayer.uses(infraLayer);
        modelLayer.uses(configLayer);
        infraLayer.uses(configLayer);
    }
}
