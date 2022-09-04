package home;

import home.BackgroundTasks.AccessCounterTask;
import home.BackgroundTasks.GetWeeklyAccountsTask;
import home.BackgroundTasks.LogSecurityEventTask;
import home.Models.SecurityClearanceEventModel;
import home.Models.SubscriptionModel;
import home.Models.SystemUser;
import home.Models.WeeklyMemberModel;
import home.Services.UpdateSubscriptionsService;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.transformation.FilteredList;
import javafx.collections.transformation.SortedList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.Pane;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.Optional;
import java.util.ResourceBundle;

import static home.Constants.*;
import static home.GlobalMethods.getTodaysDateAsStringFromDb;


public class AccessControlController implements Initializable {

    //----------------------------------------- LONG TERM ACCOUNTS TAB -------------------------------------------
    @FXML
    private TextField mSearchAccount;
    @FXML
    private Label mNameField, mAccessCountField, mAccountNumField, mAccBalanceField,
            mCellNumberField, mContractExpirationField, mPackkageNameField, mDueDateField, mMessageOut;
    @FXML
    private Pane mParentBg, mChildBg;
    @FXML
    private
    Rectangle mSubscriberImageRectangle;
    @FXML
    private ImageView mSubscribermImage;
    @FXML
    private Button mCheckInBtn;
    @FXML
    private Button mOverrideBtn;


    //----------------------------------------- SHORT TERM ACCOUNTS TAB -------------------------------------------

    @FXML
    private ProgressBar mWeeklyAccTableProgress;
    @FXML
    private TextField m7SearchAccount;
    @FXML
    private TableView<WeeklyMemberModel> weeklyMemberModelTableView;
    @FXML
    private TableColumn<WeeklyMemberModel, String> m7NameColumn;
    @FXML
    private TableColumn<WeeklyMemberModel, String> m7CellColumn;
    @FXML
    private TableColumn<WeeklyMemberModel, String> m7StarDateColumn;
    @FXML
    private TableColumn<WeeklyMemberModel, String> m7EndDateColumn;
    @FXML
    private Button m7RfershAccountsBtn;
    @FXML
    private Button m7CheckinBtn,m7OverrideBtn;
    @FXML
    private Button m7DeleteBtn;
    @FXML
    private Label m7OutputMsgLabel;

    private ObservableList<SubscriptionModel> subscriptionModelObservableList = FXCollections.observableArrayList();
    private ObservableList<WeeklyMemberModel> weeklyMemberModelObservableList = FXCollections.observableArrayList();
    private boolean isAllowedToCheckIn = false;

    private int memberIdentifier = 0;
    private SystemUser user;
    private WeeklyMemberModel globalWeeklyMemberModel;
    private TableRow<WeeklyMemberModel> globalRow;

    private Parent root;
    private Stage stage;
    private Scene scene;
    private SystemUser systemUser;
    private String authlevel = "";

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // startUpdatingSubscriptions();
        systemUser = UserSession.getSystemUser();
        authlevel = systemUser.getAuthlevel();
      /*  if(!authlevel.equals("1")){
            m7DeleteBtn.setVisible(false);
        }*/

        mSearchAccount.requestFocus();
        final UpdateSubscriptionsService service = new UpdateSubscriptionsService(subscriptionModelObservableList);
        //Initialize Update subscriptions service in background

        ProgressIndicator p = new ProgressIndicator();
        p.progressProperty().bind(service.progressProperty());

