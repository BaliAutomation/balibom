package org.qi4j.library.crudui.javafx.ui;

import java.io.File;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Dialog;
import javafx.scene.control.DialogPane;
import javafx.scene.control.Label;
import javafx.scene.control.ListView;
import javafx.scene.control.MultipleSelectionModel;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.SingleSelectionModel;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.TextFieldTableCell;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.Region;
import javafx.scene.layout.VBox;
import javafx.stage.FileChooser;
import org.apache.polygene.api.entity.EntityDescriptor;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.structure.Module;
import org.apache.polygene.api.structure.TypeLookup;
import org.apache.polygene.api.type.ArrayType;
import org.apache.polygene.api.type.CollectionType;
import org.apache.polygene.api.type.EntityCompositeType;
import org.apache.polygene.api.type.EnumType;
import org.apache.polygene.api.type.MapType;
import org.apache.polygene.api.type.StatefulAssociationValueType;
import org.apache.polygene.api.type.ValueCompositeType;
import org.apache.polygene.api.type.ValueType;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueDescriptor;
import org.qi4j.library.crudui.FieldDescriptor;

import static java.lang.Double.MAX_VALUE;
import static org.qi4j.library.crudui.javafx.ui.PropertyCtrlFactory.Mixin.humanize;

public class ParametersForm extends Dialog<Object[]>
{
    public static final int DEFAULT_LABEL_WIDTH = 240;
    public static final int DEFAULT_LABEL_HEIGHT = 64;
    private final TypeLookup typeLookup;
    private final VBox mainPane;
    private final UnitOfWorkFactory uowf;
    private final QueryBuilderFactory qbf;
    private final HashMap<String, Consumer<Object>> fields = new HashMap<>();

    private ParametersForm(@Uses String title, @Structure Module module, @Uses FieldDescriptor[] fields)
    {
        this.typeLookup = module.typeLookup();
        this.uowf = module.unitOfWorkFactory();
        this.qbf = module;
        final DialogPane dialogPane = getDialogPane();
        this.mainPane = new VBox();
        mainPane.setPrefWidth(800);
        mainPane.setMinWidth(600);
        VBox.setVgrow(mainPane, Priority.ALWAYS);

        mainPane.setAlignment(Pos.CENTER_LEFT);
        mainPane.setMaxWidth(MAX_VALUE);
        mainPane.setMaxHeight(MAX_VALUE);
        mainPane.setPadding(PropertyControl.PADDING);

        dialogPane.contentTextProperty().addListener(o -> updateGrid(fields, dialogPane));

        setTitle(title);
        dialogPane.setHeaderText("");
        dialogPane.getStyleClass().add("text-input-dialog");
        dialogPane.getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        updateGrid(fields, dialogPane);

        setResultConverter((dialogButton) ->
        {
            ButtonBar.ButtonData data = dialogButton == null ? null : dialogButton.getButtonData();
            if (data == ButtonBar.ButtonData.OK_DONE)
            {
                return argumentsInControls();
            }
            return null;
        });
    }

    private void updateGrid(FieldDescriptor[] fields, DialogPane dialogPane)
    {
        ObservableList<Node> children = mainPane.getChildren();
        children.clear();
        for (int index = 0; index < fields.length; index++)
        {
            FieldDescriptor field = fields[index];
            int height = computeHeight(field.height().get());
            int width = computeWidth(field.width().get());
            Label label = createContentLabel(field.name().get());
            label.setPrefWidth(Region.USE_COMPUTED_SIZE);
            label.setAlignment(Pos.CENTER_RIGHT);
            label.setPadding(PropertyControl.PADDING);

            Region region = selectControl(field, index, width, height);
            VBox.setVgrow(region, Priority.ALWAYS);
            region.setPadding(PropertyControl.PADDING);
            region.setMaxWidth(MAX_VALUE);
            region.setPrefWidth(600);
            Pane box;
            if (region.getUserData().equals("list"))
            {
                label.setAlignment(Pos.BOTTOM_LEFT);
                box = new VBox(label, region);
            } else if (region.getUserData().equals("table"))
            {
                label.setAlignment(Pos.BOTTOM_LEFT);
                box = new VBox(label, region);
            } else
            {
                box = new HBox(label, region);
            }
            children.add(box);
        }
        getDialogPane().setContent(mainPane);
    }

