package org.qi4j.library.crudui.javafx.support;

import java.lang.reflect.Method;
import javafx.scene.control.Button;
import org.qi4j.library.crudui.ActionScope;

public class ActionCall
{
    private final Method actionMethod;
    private final Class<?> serviceType;
    private final String label;
    private final ActionScope actionScope;
    private final boolean showResult;
    private Button button;

    public ActionCall(Class<?> serviceType, Method actionMethod, String label, ActionScope actionScope, boolean showResult)
    {
        this.actionMethod = actionMethod;
        this.serviceType = serviceType;
        this.label = label;
        this.actionScope = actionScope;
        this.showResult = showResult;
    }

    public Method actionMethod()
    {
        return actionMethod;
    }

    public Class<?> serviceType()
    {
        return serviceType;
    }

    public String label()
    {
        return label;
    }

    public boolean showResult()
    {
        return showResult;
    }

    public ActionScope actionScope() {
        return actionScope;
    }

    public Button button()
    {
        return button;
    }

    public void setButton(Button button)
    {
        this.button = button;
    }
}
