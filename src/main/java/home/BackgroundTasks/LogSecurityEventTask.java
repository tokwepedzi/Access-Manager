package home.BackgroundTasks;

import home.DatabaseConnection;
import home.GlobalMethods;
import home.Models.SecurityClearanceEventModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;

import static home.Constants.SECURITY_LOGS_TABLE;

public class LogSecurityEventTask extends Task<SecurityClearanceEventModel> {
    private final SecurityClearanceEventModel securityClearanceEventModel;

    public LogSecurityEventTask(SecurityClearanceEventModel securityClearanceEventModel) {
        this.securityClearanceEventModel = securityClearanceEventModel;
    }


    @Override
    protected SecurityClearanceEventModel call() throws Exception {
        System.out.println("Logging task started in background WITH LOG"+securityClearanceEventModel.getComments());
        try {


        //DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        String timestamp = (GlobalMethods.getTodaysDateWithTimeStampAsStringFromDb());
        String todaydate = (GlobalMethods.getTodaysDateAsStringFromDb());
        String query = "INSERT INTO " + SECURITY_LOGS_TABLE + " (event,eventowner,accountnum,timestamp,date,attributes,comments) " +
                " VALUES (?,?,?,?,?,?,?);";
        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement = null;

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, securityClearanceEventModel.getEvent().toString());
            preparedStatement.setString(2, securityClearanceEventModel.getEventowner().toString());
            preparedStatement.setString(3, securityClearanceEventModel.getAccountnum().toString());
            preparedStatement.setString(4, timestamp.toString());
            preparedStatement.setString(5, todaydate);
            preparedStatement.setString(6, securityClearanceEventModel.getAttributes().toString());
            preparedStatement.setString(7, securityClearanceEventModel.getComments().toString());

            preparedStatement.execute();
            //preparedStatement.close();
            //connection.close();
        } catch (Exception throwables) {
            throwables.printStackTrace();
        }
        System.out.println("Logging task ENDED :::: in background");
        return null;
    }
}

