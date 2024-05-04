package org.qi4j.library.javafx.ui;

import ac.bali.bom.ModelException;
import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Dialog;
import javafx.scene.control.ToolBar;
import javafx.stage.FileChooser;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.type.HasTypes;
import org.apache.polygene.api.util.Methods;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.javafx.support.Action;
import org.qi4j.library.javafx.support.ActionCall;
import org.qi4j.library.javafx.support.ActionScope;
import org.qi4j.library.javafx.support.HasListViewController;

import static org.qi4j.library.javafx.ui.PropertyCtrlFactory.Mixin.humanize;

public class ActionBar<T> extends ToolBar
    implements Initializable
{
    @Structure
    PolygeneSPI spi;

    @Structure
    Module module;

    @Uses
    Class<T> entityType;

    private Button saveButton;
    private Button cancelButton;
    private List<ActionCall> actions;
    private List<T> selectedItems;

    @Override
    public void initialize()
    {
        saveButton = new Button("Save");
        cancelButton = new Button("Cancel");
        getItems().addAll(saveButton, cancelButton);
        actions = findActions();
        for (ActionCall action : actions)
        {
            Button button = new Button(action.label());
            action.setButton(button);
            if (action.actionScope() == ActionScope.composite)
                button.setDisable(true);
            button.setOnAction(evt ->
            {
                onAction(action, evt);
                fireEvent(evt);
            });
            getItems().add(button);
        }
        setDefaultState();
    }

    public void setDefaultState()
    {
        saveButton.setDisable(true);
        cancelButton.setDisable(true);
        for (ActionCall a : actions)
        {
            a.button().setDisable(a.actionScope() == ActionScope.composite);
        }
    }

    public void onEdit()
    {
        saveButton.setDisable(false);
        cancelButton.setDisable(false);
        for (ActionCall a : actions)
        {
            a.button().setDisable(true);
        }
    }

    public void onSelected(List<T> items)
    {
        saveButton.setDisable(true);
        cancelButton.setDisable(true);
        selectedItems = items;
        for (ActionCall a : actions)
        {
            if (a.actionScope() == ActionScope.composite)
                a.button().setDisable(items.size() == 0);
            else
                a.button().setDisable(false);
        }
    }

    private List<ActionCall> findActions()
    {
        List<ActionCall> result = new ArrayList<>();
        EntityDescriptor entityDescriptor = module.typeLookup().lookupEntityModel(entityType);
        HasListViewController hasListViewController = entityDescriptor.metaInfo(HasListViewController.class);
        if (hasListViewController == null)
            return result;
        for (Class<?> service : hasListViewController.value())
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

    public void addSaveActionHandler(EventHandler<ActionEvent> onSave)
    {
        saveButton.addEventHandler(ActionEvent.ANY, onSave);
    }

    public void addCancelActionHandler(EventHandler<ActionEvent> onCancel)
    {
        cancelButton.addEventHandler(ActionEvent.ANY, onCancel);
    }

    private void onAction(ActionCall action, ActionEvent event)
    {
        try
        {
            Module m = module.typeLookup().lookupEntityModel(entityType).module().instance();
            Object service = m.serviceFinder().findService(action.serviceType()).get();
            Method method = action.actionMethod();
            Parameter[] parameters = method.getParameters();
            if (action.actionScope() == ActionScope.type)
            {
                if (parameters.length == 1)
                {
                    Class<?> pType = parameters[0].getType();
                    if (pType.equals(File.class))
                    {
                        FileChooser chooser = new FileChooser();
                        chooser.setTitle(action.label());
                        File f = chooser.showOpenDialog(ActionBar.this.getScene().getWindow());
                        if (f != null)
                        {
                            method.invoke(service, f);
                        }
                        return;
                    }
                }
            } else if (action.actionScope() == ActionScope.composite)
            {
                if (parameters.length == 1)
                {
                    Class<?> parameter1Type = parameters[0].getType();
                    if (Collection.class.isAssignableFrom(parameter1Type))
                    {
                        method.invoke(service, selectedItems);
                    } else
                    {
                        for (Object arg : selectedItems)
                        {
                            method.invoke(service, arg);
                        }
                    }
                    return;
                }
            }
            ParametersForm parametersForm = new ParametersForm(action.label(), parameters);
            Optional<Object[]> result = parametersForm.showAndWait();
            if (result.isPresent())
                method.invoke(service, result.get());
        } catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e)
        {
            if( e.getTargetException() instanceof ModelException )
            {
                new Alert(Alert.AlertType.ERROR, e.getTargetException().getMessage()).showAndWait();
            }
            else
            {
                throw new RuntimeException(e);
            }
        }
    }
}
