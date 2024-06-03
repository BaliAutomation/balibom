package org.qi4j.library.crudui.javafx.ui;

import java.util.Objects;
import java.util.Optional;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.polygene.api.identity.HasIdentity;
import org.apache.polygene.api.injection.scope.Structure;
import org.apache.polygene.api.injection.scope.Uses;
import org.apache.polygene.api.mixin.Initializable;
import org.apache.polygene.api.object.ObjectFactory;
import org.apache.polygene.api.query.Query;
import org.apache.polygene.api.query.QueryBuilder;
import org.apache.polygene.api.query.QueryBuilderFactory;
import org.apache.polygene.api.unitofwork.UnitOfWork;
import org.apache.polygene.api.unitofwork.UnitOfWorkFactory;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.apache.polygene.spi.PolygeneSPI;
import org.qi4j.library.crudui.EntityNameComparator;

public class EntityListController<T extends HasIdentity>
    implements Initializable
{
    @Structure
    UnitOfWorkFactory uowf;

    @Structure
    ObjectFactory obf;

    @Structure
    PolygeneSPI spi;

    @Structure
    private ValueBuilderFactory vbf;

    @Structure
    private QueryBuilderFactory qbf;

    @Uses
    private Class<T> entityType;

    @Uses
    ListPropertyControl<T> listCtrl;

    @Uses
    CompositePane<T> compositePane;

    @Uses
    ActionBar<T> actionBar;

    private boolean dirty;

    public void loadAll()
    {
        ObservableList<T> items = FXCollections.observableArrayList();
        UnitOfWork uow = uowf.currentUnitOfWork();
        QueryBuilder<T> builder = qbf.newQueryBuilder(entityType);
        Query<T> query = uow.newQuery(builder);
        EntityNameComparator sorter = obf.newObject(EntityNameComparator.class);
        query.stream()
            .filter(Objects::nonNull)
            .sorted(sorter)
            .forEach(items::add);
        listCtrl.setValue(items);
        items.subscribe(() -> dirty = true);
    }

    private void onSave(ActionEvent actionEvent)
    {
        save();
    }

    private void save()
    {
        try
        {
            if (uowf.isUnitOfWorkActive())
            {
                uowf.currentUnitOfWork().complete();
            }
            actionBar.setDefaultState();
            compositePane.clearForm();
            listCtrl.setDisable(false);
            listCtrl.clear();
            dirty = false;
        } finally
        {
            uowf.newUnitOfWork();
        }
    }

    private void onCancel(ActionEvent actionEvent)
    {
        cancel();
    }

    private void cancel()
    {
        try
        {
            if (uowf.isUnitOfWorkActive())
                uowf.currentUnitOfWork().discard();
            actionBar.setDefaultState();
            compositePane.clearForm();
            listCtrl.setDisable(false);
        } finally
        {
            uowf.newUnitOfWork();
        }
    }

    @Override
    public void initialize()
    {
        actionBar.addSaveActionHandler(this::onSave);
        actionBar.addCancelActionHandler(this::onCancel);
        listCtrl.addSelectionHandler(this::onSelection);
    }

    private void onSelection(ObservableValue<? extends T> source, T oldValue, T newValue)
    {
        if (dirty)
        {
            askToSaveOrAbandonChanges();
        }
        compositePane.updateWith(newValue);
    }

    private void askToSaveOrAbandonChanges()
    {
        Alert cd = new Alert(Alert.AlertType.CONFIRMATION, "Entity has changed. Save?", ButtonType.CANCEL, ButtonType.NO, ButtonType.YES);
        Optional<ButtonType> response = cd.showAndWait();
        ButtonType buttonType = response.orElse(null);
        if( buttonType != null)
        {
            if (buttonType == ButtonType.NO)
                cancel();
            if (buttonType == ButtonType.YES)
                save();
        }
    }
}
