package home;

import home.BackgroundTasks.GetProfilePicTask;
import home.Models.SystemUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.Objects;
import java.util.ResourceBundle;

public class ParentViewController implements Initializable {

    @FXML
    private StackPane contentArea;
    @FXML
    private Label moduleHeadingLabel;
    @FXML
    private Label mUserLabel;
    @FXML
    private Circle mProfilePic;
    @FXML
    private ImageView mLogoutBtn;

    private SystemUser systemUser;
    private boolean status;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private String authlevel = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        systemUser = UserSession.getSystemUser();
        getAndUpdateProfilePicture();
        authlevel = systemUser.getAuthlevel();
        if (!(systemUser == null)) {
            status = true;
            mUserLabel.setText(systemUser.getEmail());

        }
        try {
            ScrollPane scrollPane = new ScrollPane();
            Parent fxmlparent = FXMLLoader.load(Objects.requireNonNull(getClass().getResource("/home/fxml/accessControl.fxml")));

            contentArea.getChildren().removeAll();
            contentArea.getChildren().setAll(fxmlparent);

            moduleHeadingLabel.setText("DASHBOARD");

        } catch (Exception e) {
            e.printStackTrace();
        }


    }

    private void getAndUpdateProfilePicture() {
        final GetProfilePicTask getProfilePicTask = new GetProfilePicTask();

        getProfilePicTask.valueProperty().addListener(new ChangeListener<Image>() {
            @Override
            public void changed(ObservableValue<? extends Image> observableValue, Image image, Image newvalue) {
                /*mProfilePic.setFitWidth(40);
                mProfilePic.setFitHeight(40);
                mProfilePic.setPreserveRatio(true);*/
                mProfilePic.setFill(new ImagePattern(newvalue));
            }
        });
        Thread thread = new Thread(getProfilePicTask);
        thread.setDaemon(true);
        thread.start();

    }


    /*public void setUser(SystemUser user1) {
        this.user = user1;
        mUserLabel.setText(user1.getFullname());
        System.out.println("PARENTVIEW SETTER THE USER IS :" + user1.getFullname() + user1.getEmail()+user1.getCellnumber());
        user = user1;
    }*/
    //Logout Button Clicked
    public void logOutUser() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/login.fxml"));
        root = loader.load();
        LoginController loginController = loader.getController();
        // scene2Controller.displayName(username);
        //dashBoardController.setUser(null);
        UserSession.getUserSessionInstance(null);
        stage = (Stage) mLogoutBtn.getScene().getWindow();
        scene = new Scene(root);
        stage.setScene(scene);
        stage.show();
        UserSession.resetUserSession();

    }

    public void openDashBoard(ActionEvent event) throws IOException {


        try {
                //Check User rights
            if (authlevel.equals("1") || authlevel.equals("2")) {
                Parent fxmlparent = FXMLLoader.load(getClass().getResource("/home/fxml/dashboard.fxml"));
                contentArea.getChildren().removeAll();
                contentArea.getChildren().setAll(fxmlparent);
                moduleHeadingLabel.setText("DASHBOARD");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Access denied! You do not have user rights to access this module");
                alert.showAndWait();
                return;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            logOutUser();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR! "+e.getMessage());
            alert.showAndWait();
        }

    }



    public void openAccessControl(ActionEvent event) throws IOException {
        Parent fxmlparent = FXMLLoader.load(getClass().getResource("/home/fxml/accessControl.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxmlparent);
        moduleHeadingLabel.setText("ACCESS CONTROL");
    }

    public void openMembershipModule(ActionEvent event) throws IOException {
        Parent fxmlparent = FXMLLoader.load(getClass().getResource("/home/fxml/membership.fxml"));
        contentArea.getChildren().removeAll();
        contentArea.getChildren().setAll(fxmlparent);
        moduleHeadingLabel.setText("MEMBERSHIP");
    }

    public void openUsersModule(ActionEvent event) throws IOException {



        try {
            //Check User rights
            if (authlevel.equals("1") || authlevel.equals("2")) {
                Parent fxmlparent = FXMLLoader.load(getClass().getResource("/home/fxml/users.fxml"));
                contentArea.getChildren().removeAll();
                contentArea.getChildren().setAll(fxmlparent);
                moduleHeadingLabel.setText("USERS");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Access denied! You do not have user rights to access this module");
                alert.showAndWait();
                return;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            logOutUser();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR! "+e.getMessage());
            alert.showAndWait();
        }
    }

    public void openSettingsModule(ActionEvent event) throws IOException {


        try {
            //Check User rights
            if (authlevel.equals("1")) {
                Parent fxmlparent = FXMLLoader.load(getClass().getResource("/home/fxml/settings.fxml"));
                contentArea.getChildren().removeAll();
                contentArea.getChildren().setAll(fxmlparent);
                moduleHeadingLabel.setText("SETTINGS");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Access denied! You do not have user rights to access this module");
                alert.showAndWait();
                return;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            logOutUser();

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR! "+e.getMessage());
            alert.showAndWait();
        }
    }

    public void openSystemModule(ActionEvent event) throws IOException {

        /*--------------------- SHOW ALERT DIALOG FOR NOW TILL CODE BELOW CAN BE IMPLEMENTE WHE THE FUNCTIONALIYT */
        //HAS BBEN FINALIZED

        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Info:");
        alert.setHeaderText(null);
        alert.setContentText("Access denied! You do not have user rights to access this module");
        alert.showAndWait();
        return;


/*

        try {
            //Check User rights
            if (authlevel.equals("1")) {
                Parent fxmlparent = FXMLLoader.load(getClass().getResource("/home/fxml/system.fxml"));
                contentArea.getChildren().removeAll();
                contentArea.getChildren().setAll(fxmlparent);
                moduleHeadingLabel.setText("SYSTEM");
            } else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Access denied! You do not have user rights to access this module");
                alert.showAndWait();
                return;

            }
        } catch (NullPointerException e) {
            e.printStackTrace();
            logOutUser(event);

            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR! "+e.getMessage());
            alert.showAndWait();
        }
*/



    }


}
