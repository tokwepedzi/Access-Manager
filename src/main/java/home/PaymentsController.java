package home;


import home.Models.MembershipModel;
import home.Models.PaymentModelObject;
import home.Models.SubscriptionModel;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import static home.Constants.*;

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
        String query = "SELECT * FROM " + MEMBERSHIP_TABLE + " WHERE memberaccountnumber = " + "'" + selectedSubscriberAccount.getAccountnum() + "'";
        DatabaseConnection databaseConnection = new DatabaseConnection();
        Connection connection = databaseConnection.getDatabaseLinkConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            connection = databaseConnection.getDatabaseLinkConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            MembershipModel membershipModel = null;
            while (resultSet.next()) {
                        membershipModel = new MembershipModel(
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
            System.out.println(membershipModel.getName()+"TTTTTT");
            selectedMembershipModel = membershipModel;


        } catch (SQLException throwables) {
            throwables.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR P1:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: "+throwables.getMessage()+" TRACE: "+throwables.getStackTrace());
            alert.showAndWait();
        }

        try {
            connection.close();
            preparedStatement.close();
            //resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR P2:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: "+e.getMessage()+" TRACE: "+e.getStackTrace());
            alert.showAndWait();
            throw new RuntimeException(e);

        }


        String date = GlobalMethods.getTodaysDateAsStringFromDb();
        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        System.out.println(date);
        String month = LocalDate.parse(date, dateTimeFormatter).getMonth().toString();
        int year = LocalDate.parse(date, dateTimeFormatter).getYear();
        //get individual subscription fees
        double primaryFee = Double.parseDouble(selectedSubscriberAccount.getSubscriptionfee());
        double secondaryFee1 = Double.parseDouble(selectedSubscriberAccount.getSubscriptionfee1());
        double secondaryFee2 = Double.parseDouble(selectedSubscriberAccount.getSubscriptionfee2());
        double total = primaryFee+secondaryFee1+secondaryFee2;


        PaymentModelObject paymentModelObject = new PaymentModelObject("", date, month + " " + year,
                mPaymentAmount.getText().toString(), Double.toString(total), selectedSubscriberAccount.getAccountname(),
                selectedSubscriberAccount.getAccountnum(), selectedMembershipModel.getBankaccountnumber(), selectedSubscriberAccount.getIdnumber(),
                selectedSubscriberAccount.getPackagename(), selectedSubscriberAccount.getStartdate(),
                selectedSubscriberAccount.getEnddate(), selectedSubscriberAccount.getMonthsduration(),
                selectedSubscriberAccount.getMonthselapsed(), selectedSubscriberAccount.getElapsedamount(),
                Double.toString(Double.parseDouble(selectedSubscriberAccount.getTotalpaid())+Double.parseDouble(mPaymentAmount.getText().toString())), selectedSubscriberAccount.getContractvalue(),
                selectedSubscriberAccount.getAccountbalance(),Double.toString(Double.parseDouble(selectedSubscriberAccount.getAccountbalance())-Double.parseDouble(mPaymentAmount.getText())),
                mPaymentDescription.getValue().toString()

        );


        //Insert transaction into DB payments table
        String queryToInsert = "INSERT INTO " + PAYMENTS_TABLE + "(paymentdate,paymentmonthdate,paymentamount," +
                "monthlypayablesubscriptionssum,accountname,accountnum,bankaccountnum,idnum,packagename," +
                "startdate,enddate,monthsduration,monthselapsed,payableelapsed,amountpaidtodate,contractvalue," +
                "accountbalancebefore,accountbalanceafter,description)" +
                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";


        //ResultSet resultSet1 = null;
        System.out.println("Prepare to submit");

        try {
            DatabaseConnection databaseConnection1 = new DatabaseConnection();
             connection = databaseConnection1.getDatabaseLinkConnection();
            PreparedStatement preparedStatement1 = null;
            System.out.print("Submission started");
            preparedStatement1 = connection.prepareStatement(queryToInsert);
            preparedStatement1.setString(1, paymentModelObject.getPaymentdate());
            preparedStatement1.setString(2, paymentModelObject.getPaymentmonthdate());
            preparedStatement1.setDouble(3, Double.parseDouble(paymentModelObject.getPaymentamount()));
            preparedStatement1.setDouble(4, Double.parseDouble(paymentModelObject.getMonthlypayablesubscriptionssum()));
            preparedStatement1.setString(5, paymentModelObject.getAccountname());
            preparedStatement1.setString(6, paymentModelObject.getAccountnum());
            preparedStatement1.setString(7, paymentModelObject.getBankaccountnum());
            preparedStatement1.setString(8, paymentModelObject.getIdnum());
            preparedStatement1.setString(9, paymentModelObject.getPackagename());
            preparedStatement1.setString(10, paymentModelObject.getStartdate());
            preparedStatement1.setString(11, paymentModelObject.getEnddate());
            preparedStatement1.setInt(12, Integer.parseInt(paymentModelObject.getMonthsduration()));
            preparedStatement1.setInt(13,Integer.parseInt(paymentModelObject.getMonthselapsed()) );
            preparedStatement1.setDouble(14, Double.parseDouble(paymentModelObject.getPayableelapsed()));
            preparedStatement1.setDouble(15,Double.parseDouble(paymentModelObject.getAmountpaidtodate()) );
            preparedStatement1.setDouble(16, Double.parseDouble(paymentModelObject.getContractvalue()));
            preparedStatement1.setDouble(17,Double.parseDouble(paymentModelObject.getAccountbalancebefore()));
            preparedStatement1.setDouble(18,Double.parseDouble(paymentModelObject.getAccountbalanceafter()));
            preparedStatement1.setString(19,paymentModelObject.getDescription());
            preparedStatement1.execute();

            System.out.println("Submission completed");

        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR P3:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: "+e.getMessage()+" TRACE: "+e.getStackTrace());
            alert.showAndWait();
            throw new RuntimeException(e);

        }

        try {
            connection.close();
            //preparedStatement1.close();

        } catch (SQLException e) {

            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR P4:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: "+e.getMessage()+" TRACE: "+e.getStackTrace());
            alert.showAndWait();
            throw new RuntimeException(e);
        }
        System.out.println("End submit");

        //Update Subscriptions_tb with new total paid (and new account balance)? if the subscriptions service doe not auto calc balance using
         String newTotalPaidSoFar = Double.toString(Double.parseDouble(selectedSubscriberAccount.getTotalpaid())+Double.parseDouble(mPaymentAmount.getText().toString()));
            String query2 = "UPDATE "+SUBSCRIPTIONS_TABLE+" SET totalpaid = "+ "'" +newTotalPaidSoFar+"'"+ "WHERE accountnum = "+selectedSubscriberAccount.getAccountnum()+ ";";
        DatabaseConnection databaseConnection1 = new DatabaseConnection();
        connection = databaseConnection1.getDatabaseLinkConnection();
        PreparedStatement preparedStatement1 = null;
        System.out.print("Update started");
        try {
            preparedStatement1 = connection.prepareStatement(query2);
            preparedStatement1.execute();
            System.out.print("Update ended");
        } catch (SQLException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR P5:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: "+e.getMessage()+" TRACE: "+e.getStackTrace());
            alert.showAndWait();
            throw new RuntimeException(e);
        }

    }
}
