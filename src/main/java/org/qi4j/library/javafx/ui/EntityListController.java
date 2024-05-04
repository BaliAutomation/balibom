package org.qi4j.library.javafx.ui;

import java.util.List;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.scene.control.ChoiceDialog;
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
import org.qi4j.library.javafx.support.EntityNameComparator;

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
        query.stream()
            .sorted(obf.newObject(EntityNameComparator.class))
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
        if (uowf.isUnitOfWorkActive())
        {
            uowf.currentUnitOfWork().complete();
            uowf.newUnitOfWork();
        }
        actionBar.setDefaultState();
        compositePane.clearForm();
        listCtrl.setDisable(false);
        listCtrl.clear();
        dirty = false;
    }

    private void onCancel(ActionEvent actionEvent)
    {
        cancel();
    }

    private void cancel()
    {
        if (uowf.isUnitOfWorkActive())
            uowf.currentUnitOfWork().discard();
        actionBar.setDefaultState();
        compositePane.clearForm();
        listCtrl.setDisable(false);
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
        // TODO; if it is dirty, ask to Save
        String SAVE = "Save changes";
        List<String> choices = List.of(SAVE, "Discard changes", "Cancel");
        ChoiceDialog<String> cd = new ChoiceDialog<>(SAVE, choices);
        String choice = cd.showAndWait().orElse(null);
        if (choice != null)
        {
            if (choice.equals(SAVE))
            {
                save();
            } else
            {
                cancel();
            }
        }
    }
}
