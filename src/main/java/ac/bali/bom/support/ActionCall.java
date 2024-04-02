package ac.bali.bom.support;

import java.lang.reflect.Method;

public class ActionCall
{
    private final Method actionMethod;
    private final Class<?> serviceType;
    private final String label;
    private final ActionScope actionScope;

    public ActionCall(Class<?> serviceType, Method actionMethod, String label, ActionScope actionScope)
    {
        this.actionMethod = actionMethod;
        this.serviceType = serviceType;
        this.label = label;
        this.actionScope = actionScope;
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

    public ActionScope actionScope() {
        return actionScope;
    }
}
