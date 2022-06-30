package home;

import home.Models.SystemUser;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.sql.*;
import java.util.ArrayList;
import java.util.ResourceBundle;
import static home.Constants.USERS_TABLE;

public class LoginController implements Initializable {

    @FXML
    private Button loginBtn;
    @FXML
    private Button cancelBtn;
    @FXML
    private Label invalidLoginMessageLabel;
    @FXML
    private ChoiceBox<String> userNameChoiceBox;
    @FXML
    private PasswordField passwordField;
    @FXML
    private Label mErrorMessage;


    //declaring variables
    private ArrayList<String> userList;
    private Stage stage;
    private Scene scene;
    private Parent root;
    private DatabaseConnection myDatabaseConnection = new DatabaseConnection();
    private Connection connection = myDatabaseConnection.getDatabaseLinkConnection();
    PreparedStatement preparedStatement;
    private SystemUser user;


    //Login button onclickListener
    public void setLoginBtnOnAction(ActionEvent event) throws IOException {
        String username = userNameChoiceBox.getValue();
        String password = passwordField.getText().toString();
        String getUserPasswordquery = "SELECT password FROM "+USERS_TABLE+ " WHERE fullname = "+ "'"+ username+"'" ;
        //String getUserPasswordquery = "SELECT password FROM users WHERE fullname = 'Tokwe Pedzisai'";
        String storedPassword = "";
        try {
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getUserPasswordquery);
            while (resultSet.next()) {
                storedPassword = resultSet.getString(1);
            }
           // System.out.println(storedPassword);
        } catch (Exception e) {
           // System.out.println(storedPassword);
            e.printStackTrace();
            e.getCause();
        }
         if(password.equals("init#system")){
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/settings.fxml"));
            root = loader.load();
            SettingsController settingsController = loader.getController();
            // scene2Controller.displayName(username);
            //dashBoardController.setUser(null);
            UserSession.getUserSessionInstance(null);
            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();

        }
        else if (userNameChoiceBox.getValue() == null || passwordField.getText().isEmpty()) {
            mErrorMessage.setText("Username or Password cannot be empty!");
            return;
        } else if (storedPassword.equals(password)) {

            String query = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            user = new SystemUser();

            query = "SELECT * FROM " + USERS_TABLE + " WHERE fullname = "+ "'"+ username+"'" ;
            try {
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    user.setUid(resultSet.getString("uid"));
                    user.setFullname(resultSet.getString("fullname"));
                    user.setCellnumber(resultSet.getString("cellnumber"));
                    user.setIdnumber(resultSet.getString("idnumber"));
                    user.setDob(resultSet.getString("dob"));
                    user.setAddress(resultSet.getString("address"));
                    user.setGender(resultSet.getString("gender"));
                    user.setEmail(resultSet.getString("email"));
                    user.setAuthlevel(resultSet.getString("authlevel"));
                    user.setPassword(resultSet.getString("password"));
                    user.setProfilepicture(null);
                }
                UserSession.getUserSessionInstance(user);
                UserSession.setSystemUser(user);
                preparedStatement.close();
                resultSet.close();
                connection.close();

            }
            catch (SQLException throwables){
                throwables.printStackTrace();

            }

            //send user to Dashboard,
            //TODO-Create a user object and send it to the next screen for UI Display
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/home/fxml/parentView.fxml"));
            root = loader.load();
            ParentViewController dashBoardController = loader.getController();
            // scene2Controller.displayName(username);
            //dashBoardController.setUser(user);


            stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            scene = new Scene(root);
            stage.setScene(scene);
            stage.show();
        } else if (!storedPassword.equals(password)) {
            mErrorMessage.setText("Wrong password, please try again");
            return;
        }
    }

    public void setCanceBtnOnAction(ActionEvent event) {
        Stage stage = (Stage) cancelBtn.getScene().getWindow();
        stage.close();
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        userList = new ArrayList<>();
        try {
            String getAllUsersTableQuery = "SELECT * FROM " + USERS_TABLE + " ORDER BY fullname";
            Statement statement = connection.createStatement();
            ResultSet resultSet = statement.executeQuery(getAllUsersTableQuery);
            userList.clear();
            while (resultSet.next()) {
                userList.add(resultSet.getString(2));
                //resultSet.close();
            }
            userNameChoiceBox.getItems().addAll(userList);
            userNameChoiceBox.setValue(userList.get(0));
            return;
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
    }

    public void getSelectedUserAndLogin(ActionEvent event) {
        String username = userNameChoiceBox.getValue();

    }
}
