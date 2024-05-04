package org.qi4j.library.javafx.support;

import java.lang.ref.WeakReference;
import java.util.Map;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import org.apache.polygene.api.association.NamedAssociation;

public class ObservableNamedAssociationWrapper<T> extends NamedAssociationWrapper<T>
    implements NamedAssociation<T>, Property<Map<String,T>>, WritableObjectValue<Map<String,T>>
{
    private ObservableValue<? extends Map<String,T>> observable = null;
    private ExpressionHelper<Map<String,T>> helper = null;
    private InvalidationListener listener = null;
    private boolean valid = false;

    public ObservableNamedAssociationWrapper(NamedAssociation<T> association)
    {
        super(association);
    }

    @Override
    public Map<String,T> get()
    {
        valid = true;
        return next.toMap();
    }

    @Override
    public void set(Map<String,T> newValue) throws IllegalArgumentException, IllegalStateException
    {
        next.clear();
        newValue.entrySet().forEach(entry -> next.put(entry.getKey(), entry.getValue()));
        markInvalid();
    }

    @Override
    public void addListener(InvalidationListener listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(InvalidationListener listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public Map<String,T> getValue()
    {
        return next.toMap();
    }

    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bind(ObservableValue<? extends Map<String,T>> newObservable)
    {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }

        if (!newObservable.equals(this.observable)) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new ObservableNamedAssociationWrapper.Listener(this);
            }
            observable.addListener(listener);
            markInvalid();
        }
    }

    @Override
    public void unbind() {
        if (observable != null) {
            set(observable.getValue());
            observable.removeListener(listener);
            observable = null;
        }
    }

    @Override
    public void bindBidirectional(Property<Map<String,T>> other)
    {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<Map<String,T>> other)
    {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public void addListener(ChangeListener<? super Map<String,T>> listener)
    {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super Map<String,T>> listener)
    {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void setValue(Map<String,T> value)
    {
        set(value);
    }

    @Override
    public Object getBean()
    {
        return null;
    }

    @Override
    public String getName()
    {
        return "";
    }

    protected void fireValueChangedEvent() {
        ExpressionHelper.fireValueChangedEvent(helper);
    }

    private void markInvalid() {
        if (valid) {
            valid = false;
            fireValueChangedEvent();
        }
    }

    private static class Listener implements InvalidationListener, WeakListener
    {
        private final WeakReference<ObservableNamedAssociationWrapper<?>> wref;

        public Listener(ObservableNamedAssociationWrapper<?> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            ObservableNamedAssociationWrapper<?> ref = wref.get();
            if (ref == null) {
                observable.removeListener(this);
            } else {
                ref.markInvalid();
            }
        }

        @Override
        public boolean wasGarbageCollected() {
            return wref.get() == null;
        }
    }

}
