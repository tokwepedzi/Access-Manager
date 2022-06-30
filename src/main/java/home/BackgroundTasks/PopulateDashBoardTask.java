package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.DashboardInfoModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

import static home.Constants.*;

public class PopulateDashBoardTask extends Task<DashboardInfoModel> {

    public DashboardInfoModel dashboardInfoModel;

    @Override
    protected DashboardInfoModel call() throws Exception {
        System.out.println("Populate Dashboard Task Started");
        dashboardInfoModel = new DashboardInfoModel(0,0,0,0);
        LocalDate nowDate = LocalDate.now();
        try {
            Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
            String visitsTodayQuery = "SELECT COUNT(*) AS visits FROM " + SECURITY_LOGS_TABLE + " WHERE date = " + "'" + nowDate + "'";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(visitsTodayQuery);
            int visitstoday = 0;
            if (resultSet.next()) {
                visitstoday = resultSet.getInt("visits");

            }
            dashboardInfoModel.setVisitstoday(visitstoday);

            Connection connection1 = new DatabaseConnection().getDatabaseLinkConnection();
            String currententMembersQuery = "SELECT COUNT(*) AS curren FROM " + MEMBERSHIP_TABLE;
            Statement statement1 = connection1.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(currententMembersQuery);
            int currentmembers = 0;
            if (resultSet1.next()) {
                currentmembers = resultSet1.getInt("curren");

            }
            dashboardInfoModel.setCurrentmembers(currentmembers);


            Connection connection2 = new DatabaseConnection().getDatabaseLinkConnection();
            String overridesTodayQuery = "SELECT COUNT(*) AS overr FROM " +SECURITY_LOGS_TABLE + " WHERE event = " + "'"
                    +"Access Control Override"+"'" +" AND date = "+"'"+nowDate+"'";
            Statement statement2 = connection2.createStatement();
            ResultSet resultSet2 = statement2.executeQuery(overridesTodayQuery);
            int overridestoday = 0;
            if (resultSet2.next()) {
                overridestoday = resultSet2.getInt("overr");

            }
            dashboardInfoModel.setOverridestoday(overridestoday);
            dashboardInfoModel.setExpiringmembers(30);





            Connection connection3 = new DatabaseConnection().getDatabaseLinkConnection();
            String expiryQuery = "SELECT COUNT(*) AS expr FROM " +SUBSCRIPTIONS_TABLE + " WHERE DATEDIFF(day,enddate,"+"'"+nowDate+"'"+") < 30 " ;

            Statement statement3 = connection3.createStatement();
            ResultSet resultSet3 = statement3.executeQuery(expiryQuery);
            int expiring = 0;
            if (resultSet3.next()) {
                expiring = resultSet3.getInt("expr");

            }
            dashboardInfoModel.setExpiringmembers(expiring);
            updateValue(dashboardInfoModel);
            connection.close();
            connection1.close();
            connection2.close();
            System.out.println("Populate Dashboard Task Ended");


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return dashboardInfoModel;
    }
}
