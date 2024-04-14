package ac.bali.bom.ui.support;

import java.lang.reflect.Method;
import java.util.List;
import javafx.scene.Node;
import javafx.scene.control.Button;

public class ActionCall
{
    private final Method actionMethod;
    private final Class<?> serviceType;
    private final String label;
    private final ActionScope actionScope;
    private Button button;

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

    public Button button()
    {
        return button;
    }

    public void setButton(Button button)
    {
        this.button = button;
    }
}
