package home;


import home.Models.SubscriptionModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.net.URL;
import java.util.ResourceBundle;

public class PaymentsController implements Initializable {
    @FXML
    private Label mAccountName, mAccountNumber, mOverdueAmount;
    @FXML
    private ChoiceBox<String> mPaymentDescription;
    @FXML
    private TextField mPaymentAmount;
    @FXML
    private Button mTenderBtn;

    private Stage paymentsStage;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }

    public void getAccount(SubscriptionModel selectedSubscriberAccount) {
        //paymentsStage = (Stage) url.Node().getWindow();
        //SubscriptionModel subscriptionModel = (SubscriptionModel) paymentsStage.getUserData();
        System.out .println("Account name: "+selectedSubscriberAccount.getAccountname()+" Account number: "+selectedSubscriberAccount.getAccountnum());
    }

    public void tender() {
        System.out.println("Tender button Invoked");
    }
}
