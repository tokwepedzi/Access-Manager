package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.SubscriptionModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static home.Constants.SUBSCRIPTIONS_TABLE;

public class AddSubscriberTask extends Task<SubscriptionModel> {
    private final SubscriptionModel subscriptionModel ;
    private SubscriptionModel exixstingSubscriptionModel;



    public AddSubscriberTask(SubscriptionModel subscriptionModel) {
        this.subscriptionModel = subscriptionModel;
    }

    @Override
    protected SubscriptionModel call() {


        Connection connection1 = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet = null;
        try {
            String query1 = "SELECT * FROM " + SUBSCRIPTIONS_TABLE + " WHERE memberuid = " + "'" + subscriptionModel.getMemberuid() + "'";
            preparedStatement1 = connection1.prepareStatement(query1);
            resultSet = preparedStatement1.executeQuery();
            while (resultSet.next()) {
                exixstingSubscriptionModel = new SubscriptionModel(
                        resultSet.getString("memberuid"), resultSet.getString("accountname"), resultSet.getString("cellnum"),
                        resultSet.getString("idnumber"), resultSet.getString("idnumber1"), resultSet.getString("idnumber2"),
                        resultSet.getString("accountnum"), resultSet.getString("subaccount1"), resultSet.getString("subaccount2"),
                        resultSet.getString("packagename"), resultSet.getString("subscriptionfee"), resultSet.getString("subscriptionfee1"),
                        resultSet.getString("subscriptionfee2"), resultSet.getString("paymethod"), resultSet.getString("dueday"),
                        resultSet.getString("accesscount"), resultSet.getString("accesscount1"), resultSet.getString("accesscount2"),
                        resultSet.getString("startdate"), resultSet.getString("enddate"), resultSet.getString("daysleft"),
                        resultSet.getString("debitorderday"), resultSet.getString("nextduedate"), resultSet.getString("accountbalance"),
                        resultSet.getString("adjustmentdate"), resultSet.getString("accountstatus"),
                        resultSet.getString("profpic"), resultSet.getString("profpic1"),
                        resultSet.getString("profpic2")
                );
            }

            resultSet.close();
            preparedStatement1.close();
            connection1.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }






        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement;

        String query =  " INSERT INTO "+ SUBSCRIPTIONS_TABLE+ " (memberuid,accountname, cellnum, idnumber, idnumber1, idnumber2, accountnum, subaccount1, subaccount2, packagename," +
                "subscriptionfee, subscriptionfee1, subscriptionfee2, paymethod, dueday, accesscount, accesscount1, accesscount2," +
                "startdate, enddate, daysleft, debitorderday, nextduedate, accountbalance,adjustmentdate,profpic,profpic1,profpic2,accountstatus)" + " VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";

        try {
            preparedStatement = connection.prepareStatement(query);

       preparedStatement.setInt(1,Integer.parseInt(subscriptionModel.getMemberuid()));
        preparedStatement.setString(2,subscriptionModel.getAccountname());
        preparedStatement.setString(3,subscriptionModel.getCellnum());
        preparedStatement.setString(4,subscriptionModel.getIdnumber());
        preparedStatement.setString(5,subscriptionModel.getIdnumber1());
        preparedStatement.setString(6,subscriptionModel.getIdnumber2());
        preparedStatement.setString(7,subscriptionModel.getAccountnum());
        preparedStatement.setString(8,subscriptionModel.getSubaccount1());
        preparedStatement.setString(9,subscriptionModel.getSubaccount2());
        preparedStatement.setString(10,subscriptionModel.getPackagename());
        preparedStatement.setFloat(11,Float.parseFloat(subscriptionModel.getSubscriptionfee()));
        preparedStatement.setFloat(12,Float.parseFloat(subscriptionModel.getSubscriptionfee1()));
        preparedStatement.setFloat(13,Float.parseFloat(subscriptionModel.getSubscriptionfee1()));
        preparedStatement.setString(14,subscriptionModel.getPaymethod());
        preparedStatement.setString(15,subscriptionModel.getDueday());
        preparedStatement.setInt(16,Integer.parseInt(subscriptionModel.getAccesscount()));
        preparedStatement.setInt(17,Integer.parseInt(subscriptionModel.getAccesscount1()));
        preparedStatement.setInt(18,Integer.parseInt(subscriptionModel.getAccesscount2()));
        preparedStatement.setString(19,subscriptionModel.getStartdate());
        preparedStatement.setString(20,subscriptionModel.getEnddate());
        preparedStatement.setInt(21,Integer.parseInt(subscriptionModel.getDaysleft()));
        preparedStatement.setInt(22,Integer.parseInt(subscriptionModel.getDebitorderday()));
        preparedStatement.setString(23,subscriptionModel.getNextduedate());
        preparedStatement.setFloat(24,Float.parseFloat(subscriptionModel.getAccountbalance()));

            preparedStatement.setString(25,subscriptionModel.getAdjustmentdate());
            preparedStatement.setBinaryStream(26,null);
            preparedStatement.setBinaryStream(27,null);
            preparedStatement.setBinaryStream(28,null);
            preparedStatement.setString(29,subscriptionModel.getAccountstatus());

        preparedStatement.execute();
        preparedStatement.close();
        connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return subscriptionModel;
    }
}
