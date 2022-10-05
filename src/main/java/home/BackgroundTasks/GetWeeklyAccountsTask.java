package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.ShortTermMembershipModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static home.Constants.WEEKLY_MEMBERSHIP_TABLE;

public class GetWeeklyAccountsTask extends Task<ObservableList<ShortTermMembershipModel>> {


    @Override
    protected ObservableList<ShortTermMembershipModel> call() {
        ObservableList<ShortTermMembershipModel> observableList = FXCollections.observableArrayList();
        try{
        System.out.println("STARTED REFRESH WEEKLY ACCOUNTS IN BACKGROUND TASK");
        String query = "SELECT COUNT(*) AS mycount FROM " + WEEKLY_MEMBERSHIP_TABLE + " ;";
        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(query);

        int count = 0;
        if (resultSet.next()) {
            count = resultSet.getInt("mycount");
        }

        resultSet.close();
        connection.close();

        Connection connection1 = new DatabaseConnection().getDatabaseLinkConnection();

        String query1 = " SELECT fullname,idnum,cellnum,startdate,enddate FROM " + WEEKLY_MEMBERSHIP_TABLE +
                " ORDER BY fullname ASC; ";

            Statement statement1 = connection1.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(query1);

            while (resultSet1.next()) {
                String fullanme = resultSet1.getString("fullname");
                String idnum = resultSet1.getString("idnum");
                String cellnum = resultSet1.getString("cellnum");
                String startdate = resultSet1.getString("startdate");
                String enddate = resultSet1.getString("enddate");
                observableList.add(new ShortTermMembershipModel(fullanme, idnum, cellnum, startdate, enddate));
                updateProgress(observableList.size(), count);
                updateValue(observableList);
            }
            statement1.close();
            connection1.close();

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        return observableList;
    }

}
