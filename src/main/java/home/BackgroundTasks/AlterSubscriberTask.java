package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.SubscriptionModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;

import static home.Constants.SUBSCRIPTIONS_TABLE;

public class AlterSubscriberTask extends Task<SubscriptionModel> {
    private final SubscriptionModel subscriptionModel;
    private SubscriptionModel subscriptionModel1, exixstingSubscriptionModel;

    public AlterSubscriberTask(SubscriptionModel subscriptionModel) {
        this.subscriptionModel = subscriptionModel;
    }

    @Override
    protected SubscriptionModel call() {
        subscriptionModel1 = subscriptionModel;
        System.out.println("ALTER Subscriptions Task started successfully");
        Connection connection1 = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1 = null;
        try {
            String query1 = "SELECT * FROM " + SUBSCRIPTIONS_TABLE + " WHERE memberuid = " + "'" + subscriptionModel.getMemberuid() + "'";
            preparedStatement1 = connection1.prepareStatement(query1);
            resultSet1 = preparedStatement1.executeQuery();
            //get a copy of the already stored subscription model
            while (resultSet1.next()) {
                exixstingSubscriptionModel = new SubscriptionModel(
                        resultSet1.getString("memberuid"), resultSet1.getString("accountname"), resultSet1.getString("cellnum"),
                        resultSet1.getString("idnumber"), resultSet1.getString("idnumber1"), resultSet1.getString("idnumber2"),
                        resultSet1.getString("accountnum"), resultSet1.getString("subaccount1"), resultSet1.getString("subaccount2"),
                        resultSet1.getString("packagename"), resultSet1.getString("subscriptionfee"), resultSet1.getString("subscriptionfee1"),
                        resultSet1.getString("subscriptionfee2"), resultSet1.getString("paymethod"), resultSet1.getString("dueday"),
                        resultSet1.getString("accesscount"), resultSet1.getString("accesscount1"), resultSet1.getString("accesscount2"),
                        resultSet1.getString("startdate"), resultSet1.getString("enddate"), resultSet1.getString("daysleft"),
                        resultSet1.getString("debitorderday"), resultSet1.getString("nextduedate"), resultSet1.getString("accountbalance"),
                        resultSet1.getString("adjustmentdate"), resultSet1.getString("accountstatus"), resultSet1.getString("profpic"),
                        resultSet1.getString("profpic1"), resultSet1.getString("profpic2"),resultSet1.getString("monthsduration"),
                        resultSet1.getString("monthselapsed"),resultSet1.getString("contractvalue"),resultSet1.getString("totalpaid")
                        ,resultSet1.getString("elapsedamount")
                );
            }

            //These fields cannot be edited whe Altering an existing account hence setting them to the current existing values //todo Add alter dialog to show this in membership on amount field click listener
            subscriptionModel1.setAccountbalance(exixstingSubscriptionModel.getAccountbalance());
            subscriptionModel1.setAdjustmentdate(exixstingSubscriptionModel.getAdjustmentdate());
            subscriptionModel1.setAccountstatus(exixstingSubscriptionModel.getAccountstatus());
            subscriptionModel1.setMonthselapsed(exixstingSubscriptionModel.getMonthselapsed());
            subscriptionModel1.setTotalpaid(exixstingSubscriptionModel.getTotalpaid());
            subscriptionModel1.setElapsedamount(exixstingSubscriptionModel.getElapsedamount());
            resultSet1.close();
            preparedStatement1.close();
            connection1.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }





       /* Connection connection2 = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement2 = null;
        ResultSet resultSet2 = null;
        try {
            String query2 = "SELECT * FROM " + MEMBERSHIP_TABLE + " WHERE memberuid = " + "'" + subscriptionModel.getMemberuid() + "'";
            preparedStatement2 = connection1.prepareStatement(query2);
            resultSet2 = preparedStatement2.executeQuery();
            while (resultSet2.next()) {
                 subscriptionModel1 = new SubscriptionModel(
                         resultSet2.getString("memberuid"), resultSet2.getString("name")+ " "+resultSet2.getString("surname"), resultSet2.getString("cellnumber"),
                         resultSet2.getString("idnumber"), resultSet2.getString("member1idnumber"), resultSet2.getString("member2idnumber"),
                         resultSet2.getString("memberaccountnumber"), resultSet2.getString("member1accountnumber"), resultSet2.getString("member2accountnumber"),
                         resultSet2.getString("membershipdescription"), subscriptionModel.getSubscriptionfee(), subscriptionModel.getSubscriptionfee1(),
                        subscriptionModel.getSubscriptionfee2(), resultSet2.getString("paymentmethod"), subscriptionModel.getDueday(),
                        subscriptionModel.getAccesscount(), subscriptionModel.getAccesscount1(), subscriptionModel.getAccesscount2(),
                         resultSet2.getString("startdate"), subscriptionModel.getEnddate(), subscriptionModel.getDaysleft(),
                        subscriptionModel.getDebitorderday(), subscriptionModel.getNextduedate(), subscriptionModel.getAccountbalance(),
                        subscriptionModel.getAdjustmentdate(), resultSet2.getString("accountstatus")
                );
            }

            resultSet2.close();
            preparedStatement1.close();
            connection1.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }*/


        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement;
        DateFormat dateFormat = new SimpleDateFormat("dd-MM-yyyy");
        try {
            String query = null;
            try {
                query = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                        "SET accountname = " + "'" + subscriptionModel1.getAccountname() + "' " + "," +
                        "cellnum = " + "'" + subscriptionModel1.getCellnum() + "' " + "," +
                        "idnumber = " + "'" + subscriptionModel1.getIdnumber() + "' " + "," +
                        "idnumber1 = " + "'" + subscriptionModel1.getIdnumber1() + "' " + "," +
                        "idnumber2 = " + "'" + subscriptionModel1.getIdnumber2() + "' " + "," +
                        "packagename = " + "'" + subscriptionModel1.getPackagename() + "' " + "," +
                        "subscriptionfee = " + "'" + subscriptionModel1.getSubscriptionfee() + "' " + "," +
                        "subscriptionfee1 = " + "'" + subscriptionModel1.getSubscriptionfee1() + "' " + "," +
                        "subscriptionfee2 = " + "'" + subscriptionModel1.getSubscriptionfee2() + "' " + "," +
                        "paymethod = " + "'" + subscriptionModel1.getPaymethod() + "' " + "," +
                        "dueday = " + "'" + subscriptionModel1.getDueday() + "' " + "," +
                        "startdate =" + "'" + dateFormat.parse(subscriptionModel1.getStartdate()) + "' " + "," +
                        "enddate = " + "'" + dateFormat.parse( subscriptionModel1.getEnddate()) + "' " + "," +
                        "daysleft = " + "'" + subscriptionModel1.getDaysleft() + "' " + "," +
                        "debitorderday = " + "'" + subscriptionModel1.getDebitorderday() + "' " + "," +
                        "nextduedate = " + "'" + dateFormat.parse(subscriptionModel1.getNextduedate() )+ "' " + "," +
                        "accountbalance = " + "'" + exixstingSubscriptionModel.getAccountbalance() + "' " + "," +
                        "adjustmentdate = " + "'" + dateFormat.parse(exixstingSubscriptionModel.getAdjustmentdate()) + "' " + "," +
                        "monthsduration = " + "'" + subscriptionModel1.getMonthsduration() + "' " + "," +
                        "accountstatus = " + "'" + exixstingSubscriptionModel.getAccountstatus() + "' " +
                        "WHERE memberuid = " + subscriptionModel1.getMemberuid() + ";";
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }


            preparedStatement = connection.prepareStatement(query);

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
            System.out.println("ALTER Subscriptions Task ENDED successfully");
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return subscriptionModel1;
    }
}
