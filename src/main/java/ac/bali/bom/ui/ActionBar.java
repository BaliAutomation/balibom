package ac.bali.bom.ui;

import ac.bali.bom.support.Action;
import ac.bali.bom.support.ActionCall;
import ac.bali.bom.support.HasViewController;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ToolBar;
import javafx.stage.FileChooser;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.type.HasTypes;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static ac.bali.bom.ui.PropertyCtrlFactory.Mixin.humanize;

public class ActionBar<T> extends ToolBar
    implements Initializable
{
    @Structure
    Module module;

    @Uses
    Class<T> entityType;

    private Button newButton;
    private Button saveButton;
    private Button cancelButton;
    private Button deleteButton;
    private List<ActionCall> actions;

    @Override
    public void initialize() throws Exception
    {
        ObservableList<Node> children = getChildren();
        newButton = new Button("New");
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        deleteButton = new Button("Delete");
        getItems().addAll(newButton, saveButton, cancelButton, deleteButton);
        actions = findActions();
        for (ActionCall action : actions)
        {
            Button button = new Button(action.label());
            button.setOnAction(evt -> onAction(action, evt));
            getItems().add(button);
        }
        setDefaultState();
    }

    public void setDefaultState()
    {
        newButton.setDisable(false);
        saveButton.setDisable(true);
        cancelButton.setDisable(true);
        deleteButton.setDisable(true);
    }

    public void onNew()
    {
        newButton.setDisable(true);
        saveButton.setDisable(false);
        cancelButton.setDisable(false);
        deleteButton.setDisable(false);
    }

    public void onSelected()
    {
        newButton.setDisable(false);
        saveButton.setDisable(true);
        cancelButton.setDisable(true);
        deleteButton.setDisable(false);
    }

    private List<ActionCall> findActions()
    {
        List<ActionCall> result = new ArrayList<>();
        EntityDescriptor entityDescriptor = module.typeLookup().lookupEntityModel(entityType);
        HasViewController hasViewController = entityDescriptor.metaInfo(HasViewController.class);
        if (hasViewController == null)
            return result;
        for (Class<?> service : hasViewController.value())
        {
            List<ActionCall> actions = entityDescriptor.module()
                .typeLookup()
                .lookupServiceModels(service)
                .stream()
                .flatMap(HasTypes::types)
                .flatMap(type -> Arrays.stream(type.getMethods()))
                .filter(method -> method.getAnnotation(Action.class) != null)
                .map(method ->
                {
                    Action a = method.getAnnotation(Action.class);
                    String label = a.label();
                    if (label == null || label.length() == 0)
                    {
                        label = humanize(method.getName());
                    }
                    return new ActionCall(service, method, label, a.scope());
                })
                .toList();
            result.addAll(actions);
        }
        return result;
    }

    public void addNewActionHandler(EventHandler<ActionEvent> onNew)
    {
        newButton.addEventHandler(ActionEvent.ANY, onNew);
    }

    public void addSaveActionHandler(EventHandler<ActionEvent> onNew)
    {
        saveButton.addEventHandler(ActionEvent.ANY, onNew);
    }

    public void addCancelActionHandler(EventHandler<ActionEvent> onNew)
    {
        cancelButton.addEventHandler(ActionEvent.ANY, onNew);
    }

    public void addDeleteActionHandler(EventHandler<ActionEvent> onNew)
    {
        deleteButton.addEventHandler(ActionEvent.ANY, onNew);
    }

    private void onAction(ActionCall action, ActionEvent event)
    {
        try
        {
            Object service = module.serviceFinder().findService(action.serviceType()).get();
            Method method = action.actionMethod();
            Parameter[] parameters = method.getParameters();
            if (parameters.length == 1)
            {
                if (parameters[0].getType().equals(File.class))
                {
                    FileChooser chooser = new FileChooser();
                    chooser.setTitle(action.label());
                    File f = chooser.showOpenDialog(ActionBar.this.getScene().getWindow());
                    if (f != null)
                    {
                        method.invoke(service, f);
                        return;
                    }
                }
            }
            ParametersForm parametersForm = new ParametersForm(action.label(), parameters);
            Optional<Object[]> result = parametersForm.showAndWait();
            if (result.isPresent())
                method.invoke(service, result.get());
        } catch (IllegalAccessException | InvocationTargetException e)
        {
            throw new RuntimeException(e);
        }
    }
}
