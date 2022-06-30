package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.SecurityClearanceEventModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static home.Constants.SECURITY_LOGS_TABLE;

public class GetSecurityLogsTask extends Task<ObservableList<SecurityClearanceEventModel>> {
    @Override
    protected ObservableList<SecurityClearanceEventModel> call() throws Exception {

        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        String countLogsquery = " SELECT COUNT(*) AS mycount FROM " + SECURITY_LOGS_TABLE + " ;";
        Statement statement = connection.createStatement();
        ResultSet resultSet = statement.executeQuery(countLogsquery);

        int count = 0;

        if (resultSet.next()) {
            count = resultSet.getInt("mycount");
        }

        resultSet.close();
        connection.close();
        ObservableList<SecurityClearanceEventModel> observableList = FXCollections.observableArrayList();

        try {


            Connection connection1 = new DatabaseConnection().getDatabaseLinkConnection();


            String query1 = "SELECT event, eventowner, accountnum, timestamp, date, attributes, comments FROM " +
                    SECURITY_LOGS_TABLE + " ORDER BY date DESC";

            Statement statement1 = connection1.createStatement();
            ResultSet resultSet1 = statement1.executeQuery(query1);

            while (resultSet1.next()) {
                String event = resultSet1.getString("event");
                String eventowner = resultSet1.getString("eventowner");
                String accountnum = resultSet1.getString("accountnum");
                String timestamp = resultSet1.getString("timestamp");
                String date = resultSet1.getString("date");
                String attributes = resultSet1.getString("attributes");
                String comments = resultSet1.getString("comments");

                //populate observable list with securityclearancemodel objects
                observableList.add(new SecurityClearanceEventModel(event, eventowner, accountnum, timestamp, date, attributes, comments));
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
