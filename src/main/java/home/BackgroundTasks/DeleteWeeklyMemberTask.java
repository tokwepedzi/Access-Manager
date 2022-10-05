package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.ShortTermMembershipModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static home.Constants.WEEKLY_MEMBERSHIP_TABLE;

public class DeleteWeeklyMemberTask extends Task<ShortTermMembershipModel> {

    private final ShortTermMembershipModel shortTermMembershipModel;

    public DeleteWeeklyMemberTask(ShortTermMembershipModel shortTermMembershipModel) {
        this.shortTermMembershipModel = shortTermMembershipModel;
    }

    @Override
    protected ShortTermMembershipModel call() throws Exception {
        System.out.println("Started Deleting from Delete Weekly Member BG Task!");

        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        try {
            String query = "DELETE * FROM " + WEEKLY_MEMBERSHIP_TABLE + " WHERE idnum = " + "'" + shortTermMembershipModel.getIdnum() + "'";
            preparedStatement = connection.prepareStatement(query);
           preparedStatement.executeQuery();
            while (resultSet.next()) {
                System.out.println("Deleting from Delete Weekly Member BG Task!");
            }
        }catch (SQLException throwables){
            throwables.printStackTrace();

        }

        System.out.println("Ended Deleting from Delete Weekly Member BG Task!");
        return shortTermMembershipModel;
    }
}