    public void clear()
    {
        fields.clear();
    }

    private int computeHeight(Integer multiplier)
    {
        if (multiplier == 0)
            multiplier = 1;
        return DEFAULT_LABEL_HEIGHT * multiplier;
    }

    private int computeWidth(Integer multiplier)
    {
        if (multiplier == 0)
            multiplier = 1;
        return DEFAULT_LABEL_WIDTH * multiplier;
    }

    @SuppressWarnings("unchecked")
    private Region selectControl(FieldDescriptor field, int index, int width, int height)
    {
        ValueType valueType = field.type().get();
        if (valueType instanceof CollectionType cType)
        {
            ValueType collectedType = cType.collectedType();
            if (collectedType.equals(ValueType.STRING))
            {
                ScrollPane scrollPane = createListView(field.name().get(), width, height);
                ListView<String> listView = (ListView<String>) scrollPane.getContent();
                listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
                scrollPane.setUserData(new ArgConverter(index, () ->
                {
                    MultipleSelectionModel<String> selectionModel = listView.getSelectionModel();
                    ObservableList<String> selectedItems = selectionModel.getSelectedItems();
                    return selectedItems;
                }));
                return scrollPane;
            }
            if (isEntity(collectedType) )
            {
                ScrollPane scrollPane = createListView(field.name().get(), width, height);
                ObservableList<Object> loaded = load(collectedType);
                ListView<Object> listView = (ListView<Object>) scrollPane.getContent();
                listView.setItems(loaded);
                scrollPane.setUserData(new ArgConverter(index, () -> listView.getSelectionModel().getSelectedItems()));
                return scrollPane;
            }
        } else if (valueType instanceof MapType mapType)
        {
            return createEditableTable(mapType, field.name().get(), width, height);
        } else if (valueType instanceof EnumType eType)
        {
            Object[] enums = eType.primaryType().getEnumConstants();
            ComboBox<?> comboBox = createComboBox(field.name().get(), enums);
            comboBox.setUserData(new ArgConverter(index, () ->
            {
                SingleSelectionModel<?> selectionModel = comboBox.getSelectionModel();
                return selectionModel.getSelectedItem();
            }));
            return comboBox;
        } else if (valueType instanceof ArrayType aType)
        {
            ScrollPane listView = createListView(field.name().get(), width, height);
            ((ListView<?>)listView.getContent()).getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            return listView;
        } else if (valueType instanceof EntityCompositeType eType)
        {
            ScrollPane listView = createListView(field.name().get(), width, height);
            ((ListView<?>)listView.getContent()).getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
            return listView;
        } else if (valueType instanceof ValueCompositeType vType)
        {
            // TODO
        } else if (valueType instanceof StatefulAssociationValueType<?> sType)
        {
            // TODO
        } else
        {
            if (valueType.equals(ValueType.STRING))
            {
                TextField tf = new TextField();
                tf.setUserData(new ArgConverter(index, tf::getText));
                fields.put(field.name().get(), v -> tf.setText(Objects.toString(v)));
                return tf;
            } else if (isNumberType(valueType))
            {
                TextField tf = new TextField();
                fields.put(field.name().get(), v -> tf.setText(Objects.toString(v)));
                tf.setUserData(new ArgConverter(index, () -> convertNumber(tf.getText(), valueType.primaryType())));
                return tf;
            } else if (isBoolean(valueType))
            {
                CheckBox check = new CheckBox();
                fields.put(field.name().get(), v -> check.setSelected((Boolean) v));
                check.setUserData(new ArgConverter(index, check::isSelected));
                return check;
            } else if (valueType.equals(ValueType.LOCAL_DATE))
            {
                DatePicker ctrl = new DatePicker(LocalDate.now());
                fields.put(field.name().get(), v -> ctrl.setValue((LocalDate) v));
                ctrl.setUserData(new ArgConverter(index, ctrl::getValue));
                return ctrl;
            } else if (valueType.primaryType().equals(File.class))
            {
                TextField tf = new TextField();
                fields.put(field.name().get(), v -> tf.setText(Objects.toString(v)));
                tf.setUserData(index);
                Button button = new Button("...");
                button.setOnAction(evt ->
                {
                    FileChooser fileChooser = new FileChooser();
                    File f = fileChooser.showOpenDialog(button.getScene().getWindow());
                    tf.setText(f.getAbsolutePath());
                });
                HBox hBox = new HBox(tf, button);
                hBox.setUserData(new ArgConverter(index, tf::getText));
                return hBox;
            }
        }
        throw new IllegalArgumentException("Does not support arguments of type " + valueType);
    }

