package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.SystemUser;
import home.UserSession;
import javafx.concurrent.Task;
import javafx.scene.image.Image;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static home.Constants.USERS_TABLE;

public class GetProfilePicTask extends Task<Image> {
    //private final SystemUser systemUser
private Image profileImage;


    @Override
    protected Image call() throws Exception {
        System.out.println("PICTURE TASK STARTED");
        SystemUser systemUser = UserSession.getSystemUser();
        String query = null;
        InputStream inputStream =null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;

        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
        query = "SELECT * FROM " + USERS_TABLE + " WHERE fullname = "+ "'"+ systemUser.getFullname()+"'" ;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {

                    inputStream = resultSet.getBinaryStream("profilepicture");
                    OutputStream outputStream = new FileOutputStream(new File("profpic.png"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = inputStream.read(content)) != -1) {
                        outputStream.write(content, 0, size);
                    }
                    profileImage = new Image("file:profpic.png", 100, 150, true, true);
                    outputStream.hashCode();
                    inputStream.close();
            }

        }
        catch (SQLException throwables){
            throwables.printStackTrace();
        }
        System.out.println("PICTURE TASK ENDED !!!!!!!!");
        return profileImage;
    }
}
