package org.qi4j.library.crudui.javafx.ui;

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
import java.util.Map;
import java.util.Optional;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ToolBar;
import javafx.scene.layout.Priority;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import org.apache.polygene.api.composite.AmbiguousTypeException;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.structure.TypeLookup;
import org.apache.polygene.api.type.ArrayType;
import org.apache.polygene.api.type.CollectionType;
import org.apache.polygene.api.type.EntityCompositeType;
import org.apache.polygene.api.type.EnumType;
import org.apache.polygene.api.type.HasTypes;
import org.apache.polygene.api.type.MapType;
import org.apache.polygene.api.type.ValueType;
import org.apache.polygene.api.util.Classes;
import org.apache.polygene.api.value.ValueBuilder;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.crudui.Action;
import org.qi4j.library.crudui.ActionScope;
import org.qi4j.library.crudui.FieldDescriptor;
import org.qi4j.library.crudui.HasListViewController;
import org.qi4j.library.crudui.ParameterName;
import org.qi4j.library.crudui.javafx.support.ActionCall;

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
                        label = PropertyCtrlFactory.Mixin.humanize(method.getName());
                    }
                    return new ActionCall(service, method, label, a.scope(), a.showResult());
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
            FieldDescriptor[] fields = createFieldDescriptors(method.getParameters());
            ValueType p1Type = fields[0].type().get();
            boolean isFirstArgCollection = p1Type instanceof CollectionType;
            FieldDescriptor[] formFields;
            if (action.actionScope() == ActionScope.composite)
                formFields = filterOutFirst(fields);
            else
                formFields = fields;
            Optional<Object[]> result;
            if (formFields.length > 0)
            {
                if (formFields.length == 1 && p1Type.primaryType().equals(File.class))
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
                ParametersForm parametersForm = module.objectFactory().newObject(ParametersForm.class, action.label(), formFields);
                result = parametersForm.showAndWait();
            } else
            {
                result = Optional.of(new Object[0]);
            }
            if (result.isPresent())
            {
                Object[] formValues = result.get();
                if (action.actionScope() == ActionScope.composite)
                {
                    Object[] args = new Object[formValues.length + 1];
                    System.arraycopy(formValues, 0, args, 1, formValues.length);
                    invokeTargetMethod(action, service, method, args, isFirstArgCollection);
                } else
                {
                    invokeTargetMethod(action, service, method, formValues);
                }
            }
        } catch (IllegalAccessException e)
        {
            throw new RuntimeException(e);
        } catch (InvocationTargetException e)
        {
            if (e.getTargetException() instanceof ModelException)
            {
                new Alert(Alert.AlertType.ERROR, e.getTargetException().getMessage()).showAndWait();
            } else
            {
                throw new RuntimeException(e);
            }
        }
    }

    private void invokeTargetMethod(ActionCall action, Object service, Method method, Object[] args, boolean isCollection) throws IllegalAccessException, InvocationTargetException
    {
        if (isCollection)
        {
            args[0] = selectedItems;
            invokeTargetMethod(action, service, method, args);
        } else
        {
            for (Object instance : selectedItems)
            {
                args[0] = instance;
                invokeTargetMethod(action, service, method, args);
            }
        }
    }

    private void invokeTargetMethod(ActionCall action, Object service, Method method, Object[] args) throws IllegalAccessException, InvocationTargetException
    {
        Object retValue = method.invoke(service, args);
        if (action.showResult() && retValue != null)
        {
            show(action.label(), retValue);
        }
    }

    private FieldDescriptor[] filterOutFirst(FieldDescriptor[] fields)
    {
        FieldDescriptor[] result = new FieldDescriptor[fields.length - 1];
        System.arraycopy(fields, 1, result, 0, result.length);
        return result;
    }

    private void show(String title, Object retValue)
    {
        if (!spi.isComposite(retValue))
            return;
        Class<?> compositeType = spi.compositeDescriptorFor(retValue).primaryType();

        //noinspection unchecked
        CompositePane<Object> valuePane = module.newObject(CompositePane.class, compositeType, true);

        valuePane.updateWith(retValue);
        VBox root = new VBox(valuePane);
        root.setFillWidth(true);
        VBox.setVgrow(valuePane, Priority.ALWAYS);
        Scene scene = new Scene(root, 1200, 800);
        Stage compositeStage = new Stage();
        compositeStage.setScene(scene);
        compositeStage.setTitle(title);
        compositeStage.show();
        compositeStage.addEventHandler(WindowEvent.WINDOW_CLOSE_REQUEST, evt ->
        {
            compositeStage.setScene(null);
            compositeStage.close();
            root.getChildren().clear();
        });
    }

    private FieldDescriptor[] createFieldDescriptors(Parameter[] parameters)
    {
        int height = (int) new Label("Abc").getHeight();
        FieldDescriptor[] result = new FieldDescriptor[parameters.length];
        for (int i = 0; i < parameters.length; i++)
        {
            Parameter parameter = parameters[i];

            ValueBuilder<FieldDescriptor> builder = module.newValueBuilder(FieldDescriptor.class);
            FieldDescriptor proto = builder.prototype();
            ParameterName annotation = parameter.getAnnotation(ParameterName.class);
            String name;
            if (annotation != null)
            {
                name = annotation.value();
            } else
            {
                name = PropertyCtrlFactory.Mixin.humanize(parameter.getName());
            }
            proto.name().set(name);
            proto.type().set(valueTypeOf(parameter));
            proto.height().set(height);
            proto.width().set(300);
            result[i] = builder.newInstance();
        }
        return result;
    }

    private ValueType valueTypeOf(Parameter parameter)
    {
        Type type = parameter.getParameterizedType();
        return valueTypeOf(type);
    }

    private ValueType valueTypeOf(Type type)
    {
        Class<?> clazz = Classes.RAW_CLASS.apply(type);
        if (Collection.class.isAssignableFrom(clazz))
        {
            Type actualTypeArgument = ((ParameterizedType) type).getActualTypeArguments()[0];
            return CollectionType.collectionOf(valueTypeOf(actualTypeArgument));
        }
        if (Map.class.isAssignableFrom(clazz))
        {
            Type[] typeArguments = ((ParameterizedType) type).getActualTypeArguments();
            Type keyTypeArgument = typeArguments[0];
            Type valueTypeArgument = typeArguments[1];
            return MapType.of(valueTypeOf(keyTypeArgument), valueTypeOf(valueTypeArgument));
        }
        if (clazz.isArray())
        {
            return ArrayType.of(clazz.getComponentType());
        }
        if (Enum.class.isAssignableFrom(clazz))
        {
            return EnumType.of(clazz);
        }
        TypeLookup lookup = module.typeLookup();
        try
        {
            EntityDescriptor model = lookup.lookupEntityModel(clazz);
            if (model != null)
                return new EntityCompositeType(model);
        } catch (AmbiguousTypeException e)
        {
            throw new RuntimeException(e);
        }
        return ValueType.of(clazz);
    }
}