    private boolean isBoolean(ValueType valueType)
    {
        Class<?> cls = valueType.primaryType();
        boolean boxed = Boolean.class.isAssignableFrom(cls);
        boolean primitive = cls.equals(Boolean.TYPE);
        return boxed || primitive;
    }

    private boolean isNumberType(ValueType valueType)
    {
        Class<?> cls = valueType.primaryType();
        boolean boxed = Number.class.isAssignableFrom(cls);
        boolean primitive = cls.isPrimitive() && !cls.isEnum() && !cls.equals(Boolean.TYPE);
        return boxed || primitive;
    }

    @SuppressWarnings("unchecked")
    private ObservableList<Object> load(ValueType collectedType)
    {
        UnitOfWork uow = uowf.currentUnitOfWork();
        QueryBuilder<?> qb = qbf.newQueryBuilder(collectedType.primaryType());
        List<?> result = uow.newQuery(qb).stream().toList();
        return (ObservableList<Object>) FXCollections.observableList(result);
    }

    private ComboBox<?> createComboBox(String name, Object[] enums)
    {
        ObservableList<? super Object> data = FXCollections.observableArrayList(enums);
        ComboBox<?> comboBox = new ComboBox<>(data);
        comboBox.getSelectionModel().selectFirst();
        comboBox.setPadding(PropertyControl.PADDING);
        comboBox.setEditable(true);
        fields.put(name, data::setAll);
        return comboBox;
    }

    private ScrollPane createListView(String name, Integer width, Integer height)
    {
        ObservableList<? super Object> data = FXCollections.observableArrayList();
        ListView<?> listView = new ListView<>(data);
        listView.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
        listView.setPadding(PropertyControl.PADDING);
        listView.setEditable(true);
        fields.put(name, data::setAll);

        ScrollPane scrollPane = new ScrollPane(listView);
        scrollPane.setFitToWidth(true);
        scrollPane.setFitToHeight(true);
        scrollPane.setMinHeight(height);
        scrollPane.setPrefHeight(height);
        scrollPane.setMinWidth(width);
        scrollPane.setPrefWidth(width);
        scrollPane.setUserData("list");
        return scrollPane;
    }

    private Region createEditableTable(MapType mapType, String name, Integer width, Integer height)
    {
        ObservableList<Attribute> data = FXCollections.observableArrayList();
        TableView<Attribute> table = new TableView<>(data);
        fields.put(name, v ->
        {
            //noinspection unchecked
            Map<String, String> map = (Map<String, String>) v;
            data.clear();
            for (Map.Entry<String, String> entry : map.entrySet())
            {
                data.add(new Attribute(entry.getKey(), entry.getValue()));
            }
        });
        table.setMinHeight(height);
        table.setPrefHeight(height);
        table.setMinWidth(width);
        table.setPrefWidth(width);
        table.setEditable(true);
        TableColumn<Attribute, String> nameColumn = new TableColumn<>("Name");
        nameColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        nameColumn.setCellValueFactory(p -> p.getValue().nameProperty());
        nameColumn.setOnEditCommit(cellEditEvent ->
        {
            ObservableList<Attribute> items = cellEditEvent.getTableView().getItems();
            int row = cellEditEvent.getTablePosition().getRow();
            String newValue = cellEditEvent.getNewValue();
            SimpleStringProperty nameProperty = items.get(row).name;
            nameProperty.setValue(newValue);
        });

        TableColumn<Attribute, String> valueColumn = new TableColumn<>("Value");
        valueColumn.setCellFactory(TextFieldTableCell.forTableColumn());
        valueColumn.setCellValueFactory(p -> p.getValue().valueProperty());
        valueColumn.setOnEditCommit(cellEditEvent ->
        {
            ObservableList<Attribute> items = cellEditEvent.getTableView().getItems();
            int row = cellEditEvent.getTablePosition().getRow();
            String newValue = cellEditEvent.getNewValue();
            SimpleStringProperty value = items.get(row).value;
            value.setValue(newValue);
        });
        table.getColumns().addAll(nameColumn, valueColumn);

        final Button deleteButton = new Button("-");
        deleteButton.setOnAction(e ->
        {
            ObservableList<Attribute> items = table.getItems();
            for (int i = items.size() - 1; i >= 0; i--)
            {
                TableView.TableViewSelectionModel<Attribute> selectionModel = table.getSelectionModel();
                if (selectionModel.isSelected(i))
                {
                    selectionModel.clearSelection();
                    items.remove(i, i + 1);
                }
            }
        });
        final Button addButton = new Button("+");
        addButton.setOnAction(e -> data.add(new Attribute("1", "0.00")));
        HBox controls = new HBox(addButton, deleteButton);
        controls.setPadding(PropertyControl.PADDING);
        ScrollPane scroll = new ScrollPane(table);

        VBox box = new VBox(scroll, controls);
        box.setUserData("table");
        return box;
    }

