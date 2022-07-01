package home;

import com.github.sarxos.webcam.Webcam;
import com.github.sarxos.webcam.WebcamPanel;
import home.BackgroundTasks.GetSecurityLogsTask;
import home.Models.OverrideReasonModel;
import home.Models.SecurityClearanceEventModel;
import home.Models.SystemUser;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.paint.Color;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Circle;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Modality;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.*;
import java.util.Date;

import static home.Constants.*;

public class UsersController implements Initializable {
    @FXML
    private Tab mUsersTab;
    @FXML
    private Button mSaveBtn, mUploadProfilePic, mCaptureImage;
    @FXML
    private TextField mFullname, mCellnumber, mIdnumber, mAddress,
            mEmail, mPassword, mConfirmpassword;
    @FXML
    private DatePicker mDob;
    @FXML
    private ChoiceBox<String> mAuthlevel;
    @FXML
    private ChoiceBox<String> mGender;
    @FXML
    private ImageView mProfilePicImageView;
    @FXML
    private Rectangle mUserPicRec;
    @FXML
    private Button mDeleteBtn;
    @FXML
    private ImageView mDeleteIcon;
    @FXML
    private ImageView mSaveIcon;
    //---------------------------------------------------- USER ACCOUNTS TABLE TAB -------------------------------------------------
    @FXML
    private Tab mManageUsersTab;
    @FXML
    private Button mRefreshUsers,mDeleteUserfronUserAccsTab;
    @FXML
    private TableView<SystemUser> mUsersTable;
    @FXML
    private TableColumn<SystemUser, String> mUidColumn;
    @FXML
    private TableColumn<SystemUser, String> mFullNameColumn;
    @FXML
    private TableColumn<SystemUser, String> mCellNumColumn;
    @FXML
    private TableColumn<SystemUser, String> mIDColumn;
    @FXML
    private TableColumn<SystemUser, String> mDobColumn;
    @FXML
    private TableColumn<SystemUser, String> mAddressColumn;
    @FXML
    private TableColumn<SystemUser, String> mGenderColumn;
    @FXML
    private TableColumn<SystemUser, String> mEmailColumn;
    @FXML
    private TableColumn<SystemUser, String> mAuthLevelColumn;
    @FXML
    private TableColumn<SystemUser, String> mPasswordColumn;
    @FXML
    private TableColumn<SystemUser, String> mProfilePicColumn;


    //-------------------------------------SECURITY LOGS TAB -------------------------------------------------

    @FXML
    private Button mRefreshReasonsBtn,mDeleteReasonBtn,mAddNewReasonBtn;
    @FXML
    private TableView<OverrideReasonModel> mReasonsTable;
    @FXML
    TableColumn<OverrideReasonModel,String>mOverrideReasonClmn;
    @FXML
    private TextField mReasonTextFld;


    @FXML
    private TableView mLogsTableView;
    @FXML
    private Button mRefereshLogsBtn;
    @FXML
    private Button mDeleteLogBtn;
    @FXML
    private ProgressBar mLoadLogsProgress;
    @FXML
    private TextField mSearchLogs;
    @FXML
    private TableColumn<SecurityClearanceEventModel, String> mEventTableColumn;
    @FXML
    private TableColumn<SecurityClearanceEventModel, String> mEventOwnerTableColumn;
    @FXML
    private TableColumn<SecurityClearanceEventModel, String> mAccountTableColumn;
    @FXML
    private TableColumn<SecurityClearanceEventModel, String> mTimeStampTableColumn;
    @FXML
    private TableColumn<SecurityClearanceEventModel, String> mDateTableColumn;
    @FXML
    private TableColumn<SecurityClearanceEventModel, String> mCommentsTableColumn;
    @FXML
    private TableColumn<SecurityClearanceEventModel, String> mAttributesTableColumn;
    @FXML
    private TextArea mCommentsTextArea;

