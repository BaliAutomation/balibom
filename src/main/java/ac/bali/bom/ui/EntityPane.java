package ac.bali.bom.ui;

import javafx.scene.control.SplitPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.object.ObjectFactory;

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
        split.setDividerPosition(0, 0.25);
        split.widthProperty().addListener((obs, oldVal, newVal) -> {
            double totalWidth = newVal.doubleValue();
            // At 1920, divider = 0.2
            // At 500, divider = 0.5
            // y = k*x + m
            // 0.2 = k * 1920 + m
            // 0.5 = k * 500 + m
            // k = (0.2-0.5) / (1920-500 ) = 0.0002112576
            // m = 0.5 - k*500 = 0.605629
            double dividerPosition = -0.0002112576 * totalWidth + 0.605629;
            split.setDividerPositions(dividerPosition);
        });
        HBox hBox = new HBox(split);
        HBox.setHgrow(split, Priority.ALWAYS);
        VBox.setVgrow(hBox, Priority.ALWAYS);
        hBox.setFillHeight(true);
        getChildren().add(actionBar);
        getChildren().add(hBox);
        setFillWidth(true);
    }

    public void loadAll()
    {
        controller.loadAll();
    }
}
