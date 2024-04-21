package org.qi4j.library.javafx.ui;

import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;

public abstract class PropertyControl<T> extends VBox
{
    static final Insets PADDING = new Insets(5, 10, 5, 10);
    protected final PropertyCtrlFactory factory;
    private final boolean immutable;
    private final String labelText;
    protected T value;

    public PropertyControl(PropertyCtrlFactory factory, boolean immutable, String labelText)
    {
        this.factory = factory;
        this.immutable = immutable;
        this.labelText = labelText;
    }

    public abstract void clear();

    public void setValue(T value)
    {
        fireEvent(new PropertyDataEvent(this, this.value, value));
        updateTo(value);
        this.value = value;
        dirty();
    }

    protected abstract void updateTo(T value);

    protected void dirty()
    {  // TODO
//        fireEvent(new DirtyEvent(this));
    }

    protected Label labelOf()
    {
        if (labelText == null)
            return null;
        Label label = new Label(labelText);
        label.setPrefWidth(150);
        label.setAlignment(Pos.CENTER_RIGHT);
        label.setPadding(PADDING);
        return label;
    }

    protected HBox wrapInHBox(Control... controls)
    {
        HBox box = new HBox(controls);
        box.setPadding(PADDING);
        box.setDisable(immutable);
        return box;
    }

    protected VBox wrapInVBox(Control... controls)
    {
        VBox box = new VBox(controls);
        box.setPadding(PADDING);
        box.setDisable(immutable);
        return box;
    }

    public T valueOf()
    {
        return currentValue();
    }

    protected abstract T currentValue();

    public static class DirtyEvent extends Event
    {
        public static final EventType<ActionEvent> DIRTY = new EventType<>(Event.ANY, "DIRTY_FORM_DATA");
        public static final EventType<ActionEvent> ANY = DIRTY;

        public DirtyEvent(PropertyControl source)
        {
            super(null, source, DIRTY);
        }
    }

    public static class PropertyDataEvent extends Event
    {
        public static final EventType<Event> PROPERTY_DATA_CHANGED = new EventType<>(ANY, "PROPERTY_DATA_CHANGED");
        private final Object oldValue;
        private final Object newValue;

        public PropertyDataEvent(PropertyControl propertyControl, Object oldValue, Object newValue)
        {
            super(null, propertyControl, PROPERTY_DATA_CHANGED);
            this.oldValue = oldValue;
            this.newValue = newValue;
        }

        public Object getOldValue()
        {
            return oldValue;
        }

        public Object getNewValue()
        {
            return newValue;
        }
    }
}
