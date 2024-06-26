package org.qi4j.library.crudui.javafx.ui;

import javafx.scene.control.ListCell;
import org.apache.polygene.api.injection.scope.Service;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.spi.PolygeneSPI;

public class NameListCell<T> extends ListCell<T>
{
    @Structure
    PolygeneSPI spi;

    @Service
    PropertyCtrlFactory factory;

    @Override
    public void updateItem(T item, boolean empty)
    {
        super.updateItem(item, empty);
        if( item instanceof String)
        {
            setText((String) item);
        }
        else if (item != null)
        {
            setText(factory.nameOf(item));
        }
    }
}
