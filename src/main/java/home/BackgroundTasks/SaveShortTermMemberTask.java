package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.WeeklyMemberModel;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static home.Constants.WEEKLY_MEMBERSHIP_TABLE;

public class SaveWeeklyMemberTask extends Task<WeeklyMemberModel> {

    private final WeeklyMemberModel weeklyMemberModel;

    public SaveWeeklyMemberTask(WeeklyMemberModel weeklyMemberModel) {
        this.weeklyMemberModel = weeklyMemberModel;
    }

    @Override
    protected WeeklyMemberModel call() throws Exception {
        System.out.println("Save 7 day Member Task started successfully in background");

        String query = " INSERT INTO " + WEEKLY_MEMBERSHIP_TABLE + " (fullname,idnum,cellnum,startdate,enddate) " +
                " VALUES(?,?,?,?,?);";
        try {
            Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
            PreparedStatement preparedStatement = null;

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, weeklyMemberModel.getFullname());
            preparedStatement.setString(2, weeklyMemberModel.getIdnum());
            preparedStatement.setString(3, weeklyMemberModel.getCellnum());
            preparedStatement.setString(4, weeklyMemberModel.getStartdate());
            preparedStatement.setString(5, weeklyMemberModel.getEnddate());

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        return null;// todo return reslut here to check whetrher the saving was successful or not
    }
}