    ObservableList<SecurityClearanceEventModel> clearanceEventModelObservableList = FXCollections.observableArrayList();
    private String[] AuthLevelList = {"Select Access Level", "Administrator", "Supervisor", "Attendant"};
    private String[] GenderList = {"Select Gender", "Male", "Female"};
    private FileInputStream fileInputStream;
    private Image image;
    private File selectedFile;
    private PreparedStatement preparedStatement;
    private ResultSet resultSet = null;
    private DatabaseConnection myDatabaseConnection = new DatabaseConnection();
    private Connection connection = myDatabaseConnection.getDatabaseLinkConnection();
    private boolean isEditing = false;
    private String uidSelectedforEdit = null;
    private boolean cameraIsRunning = false;
    Webcam webcam = Webcam.getDefault();
    WebcamPanel webcamPanel = new WebcamPanel(webcam);
    JFrame window = new JFrame("Web Cam");
    private String selectedReasonid = "";


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        mAuthlevel.getItems().addAll(AuthLevelList);
        mAuthlevel.setValue(AuthLevelList[0]);
        mGender.getItems().addAll(GenderList);
        mGender.setValue(GenderList[0]);

        //Initialize Table Columns for Users accounts table
        mUidColumn.setCellValueFactory(new PropertyValueFactory<>("uid"));
        mFullNameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        mCellNumColumn.setCellValueFactory(new PropertyValueFactory<>("cellnumber"));
        mIDColumn.setCellValueFactory(new PropertyValueFactory<>("idnumber"));
        mDobColumn.setCellValueFactory(new PropertyValueFactory<>("dob"));
        mAddressColumn.setCellValueFactory(new PropertyValueFactory<>("address"));
        mGenderColumn.setCellValueFactory(new PropertyValueFactory<>("gender"));
        mEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        mAuthLevelColumn.setCellValueFactory(new PropertyValueFactory<>("authlevel"));
        mPasswordColumn.setCellValueFactory(new PropertyValueFactory<>("password"));
        mUsersTable.setRowFactory(tv -> {
            TableRow<SystemUser> row = new TableRow<>();
            row.setOnMouseClicked(event -> {
                if (event.getClickCount() == 1 && (!row.isEmpty())) {
                    String rowData = row.getItem().getAddress().toString();
                    upDateFieldsForEdit(row);
                }
            });
            return row;
        });

        //Initialize Table Columns for Override reasons table
        mOverrideReasonClmn.setCellValueFactory(new PropertyValueFactory<>("overridereason"));
        mReasonsTable.setRowFactory(tv ->{
            TableRow<OverrideReasonModel> row = new TableRow<>();
            row.setOnMouseClicked(event ->{
                if(event.getClickCount() == 1 && (!row.isEmpty())){
                    selectedReasonid = row.getItem().getReasonid();
                    System.out.println(" Selected reason is "+ row .getItem().getOverridereason()+ "with ID"+
                            row.getItem().getReasonid());
                }
            });
            return row;
        });

        mReasonTextFld.setTextFormatter(new TextFormatter<>((change )-> {
            change.setText(change.getText().toUpperCase());
            return change;

        }));


