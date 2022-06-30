package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.UpdateProfileImageModel;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static home.Constants.MEMBERSHIP_TABLE;
import static home.Constants.SUBSCRIPTIONS_TABLE;

public class UpdateProfilePicTask  extends Task<UpdateProfileImageModel> {

    UpdateProfileImageModel profileImageModel = new UpdateProfileImageModel("","",0);

    public UpdateProfilePicTask(UpdateProfileImageModel updateProfileImageModel){
        this.profileImageModel = updateProfileImageModel;
    }
    @Override
    protected UpdateProfileImageModel call() throws Exception {
        String query = "";
        String subscriptions_tbQuery = "";

        if(profileImageModel.getMemberidentifier()==1){
            query = "UPDATE " + MEMBERSHIP_TABLE + " " +
                    "SET profilepicture = " + "'" + profileImageModel.getPathtopic() + "' " +
                    "WHERE memberaccountnumber = "+"'"+profileImageModel.getAccountnumber()+"'"+ ";" ;
            subscriptions_tbQuery = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                    "SET profpic = " + "'" + profileImageModel.getPathtopic() + "' " +
                    "WHERE accountnum = "+"'"+profileImageModel.getAccountnumber()+"'"+";" ;

        }else  if(profileImageModel.getMemberidentifier()==2){
            query = "UPDATE " + MEMBERSHIP_TABLE + " " +
            "SET pic1 = " + "'" + profileImageModel.getPathtopic() + "' " +
                    "WHERE member1accountnumber = "+"'"+profileImageModel.getAccountnumber()+"'"+ ";" ;
            subscriptions_tbQuery = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                    "SET profpic1 = " + "'" + profileImageModel.getPathtopic() + "' " +
                    "WHERE subaccount1 = "+"'"+profileImageModel.getAccountnumber()+"'"+";" ;

        }else if (profileImageModel.getMemberidentifier()==3){
            query = "UPDATE " + MEMBERSHIP_TABLE + " " +
            "SET pic2 = " + "'" + profileImageModel.getPathtopic() + "' " +
                    "WHERE member2accountnumber = "+"'"+profileImageModel.getAccountnumber()+"'"+";" ;
            subscriptions_tbQuery = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                    "SET profpic2 = " + "'" + profileImageModel.getPathtopic() + "' " +
                    "WHERE subaccount2 = "+"'"+profileImageModel.getAccountnumber()+"'"+";" ;

        }

        try {
            Connection connection4 = new DatabaseConnection().getDatabaseLinkConnection();
            PreparedStatement preparedStatement4 = connection4.prepareStatement(query);
            preparedStatement4.execute();
            System.out.println("ACC NUM "+profileImageModel.getAccountnumber());
            System.out.println("PATH TO PIC  "+profileImageModel.getPathtopic());
            System.out.println("ID "+profileImageModel.getMemberidentifier());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }


        try {
            Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
            PreparedStatement preparedStatement = connection.prepareStatement(subscriptions_tbQuery);
            preparedStatement.execute();
            System.out.println("ACC NUM "+profileImageModel.getAccountnumber());
            System.out.println("PATH TO PIC  "+profileImageModel.getPathtopic());
            System.out.println("ID "+profileImageModel.getMemberidentifier());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return null;
    }
}
