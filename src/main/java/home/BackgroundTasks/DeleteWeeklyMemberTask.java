package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.WeeklyMemberModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static home.Constants.SUBSCRIPTIONS_TABLE;
import static home.Constants.WEEKLY_MEMBERSHIP_TABLE;

public class DeleteWeeklyMemberTask extends Task<WeeklyMemberModel> {

    private final WeeklyMemberModel weeklyMemberModel;

    public DeleteWeeklyMemberTask(WeeklyMemberModel weeklyMemberModel) {
        this.weeklyMemberModel = weeklyMemberModel;
    }

    @Override
    protected WeeklyMemberModel call() throws Exception {
        System.out.println("Started Deleting from Delete Weekly Member BG Task!");

        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "DELETE * FROM " + WEEKLY_MEMBERSHIP_TABLE + " WHERE idnum = " + "'" + weeklyMemberModel.getIdnum() + "'";
            preparedStatement = connection.prepareStatement(query);
           preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Deleting from Delete Weekly Member BG Task!");
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();

        }

        System.out.println("Ended Deleting from Delete Weekly Member BG Task!");
        return weeklyMemberModel;
    }
}
