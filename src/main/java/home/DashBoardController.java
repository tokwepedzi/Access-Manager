package home;

import home.BackgroundTasks.PopulateDashBoardTask;
import home.Models.DashboardInfoModel;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;

import java.net.URL;
import java.util.ResourceBundle;

public class DashBoardController implements Initializable {


    @FXML
    private Label mCurrentMemberLabel;
    @FXML
    private Label mNearExpireMemberships;
    @FXML
    private Label mVisitsTodayLabel;
    @FXML
    private Label mOverridesLabel;

    private DashboardInfoModel dashboardInfoModel;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {


        updateDashboardValues();


    }

    private void updateDashboardValues() {
        //Initialize dashboard numbers in background  thread
        final PopulateDashBoardTask dashBoardTask = new PopulateDashBoardTask();

        dashBoardTask.valueProperty().addListener(new ChangeListener<DashboardInfoModel>() {
            @Override
            public void changed(ObservableValue<? extends DashboardInfoModel> observableValue, DashboardInfoModel dashboardInfoModel, DashboardInfoModel newValue) {
                dashboardInfoModel = newValue;
                mCurrentMemberLabel.setText(String.valueOf(dashboardInfoModel.getCurrentmembers()));

                mNearExpireMemberships.setText(String.valueOf(dashboardInfoModel.getExpiringmembers()));
                mVisitsTodayLabel.setText(String.valueOf(dashboardInfoModel.getVisitstoday()));
                mOverridesLabel.setText(String.valueOf(dashboardInfoModel.getOverridestoday()));


            }
        });
        Thread thread = new Thread(dashBoardTask);
        thread.setDaemon(true);
        thread.start();
    }
}
