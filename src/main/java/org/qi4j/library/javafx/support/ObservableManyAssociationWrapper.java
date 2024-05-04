package org.qi4j.library.javafx.support;

import java.lang.ref.WeakReference;
import java.util.List;
import javafx.beans.InvalidationListener;
import javafx.beans.Observable;
import javafx.beans.WeakListener;
import javafx.beans.binding.Bindings;
import javafx.beans.property.Property;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.beans.value.WritableObjectValue;
import org.apache.polygene.api.association.ManyAssociation;

public class ObservableManyAssociationWrapper<T> extends ManyAssociationWrapper<T>
    implements ManyAssociation<T>, javafx.beans.property.Property<List<T>>, WritableObjectValue<List<T>>
{
    private ObservableValue<? extends List<T>> observable = null;
    private ExpressionHelper<List<T>> helper = null;
    private InvalidationListener listener = null;
    private boolean valid = false;

    public ObservableManyAssociationWrapper(ManyAssociation<T> association)
    {
        super(association);
    }

    @Override
    public List<T> get()
    {
        valid = true;
        return (List<T>) next.toList();
    }

    @Override
    public void set(List<T> newValue) throws IllegalArgumentException, IllegalStateException
    {
        next.clear();
        newValue.forEach(a -> next.add(a));
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
    public List<T> getValue()
    {
        return next.toList();
    }

    public boolean isBound() {
        return observable != null;
    }

    @Override
    public void bind(ObservableValue<? extends List<T>> newObservable)
    {
        if (newObservable == null) {
            throw new NullPointerException("Cannot bind to null");
        }

        if (!newObservable.equals(this.observable)) {
            unbind();
            observable = newObservable;
            if (listener == null) {
                listener = new ObservableManyAssociationWrapper.Listener(this);
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
    public void bindBidirectional(Property<List<T>> other)
    {
        Bindings.bindBidirectional(this, other);
    }

    @Override
    public void unbindBidirectional(Property<List<T>> other)
    {
        Bindings.unbindBidirectional(this, other);
    }

    @Override
    public void addListener(ChangeListener<? super List<T>> listener)
    {
        helper = ExpressionHelper.addListener(helper, this, listener);
    }

    @Override
    public void removeListener(ChangeListener<? super List<T>> listener)
    {
        helper = ExpressionHelper.removeListener(helper, listener);
    }

    @Override
    public void setValue(List<T> value)
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
        private final WeakReference<ObservableManyAssociationWrapper<?>> wref;

        public Listener(ObservableManyAssociationWrapper<?> ref) {
            this.wref = new WeakReference<>(ref);
        }

        @Override
        public void invalidated(Observable observable) {
            ObservableManyAssociationWrapper<?> ref = wref.get();
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