        mEventTableColumn.setCellValueFactory(new PropertyValueFactory<>("event"));
        mEventOwnerTableColumn.setCellValueFactory(new PropertyValueFactory<>("eventowner"));
        mAccountTableColumn.setCellValueFactory(new PropertyValueFactory<>("accountnum"));
        mTimeStampTableColumn.setCellValueFactory(new PropertyValueFactory<>("timestamp"));
        mDateTableColumn.setCellValueFactory(new PropertyValueFactory<>("date"));
        mCommentsTableColumn.setCellValueFactory(new PropertyValueFactory<>("comments"));
        mAttributesTableColumn.setCellValueFactory(new PropertyValueFactory<>("attributes"));
        refreshOverrideReasonsTable(null);
        refreshSecurityLogs();
    }

    //refresh override reasons table
    public void refreshOverrideReasonsTable(ActionEvent event){
        System.out.println("Refreshing table");
        String query = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        OverrideReasonModel reasonModel = null;
        ObservableList<OverrideReasonModel> overrideReasonModelObservableList = FXCollections.observableArrayList();
        overrideReasonModelObservableList.clear();
        query = "SELECT * FROM " + OVERRIDE_REASONS_TABLE ;
        try {
            connection = myDatabaseConnection.getDatabaseLinkConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                overrideReasonModelObservableList.add(new OverrideReasonModel(
                        resultSet.getString("reasonid"),
                        resultSet.getString("overridereason")));

            }
            preparedStatement.close();
            resultSet.close();
            mReasonsTable.setItems(overrideReasonModelObservableList);
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }
    //delete the selected reason from the database from reasons table
    public void deleteSelectedReason(ActionEvent event){
        if(!selectedReasonid.isEmpty()) {
            System.out.println("Deleting reason");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this Override reason");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM " + OVERRIDE_REASONS_TABLE + " WHERE reasonid =" + selectedReasonid;
                    connection = myDatabaseConnection.getDatabaseLinkConnection();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();

                } catch (SQLException ex) {
                }

                refreshOverrideReasonsTable(null);
                return;
            }
        }else if(selectedReasonid.isEmpty()){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING!");
            alert.setHeaderText(null);
            alert.setContentText("No Override reason has been selected for deletion, please select reason from the Override reasons table");

        }

    }
    // add new override reason to database in reasons table
    public void addNewOverrideReason(ActionEvent event){
        System.out.println("Adding new reason");

        if (!mReasonTextFld.getText().isEmpty() ) {
            // System.out.println("Membership Package saveClicks");
            String query = "INSERT INTO " + OVERRIDE_REASONS_TABLE + " (overridereason) " +
                    " VALUES(?);";
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, mReasonTextFld.getText().toString());
                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Override reason added successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                refreshOverrideReasonsTable(null);
                mReasonTextFld.clear();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: "+throwables.getMessage());
                alert.showAndWait();
                throwables.printStackTrace();
            }
        }

    }

    private void upDateFieldsForEdit(TableRow<SystemUser> row) {
        //Fill user details fields with with user info on table row click
        isEditing = true;//true == user has clicked a table row for editing
        uidSelectedforEdit = row.getItem().getUid();
        String query1 = null;
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1 = null;
        connection = myDatabaseConnection.getDatabaseLinkConnection();
        query1 = "SELECT profilepicture FROM users_tb WHERE uid = " + row.getItem().getUid();

        InputStream inputStream = null;
        mFullname.setText(row.getItem().getFullname());
        mCellnumber.setText(row.getItem().getCellnumber());
        mIdnumber.setText(row.getItem().getIdnumber());
        mDob.setValue(LocalDate.parse(row.getItem().getDob()));
        mAddress.setText(row.getItem().getAddress());
        mGender.setValue(row.getItem().getGender());
        mEmail.setText(row.getItem().getEmail());
        mAuthlevel.setValue(row.getItem().getAuthlevel());
        mPassword.setText(row.getItem().getPassword());
        mConfirmpassword.setText(row.getItem().getPassword());
        try {
            //getting Profile picture of the selected user from  the database and update ui
            preparedStatement1 = connection.prepareStatement(query1);
            resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {
                inputStream = resultSet1.getBinaryStream("profilepicture");
                OutputStream outputStream = new FileOutputStream(new File("profpic.png"));
                byte[] content = new byte[1024];
                int size = 0;
                while ((size = inputStream.read(content)) != -1) {
                    outputStream.write(content, 0, size);
                }
                image = new Image("file:profpic.png", 100, 150, true, true);
                mProfilePicImageView = new ImageView(image);
                mProfilePicImageView.setFitWidth(100);
                mProfilePicImageView.setFitHeight(150);
                mProfilePicImageView.setPreserveRatio(true);
                mUserPicRec.setFill(new ImagePattern(image));
                outputStream.hashCode();

            }

            inputStream.close();
            connection.close();


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }


    }

    public void mSaveBtnClicked(ActionEvent event) {
        //Validate text input fields
        if (!validateFullNameInput() | !validateCellNumberInput() | !validateIdNumberInput() | !validateDobInput()
                | !validateAddressInput() | !validateGenderInput() | !validateEmaiInput() | !validateAuthLevelInput()
                | !validatePasswordInput() | !validatePasswordConfirmInput()) {
            return;
        } else if (isEditing == false && uidSelectedforEdit == null) {
            // Save new User details
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                String query = "INSERT INTO " + USERS_TABLE + "(fullname,cellnumber,idnumber,dob,address,gender,email,authlevel,password,profilepicture)" +
                        "VALUES(?,?,?,?,?,?,?,?,?,?)";
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, mFullname.getText());
                preparedStatement.setString(2, mCellnumber.getText());
                preparedStatement.setString(3, mIdnumber.getText());
                preparedStatement.setString(4, mDob.getValue().toString());
                preparedStatement.setString(5, mAddress.getText());
                preparedStatement.setString(6, mGender.getValue().toString());
                preparedStatement.setString(7, mEmail.getText());
                if (mAuthlevel.getValue().equals("Administrator")) {
                    preparedStatement.setString(8, "1");
                }else if(mAuthlevel.getValue().equals("Supervisor")){
                    preparedStatement.setString(8, "2");
                }else if(mAuthlevel.getValue().equals("Attendant")){
                    preparedStatement.setString(8, "3");
                }

               /* "(title,name,surname,idnumber,address," +
                        "cellnumber,email,occupation,nextofkin,nextofkincell,memberaccountnumber,contractnumber," +
                        "mc,member1name,member1idnumber,member1accountnumber,member2name,member2idnumber," +
                        "member2accountnumber,startdate,cardfee,joiningfee,totalreceived,upfrontpayment,bankname," +
                        "branchcode,bankaccountnumber,bankaccounttype,debitorderdate,payerdetails,payeridnumber," +
                        "payercellnumber,payeremail,membershipdescription,minimumduration,profilepicture,membersignature," +
                        "payersignature)"*/

                preparedStatement.setString(9, mPassword.getText());
                fileInputStream = new FileInputStream(selectedFile);
                preparedStatement.setBinaryStream(10, fileInputStream, (int) selectedFile.length());
                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("User information added successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                clearFields();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (SQLException throwables) {
                throwables.getCause();
                throwables.printStackTrace();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error:");
                alert.setHeaderText(null);
                alert.setContentText("Error:" + e.getMessage());
                alert.showAndWait();

            }
        } else if (isEditing == true & uidSelectedforEdit != null) {
            // Handling instance where Profile Picture is also being edited
            if (selectedFile != null) {
                try {
                    fileInputStream = new FileInputStream(selectedFile);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
            }


            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                //Alter only non Image Fields and save to Database
                String query = "UPDATE " + USERS_TABLE + " " +
                        "SET fullname = " + "'" + mFullname.getText() + "' " + "," +
                        "cellnumber = " + "'" + mCellnumber.getText() + "' " + "," +
                        "idnumber = " + "'" + mIdnumber.getText() + "' " + "," +
                        "dob = " + "'" + mDob.getValue() + "' " + "," +
                        "address = " + "'" + mAddress.getText() + "' " + "," +
                        "gender = " + "'" + mGender.getValue() + "' " + "," +
                        "email = " + "'" + mEmail.getText() + "' " + "," +
                        "authlevel = " + "'" + mAuthlevel.getValue() + "' " + "," +
                        "password = " + "'" + mPassword.getText() + "' " +
                        "WHERE uid = " + uidSelectedforEdit + ";";

                //Alteration includes Profile picture field, save to Database
                String query2 = "UPDATE " + USERS_TABLE + " " +
                        "SET fullname = " + "'" + mFullname.getText() + "' " + "," +
                        "cellnumber = " + "'" + mCellnumber.getText() + "' " + "," +
                        "idnumber = " + "'" + mIdnumber.getText() + "' " + "," +
                        "dob = " + "'" + mDob.getValue() + "' " + "," +
                        "address = " + "'" + mAddress.getText() + "' " + "," +
                        "gender = " + "'" + mGender.getValue() + "' " + "," +
                        "email = " + "'" + mEmail.getText() + "' " + "," +
                        "authlevel = " + "'" + mAuthlevel.getValue() + "' " + "," +
                        "profilepicture = " + "?" + "," +
                        "password = " + "'" + mPassword.getText() + "' " +
                        "WHERE uid = " + uidSelectedforEdit + ";";
                if (selectedFile == null) {
                    preparedStatement = connection.prepareStatement(query);
                } else {
                    preparedStatement = connection.prepareStatement(query2);
                    preparedStatement.setBinaryStream(1, fileInputStream, (int) selectedFile.length());
                }
                preparedStatement.execute();

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("User information added successfully");
                alert.showAndWait();

                preparedStatement.close();
                clearFields();
                connection.close();
            } catch (SQLException throwables) {
                throwables.getCause();
                throwables.printStackTrace();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Error:");
                alert.setHeaderText(null);
                alert.setContentText("Error:" + e.getMessage());
                alert.showAndWait();
            }
        }
    }

    public void deleteUserAccountFromDb(ActionEvent event) {
        if (isEditing == false) {//no record selected for editing
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm?");
            alert.setHeaderText(null);
            alert.setContentText("No record selected for deletion. Please select a record you want to delete");
            alert.showAndWait();

        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this record");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                try {
                    connection = myDatabaseConnection.getDatabaseLinkConnection();
                    String query = "DELETE FROM " + USERS_TABLE + " WHERE uid =" + uidSelectedforEdit;
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();

                } catch (SQLException ex) {
                }
            }
            clearFields();
            refreshUsersTable(null);
        }

    }

    private void clearFields() {
        mFullname.setText("");
        mCellnumber.setText("");
        mIdnumber.setText("");
        mDob.setValue(null);
        mAddress.setText("");
        mGender.setValue("");
        mEmail.setText("");
        mAuthlevel.setValue("");
        mPassword.setText("");
        mConfirmpassword.setText("");
        mUserPicRec.setFill(Color.TRANSPARENT);

    }

    public ImageView uploadImageAction(ActionEvent event) {
       // mUserPicRec.setStroke(Color.TRANSPARENT);
        try {
            FileChooser fileChooser = new FileChooser();
            selectedFile = fileChooser.showOpenDialog(null);
            if (selectedFile != null) {
                // image = new Image(selectedFile.toURI().toString(), 100, 150, true, true);//path,PrefWidth,PrefHeight,PreserverRatio,Smooth
                // mProfilePicImageView = new ImageView(image);
                // mProfilePicImageView.setFitWidth(100);
                //mProfilePicImageView.setFitHeight(150);
                //mProfilePicImageView.setPreserveRatio(true);

                URL url = selectedFile.toURI().toURL();
                mUserPicRec.setFill(new ImagePattern(new Image(url.toExternalForm())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }


    public ImageView captureImageWithCam(ActionEvent event) {
        mUserPicRec.setStroke(Color.TRANSPARENT);
        try {

            // webcam.setViewSize(WebcamResolution.VGA.getSize());

            if (!cameraIsRunning) {
                webcamPanel.setFPSDisplayed(true);
                webcamPanel.setDisplayDebugInfo(true);
                webcamPanel.setImageSizeDisplayed(true);
                webcamPanel.setMirrored(true);

                window.add(webcamPanel);
                window.setResizable(true);
                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                window.pack();
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                cameraIsRunning = true;
            } else {
                String path = "C:\\Gym Proctor\\Webcam Images\\" + new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());

                File file = new File(path);
                if (!file.exists()) {
                    try {
                        file.mkdirs();
                    } catch (Exception e) {
                        e.printStackTrace();
                       // System.out.println("Error creating Directory: " + e.getMessage());
                    }
                }
                ImageIO.write(webcam.getImage(), "PNG", new File(path));
                selectedFile = file;
                URL url = selectedFile.toURI().toURL();
                mUserPicRec.setFill(new ImagePattern(new Image(url.toExternalForm())));
                //webcam.close();
                cameraIsRunning = false;

            }

        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;

    }

    public void refreshUsersTable(Event event) {
        String query = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        SystemUser systemUser = null;

       /* memberuid,title, name, surname, idnumber, address, cellnumber, email, occupation, nextofkin, nextofkincell,
                memberaccountnumber, contractnumber, mc, member1name, member1idnumber, member1accountnumber, member2name,
                member2idnumber, member2accountnumber, startdate, cardfee, joiningfee, totalreceived, upfrontpayment, bankname,
                branchcode, bankaccountnumber, bankaccounttype, debitorderdate, payerdetails, payeridnumber,
                payercellnumber, payeremail, membershipdescription, minimumduration, profilepicture,membercontractdoc,gender
                ,telnumber,paymentmethod;*/


        ObservableList<SystemUser> UsersList = FXCollections.observableArrayList();
        UsersList.clear();
        query = "SELECT * FROM " + USERS_TABLE + " ORDER BY 11";
        try {
            connection = myDatabaseConnection.getDatabaseLinkConnection();
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                UsersList.add(new SystemUser(
                        resultSet.getString("uid"),
                        resultSet.getString("fullname"),
                        resultSet.getString("cellnumber"),
                        resultSet.getString("idnumber"),
                        resultSet.getString("dob"),
                        resultSet.getString("address"),
                        resultSet.getString("gender"),
                        resultSet.getString("email"),
                        resultSet.getString("authlevel"),
                        resultSet.getString("password"),
                        resultSet.getString("profilepicture")));
            }
            preparedStatement.close();
            resultSet.close();
            mUsersTable.setItems(UsersList);
            connection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void refreshSecurityLogs() {
       // System.out.println("REFRESHING LOG ENTRIES");
        final GetSecurityLogsTask task = new GetSecurityLogsTask();

        mLoadLogsProgress.progressProperty().bind(task.progressProperty());
        mLoadLogsProgress.visibleProperty().bind(task.runningProperty());

        task.valueProperty().addListener(new ChangeListener<ObservableList<SecurityClearanceEventModel>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<SecurityClearanceEventModel>> observableValue, ObservableList<SecurityClearanceEventModel> oldValue, ObservableList<SecurityClearanceEventModel> newvalue) {
                clearanceEventModelObservableList = newvalue;
                mLogsTableView.setItems(newvalue);
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();

        FilteredList<SecurityClearanceEventModel> filteredList = new FilteredList<>(clearanceEventModelObservableList,b ->true);
        mSearchLogs.textProperty().addListener((observable, oldValue, newvalue) -> {
            filteredList.setPredicate(securityClearanceEventModel -> {
                //If there is no search value, display all  the members
                if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null) {
                    return true;
                }
                String searchKeyWord = newvalue.toLowerCase();
                if (securityClearanceEventModel.getEventowner().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true; // a match has been found in event owner field
                } else if (securityClearanceEventModel.getAccountnum().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true; // a match has been found in accountnum field
                } else if (securityClearanceEventModel.getDate().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true; // a match has been found in date field
                } else
                    return false;
            });

            //Sort the filtered data
            SortedList<SecurityClearanceEventModel> sortedList = new SortedList<>(filteredList);
            //Bind sorted result with TableView
            sortedList.comparatorProperty().bind(mLogsTableView.comparatorProperty());
            //Apply filtered and sorted data t o the tableVIew
            mLogsTableView.setItems(sortedList);


        });

        //Set Table Row Item Click Listener
        mLogsTableView.setRowFactory(tv -> {
            TableRow<SecurityClearanceEventModel> row = new TableRow<>();
            row.setOnMouseClicked(event1 -> {
                if (event1.getClickCount() == 1 && (!row.isEmpty())) {
                    String comments = row.getItem().getComments().toString();
                    mCommentsTextArea.setWrapText(true);
                    mCommentsTextArea.setText(comments);
                }
            });
            return row;

        });


    }

    public void deleteSelectedLogEntry() {
        System.out.println("DELETING LOG ENTRY");
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.initModality(Modality.APPLICATION_MODAL);
        alert.setTitle("Alert:");
        alert.setHeaderText(null);
        alert.setContentText("You are about to delete all system security logs. Are you certain you want to proceed?");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK) {
            try{
            String query = "DELETE FROM "+ SECURITY_LOGS_TABLE;
             connection = myDatabaseConnection.getDatabaseLinkConnection();
            PreparedStatement statement = null;
            statement = connection.prepareStatement(query);
            statement.execute();
            connection.close();
            statement.close();}catch (SQLException throwables){
                throwables.printStackTrace();
            }

            Alert alert1 = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Security Logs cleared successfully");
            alert.showAndWait();
            refreshSecurityLogs();

        }else if (result.get() == ButtonType.CANCEL) {
            //close and return
            return;
        }

    }


    //Methods to validate fields
    private boolean validateFullNameInput() {
        String val = mFullname.getText().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Name field cannot be empty");
            alert.showAndWait();
            return false;
        } else {

            return true;
        }
    }

    private boolean validateCellNumberInput() {
        String val = mCellnumber.getText().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Cell number is required");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateIdNumberInput() {
        String val = mIdnumber.getText().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("ID number is required");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }

    }

    //------------------------------------------------------------------------------------
    private boolean validateDobInput() {
        String val = mDob.getValue().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Date of birth is required");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAddressInput() {
        String val = mAddress.getText().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("An address is required");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateGenderInput() {
        String val = mGender.getValue().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Gender is required");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateEmaiInput() {
        String val = mEmail.getText().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("An email address is required");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean validateAuthLevelInput() {
        String val = mAuthlevel.getValue().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("An Access level is required");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePasswordInput() {
        String val = mPassword.getText().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Password cannot be empty");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

    private boolean validatePasswordConfirmInput() {
        String val = mConfirmpassword.getText().toString();
        if (val.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Input fields empty");
            alert.setContentText("Password confirmation field cannot be empty");
            alert.showAndWait();
            return false;
        } else {
            return true;
        }
    }

}
