package home;

import home.BackgroundTasks.LogSecurityEventTask;
import home.Models.OverrideReasonModel;
import home.Models.SecurityClearanceEventModel;
import home.Models.ShortTermPackageModel;
import home.Models.SystemUser;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ChoiceBox;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import static home.Constants.OVERRIDE_REASONS_TABLE;
import static home.Constants.SHORT_TERM_PACKAGES_TABLE;


public class AccessOverrideController implements Initializable {

    @FXML
    private ChoiceBox<String> mOverrideReason;
    @FXML
    private Button mSubmitOverrideBtn;
    private  Stage stage;
    private Connection connection;
    private ObservableList<OverrideReasonModel> overrideReasonModelObservableList;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            //Run query to fetch short term membership packages
            PreparedStatement preparedStatement = null;
            overrideReasonModelObservableList = FXCollections.observableArrayList();
            String query = "SELECT * FROM " + OVERRIDE_REASONS_TABLE ;
            connection = new DatabaseConnection().getDatabaseLinkConnection();
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = null;
            resultSet = preparedStatement.executeQuery();
            overrideReasonModelObservableList.clear();
            while (resultSet.next()) {
                overrideReasonModelObservableList.add(new OverrideReasonModel(
                        resultSet.getString("reasonid"),
                        resultSet.getString("overridereason")
                ));
            }

            for (int i = 0; i < overrideReasonModelObservableList.size(); i++) {
                mOverrideReason.getItems().add(overrideReasonModelObservableList.get(i).getOverridereason());
            }
           //mOverrideReason.setValue(shortTermPackageList.get(0).getPackagename());
            // Close connections to database for this query
            connection.close();
            preparedStatement.close();
            resultSet.close();
        }catch (SQLException throwables){
            throwables.printStackTrace();
        }catch (IndexOutOfBoundsException e){
            e.printStackTrace();

        }




    }

    public void submitoverrideandgobacktoaccesscontroller(ActionEvent event) throws IOException {
        stage =(Stage) mSubmitOverrideBtn.getScene().getWindow();
        if(event.getTarget()==mSubmitOverrideBtn){
            SecurityClearanceEventModel securityClearanceEventModel = (SecurityClearanceEventModel) stage.getUserData();
            //attach comment from mOverrideReason Choice box
            securityClearanceEventModel.setComments(mOverrideReason.getValue());

            LogSecurityEventTask logSecurityEventTask = new LogSecurityEventTask(securityClearanceEventModel);
            Thread thread = new Thread(logSecurityEventTask);
            thread.setDaemon(true);
            thread.start();
            stage.close();
        }else if (event.getTarget().equals(ButtonType.CLOSE)){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING!");
            alert.setHeaderText(null);
            alert.setContentText("ACTION IS NOT ALLOWED,PLEASE LOG A REASON FOR OVERRIDE");
            Optional<ButtonType> action = alert.showAndWait();
            return;
        }
    }
}
