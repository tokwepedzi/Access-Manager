package home;


import home.Models.MembershipModel;
import home.Models.PaymentModelObject;
import home.Models.SubscriptionModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static home.Constants.MEMBERSHIP_TABLE;

public class PaymentsController implements Initializable {
    @FXML
    private AnchorPane mAnchor;
    @FXML
    private Label mAccountName, mAccountNumber, mOverdueAmount;
    @FXML
    private ChoiceBox<String> mPaymentDescription;
    @FXML
    private TextField mPaymentAmount;
    @FXML
    private Button mTenderBtn;

    private Stage paymentsStage;
    private SubscriptionModel selectedSubscriberAccount;
    private MembershipModel selectedMembershipModel;
    private String[] paymentDescriptionList = {"SUBSCRIPTION", "OTHER"};


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        //Initialize payment description choice box
        mPaymentDescription.getItems().addAll(paymentDescriptionList);
        mPaymentDescription.setValue(paymentDescriptionList[0]);

    }

    public void getAccount(SubscriptionModel subscriptionModel) {
        selectedSubscriberAccount = subscriptionModel;

        // set label fields with text
        mAccountName.setText(subscriptionModel.getAccountname());
        mAccountNumber.setText(subscriptionModel.getAccountnum());
        mOverdueAmount.setText(subscriptionModel.getAccountbalance());
    }


    public void submitTender() {
        //Get the Member the membership object from the database (membership table)
        String query = "SELECT * FROM "+MEMBERSHIP_TABLE+" WHERE accountnum = "+"'"+selectedSubscriberAccount.getAccountnum()+"'";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getDatabaseLinkConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getDatabaseLinkConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                selectedMembershipModel = new MembershipModel(
                        resultSet.getString("memberuid"),
                        resultSet.getString("title"), resultSet.getString("name"),
                        resultSet.getString("surname"), resultSet.getString("idnumber"),
                        resultSet.getString("address"), resultSet.getString("cellnumber"),
                        resultSet.getString("email"), resultSet.getString("occupation"),
                        resultSet.getString("nextofkin"), resultSet.getString("nextofkincell"),
                        resultSet.getString("memberaccountnumber"), resultSet.getString("contractnumber"),
                        resultSet.getString("mc"), resultSet.getString("member1name"),
                        resultSet.getString("member1idnumber"), resultSet.getString("member1accountnumber"),
                        resultSet.getString("member2name"), resultSet.getString("member2idnumber"),
                        resultSet.getString("member2accountnumber"), resultSet.getString("startdate"),
                        resultSet.getString("cardfee"), resultSet.getString("joiningfee"),
                        resultSet.getString("totalreceived"), resultSet.getString("upfrontpayment"),
                        resultSet.getString("bankname"), resultSet.getString("bankaccountnumber"),
                        resultSet.getString("bankaccounttype"), resultSet.getString("debitorderdate"),
                        resultSet.getString("payerdetails"),
                        resultSet.getString("payeridnumber"), resultSet.getString("payercellnumber"),
                        resultSet.getString("payeremail"), resultSet.getString("membershipdescription"),
                        resultSet.getString("minimumduration"), resultSet.getString("profilepicture"),
                        resultSet.getString("membercontractdoc"), resultSet.getString("gender"), resultSet.getString("telnumber"),
                        resultSet.getString("paymentmethod"), resultSet.getString("accountstatus"),
                        resultSet.getString("pic1"), resultSet.getString("pic2"), resultSet.getString("paymenttype")
                );
            }

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        String date = GlobalMethods.getTodaysDateAsStringFromDb();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
        System.out.println(date);

        PaymentModelObject paymentModelObject = new PaymentModelObject("", date,
                LocalDate.parse(date, dateTimeFormatter).getMonth().toString() +
                        LocalDate.parse(date, dateTimeFormatter).getYear(), mPaymentAmount.getText().toString(),
                String.valueOf(Float.parseFloat(selectedSubscriberAccount.getSubscriptionfee()
                        + Float.parseFloat(selectedSubscriberAccount.getSubscriptionfee1() +
                        Float.parseFloat(selectedSubscriberAccount.getSubaccount2())))),
                selectedSubscriberAccount.getAccountname(),selectedSubscriberAccount.getAccountnum(),
                selectedMembershipModel.getBankaccountnumber(),selectedSubscriberAccount.getIdnumber(),
                selectedSubscriberAccount.getPackagename(),selectedSubscriberAccount.getStartdate(),
                selectedSubscriberAccount.getEnddate(),selectedSubscriberAccount.getMonthsduration(),
                selectedSubscriberAccount.getMonthselapsed(),selectedSubscriberAccount.getElapsedamount(),
                selectedSubscriberAccount.getTotalpaid(),selectedSubscriberAccount.getContractvalue(),
                selectedSubscriberAccount.getAccountbalance()

                );
    }
}
