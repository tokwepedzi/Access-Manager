package home;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Rectangle2D;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.stage.Modality;
import javafx.stage.Screen;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Objects;

import static home.SuperCon.AUTH_TB;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception {

       // ScrollPane scrollPane = new ScrollPane();
        Parent root = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/home/fxml/login.fxml")));
        // primaryStage.initStyle(StageStyle.UNDECORATED);
        primaryStage.setTitle("Xpressions Wellness Center");
       // primaryStage.setResizable(false);
        Image icon = new Image("/logoicon.png");
        primaryStage.getIcons().add(icon);
        primaryStage.setScene(new Scene(root, 650, 500));
        //primaryStage.setResizable(false);
        primaryStage.show();

        Rectangle2D rectangle2D = Screen.getPrimary().getVisualBounds();
        primaryStage.setX((rectangle2D.getWidth()-primaryStage.getWidth())/2);
        primaryStage.setY((rectangle2D.getHeight()-primaryStage.getHeight())/4);
        try {


            Connection authConnection = new RemoteAuthConnection().getDatabaseLinkConnection();
            String authquery = "SELECT authstate FROM " + AUTH_TB + " WHERE customername = " + "'" + "Xpressions" + "'";
            Statement statement = authConnection.createStatement();
            ResultSet resultSet = statement.executeQuery(authquery);
            int state =0;

            if (resultSet.next()) {
                state = resultSet.getInt("authstate");
               // System.out.println("THE STATE  IS " + state);
            }

            resultSet.close();
            statement.close();
            authConnection.close();

           if (state==1) {
                ScrollPane scrollPane = new ScrollPane();
                Parent root1 = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/home/fxml/login.fxml")));
                // primaryStage.initStyle(StageStyle.UNDECORATED);
                primaryStage.setTitle("Xpressions Wellness Center");
               // Image icon1 = new Image("/logoicon.png");
                primaryStage.getIcons().add(icon);
                primaryStage.setScene(new Scene(root1, 700, 500));
                primaryStage.show();
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("SYSTEM ERROR! A serious error has occurred with state "+"System denied, "+" contact : pedzi@transactafrica (+27 81 423 6043) " +
                        "for support");
                alert.showAndWait();
               //Close the app if  the remote authstate is set to disallow access, license agreement might have been violeted
                primaryStage.close();

            }
        }catch (SQLException throwables){
            throwables.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR 1 "+throwables.getMessage() + "CAUSED BY"+throwables.getCause());
            alert.showAndWait();
           // primaryStage.close();
            return;
        }catch (Exception e){
            e.printStackTrace();
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR 2 "+e.getMessage() + "CAUSED BY "+e.getCause());
            alert.showAndWait();
           // primaryStage.close();
            return;
        }



    }


    public static void main(String[] args) {
        launch(args);
    }
}
