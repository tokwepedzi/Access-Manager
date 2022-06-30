package home.Services;

import home.BackgroundTasks.AlterSubscriberTask;
import home.Models.SubscriptionModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AlterSubscriberService extends Service<SubscriptionModel> {
    private final SubscriptionModel subscriptionModel;
    public AlterSubscriberService(SubscriptionModel subscriptionModel) {
        this.subscriptionModel = subscriptionModel;
    }

    @Override
    protected Task<SubscriptionModel> createTask() {
       // System.out.println("ALTER Subscriber Service started successfully");

        return new AlterSubscriberTask(subscriptionModel);
    }
}
