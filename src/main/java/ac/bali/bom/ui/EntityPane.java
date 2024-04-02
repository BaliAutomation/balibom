package ac.bali.bom.ui;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.object.ObjectFactory;

import java.util.List;

public class EntityPane<T extends HasIdentity> extends VBox
    implements Initializable
{
    CompositePane<T> compositeForm;
    ActionBar<T> actionBar;
    ListPropertyControl<T> entityList;
    EntityListController<T> controller;

    @Structure
    ObjectFactory obf;

    @Uses
    Class<T> entityType;

    @SuppressWarnings("unchecked")
    @Override
    public void initialize() throws Exception
    {
        actionBar = obf.newObject(ActionBar.class, entityType);
        compositeForm = obf.newObject(CompositePane.class, entityType);
        entityList = obf.newObject(ListPropertyControl.class, entityType);
        controller = obf.newObject(EntityListController.class, entityType, compositeForm, actionBar, entityList);
        SplitPane split = new SplitPane(entityList, compositeForm);
        HBox hBox = new HBox(split);
        hBox.setFillHeight(true);
        getChildren().add(actionBar);
        getChildren().add(hBox);
        setFillWidth(true);
    }

    public void loadAll() {
        controller.loadAll();
    }
}