    private Object convertNumber(String text, Class<?> type)
    {
        if (type.equals(Integer.class) || type.equals(Integer.TYPE))
        {
            return Integer.parseInt(text);
        }
        if (type.equals(Double.class) || type.equals(Double.TYPE))
        {
            return Double.parseDouble(text);
        }
        if (type.equals(Long.class) || type.equals(Long.TYPE))
        {
            return Long.parseLong(text);
        }
        if (type.equals(Short.class) || type.equals(Short.TYPE))
        {
            return Short.parseShort(text);
        }
        if (type.equals(Float.class) || type.equals(Float.TYPE))
        {
            return Float.parseFloat(text);
        }
        if (type.equals(Byte.class) || type.equals(Byte.TYPE))
        {
            return Byte.parseByte(text);
        }
        if (type.equals(Character.class) || type.equals(Character.TYPE))
        {
            if (text.length() == 0)
                return null;
            return text.charAt(0);
        }
        if (type.equals(Boolean.class) || type.equals(Boolean.TYPE))
        {
            return Boolean.parseBoolean(text);
        }
        return null;
    }

    private boolean isValue(Class<?> type)
    {
        try
        {
            ValueDescriptor model = typeLookup.lookupValueModel(type);
            return model != null;
        } catch (Exception e)
        {
            return false;
        }
    }

    private boolean isEntity(ValueType type)
    {
        if( type instanceof EntityCompositeType)
            return true;
        List<EntityDescriptor> models = typeLookup.lookupEntityModels(type.primaryType());
        return models != null && models.size() > 0;
    }

    private static Label createContentLabel(String name)
    {
        Label label = new Label(humanize(name));
        label.setMaxWidth(MAX_VALUE);
        label.setMaxHeight(MAX_VALUE);
        label.getStyleClass().add("content");
        label.setWrapText(true);
        label.setMaxWidth(DEFAULT_LABEL_WIDTH);
        label.setPrefWidth(DEFAULT_LABEL_WIDTH);
        label.setMinWidth(DEFAULT_LABEL_WIDTH);
        return label;
    }

    private Object[] argumentsInControls()
    {
        ObservableList<Node> boxes = mainPane.getChildren();
        List<Object> result = new ArrayList<>();

        for (Node n : boxes)
        {
            Pane box = (Pane) n;
            Node control = box.getChildren().get(1);
            Object userData = control.getUserData();
            if (userData instanceof ArgConverter converter)
                result.add(converter.supplier.get());
        }
        return result.toArray();
    }

    public void setValue(String name, Object value)
    {
        Consumer<Object> consumer = fields.get(name);
        if (consumer != null)
            consumer.accept(value);
    }

    private record ArgConverter(int index, Supplier<Object> supplier)
    {
    }

    public static class Attribute
    {
        private final SimpleStringProperty name;

        private final SimpleStringProperty value;

        public Attribute(String name, String value)
        {
            this.name = new SimpleStringProperty(name);
            this.value = new SimpleStringProperty(value);
        }

        public String getName()
        {
            return name.get();
        }

        public SimpleStringProperty nameProperty()
        {
            return name;
        }

        public void setName(String name)
        {
            this.name.set(name);
        }

        public String getValue()
        {
            return value.get();
        }

        public SimpleStringProperty valueProperty()
        {
            return value;
        }

        public void setValue(String value)
        {
            this.value.set(value);
        }
    }
}