package org.qi4j.library.crudui.javafx.support;

import java.lang.ref.WeakReference;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.binding.Bindings;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import org.apache.polygene.api.property.Property;
import org.apache.polygene.api.property.PropertyWrapper;

@SuppressWarnings("unchecked")
public class ObservablePropertyWrapper<T> extends PropertyWrapper
    implements javafx.beans.property.Property<T>, WritableObjectValue<T>
{
    private ObservableValue<? extends T> observable = null;
    private ExpressionHelper<T> helper = null;
    private InvalidationListener listener = null;
    private boolean valid = false;

    public ObservablePropertyWrapper(Property<T> property)
    {
        super((Property<Object>) property);
    }

    @Override
    public T get()
    {
        valid = true;
        return (T) super.get();
    }

    @Override
    public void set(Object newValue) throws IllegalArgumentException, IllegalStateException
    {
        if (super.get() != newValue) {
            super.set(newValue);
            markInvalid();
        }
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
    public void addListener(ChangeListener<? super T> listener) {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super T> listener) {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public T getValue()
    {
        return (T) super.get();
    }

    @Override
    public void setValue(T value)
    {
        set(value);
    }

    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bind(final ObservableValue<? extends T> newObservable) {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }

        if (!newObservable.equals(this.observable)) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new ObservablePropertyWrapper.Listener(this);
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
    public void bindBidirectional(javafx.beans.property.Property<T> other) {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(javafx.beans.property.Property<T> other) {
        Bindings.unbindBidirectional(this, other);
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
        private final WeakReference<ObservablePropertyWrapper<?>> wref;

        public Listener(ObservablePropertyWrapper<?> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            ObservablePropertyWrapper<?> ref = wref.get();
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
