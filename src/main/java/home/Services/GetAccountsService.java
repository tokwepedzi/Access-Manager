package home.Services;

import home.BackgroundTasks.GetAccountsTask;
import home.Models.MemberSearchModel;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class GetAccountsService extends Service<ObservableList<MemberSearchModel>> {
    @Override
    protected Task<ObservableList<MemberSearchModel>> createTask() {
        return new GetAccountsTask();
    }
}
