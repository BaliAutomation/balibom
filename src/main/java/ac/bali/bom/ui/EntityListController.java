package ac.bali.bom.ui;

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
import org.apache.polygene.api.usecase.Usecase;
import org.apache.polygene.api.usecase.UsecaseBuilder;
import org.apache.polygene.api.value.ValueBuilderFactory;
import org.apache.polygene.spi.PolygeneSPI;

import java.util.List;

import static ac.bali.bom.ui.EntityListController.StateMachine.*;

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

    StateMachine stateMachine = defaultState;

    public void loadAll()
    {
        Usecase usecase = UsecaseBuilder.newUsecase("loadAll - " + entityType.getSimpleName());
        ObservableList<T> items = FXCollections.observableArrayList();
        try (UnitOfWork uow = uowf.newUnitOfWork(usecase))
        {
            QueryBuilder<T> builder = qbf.newQueryBuilder(entityType);
            Query<T> query = uow.newQuery(builder);
            query.forEach(entity -> items.add(uow.toValue(entityType, entity)));
        }
        listCtrl.setValue(items);
        stateMachine = stateMachine.loadAll();
    }

    private void onNew(ActionEvent actionEvent)
    {
        actionBar.onNew();
        listCtrl.setDisable(true);
        compositePane.clearForm();
        stateMachine = stateMachine.neww();
    }

    private void onSave(ActionEvent actionEvent)
    {
        Usecase usecase = UsecaseBuilder.newUsecase("onSave Action - " + entityType.getSimpleName());
        try (UnitOfWork uow = uowf.newUnitOfWork(usecase))
        {
            save(uow);
            uow.complete();
        }
        actionBar.setDefaultState();
        compositePane.clearForm();
        listCtrl.setDisable(false);
        stateMachine = stateMachine.save();
    }

    private void onCancel(ActionEvent actionEvent)
    {
        actionBar.setDefaultState();
        compositePane.clearForm();
        listCtrl.setDisable(false);
        stateMachine = stateMachine.cancel();
    }

    private void onDelete(ActionEvent actionEvent)
    {
        compositePane.clearForm();
        listCtrl.setDisable(true);
        Usecase usecase = UsecaseBuilder.newUsecase("onDelete Action - " + entityType.getSimpleName());
        try (UnitOfWork uow = uowf.newUnitOfWork(usecase))
        {
            listCtrl.selected(item ->
            {
                T entity = uow.toEntity(entityType, item);
                uow.remove(entity);
            });
            uow.complete();
        }
        actionBar.setDefaultState();
        listCtrl.setDisable(false);
        stateMachine = stateMachine.delete();
    }

    public void onDirty(ActionEvent event)
    {
        stateMachine = stateMachine.edit();
    }

    public void save(UnitOfWork uow)
    {
        if (stateMachine == editing || stateMachine == adding)
        {
            T valueComposite = compositePane.toValue();
            T entity = uow.toEntity(entityType, valueComposite);
        }
        stateMachine.save();
    }

    @Override
    public void initialize() throws Exception
    {
        actionBar.addNewActionHandler(this::onNew);
        actionBar.addSaveActionHandler(this::onSave);
        actionBar.addCancelActionHandler(this::onCancel);
        actionBar.addDeleteActionHandler(this::onDelete);
        actionBar.addNewActionHandler(this::onNew);
        listCtrl.addEventHandler(PropertyControl.DirtyEvent.ANY, this::onDirty);
        listCtrl.addSelectionHandler(this::onSelection);
    }

    private void onSelection(ObservableValue<? extends T> source, T oldValue, T newValue)
    {
        if (stateMachine == editing || stateMachine == adding)
        {
            askToSaveOrAbandonChanges(newValue);
        }
        compositePane.updateWith(newValue);
    }

    private void askToSaveOrAbandonChanges(T newValue)
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
                Usecase usecase = UsecaseBuilder.newUsecase("onSelection -> Save after Prompt");
                try(UnitOfWork uow = uowf.newUnitOfWork(usecase))
                {
                    save(uow);
                }
                stateMachine = stateMachine.save();
            } else
            {
                stateMachine = stateMachine.cancel();
            }
        }
    }

    enum StateMachine
    {
        defaultState, adding, editing;

        StateMachine cancel()
        {
            return defaultState;
        }

        StateMachine save()
        {
            return defaultState;
        }

        StateMachine neww()
        {
            return adding;
        }

        StateMachine delete()
        {
            return defaultState;
        }

        StateMachine loadAll()
        {
            return defaultState;
        }

        public StateMachine edit()
        {
            return editing;
        }
    }
}
