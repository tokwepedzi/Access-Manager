package home.Services;

import home.BackgroundTasks.UpdateSubscriptionsTask;
import home.Models.SubscriptionModel;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UpdateSubscriptionsService extends Service<ObservableList<SubscriptionModel>> {
    private final ObservableList<SubscriptionModel> subscriptionModelObservableList;

    public UpdateSubscriptionsService(ObservableList<SubscriptionModel> subscriptionModelObservableList) {
        this.subscriptionModelObservableList = subscriptionModelObservableList;
    }

    @Override
    protected Task<ObservableList<SubscriptionModel>> createTask() {
        //System.out.println("Update Subscription Service started successfully");
        return new UpdateSubscriptionsTask(subscriptionModelObservableList);
    }
}
