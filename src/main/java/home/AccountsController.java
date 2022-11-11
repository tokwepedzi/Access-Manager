package home;

import home.BackgroundTasks.RunPaymentsReportTask;
import home.Models.PaymentModelObject;
import home.Services.UploadPaymentsService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;
import java.io.File;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    @FXML
    private Button mImportBtn;
    @FXML
    private Button mRunPaymentsReportBtn;
    @FXML
    private ProgressBar mImportProgressBar;
    @FXML
    private Label mImportNotes;
    @FXML
    private ImageView mExcelIcon;
    @FXML
    private TextField mLastColumnNum;
    @FXML
    private TableView<PaymentModelObject> mPaymentsTableView;
    @FXML
    private TableColumn<PaymentModelObject,String> mTransactionIdCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mPaymentDateCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mPaymentMonthDateCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mPaymentAmountCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mTotalSubscriptionSumCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mAccountNameCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mAccountNumCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mBankAccountNumCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mIdNumCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mSubscriptionPackageCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mStartDateCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mEndDateCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mMonthsDurationCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mMonthsElapsedCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mPayableElapsedCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mAmountPaidToDateCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mContractValeCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mBalanceBeforePaymentCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mBalanceAfterPaymentCol;
    @FXML
    private TableColumn<PaymentModelObject,String> mPaymentDescriptionCol;
    @FXML
    private TextField mSearchPaymentsFilter;
    @FXML
    private DatePicker mStartDatePicker, mEndDatePicker;
    @FXML
    private RadioButton mFilterRadioButton;
    @FXML
    private RadioButton mFilterRadioButton1;
    private ObservableList<PaymentModelObject> paymentModelObjectObservableList = FXCollections.observableArrayList();





    private File selectedFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mExcelIcon.setVisible(false);
        mImportProgressBar.setVisible(false);

        //Map table columns to titles
        mTransactionIdCol.setCellValueFactory(new PropertyValueFactory<>("transactionid"));
        mPaymentDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentdate"));
        mPaymentMonthDateCol.setCellValueFactory(new PropertyValueFactory<>("paymentmonthdate"));
        mPaymentAmountCol.setCellValueFactory(new PropertyValueFactory<>("paymentamount"));
        mTotalSubscriptionSumCol.setCellValueFactory(new PropertyValueFactory<>("monthlypayablesubscriptionssum"));
        mAccountNameCol.setCellValueFactory(new PropertyValueFactory<>("accountname"));
        mAccountNumCol.setCellValueFactory(new PropertyValueFactory<>("accountnum"));
        mBankAccountNumCol.setCellValueFactory(new PropertyValueFactory<>("bankaccountnum"));
        mIdNumCol.setCellValueFactory(new PropertyValueFactory<>("idnum"));
        mSubscriptionPackageCol.setCellValueFactory(new PropertyValueFactory<>("packagename"));
        mStartDateCol.setCellValueFactory(new PropertyValueFactory<>("startdate"));
        mEndDateCol.setCellValueFactory(new PropertyValueFactory<>("enddate"));
        mMonthsDurationCol.setCellValueFactory(new PropertyValueFactory<>("monthsduration"));
        mMonthsElapsedCol.setCellValueFactory(new PropertyValueFactory<>("monthselapsed"));
        mPayableElapsedCol.setCellValueFactory(new PropertyValueFactory<>("payableelapsed"));;
        mAmountPaidToDateCol.setCellValueFactory(new PropertyValueFactory<>("amountpaidtodate"));
        mContractValeCol.setCellValueFactory(new PropertyValueFactory<>("contractvalue"));
        mBalanceBeforePaymentCol.setCellValueFactory(new PropertyValueFactory<>("accountbalancebefore"));
        mBalanceAfterPaymentCol.setCellValueFactory(new PropertyValueFactory<>("accountbalanceafter"));
        mPaymentDescriptionCol.setCellValueFactory(new PropertyValueFactory<>("description"));


    }

    public void importPaymentsFromExcel(ActionEvent actionEvent){
        System.out.println("Import button Clicked!");
        try{
            FileChooser fileChooser = new FileChooser();
            selectedFile = fileChooser.showOpenDialog(null);

            //get file URL
            //URL url = selectedFile.getAbsoluteFile().toURI().toURL();
           // System.out.println("FILE URL: "+url+" NAME:"+selectedFile.getName());
            mImportNotes.setText(selectedFile.getPath());
           // if(FilenameUtils.getExtension(selectedFile))
            mExcelIcon.setVisible(true);
            mImportProgressBar.setVisible(true);
            String path = "C:\\Gym Proctor\\Webcam Images\\debit.xlsx";

            //start upload payments service and pass the path to excel sheet to the service
            UploadPaymentsService service = new UploadPaymentsService();

            mImportProgressBar.progressProperty().bind(service.progressProperty());
            mImportProgressBar.visibleProperty().bind(service.runningProperty());

            //Listen for value property from service
            service.valueProperty().addListener(new ChangeListener<ObservableList<PaymentModelObject>>() {
                @Override
                public void changed(ObservableValue<? extends ObservableList<PaymentModelObject>> observableValue, ObservableList<PaymentModelObject> paymentModelObjects, ObservableList<PaymentModelObject> newValue) {
                   paymentModelObjectObservableList = newValue;
                   mPaymentsTableView.setItems(newValue);

                }
            });
           // service.setPath(url+"");
            service.setPath(path);

            service.setLastRowNum(Integer.parseInt(mLastColumnNum.getText())-1);
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("NOTIFICATION:");
            alert.setHeaderText(null);
            alert.setContentText("Service is about to start");
            alert.showAndWait();

            service.start();

        }catch (Exception e){

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR P2:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: "+e.getMessage()+" TRACE: "+e.getStackTrace());
            alert.showAndWait();
            e.printStackTrace();
            throw new RuntimeException(e);

        }



    }

    public void mRunPaymentsReportBtn(ActionEvent actionEvent){
        try{
        System.out.println("RUN reports BTN Clicked");
        boolean getAllPayments = true;
        final RunPaymentsReportTask runPaymentsReportTask = new RunPaymentsReportTask(getAllPayments);
        runPaymentsReportTask.valueProperty().addListener(new ChangeListener<ObservableList<PaymentModelObject>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<PaymentModelObject>> observableValue, ObservableList<PaymentModelObject> paymentModelObjects, ObservableList<PaymentModelObject> newValue) {
                paymentModelObjectObservableList = newValue;
                mPaymentsTableView.setItems(newValue);

            }
        });

        Thread thread = new Thread(runPaymentsReportTask);
        thread.setDaemon(true);
        thread.start();}
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("ERROR P2:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR: "+e.getMessage()+" TRACE: "+e.getStackTrace());
            alert.showAndWait();
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }
}
