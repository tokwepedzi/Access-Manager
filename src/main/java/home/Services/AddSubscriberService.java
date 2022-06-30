package home.Services;

import home.BackgroundTasks.AddSubscriberTask;
import home.Models.SubscriptionModel;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class AddSubscriberService extends Service<SubscriptionModel> {

    private final SubscriptionModel subscriptionModel ;

    public AddSubscriberService(SubscriptionModel subscriptionModel) {
        this.subscriptionModel = subscriptionModel;
    }

    @Override
    protected Task<SubscriptionModel> createTask() {
      //  System.out.println("Add Subscriber Service started successfully");
        //boolean taskWaSuccessful = true;
        return new AddSubscriberTask(subscriptionModel);
    }
}
