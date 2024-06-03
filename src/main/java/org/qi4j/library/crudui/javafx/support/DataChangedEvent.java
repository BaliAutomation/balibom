package org.qi4j.library.crudui.javafx.support;

import javafx.event.Event;
import javafx.event.EventType;

public class DataChangedEvent<S> extends Event {
    public static final EventType<DataChangedEvent<?>> ANY = new EventType<> (Event.ANY, "DATA_CHANGED");
    private final Object source;
    private final S oldValue;
    private final S newValue;

    public DataChangedEvent(Object source, S oldValue, S newValue)
    {
        super(ANY);
        this.source = source;
        this.oldValue = oldValue;
        this.newValue = newValue;
    }

    @Override
    @SuppressWarnings("unchecked")
    public EventType<? extends DataChangedEvent<S>> getEventType() {
        return (EventType<? extends DataChangedEvent<S>>) super.getEventType();
    }

    @Override
    public Object getSource()
    {
        return source;
    }

    public S getOldValue()
    {
        return oldValue;
    }

    public S getNewValue()
    {
        return newValue;
    }
}
