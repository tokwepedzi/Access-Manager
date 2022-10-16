package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.PaymentModelObject;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Statement;

import static home.Constants.PAYMENTS_TABLE;


public class RunPaymentsReportTask extends Task<ObservableList<PaymentModelObject>> {
    private boolean isGettingAllPayments = false;

public RunPaymentsReportTask(boolean isGettingAll){
    this.isGettingAllPayments = isGettingAll;
}

    @Override
    protected ObservableList<PaymentModelObject> call() throws Exception {
        //Use case switch statement to decide filter Logic???----CHECK ACCESS COUNTER TASK FOR EXAMPLE
        boolean getAllUnFiltered = isGettingAllPayments;
        PaymentModelObject paymentModelObject = null;
        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        String getAllQuery = "SELECT*FROM "+PAYMENTS_TABLE;
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(getAllQuery);
        while(resultSet.next()){
            paymentModelObject = (new PaymentModelObject(
               resultSet.getString("transactionid"),resultSet.getString("paymentdate"),
                    resultSet.getString("paymentmonthdate"),resultSet.getString("paymentamount"),
                    resultSet.getString("monthlypayablesubscriptionssum"),resultSet.getString("accountname"),
                    resultSet.getString("accountnum"),resultSet.getString("bankaccountnum"),
                    resultSet.getString("idnum"),resultSet.getString("packagename"),
                    resultSet.getString("startdate"),resultSet.getString("enddate"),
                    resultSet.getString("monthsduration"),resultSet.getString("monthselapsed"),
                    resultSet.getString("payableelapsed"),resultSet.getString("amountpaidtodate"),
                    resultSet.getString("contractvalue"),resultSet.getString("accountbalancebefore"),
                    resultSet.getString("accountbalanceafter"),resultSet.getString("description")
            ));

        }
        return null;
    }
}