        service.valueProperty().addListener(new ChangeListener<ObservableList<SubscriptionModel>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<SubscriptionModel>> observableValue, ObservableList<SubscriptionModel> subscriptionModelObservableList, ObservableList<SubscriptionModel> newValue) {
                //  System.out.println("Starting Dashboard Service");

            }
        });

        service.start();
        //  System.out.println("Starting Dashboard Service");

        // map account search table colums to titles
        m7NameColumn.setCellValueFactory(new PropertyValueFactory<>("fullname"));
        m7CellColumn.setCellValueFactory(new PropertyValueFactory<>("cellnum"));
        m7StarDateColumn.setCellValueFactory(new PropertyValueFactory<>("startdate"));
        m7EndDateColumn.setCellValueFactory(new PropertyValueFactory<>("enddate"));


       /* for (int i = 0; i < subscriptionModelObservableList.size(); i++) {
            System.out.println("DASH BOARD CONTROLLER UPDATE SUBSCRIPTIONS LISTENER " + subscriptionModelObservableList.get(i).getAccountnum()
                    + " BALANCE " + subscriptionModelObservableList.get(i).getAccountbalance());
        }*/


      /*  for (int i = 0; i < subscriptionModelObservableList.size(); i++) {
            System.out.println("DASH BOARD CONTROLLER UPDATE SUBSCRIPTIONS LISTENER " + subscriptionModelObservableList.get(i).getAccountnum()
                    + " BALANCE " + subscriptionModelObservableList.get(i).getAccountbalance());
        }*/

        clearFileds();
        refreshWeeklyAccountsTable();

        mSearchAccount.setTextFormatter(new TextFormatter<>((change )-> {
            change.setText(change.getText().toUpperCase());
            return change;

        }));


    }



    public void searchForAccount() {
        System.out.println("Typing...." + subscriptionModelObservableList.size());
        FilteredList<SubscriptionModel> filteredList = new FilteredList<>(subscriptionModelObservableList, b -> true);
        mSearchAccount.textProperty().addListener((observable, oldValue, newvalue) -> {
            filteredList.setPredicate(subscriptionModel -> {
                //If there is no search value, display all  the members
                if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null) {

                    return false;
                }
                String searchKeyWord = newvalue.toLowerCase();
                if (subscriptionModel.getAccountnum().toLowerCase().matches(searchKeyWord) ||
                        subscriptionModel.getSubaccount1().toLowerCase().matches(searchKeyWord) ||
                        subscriptionModel.getSubaccount2().toLowerCase().matches(searchKeyWord)) {
                    // System.out.println(subscriptionModel.getAccountname() + " " + subscriptionModel.getAccountnum());
                    setFields(subscriptionModel);
                    return true; //Means a match has been found in account number field
                } else if ((subscriptionModel.getAccountnum().toLowerCase().matches(searchKeyWord) ||
                        subscriptionModel.getSubaccount1().toLowerCase().matches(searchKeyWord) ||
                        subscriptionModel.getSubaccount2().toLowerCase().matches(searchKeyWord)) && subscriptionModel.getAccountstatus().equals("PAUSED") || subscriptionModel.getAccountstatus().equals("SUSPENDED")) {

                    setAlert(subscriptionModel);
                    return true;
                }

                return false;
            });

            /*if(filteredList.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Account not found or does not exist, please confirm entry");
                alert.showAndWait();

                return;

            }*/
        });
    }

    private void setAlert(SubscriptionModel subscriptionModel) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Info:");
        alert.setHeaderText(null);
        alert.setContentText("Not allowed, account status is " + subscriptionModel.getAccountstatus());
        alert.showAndWait();
        return;
    }

    public void checkInSubscriber() {
        if (isAllowedToCheckIn) {
            // System.out.println("Checking In Subscriber");
            String accountnumber = mSearchAccount.getText().toString();
            AccessCounterTask counterTask = new AccessCounterTask(accountnumber, memberIdentifier);
            Thread thread = new Thread(counterTask);
            thread.setDaemon(true);
            thread.start();

            // System.out.println("Logging reason");
            UserSession userSessionInstance = UserSession.getUserSessionInstance(null);
            SystemUser systemUser =
                    userSessionInstance.getSystemUser();

            // System.out.println("THE USER IS :" + systemUser.getFullname());
            SecurityClearanceEventModel securityClearanceEventModel = new SecurityClearanceEventModel(
                    "Access Control Checkin", systemUser.getFullname(), mSearchAccount.getText().toString()
                    , "", "", "Long Term Account", "System allowed");

                    /*AccessCounterTask counterTask = new AccessCounterTask(accountnumber, memberIdentifier);
                    Thread thread = new Thread(counterTask);
                    thread.setDaemon(true);
                    thread.start();*/

            LogSecurityEventTask logSecurityEventTask = new LogSecurityEventTask(securityClearanceEventModel);
            Thread thread1 = new Thread(logSecurityEventTask);
            thread1.setDaemon(true);
            thread1.start();

            clearFileds();
        } else if (!isAllowedToCheckIn) {

            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Access denied for the current member. If you wish to grant access use override " +
                    "and log in the reason for the override");
            alert.showAndWait();

        }
    }

    private void clearFileds() {
       /* String path = "src\\home\\resources\\user_icon.png";

        File picfile = new File(path);
        URL url = null;
        try {
            url = picfile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));*/
        memberIdentifier = 0;
        mSearchAccount.setText("");
        mNameField.setText("");
        mAccessCountField.setText("");
        mAccountNumField.setText("");
        mAccBalanceField.setText("");
        mCellNumberField.setText("");
        mContractExpirationField.setText("");
        mPackkageNameField.setText("");
        mDueDateField.setText("");
        mParentBg.setStyle("-fx-background-color:  #FFFFFF;" + "-fx-background-radius: 20;");
        //Image image = new Image("/user_icon.png");
        // mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
        isAllowedToCheckIn = false;
        mMessageOut.setText("");
        mSearchAccount.requestFocus();


    }

    public void overrideAction() {
        // System.out.println("Override Action on Subscriber");
        if (!isAllowedToCheckIn) {
            try {


                String accountnumber = mSearchAccount.getText().toString();
                AccessCounterTask counterTask = new AccessCounterTask(accountnumber, memberIdentifier);
                Thread thread1 = new Thread(counterTask);
                thread1.setDaemon(true);
                thread1.start();

                mMessageOut.setText("Check in successful");
                SystemUser systemUser = UserSession.getUserSessionInstance(null).getSystemUser();
                // System.out.println("THE USER IS :" + systemUser.getFullname());
                SecurityClearanceEventModel securityClearanceEventModel = new SecurityClearanceEventModel(
                        "Access Control Override", systemUser.getFullname(), mSearchAccount.getText().toString()
                        , "", "", "Long Term Account", "");


                stage = new Stage();
                stage.setResizable(false);
                stage.initStyle(StageStyle.UNDECORATED);
                stage.setUserData(securityClearanceEventModel);
                Parent root = FXMLLoader.load(getClass().getResource("/home/fxml/accessOverride.fxml"));
                Scene scene1 = new Scene(root, 450, 420);
                stage.setScene(scene1);
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.initOwner(m7OverrideBtn.getScene().getWindow());
                stage.showAndWait();
                clearFileds();
                //refreshWeeklyAccountsTable();
            }catch (NullPointerException e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("WARNING!");
                alert.setHeaderText(null);
                alert.setContentText("Null selection: "+e.getMessage());
                alert.showAndWait();
            }catch (Exception e){
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.initModality(Modality.APPLICATION_MODAL);
                alert.setTitle("WARNING!");
                alert.setHeaderText(null);
                alert.setContentText("Error: "+e.getMessage());
                alert.showAndWait();
                e.printStackTrace();
            }
        } else if (isAllowedToCheckIn) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("No need to override, member is up to date");
            alert.showAndWait();
            clearFileds();

        }

        memberIdentifier = 0;
    }

    public void handleKeyPressed(KeyEvent event) {
        if (event.getCode().equals(KeyCode.SHIFT)) {
            checkInSubscriber();
        } else if (event.getCode().equals(KeyCode.BACK_SPACE)) {
            overrideAction();
        }

        // System.out.println("LENTS DO NOTHING ON ENETR TILL WE CAN HANDLE THIS LOGIC PROPERLY");
    }

    private void setFields(SubscriptionModel subscriptionModel) {
       /* mNameField,mAccessCountField,mAccountNumField,mAccBalanceField,
                mCellNumberField,mContractExpirationField,mPackkageNameField,mDueDateField;*/
        // System.out.println(" Filtered Subscriber id " + subscriptionModel.getAccountname());
        // System.out.println(" Account status is " + subscriptionModel.getAccountstatus());
        if (subscriptionModel.getAccountstatus().equals(ACC_STATUS_ACTIVE)) {
            String enteredAccountNumber = mSearchAccount.getText().toString();
            String mainMemberAccount = subscriptionModel.getAccountnum();
            String member1Account = subscriptionModel.getSubaccount1();
            String member2Account = subscriptionModel.getSubaccount2();

            if (enteredAccountNumber.equals(mainMemberAccount)) {
                try {
                    updateUiForMainMember(subscriptionModel);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                return;
            } else if (enteredAccountNumber.equals(member1Account)) {
                try {
                    updateUiForMember1(subscriptionModel);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                return;
            } else if (enteredAccountNumber.equals(member2Account)) {
                try {
                    updateUiForMember2(subscriptionModel);
                } catch (MalformedURLException e) {
                    throw new RuntimeException(e);
                }
                return;
            }


        } else if (subscriptionModel.getAccountstatus().equals(ACC_STATUS_PAUSED)) {
            // System.out.println("The Account has been paused!");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("This account is not active, membership account is on pause");
            alert.showAndWait();
            clearFileds();
            return;
        } else if (subscriptionModel.getAccountstatus().equals(ACC_STATUS_SUSPENDED)) {
            // System.out.println("The Account has been suspended!");
            // System.out.println("The Account has been paused!");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("This account is not active, membership account is on suspension");
            alert.showAndWait();
            clearFileds();
            return;
        }

    }

    public void setUser(SystemUser user) {
        this.user = user;

    }

    private void updateUiForMember2(SubscriptionModel subscriptionModel) throws MalformedURLException {
        //isMember2 = true;
        memberIdentifier = 3;
        LocalDate todaysDate = LocalDate.parse(getTodaysDateAsStringFromDb());
        LocalDate nextdueDate = LocalDate.parse(subscriptionModel.getNextduedate());
        mNameField.setText("PARENT ACCOUNT: " + subscriptionModel.getAccountname());
        mAccessCountField.setText("ACCESS COUNT " + subscriptionModel.getAccesscount2());
        mAccountNumField.setText("SUB ACCOUNT NUMBER: " + subscriptionModel.getSubaccount2());
        mAccBalanceField.setText("ACCOUNT BALANCE: " + subscriptionModel.getAccountbalance());
        mCellNumberField.setText("PARENT CELL NUMBER " + subscriptionModel.getCellnum());
        mContractExpirationField.setText("CONTRACT ENDS IN " + subscriptionModel.getDaysleft() + " DAYS");
        mPackkageNameField.setText("PACKAGE TYPE: " + subscriptionModel.getPackagename());
        mDueDateField.setText("NEXT DUE DATE IS ON :" + subscriptionModel.getNextduedate());
        //f account balance is greater than zero and  the next due date has passed
        //if ((Float.parseFloat(subscriptionModel.getAccountbalance())) > (Float.parseFloat(subscriptionModel.getSubscriptionfee())) && nextdueDate.isBefore(todaysDate)) {
        if ((Float.parseFloat(subscriptionModel.getAccountbalance())) > 0) {
            mParentBg.setStyle("-fx-background-color:  #C80815;" + "-fx-background-radius: 20;");
            isAllowedToCheckIn = false;
        } else {
            mParentBg.setStyle("-fx-background-color:  #228B22;" + "-fx-background-radius: 20;");
            isAllowedToCheckIn = true;

        }
       /* String query = "SELECT pic2 FROM " + MEMBERSHIP_TABLE + " WHERE member2accountnumber = " + "'"
                + subscriptionModel.getSubaccount2() + "'" + ";";
        String columnname = "pic2";*/
        File picfile = new File(subscriptionModel.getProfilepic2());
        URL url = null;
        try {
            url = picfile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
        if((new Image(url.toExternalForm()).isError())){
            picfile = new File(DEFAULT_USER_PIC.toString());
            url = picfile.toURI().toURL();
            mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
        }

        // mSubscriberImageRectangle.setImage(getMemberImage(query, columnname));
    }

    private void updateUiForMember1(SubscriptionModel subscriptionModel) throws MalformedURLException {
        //isMember1 = true;
        memberIdentifier = 2;

        LocalDate todaysDate = LocalDate.parse(getTodaysDateAsStringFromDb());
        LocalDate nextdueDate = LocalDate.parse(subscriptionModel.getNextduedate());
        mNameField.setText("PARENT ACCOUNT: " + subscriptionModel.getAccountname());
        mAccessCountField.setText("ACCESS COUNT " + subscriptionModel.getAccesscount1());
        mAccountNumField.setText("SUB ACCOUNT NUMBER: " + subscriptionModel.getSubaccount1());
        mAccBalanceField.setText("ACCOUNT BALANCE: " + subscriptionModel.getAccountbalance());
        mCellNumberField.setText("PARENT CELL NUMBER " + subscriptionModel.getCellnum());
        mContractExpirationField.setText("CONTRACT ENDS IN " + subscriptionModel.getDaysleft() + " DAYS");
        mPackkageNameField.setText("PACKAGE TYPE: " + subscriptionModel.getPackagename());
        mDueDateField.setText("NEXT DUE DATE IS ON :" + subscriptionModel.getNextduedate());
        //f account balance is greater than zero and  the next due date has passed
        // if ((Float.parseFloat(subscriptionModel.getAccountbalance())) > (Float.parseFloat(subscriptionModel.getSubscriptionfee())) && nextdueDate.isBefore(todaysDate)) {
        if ((Float.parseFloat(subscriptionModel.getAccountbalance())) > 0) {

            mParentBg.setStyle("-fx-background-color:  #C80815;" + "-fx-background-radius: 20;");
            isAllowedToCheckIn = false;
        } else {
            mParentBg.setStyle("-fx-background-color:  #228B22;" + "-fx-background-radius: 20;");
            isAllowedToCheckIn = true;

        }
       /* String query = "SELECT pic1 FROM " + MEMBERSHIP_TABLE + " WHERE member1accountnumber = " + "'"
                + subscriptionModel.getSubaccount1() + "'" + ";";
        String columnname = "pic1";*/

        File picfile = new File(subscriptionModel.getProfilepic1());
        URL url = null;
        try {
            url = picfile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
        if((new Image(url.toExternalForm()).isError())){
            picfile = new File(DEFAULT_USER_PIC.toString());
            url = picfile.toURI().toURL();
            mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
        }


    }

    private void updateUiForMainMember(SubscriptionModel subscriptionModel) throws MalformedURLException {
        //isMainMember = true;
        memberIdentifier = 1;

        LocalDate todaysDate = LocalDate.parse(getTodaysDateAsStringFromDb());
        LocalDate nextdueDate = LocalDate.parse(subscriptionModel.getNextduedate());
        mNameField.setText("NAME: " + subscriptionModel.getAccountname());
        mAccessCountField.setText("ACCESS COUNT " + subscriptionModel.getAccesscount());
        mAccountNumField.setText("ACCOUNT NUMBER: " + subscriptionModel.getAccountnum());
        mAccBalanceField.setText("ACCOUNT BALANCE: " + subscriptionModel.getAccountbalance());
        mCellNumberField.setText("CELL NUMBER " + subscriptionModel.getCellnum());
        mContractExpirationField.setText("CONTRACT ENDS IN " + subscriptionModel.getDaysleft() + " DAYS");
        mPackkageNameField.setText("PACKAGE TYPE: " + subscriptionModel.getPackagename());
        mDueDateField.setText("NEXT DUE DATE IS ON :" + subscriptionModel.getNextduedate());
        //f account balance is greater than zero and  the next due date has passed
        // if ((Float.parseFloat(subscriptionModel.getAccountbalance())) > (Float.parseFloat(subscriptionModel.getSubscriptionfee())) && nextdueDate.isBefore(todaysDate)) {
       // if ((Float.parseFloat(subscriptionModel.getAccountbalance())) > (Float.parseFloat(subscriptionModel.getSubscriptionfee()))) {
        if ((Float.parseFloat(subscriptionModel.getAccountbalance())) > 0) {

            mParentBg.setStyle("-fx-background-color: #C80815;" + "-fx-background-radius: 20;");
            isAllowedToCheckIn = false;

        } else {//User is green
            mParentBg.setStyle("-fx-background-color:  " + MY_COLOR_GREEN + ";-fx-background-radius: 20;");
            isAllowedToCheckIn = true;



/*
            try {
                String query = "UPDATE " + SUBSCRIPTIONS_TABLE + " SET accesscount = accesscount+1 WHERE accountnum = "
                        +"'"+ subscriptionModel.getAccountnum() +"'"+ ";";
                Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
                PreparedStatement statement = null;
                statement = connection.prepareStatement(query);
                statement.execute();
                connection.close();
                statement.close();
                //alreadyExecuted = true;
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }*/

        }

       /* String query = "SELECT profilepicture FROM " + MEMBERSHIP_TABLE + " WHERE memberaccountnumber = " + "'"
                + subscriptionModel.getAccountnum() + "'" + ";";
        String columnname = "profilepicture";*/
        File picfile = new File(subscriptionModel.getProfilepic());
        URL url = null;
        try {
            url = picfile.toURI().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }

        mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
        if((new Image(url.toExternalForm()).isError())){
            picfile = new File(DEFAULT_USER_PIC.toString());
            url = picfile.toURI().toURL();
            mSubscriberImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
        }


    }

   /* private Image getMemberImage(String query, String columnname) {
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet = null;
        InputStream inputStream = null;
        try {
            Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
            preparedStatement1 = connection.prepareStatement(query);
            resultSet = preparedStatement1.executeQuery();

            while (resultSet.next()) {
                try {
                    inputStream = resultSet.getBinaryStream(columnname);
                    OutputStream outputStream = new FileOutputStream(new File("profpic.png"));
                    byte[] content = new byte[1024];
                    int size = 0;
                    while ((size = inputStream.read(content)) != -1) {
                        outputStream.write(content, 0, size);
                    }
                    subscriberImage = new Image("file:profpic.png", 100, 150, true, true);
                    outputStream.hashCode();
                    inputStream.close();
                } catch (NullPointerException e) {
                   // System.out.println("Null pointer caught reading  main Image");
                    e.printStackTrace();
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }


        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
        return subscriberImage;
    }*/

    public void refreshWeeklyAccountsTable() {
        m7OutputMsgLabel.setText("...");
        // System.out.println("REFRESHING WEEKLY MEMBER ACCOUNTS TABLE");
        final GetWeeklyAccountsTask task = new GetWeeklyAccountsTask();


        mWeeklyAccTableProgress.progressProperty().bind(task.progressProperty());
        mWeeklyAccTableProgress.visibleProperty().bind(task.runningProperty());

        task.valueProperty().addListener(new ChangeListener<ObservableList<WeeklyMemberModel>>() {
            @Override
            public void changed(ObservableValue<? extends ObservableList<WeeklyMemberModel>> observableValue, ObservableList<WeeklyMemberModel> oldValue, ObservableList<WeeklyMemberModel> newValue) {
                weeklyMemberModelObservableList = newValue;
                weeklyMemberModelTableView.setItems(newValue);
            }
        });

        Thread thread = new Thread(task);
        thread.setDaemon(true);
        thread.start();


        FilteredList<WeeklyMemberModel> filteredList = new FilteredList<>(weeklyMemberModelObservableList, b -> true);
        m7SearchAccount.textProperty().addListener((observable, oldValue, newvalue) -> {
            filteredList.setPredicate(weeklyMemberModel -> {
                //If there is no search value, display all  the members
                if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null) {
                    return true;
                }
                String searchKeyWord = newvalue.toLowerCase();
                if (weeklyMemberModel.getFullname().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true; //Means a match has been found in name field
                } else if (weeklyMemberModel.getCellnum().toLowerCase().indexOf(searchKeyWord) > -1) {
                    return true; //Means a match has been found in surname field
                } else
                    return false;
            });

            //Sort the filtered data
            SortedList<WeeklyMemberModel> sortedList = new SortedList<>(filteredList);
            //Bind sorted result with TableView
            sortedList.comparatorProperty().bind(weeklyMemberModelTableView.comparatorProperty());
            //Apply filtered and sorted data t o the tableVIew
            weeklyMemberModelTableView.setItems(sortedList);


        });

        //Set Table Row Item Click Listener
        weeklyMemberModelTableView.setRowFactory(tv -> {
            TableRow<WeeklyMemberModel> row = new TableRow<>();
            row.setOnMouseClicked(event1 -> {
                if (event1.getClickCount() == 1 && (!row.isEmpty())) {
                    String rowData = row.getItem().getFullname().toString();

                    setSelectedMember(row);

                }
            });
            return row;

        });

    }

    private void setSelectedMember(TableRow<WeeklyMemberModel> row) {
        globalWeeklyMemberModel = row.getItem();
        globalRow = row;
        // System.out.println("SELECTED 7 DAY ACCOUNT FOR " + globalWeeklyMemberModel.getFullname());
        m7SearchAccount.setText(row.getItem().getFullname());
    }

    public void checkInWeeklySubscriber() {
        try {


        // System.out.println("CHECKING IN WEEKLY MEMBER ");
        LocalDate todayDate = LocalDate.parse(GlobalMethods.getTodaysDateAsStringFromDb());
        LocalDate startDate = LocalDate.parse(globalWeeklyMemberModel.getStartdate());
        LocalDate endDate = LocalDate.parse(globalWeeklyMemberModel.getEnddate());

        if ((todayDate.isEqual(todayDate) || todayDate.isAfter(startDate)) && (todayDate.isBefore(endDate) || todayDate.isEqual(endDate))) {
            // System.out.println("MEMBER QUALIFIES ......CHECKING IN WEEKLY MEMBER ");
            // System.out.println("Logging reason");
            SystemUser systemUser = UserSession.getUserSessionInstance(null).getSystemUser();

            //  System.out.println("THE USER IS :" + systemUser.getFullname());
            SecurityClearanceEventModel securityClearanceEventModel = new SecurityClearanceEventModel(
                    "Access Control Checkin", systemUser.getFullname(), globalWeeklyMemberModel.getFullname().toString()
                    , "", "", "7 Day account", "System allowed");

                    /*AccessCounterTask counterTask = new AccessCounterTask(accountnumber, memberIdentifier);
                    Thread thread = new Thread(counterTask);
                    thread.setDaemon(true);
                    thread.start();*/

            LogSecurityEventTask logSecurityEventTask = new LogSecurityEventTask(securityClearanceEventModel);
            Thread thread = new Thread(logSecurityEventTask);
            thread.setDaemon(true);
            thread.start();
            m7OutputMsgLabel.setText("Check in successful");
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Check in Successful!");
            alert.showAndWait();

            //todo delay seconds
            GlobalMethods.delayWithSeconds(2);
            refreshWeeklyAccountsTable();
            m7SearchAccount.clear();

        } else {
            // System.out.println("MEMBER DOES NOT QUALIFY ABANDONING ");
           // globalRow.setStyle("-fx-background-color: red ;");
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("WARNING!");
            alert.setHeaderText(null);
            alert.setContentText("The selected account holder does not qualify for access. Expired access");
            alert.showAndWait();
            return;
           // overrideWeekelyMember(null);


        }
        }catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("Null selection: "+e.getMessage());
            alert.showAndWait();

        }
    }

//Override short term account member
    public void overrideWeekelyMember(ActionEvent event) {
        try {

            SystemUser systemUser = UserSession.getUserSessionInstance(null).getSystemUser();
            //set SecurityClearanceEventModel to pass to the override stage
            SecurityClearanceEventModel securityClearanceEventModel = new SecurityClearanceEventModel(
                    "Access Control Override ", systemUser.getFullname(), globalWeeklyMemberModel.getFullname().toString()
                    , "", "", "7 Day account", "");
           // Node node = (Node) event.getSource();
          //create new stage and open override window
           stage = new Stage();
            stage.setResizable(false);
            stage.initStyle(StageStyle.UNDECORATED);
            stage.setUserData(securityClearanceEventModel);
            Parent root = FXMLLoader.load(getClass().getResource("/home/fxml/accessOverride.fxml"));
            Scene scene1 = new Scene(root,450,420);
            stage.setScene(scene1);
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.initOwner(m7OverrideBtn.getScene().getWindow());
            stage.showAndWait();
            refreshWeeklyAccountsTable();

        } catch (NullPointerException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.initModality(Modality.APPLICATION_MODAL);
            alert.setTitle("WARNING!");
            alert.setHeaderText(null);
            alert.setContentText("Null selection: "+e.getMessage());
            alert.showAndWait();
        }catch (Exception e) {
            e.printStackTrace();
        }


    }


    public void deleteWeeklyMemberAccount() {
        //todo implement method
        PreparedStatement preparedStatement = null;
        if (globalWeeklyMemberModel == null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("No member selected for deletion. Please select a member you want to delete");
            alert.showAndWait();
            return;
        } else if (globalWeeklyMemberModel != null) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this record");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM " + WEEKLY_MEMBERSHIP_TABLE + " WHERE idnum = " + "'" + globalWeeklyMemberModel.getIdnum() + "'" + ";";
                    Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
                clearFileds();
                refreshWeeklyAccountsTable();
                return;
            }
        }
       /* System.out.println("SHORT TERM ACCOUNTS DELETING");
        DeleteWeeklyMemberTask deleteWeeklyMemberTask = new DeleteWeeklyMemberTask(globalWeeklyMemberModel);
        Thread thread = new Thread(deleteWeeklyMemberTask);
        thread.setDaemon(true);
        thread.start();
        refreshWeeklyAccountsTable();*/
    }


}