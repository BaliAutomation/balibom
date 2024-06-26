package org.qi4j.library.crudui.javafx.ui;

import javafx.event.Event;
import javafx.event.EventType;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.control.Control;
import javafx.scene.control.Label;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import org.apache.polygene.api.association.AssociationDescriptor;
import org.apache.polygene.api.association.NamedAssociation;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Uses;

public class NamedAssociationControl<T> extends VBox
{
    static final Insets PADDING = new Insets(5, 10, 5, 10);
    protected final PropertyCtrlFactory factory;
    private final String labelText;
    protected NamedAssociation<T> value;

    public NamedAssociationControl(@Service PropertyCtrlFactory factory, @Uses AssociationDescriptor descriptor, @Uses boolean withLabel)
    {
        this.factory = factory;
        this.labelText = withLabel ? factory.nameOf(descriptor) : null;
    }

    public void clear()
    {
        // TODO
    }

    protected void load(NamedAssociation<T> value)
    {
        this.value = value;
    }

    public void setAssociation(String name, T value)
    {
        fireEvent(new AssociationDataEvent(this, AssociationDataEvent.ASSOCIATION_ADDED, value));
        this.value.put(name, value);
    }

    public void removeAssociation(String name)
    {
        this.value.remove(name);
        fireEvent(new AssociationDataEvent(this, AssociationDataEvent.ASSOCIATION_REMOVED, value));
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
//        box.setDisable(immutable);
        return box;
    }

    protected VBox wrapInVBox(Control... controls)
    {
        VBox box = new VBox(controls);
        box.setPadding(PADDING);
//        box.setDisable(immutable);
        return box;
    }

    public static class DirtyEvent extends Event
    {
        public static final EventType<DirtyEvent> DIRTY = new EventType<>(Event.ANY, "DIRTY_FORM_DATA");
        public static final EventType<DirtyEvent> ANY = DIRTY;

        public DirtyEvent(NamedAssociationControl source)
        {
            super(null, source, DIRTY);
        }
    }

    public static class AssociationDataEvent extends Event
    {
        public static final EventType<Event> ASSOCIATION_ADDED = new EventType<>(ANY, "ASSOCIATION_ADDED");
        public static final EventType<Event> ASSOCIATION_REMOVED = new EventType<>(ANY, "ASSOCIATION_REMOVED");
        private final Object value;

        public AssociationDataEvent(NamedAssociationControl assocControl, EventType eventType, Object value)
        {
            super(assocControl, assocControl, eventType);
            this.value = value;
        }

        public Object getValue()
        {
            return value;
        }
    }
}
