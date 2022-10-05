package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.ShortTermMembershipModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static home.Constants.WEEKLY_MEMBERSHIP_TABLE;

public class SaveShortTermMemberTask extends Task<ShortTermMembershipModel> {

    private final ShortTermMembershipModel shortTermMembershipModel;

    public SaveShortTermMemberTask(ShortTermMembershipModel shortTermMembershipModel) {
        this.shortTermMembershipModel = shortTermMembershipModel;
    }

    @Override
    protected ShortTermMembershipModel call() throws Exception {
        System.out.println("Save 7 day Member Task started successfully in background");

        String query = " INSERT INTO " + WEEKLY_MEMBERSHIP_TABLE + " (fullname,idnum,cellnum,startdate,enddate) " +
                " VALUES(?,?,?,?,?);";
        try {
            Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
            PreparedStatement preparedStatement = null;

            preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, shortTermMembershipModel.getFullname());
            preparedStatement.setString(2, shortTermMembershipModel.getIdnum());
            preparedStatement.setString(3, shortTermMembershipModel.getCellnum());
            preparedStatement.setString(4, shortTermMembershipModel.getStartdate());
            preparedStatement.setString(5, shortTermMembershipModel.getEnddate());

            preparedStatement.execute();
            preparedStatement.close();
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }



        return null;// todo return reslut here to check whetrher the saving was successful or not
    }
}
