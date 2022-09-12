package home;

import com.github.sarxos.webcam.*;
import com.itextpdf.io.image.ImageDataFactory;
import com.itextpdf.kernel.colors.Color;
import com.itextpdf.kernel.colors.ColorConstants;
import com.itextpdf.kernel.pdf.PdfDocument;
import com.itextpdf.kernel.pdf.PdfWriter;
import com.itextpdf.layout.Document;
import com.itextpdf.layout.borders.Border;
import com.itextpdf.layout.borders.SolidBorder;
import com.itextpdf.layout.element.AreaBreak;
import com.itextpdf.layout.element.Cell;
import com.itextpdf.layout.element.Paragraph;
import com.itextpdf.layout.element.Table;
import com.itextpdf.layout.properties.AreaBreakType;
import com.itextpdf.layout.properties.HorizontalAlignment;
import com.itextpdf.layout.properties.TextAlignment;
import home.BackgroundTasks.SaveWeeklyMemberTask;
import home.BackgroundTasks.UpdateProfilePicTask;
import home.Models.*;
import home.Services.AddSubscriberService;
import home.Services.AlterSubscriberService;
import home.Services.GetAccountsService;
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
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.*;
import javafx.scene.image.Image;
import javafx.scene.layout.*;
import javafx.scene.paint.ImagePattern;
import javafx.scene.shape.Rectangle;
import javafx.stage.FileChooser;
import javafx.stage.Window;
import org.krysalis.barcode4j.impl.code128.Code128Bean;
import org.krysalis.barcode4j.output.bitmap.BitmapCanvasProvider;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;

import java.awt.image.BufferedImage;
import java.io.*;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.FileAlreadyExistsException;
import java.nio.file.Files;
import java.sql.*;
import java.text.SimpleDateFormat;
import java.time.Duration;
import java.time.LocalDate;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import static home.Constants.*;
import static home.GlobalMethods.nthDayOfFollowingMonth;

public class MembershipController extends Window implements Initializable {
    public static final String CO_NAME = "X-pressions Wellness Centre";
    //------------------------------------------------ Contracts Tab--------------------------------------------
    @FXML
    private ChoiceBox<String> mTitles;
    @FXML
    private ChoiceBox<String> mGender;
    @FXML
    private ChoiceBox<String> mMembershipDesc;
    @FXML
    private ChoiceBox<String> mMinDuration;
    @FXML
    private ChoiceBox<String> mPaymentMethods;
    @FXML
    private ChoiceBox<String> mPaymentType, mBankName, mBankAccType;
    @FXML
    private TextField mName, mSurname, mIdNumber, mAddress, mCellNumber, mTelNumber, mOccupation, mEmail, mContractNum,
            mNextOfKin, mAccNum, mNextOfKinCell, mMc, mMember1FullName, mMember2FullNames, mMember1Id, mMember2Id, mMemberCardNum1,
            mMemberCardNum2, mTotAmntRec, mCardFee, mJoiningFee, mUpFrontPayment, mBankAccNumber,
            mPayerDetails, mPayerIdNum, mPayerCellNum, mPayerEmail;
    @FXML
    private DatePicker mStartDate;
    @FXML
    private ChoiceBox<String> mDebitOrderDate;
    @FXML
    private Button mSaveBtn, mPrintContractBtn;
    @FXML
    private Label mDeclaration, mTermsAndCs;
    @FXML
    private ImageView mMainMemberProfPic;
    @FXML
    private ImageView mMember1ProfPic;
    @FXML
    private ImageView mMember2ProfPic;
    @FXML
    private ImageView mCompanyLogo;
    @FXML
    private VBox mUploadImageCombo;
    @FXML
    private Rectangle mRectPic, mRectPic1, mRectPic2;


    //-----------------------------------------Member Account Search Tab------------------------------------------------
    @FXML
    private TableView<MemberSearchModel> mMembersTableView;
    @FXML
    private TextField mAccountSearch;
    @FXML
    private TableColumn<MemberSearchModel, String> mTitleTableColumn;

    @FXML
    private TableColumn<MemberSearchModel, String> mNameTableColumn;
    @FXML
    private TableColumn<MemberSearchModel, String> mSurnameTableColumn;
    @FXML
    private TableColumn<MemberSearchModel, String> mAccNumTableColumn;
    @FXML
    private TableColumn<MemberSearchModel, String> mCellNumTableColumn;
    @FXML
    private TableColumn<MemberSearchModel, String> mIdNumTableColumn;
    @FXML
    private ChoiceBox mAccountStatus;
    @FXML
    private Button mUpdateAccountStatus;
    @FXML
    private Button mRefreshTableBtn;
    @FXML
    private ProgressBar mLoadTableProgress;
    @FXML
    private AnchorPane mAccountsTableAPane;


    //------------------------------------------------ Membership Images/ Card/Forms Tab--------------------------------------------

    @FXML
    private Rectangle mProfileImageRectangle;
    @FXML
    private ImageView mProfileImage;
    @FXML
    private Button mUploadProfieImage, mCaptureProfilrImage, mSaveProfileImage;
    @FXML
    private Rectangle mDocumentsProfilePic;
    @FXML
    private ImageView mDocumentsLogo;
    @FXML
    private TextField mDocumentsName, mDocumentsSurname, mDocumentsCell, mMembershipCardName, mMembershipCardSurname, mMembershipCardAccount;
    @FXML
    private Button mDowloadIndemnity;
    @FXML
    private ImageView mDocumentsMaleCard;
    @FXML
    private ImageView mDocumentsFemaleCard;
    @FXML
    private DatePicker mDocumentsDate;
    @FXML
    private ChoiceBox<String> mIndemnitySelector;
    @FXML
    private CheckBox mMaleCheckBox, mFemaleCheckBox, mShortPackageOption;

    ObservableList<MemberSearchModel> memberSearchModelObservableList = FXCollections.observableArrayList();

    //------------------------------------------------ Membership Packages Tab--------------------------------------------

    @FXML
    private Button mSavePackage, mDeletePackage, mRefreshPackages;
    @FXML
    private TextField mPackageName, mAmount, mAmount1, mAmount2;
    @FXML
    private TextField mShortTermPckgName, mShortTermPckgFee;
    @FXML
    private ChoiceBox<String> mShortTermPckgSelector, mShortTermPckgDuration;
    @FXML
    private Tab mPackagesTab;
    @FXML
    private TableView mPackagesTable;
    @FXML
    private TableColumn<MembershipPackageModel, String> mPackageNameColumn;
    /* @FXML
     private TableColumn<MembershipPackageModel, String> mCyclePeriodColumn;*/
    @FXML
    private TableColumn<MembershipPackageModel, String> mAmountColumn;
    @FXML
    private TableColumn<MembershipPackageModel, String> mAmount1Column;
    @FXML
    private TableColumn<MembershipPackageModel, String> mAmount2Column;
    @FXML
    private TableColumn<MembershipPackageModel, String> mAccountStatusColumn;
    @FXML
    private HBox mSecondaryFeesHBox;
    @FXML
    private TableView mShortTermPackagesTable;
    @FXML
    private TableColumn<ShortTermPackageModel, String> mShortTermPckgNameClmn;
    @FXML
    private TableColumn<ShortTermPackageModel, String> mShortTermPckgFeesClmn;
    @FXML
    private TableColumn<ShortTermPackageModel, String> mShortTermDurationDaysClmn;


    //----------------------------------------Short Term Contracts and Packages Tab-----------------------------------------

    @FXML
    private TextField m7Fullaname, m7IdNUm, m7CellNum;
    @FXML
    private DatePicker m7DatePicker;
    @FXML
    private Label m7EndDate, mShortFeesHint, mShortDaysHint;
    @FXML
    private Button m7SaveBtn;
    @FXML
    private StackPane contentArea;
    @FXML
    private Label moduleHeadingLabel, mDurationText;
    @FXML
    private Tab mShortTermContractsTab;
    @FXML
    private Pane mManageSTPackagesPane;
   /* @FXML
    private ChoiceBox<String> mShortTermPckgSelector;*/


    private static String TAG = "MembshpContller";
    private String[] GenderList = {"SELECT GENDER", "MALE", "FEMALE"};
    private String[] TitlesList = {"TITLE", "MR", "MRS", "MS", "MISS"};
    private String[] AccountStatusList = {"ACTIVE", "PAUSED", "SUSPENDED"};
    private String[] paymentMethodsList = {"DEBIT ORDER", "CASH"};
    private String[] memberLevelList = {"MAIN MEMBER", "MEMBER 1", "MEMBER 2"};
    private String[] paymentTypeList = {"CARD", "CASH"};
    private final String[] listOfBankAccountTypes = {"CHEQUE", "SAVINGS"};
    private String[] listOfBanksInSA = {"ABSA BANK (632 005)", "AFRICAN BANK (430 000)", "BIDVEST BANK (462 005)",
            "CAPITEC BANK (470 010)", "FIRST NATIONAL BANK (250 655)", "FIRST RAND BANK (250 655)", "INVESTEC BANK (580 105)"
            , "MERCANTILE BANK (450 105)", "NEDBANK (198 765)", "OLD MUTUAL BANK (462 000)", "SASFIN BANK (683 000)"
            , "STANDARD BANK (051 001)", "SA POST OFFICE (460 005)", "TYME BANK (678 910)"};
    private final String[] minDurationList = {"1", "2", "3", "6", "12", "24", "36"};
    private final String[] debitorderdaysList = {"15", "25", "28", "29", "30", "31"};
    private final String[] durationDaysList = {"1", "7"};

    private ObservableList<MembershipPackageModel> packagesList;
    private ObservableList<ShortTermPackageModel> shortTermPackageList;
    // private String[] MinDurationList = {"One Month", "3 Months", "6 Months", "12 Months"};
    private File selectedprofilepicture, selectedprofilepicture1, selectedprofilepicture2;
    private FileInputStream fileInputStream, fileInputStream1, fileInputStream2;
    private Image globalVarProfPicImage, globalVarProfPicImage1, globalVarProfPicImage2;

    private String title, gender, membershipDesc, minDuration, name, surname, idNum, address, tellNum, occupation, cellNum,
            email, contractNum, accountNum, nextOfKin, nextOfKinCell, mc, member1FullName, member2FullName, member1Id,
            member2Id, member1CardNum, member2CardNum, bankName,
            branchCode, bankAccNum, bankAccType, payerDetails, payerIdNum, payerCellNum, payerEmail;
    private float totalAmntReceived, cardFee, joiningFee, upfrontPayment;

    private PreparedStatement preparedStatement;
    private DatabaseConnection myDatabaseConnection = new DatabaseConnection();
    private Connection connection = myDatabaseConnection.getDatabaseLinkConnection();
    private ResultSet resultSet = null;
    String paymentmethod = "";
    String paymenttype = "";
    String accountstatus = "ACTIVE";
    private boolean isEditingContract = false;
    private String accountSelectedForEditing, memberUidSelectedForEditing;
    private MembershipModel globalVarOfSelcetectedMember;
    private boolean cameraIsRunning = false;
    boolean isEditingPackage = false;
    boolean isEditingShortTermPackage = false;
    private boolean isMaleGender = false;
    private boolean isFemaleGender = false;
    private String packageIdSelectedForEdit = null;
    private String shortTermpackageIdSelectedForEdit = null;
    private MembershipPackageModel selectedPackage;
    private ShortTermPackageModel selectedShortTermPackage;
    private boolean isShortTermPackage = false;

    private Image profileEditorImage;
    private String authlevel = "";
    private SystemUser systemUser;


    File picfile, picfile1, picfile2, defaultPicFile;
    URL url = null;
    URL url1 = null;
    URL url2 = null;


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

        try {
            try {
                systemUser = UserSession.getSystemUser();
                authlevel = systemUser.getAuthlevel();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR 1 " + e.getMessage());
                alert.showAndWait();
            }
            try {
                mTitles.getItems().addAll(TitlesList);
                mTitles.setValue(TitlesList[0]);
                mGender.getItems().addAll(GenderList);
                mGender.setValue(GenderList[0]);
                mPaymentMethods.getItems().addAll(paymentMethodsList);
                mPaymentType.getItems().addAll(paymentTypeList);
                mMinDuration.getItems().addAll(minDurationList);// todo add durations list in database
                // mMinDuration.setValue(MinDurationList[0]);
                mDebitOrderDate.getItems().addAll(debitorderdaysList);
                mAccountStatus.getItems().addAll(AccountStatusList);
                mIndemnitySelector.getItems().addAll(memberLevelList);

                mShortTermPckgDuration.getItems().addAll(durationDaysList);
                mBankName.getItems().addAll(listOfBanksInSA);
                mBankAccType.getItems().addAll(listOfBankAccountTypes);
                packagesList = FXCollections.observableArrayList();
                shortTermPackageList = FXCollections.observableArrayList();
                mTotAmntRec.setText("0");
                mCardFee.setText("0");
                mJoiningFee.setText("0");
                mUpFrontPayment.setText("0");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR 2 " + e.getMessage());
                alert.showAndWait();
            }
            // Get Membership packages types (as a Java Object Model) from database and initialize respective choice box
            try {
                //Run query to fetch membership packages
                String query = "SELECT * FROM " + PACKAGES_TABLE;
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = null;
                resultSet = preparedStatement.executeQuery();
                packagesList.clear();
                while (resultSet.next()) {
                    packagesList.add(new MembershipPackageModel(
                            resultSet.getString("packageid"),
                            resultSet.getString("packagename"),
                            resultSet.getString("packagefee"),
                            resultSet.getString("packagefee1"),
                            resultSet.getString("packagefee2")

                    ));
                }
                for (int i = 0; i < packagesList.size(); i++) {
                    mMembershipDesc.getItems().add(packagesList.get(i).getPackagename());
                }
                mMembershipDesc.setValue(packagesList.get(0).getPackagename());
                // Close connections to database for this query
                preparedStatement.close();
                resultSet.close();
                connection.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING!");
                alert.setHeaderText(null);
                alert.setContentText("Error:3 " + e.getMessage());
            }

            try {
                //Run query to fetch short term membership packages
                String query = "SELECT * FROM " + SHORT_TERM_PACKAGES_TABLE;
                connection = new DatabaseConnection().getDatabaseLinkConnection();
                preparedStatement = connection.prepareStatement(query);
                ResultSet resultSet = null;
                resultSet = preparedStatement.executeQuery();
                shortTermPackageList.clear();
                while (resultSet.next()) {
                    shortTermPackageList.add(new ShortTermPackageModel(
                            resultSet.getString("packageid"),
                            resultSet.getString("packagename"),
                            resultSet.getString("packagefee"),
                            resultSet.getString("daysduration")
                    ));
                }

                for (int i = 0; i < shortTermPackageList.size(); i++) {
                    mShortTermPckgSelector.getItems().add(shortTermPackageList.get(i).getPackagename());
                }
                mShortTermPckgSelector.setValue(shortTermPackageList.get(0).getPackagename());
                // Close connections to database for this query
                connection.close();
                preparedStatement.close();
                resultSet.close();
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                e.printStackTrace();

            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING!");
                alert.setHeaderText(null);
                alert.setContentText("Error: 4" + e.getMessage());
            }

            try {

                // map account search table colums to titles
                mTitleTableColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
                mNameTableColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
                mSurnameTableColumn.setCellValueFactory(new PropertyValueFactory<>("surname"));
                mAccNumTableColumn.setCellValueFactory(new PropertyValueFactory<>("memberaccountnumber"));
                mCellNumTableColumn.setCellValueFactory(new PropertyValueFactory<>("cellnumber"));
                mIdNumTableColumn.setCellValueFactory(new PropertyValueFactory<>("idnumber"));
                mAccountStatusColumn.setCellValueFactory(new PropertyValueFactory<>("accountstatus"));
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR 5 " + e.getMessage());
                alert.showAndWait();
            }

            //rfreshAccountsTable(); (SUSPEND FUNCTION TO REFRESH TABLE AUTOMATICALLY)


            //Listen for Membership packages choice box clicks and get package from db and set global var selectedpackage on click
            //mMembershipDesc.setOnAction(this::getMembershipPackage);

            mIndemnitySelector.setOnAction(this::updateMembershipDocumentsFields);
            mShortTermPckgSelector.setOnAction(this::setHintFields);
            try {
                loadAllShortTermPackages();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR 6 " + e.getMessage());
                alert.showAndWait();
            }

            try {
                rfreshAccountsTable();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR 7" + e.getMessage());
                alert.showAndWait();
            }

            try {

                defaultPicFile = new File("user_icon.png");
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR! 8" + e.getMessage());
                alert.showAndWait();
            }

            //-------------------------------------------- SET TEXT FIELDS TO CAPITALISE LETTERS----------------------------

            mAccountSearch.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            m7Fullaname.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            m7IdNUm.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            m7CellNum.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mName.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mSurname.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mIdNumber.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mAddress.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mCellNumber.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mTelNumber.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mOccupation.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mEmail.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mContractNum.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mNextOfKin.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mAccNum.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mNextOfKinCell.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMc.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMember1FullName.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMember2FullNames.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMember1Id.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMember2Id.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMemberCardNum1.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMemberCardNum2.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mTotAmntRec.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mCardFee.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mJoiningFee.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mUpFrontPayment.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mBankAccNumber.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mPayerDetails.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mPayerIdNum.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mPayerCellNum.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mPayerEmail.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mAccountSearch.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mDocumentsName.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mDocumentsSurname.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mDocumentsCell.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMembershipCardName.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMembershipCardSurname.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mMembershipCardAccount.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            ;
            mPackageName.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mAmount.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mAmount1.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mAmount2.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));

            mShortTermPckgName.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
            mShortTermPckgFee.setTextFormatter(new TextFormatter<>((change) -> {
                change.setText(change.getText().toUpperCase());
                return change;

            }));
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING!");
            alert.setHeaderText(null);
            alert.setContentText("Error: 9" + e.getMessage());
        }
    }

    public void getMembershipPackage() {
        String packagename = mMembershipDesc.getValue().toString();
        String query = "SELECT * FROM " + PACKAGES_TABLE + " WHERE packagename = " + "'" + packagename + "'";
        try {
            //Run query to fetch membership packages
            connection = myDatabaseConnection.getDatabaseLinkConnection();
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = null;
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                selectedPackage = (new MembershipPackageModel(
                        resultSet.getString("packageid"),
                        resultSet.getString("packagename"),
                        resultSet.getString("packagefee"),
                        resultSet.getString("packagefee1"),
                        resultSet.getString("packagefee2")

                ));
            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR! 10" + e.getMessage());
            alert.showAndWait();
        }
        // return selectedPackage;
    }

    public void rfreshAccountsTable() {
        try {

            // System.out.println("Load table click");
            final GetAccountsService service = new GetAccountsService();// this service takes the the heavy task of fetchiching the account fromdb to a backgroung thread
            //can use this to programaticall add progressbar to anchorpane
            // ProgressIndicator p = new ProgressIndicator();
            //p.setMaxSize(140,140);

            mLoadTableProgress.progressProperty().bind(service.progressProperty());
            mLoadTableProgress.visibleProperty().bind(service.runningProperty());
            //mMembersTableView.itemsProperty().bind(service.valueProperty());
            try {
                service.valueProperty().addListener(new ChangeListener<ObservableList<MemberSearchModel>>() {
                    @Override
                    public void changed(ObservableValue<? extends ObservableList<MemberSearchModel>> observableValue, ObservableList<MemberSearchModel> oldValue, ObservableList<MemberSearchModel> newValue) {
                        memberSearchModelObservableList = newValue;
                        mMembersTableView.setItems(newValue);

                    }
                });

                service.start();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR! 11" + e.getMessage());
                alert.showAndWait();
            }

            FilteredList<MemberSearchModel> filteredList = new FilteredList<>(memberSearchModelObservableList, b -> true);
            mAccountSearch.textProperty().addListener((observable, oldValue, newvalue) -> {
                filteredList.setPredicate(memberSearchModel -> {
                    //If there is no search value, display all  the members
                    if (newvalue.isEmpty() || newvalue.isBlank() || newvalue == null) {
                        return true;
                    }
                    String searchKeyWord = newvalue.toLowerCase();
                    if (memberSearchModel.getName().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true; //Means a match has been found in name field
                    } else if (memberSearchModel.getSurname().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true; //Means a match has been found in surname field
                    } else if (memberSearchModel.getMemberaccountnumber().toLowerCase().indexOf(searchKeyWord) > -1) {
                        return true; //Means a match has been found in account number field
                    } else
                        return false;
                });

                //Sort the filtered data
                SortedList<MemberSearchModel> sortedList = new SortedList<>(filteredList);
                //Bind sorted result with TableView
                sortedList.comparatorProperty().bind(mMembersTableView.comparatorProperty());
                //Apply filtered and sorted data t o the tableVIew
                mMembersTableView.setItems(sortedList);


            });

            //Set Table Row Item Click Listener
            mMembersTableView.setRowFactory(tv -> {
                TableRow<MemberSearchModel> row = new TableRow<>();
                row.setOnMouseClicked(event1 -> {
                    if (event1.getClickCount() == 1 && (!row.isEmpty())) {
                        String rowData = row.getItem().getMemberaccountnumber().toString();
                        getMemberForEdit(row);
                    }
                });
                return row;

            });

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING!");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
        }
    }


    private void getMemberForEdit(TableRow<MemberSearchModel> row) {
        //Fill user details fields with with user info on table row click
        isEditingContract = true;//true == user has clicked a table row for editing
        memberUidSelectedForEditing = row.getItem().getMemberuid();
        accountSelectedForEditing = row.getItem().getMemberaccountnumber();
        String query1 = "SELECT * FROM " + MEMBERSHIP_TABLE + " WHERE memberuid = " + "'" + memberUidSelectedForEditing + "'" + ";";
        PreparedStatement preparedStatement1 = null;
        ResultSet resultSet1 = null;
        MembershipModel membershipModel = null;

        InputStream inputStream = null;
        InputStream inputStream1 = null;
        InputStream inputStream2 = null;

        try {
            connection = myDatabaseConnection.getDatabaseLinkConnection();
            preparedStatement1 = connection.prepareStatement(query1);
            resultSet1 = preparedStatement1.executeQuery();
            while (resultSet1.next()) {

                membershipModel = new MembershipModel(
                        resultSet1.getString("memberuid"),
                        resultSet1.getString("title"), resultSet1.getString("name"),
                        resultSet1.getString("surname"), resultSet1.getString("idnumber"),
                        resultSet1.getString("address"), resultSet1.getString("cellnumber"),
                        resultSet1.getString("email"), resultSet1.getString("occupation"),
                        resultSet1.getString("nextofkin"), resultSet1.getString("nextofkincell"),
                        resultSet1.getString("memberaccountnumber"), resultSet1.getString("contractnumber"),
                        resultSet1.getString("mc"), resultSet1.getString("member1name"),
                        resultSet1.getString("member1idnumber"), resultSet1.getString("member1accountnumber"),
                        resultSet1.getString("member2name"), resultSet1.getString("member2idnumber"),
                        resultSet1.getString("member2accountnumber"), resultSet1.getString("startdate"),
                        resultSet1.getString("cardfee"), resultSet1.getString("joiningfee"),
                        resultSet1.getString("totalreceived"), resultSet1.getString("upfrontpayment"),
                        resultSet1.getString("bankname"), resultSet1.getString("bankaccountnumber"),
                        resultSet1.getString("bankaccounttype"), resultSet1.getString("debitorderdate"),
                        resultSet1.getString("payerdetails"),
                        resultSet1.getString("payeridnumber"), resultSet1.getString("payercellnumber"),
                        resultSet1.getString("payeremail"), resultSet1.getString("membershipdescription"),
                        resultSet1.getString("minimumduration"), resultSet1.getString("profilepicture"),
                        resultSet1.getString("membercontractdoc"), resultSet1.getString("gender"), resultSet1.getString("telnumber"),
                        resultSet1.getString("paymentmethod"), resultSet1.getString("accountstatus"),
                        resultSet1.getString("pic1"), resultSet1.getString("pic2"), resultSet1.getString("paymenttype")
                );
            }
            // System.out.println("QUERY!::" + membershipModel.getTitle() + " " + membershipModel.getMemberuid());
            globalVarOfSelcetectedMember = membershipModel;

            updateMembershipFieldsForEditing(membershipModel);
            System.out.println("Should update fileds for edit here");

        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (MalformedURLException e) {
            System.out.println("Mslformed excp here");
            throw new RuntimeException(e);
        }


    }

    public void updateAccountStatus(ActionEvent event) {
        // System.out.println("New Account update status..");
        if (isEditingContract == false) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("No package selected for deletion. Please select a package you want to delete");
            alert.showAndWait();
            return;
        } else {
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                String query = "UPDATE " + MEMBERSHIP_TABLE + " " +
                        "SET accountstatus = " + "'" + mAccountStatus.getValue().toString() + "' " +
                        "WHERE memberuid = " + memberUidSelectedForEditing + ";";

                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Account status Updated successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();

                //update subscriptions
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                String query1 = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                        "SET accountstatus = " + "'" + mAccountStatus.getValue().toString() + "' " +
                        "WHERE memberuid = " + memberUidSelectedForEditing + ";";

                preparedStatement = connection.prepareStatement(query1);
                preparedStatement.execute();
                preparedStatement.close();
                connection.close();

            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
    }

    private void updateMembershipFieldsForEditing(MembershipModel membershipModel) throws MalformedURLException {
        try {

            if (!membershipModel.getProfilepicture().isEmpty()) {
                try {
                    picfile = new File(globalVarOfSelcetectedMember.getProfilepicture());
                    url = picfile.toURI().toURL();
                    mRectPic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                    if ((new Image(url.toExternalForm()).isError())) {
                        picfile = new File(defaultPicFile.toString());
                        url = picfile.toURI().toURL();
                        mRectPic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                        System.out.println("Images may be missing main pic");
                        // return;
                    }

                } catch (Exception e) {
                    picfile = new File(defaultPicFile.toString());
                    url = picfile.toURI().toURL();
                    mRectPic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                    e.printStackTrace();
                    System.out.println("Images may be missing main pic");

                }
            }

            if (!membershipModel.getPic1().isEmpty()) {
                try {
                    picfile1 = new File(globalVarOfSelcetectedMember.getPic1());
                    url1 = picfile1.toURI().toURL();
                    mRectPic1.setFill(new ImagePattern(new Image(url1.toExternalForm())));
                    if ((new Image(url1.toExternalForm()).isError())) {
                        picfile1 = new File(defaultPicFile.toString());
                        url1 = picfile1.toURI().toURL();
                        mRectPic1.setFill(new ImagePattern(new Image(url1.toExternalForm())));
                        System.out.println("Images may be missing pic1");

                    }
                } catch (Exception e) {
                    picfile1 = new File(defaultPicFile.toString());
                    url1 = picfile1.toURI().toURL();
                    mRectPic1.setFill(new ImagePattern(new Image(url1.toExternalForm())));
                    e.printStackTrace();
                    System.out.println("Images may be missing pic1");
                }
            }

            if (!membershipModel.getPic2().isEmpty()) {
                try {
                    picfile2 = new File(globalVarOfSelcetectedMember.getPic2());
                    url2 = picfile2.toURI().toURL();
                    mRectPic2.setFill(new ImagePattern(new Image(url2.toExternalForm())));
                    if ((new Image(url2.toExternalForm()).isError())) {
                        picfile2 = new File(defaultPicFile.toString());
                        url2 = picfile2.toURI().toURL();
                        mRectPic2.setFill(new ImagePattern(new Image(url2.toExternalForm())));
                        System.out.println("Images may be missing pic 2");
                    }
                } catch (Exception e) {
                    picfile2 = new File(defaultPicFile.toString());
                    url2 = picfile2.toURI().toURL();
                    mRectPic2.setFill(new ImagePattern(new Image(url2.toExternalForm())));
                    e.printStackTrace();
                    System.out.println("Images may be missing pic 2");
                }

            }
        } catch (IllegalArgumentException e) {

        }
        //Update Text fields
        mTitles.setValue(membershipModel.getTitle());
        mGender.setValue(membershipModel.getGender());
        mName.setText(membershipModel.getName());
        mSurname.setText(membershipModel.getSurname());
        mIdNumber.setText(membershipModel.getIdnumber());

        mAddress.setText(membershipModel.getAddress());
        mCellNumber.setText(membershipModel.getCellnumber());
        mTelNumber.setText(membershipModel.getTelnumber());
        mOccupation.setText(membershipModel.getOccupation());
        mEmail.setText(membershipModel.getEmail());
        mContractNum.setText(membershipModel.getContractnumber());
        mNextOfKin.setText(membershipModel.getNextofkin());
        mAccNum.setText(membershipModel.getMemberaccountnumber());
        mNextOfKinCell.setText(membershipModel.getNextofkincell());
        mMc.setText(membershipModel.getMc());
        mMember1FullName.setText(membershipModel.getMember1name());
        mMember2FullNames.setText(membershipModel.getMember2name());
        mNextOfKin.setText(membershipModel.getNextofkin());
        mMember1Id.setText(membershipModel.getMember1idnumber());
        mMember2Id.setText(membershipModel.getMember2idnumber());
        mMemberCardNum1.setText(membershipModel.getMember1accountnumber());
        mMemberCardNum2.setText(membershipModel.getMember2accountnumber());
        mStartDate.setValue(LocalDate.parse(membershipModel.getStartdate()));
        mTotAmntRec.setText(membershipModel.getTotalreceived());
        // mMembershipDesc.setValue(membershipModel.getMembershipdescription());
        mMembershipDesc.getSelectionModel().select(membershipModel.getMembershipdescription());
        mMinDuration.setValue(membershipModel.getMinimumduration());
        mMemberCardNum2.setText(membershipModel.getMember2accountnumber());
        mPaymentMethods.setValue(membershipModel.getPaymentmethod());
        mPaymentType.setValue(membershipModel.getPaymentype());
        mCardFee.setText(membershipModel.getCardfee().toString());
        mJoiningFee.setText(membershipModel.getJoiningfee());
        mUpFrontPayment.setText(membershipModel.getUpfrontpayment());
        mBankName.setValue(membershipModel.getBankname());
        mDebitOrderDate.setValue(membershipModel.getDebitorderdate());
        mBankAccNumber.setText(membershipModel.getBankaccountnumber());
        mBankAccType.setValue(membershipModel.getBankaccounttype());
        mPayerDetails.setText(membershipModel.getPayerdetails());
        mPayerIdNum.setText(membershipModel.getPayeridnumber());
        mPayerCellNum.setText(membershipModel.getPayercellnumber());
        mPayerEmail.setText(membershipModel.getPayeremail());
        mMemberCardNum2.setText(membershipModel.getMember2accountnumber());
    }

    public void updateMembershipDocumentsFields(ActionEvent event) {

        if (isEditingContract == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Selection Error:");
            alert.setHeaderText(null);
            alert.setContentText("No Membership account selected for this action, please select the appropriate account ");
            alert.showAndWait();
            return;
        } else if (isEditingContract == true) {
            String member = mIndemnitySelector.getValue().toString();

            switch (member) {
                case "MAIN MEMBER": {
                    try {
                        if (!globalVarOfSelcetectedMember.getProfilepicture().isEmpty()) {
                            File picfile = new File(globalVarOfSelcetectedMember.getProfilepicture());
                            URL url = null;
                            url = picfile.toURI().toURL();
                            mDocumentsProfilePic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                            if ((new Image(url.toExternalForm()).isError())) {
                                picfile = new File(defaultPicFile.toString());
                                url = picfile.toURI().toURL();
                                mDocumentsProfilePic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                                System.out.println("Images may be missing pic 2");
                            }
                        }
                        mDocumentsName.setText(globalVarOfSelcetectedMember.getName());
                        mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());
                        mDocumentsCell.setText(globalVarOfSelcetectedMember.getCellnumber());
                        mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());

                        //Update membership cards fields
                        mMembershipCardName.setText(globalVarOfSelcetectedMember.getName());
                        mMembershipCardSurname.setText(globalVarOfSelcetectedMember.getSurname());
                        mMembershipCardAccount.setText(globalVarOfSelcetectedMember.getMemberaccountnumber());

                    } catch (MalformedURLException e) {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
                case "MEMBER 1": {
                    try {
                        if (!globalVarOfSelcetectedMember.getPic1().isEmpty()) {
                            File picfile = new File(globalVarOfSelcetectedMember.getPic1());
                            URL url = null;

                            url = picfile.toURI().toURL();
                            mDocumentsProfilePic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                            if ((new Image(url.toExternalForm()).isError())) {
                                picfile = new File(defaultPicFile.toString());
                                url = picfile.toURI().toURL();
                                mDocumentsProfilePic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                                System.out.println("Images may be missing pic 2");
                            }
                        }
                        mDocumentsName.setText(globalVarOfSelcetectedMember.getMember1name());
                        // mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());
                        mDocumentsCell.setText(globalVarOfSelcetectedMember.getCellnumber());
                        //mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());

                        //Update membership cards fields
                        mMembershipCardName.setText(globalVarOfSelcetectedMember.getMember1name());
                        //mMembershipCardSurname.setText(globalVarOfSelcetectedMember.getSurname());
                        mMembershipCardAccount.setText(globalVarOfSelcetectedMember.getMember1accountnumber());

                    } catch (MalformedURLException e) {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
                case "MEMBER 2": {
                    try {
                        if (!globalVarOfSelcetectedMember.getPic2().isEmpty()) {
                            File picfile = new File(globalVarOfSelcetectedMember.getPic2());
                            URL url = null;

                            url = picfile.toURI().toURL();
                            mDocumentsProfilePic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                            if ((new Image(url.toExternalForm()).isError())) {
                                picfile = new File(defaultPicFile.toString());
                                url = picfile.toURI().toURL();
                                mDocumentsProfilePic.setFill(new ImagePattern(new Image(url.toExternalForm())));
                                System.out.println("Images may be missing pic 2");
                            }
                        }
                        mDocumentsName.setText(globalVarOfSelcetectedMember.getMember2name());
                        // mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());
                        mDocumentsCell.setText(globalVarOfSelcetectedMember.getCellnumber());
                        //mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());

                        //Update membership cards fields
                        mMembershipCardName.setText(globalVarOfSelcetectedMember.getMember2name());
                        //mMembershipCardSurname.setText(globalVarOfSelcetectedMember.getSurname());
                        mMembershipCardAccount.setText(globalVarOfSelcetectedMember.getMember2accountnumber());

                    } catch (MalformedURLException e) {

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                break;
                default: {

                    mDocumentsName.setText("");
                    // mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());
                    mDocumentsCell.setText("");
                    //mDocumentsSurname.setText(globalVarOfSelcetectedMember.getSurname());

                    //Update membership cards fields
                    mMembershipCardName.setText("");
                    //mMembershipCardSurname.setText(globalVarOfSelcetectedMember.getSurname());
                    mMembershipCardAccount.setText("");

                    //set Profile picture
                    mDocumentsProfilePic.setFill(null);
                }
                break;


            }
        }

    }

    public ImageView uploadProfileImage(Event event) {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter imagefilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
            fileChooser.setTitle("Upload Main Profile Picture");
            fileChooser.getExtensionFilters().add(imagefilter);
            selectedprofilepicture = fileChooser.showOpenDialog(null);
            if (selectedprofilepicture != null) {
                profileEditorImage = new Image(selectedprofilepicture.toURI().toString());
                URL url = selectedprofilepicture.toURI().toURL();
                mProfileImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;
    }

    public void saveProfileImage() {
        System.out.println("Save Image clicked");

        String path = PATH_TO_PICS;
        File file = new File(path);
        File source = selectedprofilepicture;
        String memberClass = mIndemnitySelector.getValue().toString();

        File dest = null;
        String accnum = "";
        int memberIdentifier = 0;
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Error creating Directory: " + e.getMessage());
                alert.showAndWait();
            }
        }
        try {

            switch (memberClass) {
                case "MAIN MEMBER":
                    dest = new File(PATH_TO_PICS + globalVarOfSelcetectedMember.getMemberaccountnumber() + "_pic.png");
                    accnum = globalVarOfSelcetectedMember.getMemberaccountnumber();
                    memberIdentifier = 1;
                    break;
                case "MEMBER 1":
                    dest = new File(PATH_TO_PICS + globalVarOfSelcetectedMember.getMember1accountnumber() + "_pic.png");
                    accnum = globalVarOfSelcetectedMember.getMember1accountnumber();
                    memberIdentifier = 2;
                    break;
                case "MEMBER 2":
                    dest = new File(PATH_TO_PICS + globalVarOfSelcetectedMember.getMember2accountnumber() + "_pic.png");
                    accnum = globalVarOfSelcetectedMember.getMember2accountnumber();
                    memberIdentifier = 3;
                    break;
                default:

                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Info:");
                    alert.setHeaderText(null);
                    alert.setContentText("Please specify the member in drop down list above ");
                    alert.showAndWait();

                    break;
            }

            try {
                Files.copy(source.toPath(), dest.toPath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Profile Image update successfully");
                alert.showAndWait();
            } catch (FileAlreadyExistsException e) {
                Files.delete(dest.toPath());
                Files.copy(source.toPath(), dest.toPath());
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Profile Image update successfully");
                alert.showAndWait();
            }

            UpdateProfileImageModel updateProfileImageModel = new UpdateProfileImageModel(accnum, dest.toString(), memberIdentifier);
            final UpdateProfilePicTask updateProfilePicTask = new UpdateProfilePicTask(updateProfileImageModel);
            Thread thread = new Thread(updateProfilePicTask);
            thread.setDaemon(true);
            thread.start();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

/*
    public ImageView uploadMember1PicAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter imagefilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
            fileChooser.setTitle("Upload Member1 Profile Picture");
            fileChooser.getExtensionFilters().add(imagefilter);
            selectedprofilepicture1 = fileChooser.showOpenDialog(null);
            if (selectedprofilepicture1 != null) {
                globalVarProfPicImage1 = new Image(selectedprofilepicture1.toURI().toString(), 200, 200, true, true);//path,PrefWidth,PrefHeight,PreserverRatio,Smooth
                mMember1ProfPic.setImage(globalVarProfPicImage1);
                mMember1ProfPic.setFitWidth(170);
                mMember1ProfPic.setFitHeight(170);
                mMember1ProfPic.setPreserveRatio(true);

                URL url = selectedprofilepicture1.toURI().toURL();
            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return null;

    }
*/

/*
    public ImageView uploadMember2PicAction(ActionEvent event) {
        try {
            FileChooser fileChooser = new FileChooser();
            FileChooser.ExtensionFilter imagefilter = new FileChooser.ExtensionFilter("Image Files", "*.jpg", "*.png");
            fileChooser.setTitle("Upload Member1 Profile Picture");
            fileChooser.getExtensionFilters().add(imagefilter);
            selectedprofilepicture2 = fileChooser.showOpenDialog(null);
            if (selectedprofilepicture2 != null) {
                globalVarProfPicImage2 = new Image(selectedprofilepicture2.toURI().toString(), 200, 200, true, true);//path,PrefWidth,PrefHeight,PreserverRatio,Smooth
                mMember2ProfPic.setImage(globalVarProfPicImage2);
                mMember2ProfPic.setFitWidth(170);
                mMember2ProfPic.setFitHeight(170);
                mMember2ProfPic.setPreserveRatio(true);

                URL url = selectedprofilepicture2.toURI().toURL();

            }
        } catch (Exception e) {
            e.printStackTrace();
            e.getCause();
        }
        return mMember2ProfPic;

    }
*/


    public void captureProfileImage(ActionEvent event) throws IOException {

        String path = PATH_TO_PICS;
        String capturedimagepath = "";
        File file = new File(path);
        String memberClass = "";
        // File source = selectedprofilepicture;
        try {
            memberClass = mIndemnitySelector.getValue().toString();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Please specify the member in drop down list above ");
            alert.showAndWait();
            return;

        }

        Webcam webcam = Webcam.getDefault();
        // webcam.setViewSize(WebcamResolution.QVGA.getSize());
        WebcamPanel webcamPanel = new WebcamPanel(webcam);
        JFrame window = new JFrame("Web Cam");
        if (webcam != null && !cameraIsRunning) {
            //webcam = Webcam.getDefault();
            try {
                webcamPanel.setFPSDisplayed(true);
                webcamPanel.setDisplayDebugInfo(true);
                webcamPanel.setImageSizeDisplayed(true);
                webcamPanel.setMirrored(true);

                window.setPreferredSize(new Dimension(400, 300));
                window.add(webcamPanel);
                window.setResizable(true);

                window.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
                window.pack();
                window.setVisible(true);
                window.setLocationRelativeTo(null);
                window.setVisible(true);
                cameraIsRunning = true;
            } catch (Exception e) {
                e.printStackTrace();
                e.getCause();
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("WEB CAM ERROR!: Web cam not available or " + e.getMessage());
                alert.showAndWait();
            }
        } else if (cameraIsRunning) {
            //  String path = "C:\\Gym Proctor\\Webcam Images\\" + new SimpleDateFormat("yyyyMMddHHmmss'.png'").format(new Date());

            switch (memberClass) {
                case "MAIN MEMBER":
                    capturedimagepath = PATH_TO_PICS + globalVarOfSelcetectedMember.getMemberaccountnumber() + "_pic.png";
                    break;
                case "MEMBER 1":
                    capturedimagepath = PATH_TO_PICS + globalVarOfSelcetectedMember.getMember1accountnumber() + "_pic.png";
                    break;
                case "MEMBER 2":
                    capturedimagepath = PATH_TO_PICS + globalVarOfSelcetectedMember.getMember2accountnumber() + "_pic.png";
                    break;
                default:
                    // System.out.println("No Image found on the indemnity form!");
                    break;
            }
            if (!file.exists()) {
                try {
                    file.mkdirs();
                } catch (Exception e) {
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Info:");
                    alert.setHeaderText(null);
                    alert.setContentText("Error creating Directory: " + e.getMessage());
                    alert.showAndWait();
                }
            }


            try {
                ImageIO.write(webcam.getImage(), "PNG", new File(capturedimagepath));

            } catch (IOException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error:");
                alert.setHeaderText(null);
                alert.setContentText("No Image captured, please sure that a webcam is plugged in working properly " + e.getMessage());
                alert.showAndWait();
                throw new RuntimeException(e);

            }
            selectedprofilepicture = new File(capturedimagepath);
            URL url = null;
            try {
                url = selectedprofilepicture.toURI().toURL();
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
            mProfileImageRectangle.setFill(new ImagePattern(new Image(url.toExternalForm())));
            //webcam.close();
            //cameraIsRunning = false;

            webcam.addWebcamListener(new WebcamListener() {
                @Override
                public void webcamOpen(WebcamEvent webcamEvent) {

                }

                @Override
                public void webcamClosed(WebcamEvent webcamEvent) {

                }

                @Override
                public void webcamDisposed(WebcamEvent webcamEvent) {

                }

                @Override
                public void webcamImageObtained(WebcamEvent webcamEvent) {

                    window.dispose();
                }
            });

        }
        //return null;

    }

    public void saveMemberDetails(ActionEvent event) throws IOException {
        try {
            connection = myDatabaseConnection.getDatabaseLinkConnection();

            if (!validateNameInput() /*| !validateCellNumberInput() | !validateIdNumberInput() | !validateDobInput()
                | !validateAddressInput() | !validateGenderInput() | !validateEmaiInput() | !validateAuthLevelInput()
                | !validatePasswordInput() | !validatePasswordConfirmInput()*/) {
                return;
            } else if (isEditingContract == false && memberUidSelectedForEditing == null) {
                //  System.out.print("Save button clicked:::");
                //save member details first and use the uid to create the account number
                // based on  the date,update the account number fields
                title = mTitles.getValue().toString();
                gender = mGender.getValue().toString();
                membershipDesc = mMembershipDesc.getValue().toString();
                minDuration = mMinDuration.getValue().toString();

                name = mName.getText().toString().trim();
                surname = mSurname.getText().toString().trim();
                idNum = mIdNumber.getText().toString().trim();
                address = mAddress.getText().toString().trim();
                cellNum = mCellNumber.getText().toString();
                tellNum = mTelNumber.getText().toString().trim();
                occupation = mOccupation.getText().toString();
                email = mEmail.getText().toString();
                contractNum = mContractNum.getText().toString();
                nextOfKin = mNextOfKin.getText().toString();
                accountNum = mAccNum.getText().toString();
                nextOfKinCell = mNextOfKinCell.getText().toString();
                mc = mMc.getText().toString();
                member1FullName = mMember1FullName.getText().toString();
                member2FullName = mMember2FullNames.getText().toString();
                member1Id = mMember1Id.getText().toString();
                member2Id = mMember2Id.getText().toString();
                member1CardNum = mMemberCardNum1.getText().toString();
                member2CardNum = mMemberCardNum2.getText().toString();
                paymentmethod = mPaymentMethods.getValue().toString();
                paymenttype = mPaymentType.getValue().toString();
                accountstatus = "ACTIVE";
                try {
                    totalAmntReceived = Float.parseFloat(mTotAmntRec.getText().toString());
                } catch (Exception e) {

                }
                try {
                    cardFee = Float.parseFloat(mCardFee.getText().toString());
                } catch (Exception e) {

                }
                try {
                    joiningFee = Float.parseFloat(mJoiningFee.getText().toString());
                } catch (Exception e) {

                }
                try {
                    upfrontPayment = Float.parseFloat(mUpFrontPayment.getText().toString());
                } catch (Exception e) {

                }
                bankName = mBankName.getValue().toString();
                bankAccNum = mBankAccNumber.getText().toString();
                bankAccType = mBankAccType.getValue().toString();
                payerDetails = mPayerDetails.getText().toString();
                payerIdNum = mPayerIdNum.getText().toString();
                payerCellNum = mPayerCellNum.getText().toString();
                payerEmail = mPayerEmail.getText().toString();


                String query = "INSERT INTO " + MEMBERSHIP_TABLE + "(title,name,surname,idnumber,address," +
                        "cellnumber,email,occupation,nextofkin,nextofkincell,memberaccountnumber,contractnumber," +
                        "mc,member1name,member1idnumber,member1accountnumber,member2name,member2idnumber," +
                        "member2accountnumber,startdate,cardfee,joiningfee,totalreceived,upfrontpayment,bankname," +
                        "bankaccountnumber,bankaccounttype,debitorderdate,payerdetails,payeridnumber," +
                        "payercellnumber,payeremail,membershipdescription,minimumduration,profilepicture,membercontractdoc" +
                        ",gender,telnumber,paymentmethod,paymenttype,accountstatus,pic1,pic2)" +
                        "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)";
                preparedStatement = null;

                try {
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.setString(1, mTitles.getValue().toString());
                    preparedStatement.setString(2, mName.getText().toString());
                    preparedStatement.setString(3, mSurname.getText().toString());
                    preparedStatement.setString(4, mIdNumber.getText().toString());
                    preparedStatement.setString(5, mAddress.getText().toString());
                    preparedStatement.setString(6, mCellNumber.getText().toString());
                    preparedStatement.setString(7, mEmail.getText().toString());
                    preparedStatement.setString(8, mOccupation.getText().toString());
                    preparedStatement.setString(9, mNextOfKin.getText().toString());
                    preparedStatement.setString(10, mNextOfKinCell.getText().toString());
                    preparedStatement.setString(11, mAccNum.getText().toString());
                    // preparedStatement.setString(12,mIdNumber.getText().toString());
                    preparedStatement.setString(12, mContractNum.getText().toString());
                    preparedStatement.setString(13, mMc.getText().toString());
                    preparedStatement.setString(14, mMember1FullName.getText().toString());
                    preparedStatement.setString(15, mMember1Id.getText().toString());
                    preparedStatement.setString(16, mMemberCardNum1.getText().toString());
                    preparedStatement.setString(17, mMember2FullNames.getText().toString());
                    preparedStatement.setString(18, mMember2Id.getText().toString());
                    preparedStatement.setString(19, mMemberCardNum2.getText().toString());
                    preparedStatement.setString(20, mStartDate.getValue().toString());
                    preparedStatement.setFloat(21, Float.parseFloat(mCardFee.getText().toString()));
                    preparedStatement.setFloat(22, Float.parseFloat(mJoiningFee.getText().toString()));
                    preparedStatement.setFloat(23, Float.parseFloat(mTotAmntRec.getText().toString()));
                    preparedStatement.setFloat(24, Float.parseFloat(mUpFrontPayment.getText().toString()));
                    preparedStatement.setString(25, mBankName.getValue().toString());
                    preparedStatement.setString(26, mBankAccNumber.getText().toString());
                    preparedStatement.setString(27, mBankAccType.getValue().toString());
                    preparedStatement.setString(28, mDebitOrderDate.getValue().toString());
                    preparedStatement.setString(29, mPayerDetails.getText().toString());
                    preparedStatement.setString(30, mPayerIdNum.getText().toString());
                    preparedStatement.setString(31, mPayerCellNum.getText().toString());
                    preparedStatement.setString(32, mPayerEmail.getText().toString());
                    preparedStatement.setString(33, mMembershipDesc.getValue().toString());
                    preparedStatement.setString(34, mMinDuration.getValue().toString());
                    preparedStatement.setString(35, "");
                    preparedStatement.setString(36, "");
                    preparedStatement.setString(37, mGender.getValue().toString());
                    preparedStatement.setString(38, mTelNumber.getText().toString());
                    preparedStatement.setString(39, paymentmethod);
                    preparedStatement.setString(40, paymenttype);
                    preparedStatement.setString(41, accountstatus);
                    preparedStatement.setString(42, "");
                    preparedStatement.setString(43, "");
                    // todo ***This is for  the backed up pdf
                    //  preparedStatement.setBinaryStream(3,mPayerEmail.getText().toString());
                    preparedStatement.execute();
                    preparedStatement.close();
                    generateAccountNumber(name, surname, idNum);
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("SQL Exception Error: " + throwables.getMessage());
                    alert.setHeaderText(null);
                    alert.setContentText("Error:" + throwables.getMessage());
                    alert.showAndWait();
                }
                refreshfields();
            } else if (isEditingContract == true && memberUidSelectedForEditing != null) {

                if (selectedprofilepicture != null) { //check if user has already selected profile picture
                    try {
                        fileInputStream = new FileInputStream(selectedprofilepicture);
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    }
                }
                if (selectedprofilepicture1 != null) { //check if user has already selected profile picture
                    try {
                        fileInputStream1 = new FileInputStream(selectedprofilepicture1);
                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                    }
                }
                if (selectedprofilepicture2 != null) { //check if user has already selected profile picture
                    try {
                        fileInputStream2 = new FileInputStream(selectedprofilepicture2);
                    } catch (FileNotFoundException e) {

                        e.printStackTrace();
                    }
                }

                try {

                    String editTextFieldsQuery = "UPDATE " + MEMBERSHIP_TABLE + " " +
                            "SET title = " + "'" + mTitles.getValue().toString() + "' " + "," +
                            "name = " + "'" + mName.getText() + "' " + "," +
                            "surname = " + "'" + mSurname.getText() + "' " + "," +
                            "idnumber = " + "'" + mIdNumber.getText() + "' " + "," +
                            "address = " + "'" + mAddress.getText() + "' " + "," +
                            "cellnumber = " + "'" + mCellNumber.getText() + "' " + "," +
                            "email = " + "'" + mEmail.getText() + "' " + "," +
                            "occupation = " + "'" + mOccupation.getText() + "' " + "," +
                            "nextofkin = " + "'" + mNextOfKin.getText() + "' " + "," +
                            "nextofkincell = " + "'" + mNextOfKinCell.getText() + "' " + "," +
                            // "memberaccountnumber = " + "'" + mAccNum.getText() + "' " + "," +
                            // "contractnumber = " + "'" + mContractNum.getText() + "' " + "," +
                            "mc = " + "'" + mMc.getText() + "' " + "," +
                            "member1name = " + "'" + mMember1FullName.getText() + "' " + "," +
                            "member1idnumber = " + "'" + mMember1Id.getText() + "' " + "," +
                            "member1accountnumber = " + "'" + mMemberCardNum1.getText() + "' " + "," +
                            "member2name = " + "'" + mMember2FullNames.getText() + "' " + "," +
                            "member2idnumber = " + "'" + mMember2Id.getText() + "' " + "," +
                            "member2accountnumber = " + "'" + mMemberCardNum2.getText() + "' " + "," +
                            "startdate = " + "'" + mStartDate.getValue() + "' " + "," +
                            "cardfee = " + "'" + mCardFee.getText() + "' " + "," +
                            "joiningfee = " + "'" + Double.parseDouble(mJoiningFee.getText()) + "' " + "," +
                            "totalreceived = " + "'" + Double.parseDouble(mTotAmntRec.getText()) + "' " + "," +
                            "upfrontpayment = " + "'" + Double.parseDouble(mUpFrontPayment.getText()) + "' " + "," +
                            "bankname = " + "'" + mBankName.getValue() + "' " + "," +
                            "bankaccountnumber = " + "'" + mBankAccNumber.getText() + "' " + "," +
                            "bankaccounttype = " + "'" + mBankAccType.getValue() + "' " + "," +
                            "debitorderdate = " + "'" + mDebitOrderDate.getValue() + "' " + "," +
                            "payerdetails = " + "'" + mPayerDetails.getText() + "' " + "," +
                            "payeridnumber = " + "'" + mPayerIdNum.getText() + "' " + "," +
                            "payercellnumber = " + "'" + mPayerCellNum.getText() + "' " + "," +
                            "payeremail = " + "'" + mPayerEmail.getText() + "' " + "," +
                            "membershipdescription = " + "'" + mMembershipDesc.getValue() + "' " + "," +
                            "minimumduration = " + "'" + mMinDuration.getValue() + "' " + "," +
                            //"profilepicture = " + "'" + mMemberProfPic.getValue() + "' " + "," +
                            "gender = " + "'" + mGender.getValue() + "' " + "," +
                            "telnumber = " + "'" + mTelNumber.getText() + "' " + "," +
                            "paymentmethod = " + "'" + mPaymentMethods.getValue() + "' " + "," +
                            "accountstatus = " + "'" + accountstatus.toString() + "' " + "," +
                            "paymenttype = " + "'" + mPaymentType.getValue() + "'" +
                            "WHERE memberuid = " + memberUidSelectedForEditing + ";";

                    //Alteration includes Profile picture field, save to Database
                    String editTextFieldsAndProfPicQuery = "UPDATE " + MEMBERSHIP_TABLE + " " +
                            "SET title = " + "'" + mTitles.getValue().toString() + "' " + "," +
                            "name = " + "'" + mName.getText() + "' " + "," +
                            "surname = " + "'" + mSurname.getText() + "' " + "," +
                            "idnumber = " + "'" + mIdNumber.getText() + "' " + "," +
                            "address = " + "'" + mAddress.getText() + "' " + "," +
                            "cellnumber = " + "'" + mCellNumber.getText() + "' " + "," +
                            "email = " + "'" + mEmail.getText() + "' " + "," +
                            "occupation = " + "'" + mOccupation.getText() + "' " + "," +
                            "nextofkin = " + "'" + mNextOfKin.getText() + "' " + "," +
                            "nextofkincell = " + "'" + mNextOfKinCell.getText() + "' " + "," +
                            // "memberaccountnumber = " + "'" + mAccNum.getText() + "' " + "," +
                            // "contractnumber = " + "'" + mContractNum.getText() + "' " + "," +
                            "mc = " + "'" + mMc.getText() + "' " + "," +
                            "member1name = " + "'" + mMember1FullName.getText() + "' " + "," +
                            "member1idnumber = " + "'" + mMember1Id.getText() + "' " + "," +
                            "member1accountnumber = " + "'" + mMemberCardNum1.getText() + "' " + "," +
                            "member2name = " + "'" + mMember2FullNames.getText() + "' " + "," +
                            "member2idnumber = " + "'" + mMember2Id.getText() + "' " + "," +
                            "member2accountnumber = " + "'" + mMemberCardNum2.getText() + "' " + "," +
                            "startdate = " + "'" + mStartDate.getValue() + "' " + "," +
                            "cardfee = " + "'" + mCardFee.getText() + "' " + "," +
                            "joiningfee = " + "'" + mJoiningFee.getText() + "' " + "," +
                            "totalreceived = " + "'" + mTotAmntRec.getText() + "' " + "," +
                            "upfrontpayment = " + "'" + mUpFrontPayment.getText() + "' " + "," +
                            "bankname = " + "'" + mBankName.getValue() + "' " + "," +
                            "bankaccountnumber = " + "'" + mBankAccNumber.getText() + "' " + "," +
                            "bankaccounttype = " + "'" + mBankAccType.getValue() + "' " + "," +
                            "debitorderdate = " + "'" + mDebitOrderDate.getValue() + "' " + "," +
                            "payerdetails = " + "'" + mPayerDetails.getText() + "' " + "," +
                            "payeridnumber = " + "'" + mPayerIdNum.getText() + "' " + "," +
                            "payercellnumber = " + "'" + mPayerCellNum.getText() + "' " + "," +
                            "payeremail = " + "'" + mPayerEmail.getText() + "' " + "," +
                            "membershipdescription = " + "'" + mMembershipDesc.getValue() + "' " + "," +
                            "minimumduration = " + "'" + mMinDuration.getValue() + "' " + "," +
                            "profilepicture = " + "?" + "," +
                            "gender = " + "'" + mGender.getValue() + "' " + "," +
                            "telnumber = " + "'" + mTelNumber.getText() + "' " + "," +
                            "paymentmethod = " + "'" + mPaymentMethods.getValue() + "' " + "," +
                            "accountstatus = " + "'" + accountstatus + "'" + "," +
                            "paymenttype = " + "'" + mPaymentType.getValue() + "'" + "," +
                            "pic1 =" + "?" + "," +
                            "pic2 =" + "? " + " " +
                            " WHERE memberuid = " + "'" + memberUidSelectedForEditing + "'" + ";";


                    if (selectedprofilepicture == null && selectedprofilepicture1 == null && selectedprofilepicture2 == null) {
                        preparedStatement = connection.prepareStatement(editTextFieldsQuery);

                    } else if (selectedprofilepicture == null || selectedprofilepicture1 == null || selectedprofilepicture2 == null) {
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Info:");
                        alert.setHeaderText(null);
                        alert.setContentText("To edit profile images of an existing account, you must reselect all three profile images");
                        alert.showAndWait();
                        return;
                    }
                    preparedStatement.execute();
                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                    alert.setTitle("Info:");
                    alert.setHeaderText(null);
                    alert.setContentText("User information Updated successfully");
                    alert.showAndWait();
                    preparedStatement.close();

                    //preparing data to alter the respective subscriber/member in the subscriptions table
                    if (isEditingContract == true) {
                        getMembershipPackage();

                        LocalDate startDate = LocalDate.parse(mStartDate.getValue().toString());
                        LocalDate endDate = startDate.plusMonths(Long.parseLong(mMinDuration.getValue().toString()));
                        Long daysLeft = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
                        LocalDate nowDate = LocalDate.now();
                        LocalDate nextDueDate = nthDayOfFollowingMonth(Integer.parseInt(mDebitOrderDate.getValue()), nowDate);


                        SubscriptionModel subscriptionModel = new SubscriptionModel(memberUidSelectedForEditing,
                                mName.getText() + " " + mSurname.getText(), mCellNumber.getText(), mIdNumber.getText()
                                , mMember1Id.getText(), mMember2Id.getText(), "", "", "",
                                selectedPackage.getPackagename(), selectedPackage.getPackagefee(), selectedPackage.getPackagefee1(),
                                selectedPackage.getPackagefee2(), mPaymentMethods.getValue(), mDebitOrderDate.getValue()
                                , "", "", "", "mStartDate.getValue().toString()", endDate.toString()
                                , String.valueOf(daysLeft), mDebitOrderDate.getValue(), nextDueDate.toString(),
                                "", "", "", "", "", "",
                                mMinDuration.getValue(), "",
                                "",
                                "", "");
                        //Must ignore empty fields when altering/updating the database
                        startAlterSubscriberService(subscriptionModel);
                    }
                    // clearFields(); todo create clear fields method, also call initialize method??? so as to refresh
                    //  todo and update any changes made

                    // System.out.println("Should now Execute method to clear input fields");

                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            refreshfields();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Error saving details: " + e.getMessage());
            alert.showAndWait();
        }

    }

    private void refreshfields() throws IOException {

        // mMainMemberProfPic.setImage(null);
        // mMember1ProfPic.setImage(null);
        // mMember2ProfPic.setImage(null);
        selectedprofilepicture = null;
        selectedprofilepicture1 = null;
        selectedprofilepicture2 = null;

        //Update Text fields
        mTitles.setValue(null);
        mGender.setValue(null);
        mName.setText("");
        mSurname.setText("");
        mIdNumber.setText("");
        mAddress.setText("");
        mCellNumber.setText("");
        mTelNumber.setText("");
        mOccupation.setText("");
        mEmail.setText("");
        mContractNum.setText("");
        mNextOfKin.setText("");
        mAccNum.setText("");
        mNextOfKinCell.setText("");
        mMc.setText("");
        mMember1FullName.setText("");
        mMember2FullNames.setText("");
        mNextOfKin.setText("");
        mMember1Id.setText("");
        mMember2Id.setText("");
        mMemberCardNum1.setText("");
        mMemberCardNum2.setText("");
        mStartDate.setValue(null);
        mTotAmntRec.setText("0");
        // mMembershipDesc.setValue(membershipModel.getMembershipdescription());
        mMembershipDesc.getSelectionModel().select(null);
        mMinDuration.setValue(null);
        mMemberCardNum2.setText("");
        mPaymentMethods.setValue(null);
        mPaymentType.setValue(null);
        mCardFee.setText("0");
        mJoiningFee.setText("0");
        mUpFrontPayment.setText("0");
        mBankName.setValue(null);
        mDebitOrderDate.setValue(null);
        mBankAccNumber.setText("");
        mBankAccType.setValue(null);
        mPayerDetails.setText("");
        mPayerIdNum.setText("");
        mPayerCellNum.setText("");
        mPayerEmail.setText("");
        mMemberCardNum2.setText("");
    }


    /*public void enableShortTermPackageFields(ActionEvent event) {
        if (mShortPackageOption.isSelected()) {
            isShortTermPackage = true;
            mShortPackageOption.setSelected(true);
            mDurationText.setVisible(true);
            mShortTermDuration.setVisible(true);
            mSecondaryFeesHBox.setVisible(false);
        } else {
            System.out.println("Is not selected");
            isShortTermPackage = false;
            mShortPackageOption.setSelected(false);
            mDurationText.setVisible(false);
            mShortTermDuration.setVisible(false);
            mSecondaryFeesHBox.setVisible(true);
        }

    }*/

    public void setEndDateFor7DayMemeber() {

        try {
            //TODO WRITE NEW SUITABLE CONDITIONS
            LocalDate localDateTime = LocalDate.parse(m7DatePicker.getValue().toString());
            LocalDate endDate = localDateTime.plusDays(Integer.parseInt(mShortDaysHint.getText()));
            m7EndDate.setText(endDate.toString());

        } catch (NullPointerException e) {
            e.printStackTrace();
        }

    }

    public void setHintFields(ActionEvent event) {

        String shorttermpackagename = mShortTermPckgSelector.getValue();
        String query = "SELECT * FROM " + SHORT_TERM_PACKAGES_TABLE + " WHERE packagename = " + "'" + shorttermpackagename + "'";
        try {
            //Run query to fetch membership packages
            connection = myDatabaseConnection.getDatabaseLinkConnection();
            preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = null;
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                selectedShortTermPackage = (new ShortTermPackageModel(
                        resultSet.getString("packageid"),
                        resultSet.getString("packagename"),
                        resultSet.getString("packagefee"),
                        resultSet.getString("daysduration")

                ));
            }
            mShortFeesHint.setText(selectedShortTermPackage.getPackagefee());
            mShortDaysHint.setText(selectedShortTermPackage.getDaysduration());
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

    }

    public void save7DayMembership() {
        // System.out.println("Saving 7 Day membership to DB");
        WeeklyMemberModel weeklyMemberModel = new WeeklyMemberModel(m7Fullaname.getText(), m7IdNUm.getText(),
                m7CellNum.getText(), m7DatePicker.getValue().toString(), m7EndDate.getText());
        final SaveWeeklyMemberTask saveWeeklyMemberTask = new SaveWeeklyMemberTask(weeklyMemberModel);
        Thread thread = new Thread(saveWeeklyMemberTask);
        thread.setDaemon(true);
        thread.start();


        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Result");
        alert.setContentText("Saved Details successfully");
        alert.showAndWait();
        //TODO confirm saving was successfully properly by getting result from bg task
        //TODO clear fields
        m7Fullaname.setText("");
        m7IdNUm.setText("");
        m7CellNum.setText("");
        m7EndDate.setText("");
        return;

    }

    public void saveNewShortTermPackage(ActionEvent event) {
        System.out.println("Should save the package");
        if (isEditingShortTermPackage == false && shortTermpackageIdSelectedForEdit == null) {
            // System.out.println("Membership Package saveClicks");
            String query = "INSERT INTO " + SHORT_TERM_PACKAGES_TABLE + " (packagename,packagefee,daysduration) " +
                    " VALUES(?,?,?);";
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, mShortTermPckgName.getText().toString());
                preparedStatement.setFloat(2, Float.parseFloat(mShortTermPckgFee.getText().toString()));
                preparedStatement.setInt(3, Integer.parseInt(mShortTermPckgDuration.getValue().toString()));

                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Package information added successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                loadAllShortTermPackages();
                clearShortTermPackageTextFields();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: " + throwables.getMessage());
                alert.showAndWait();
                throwables.printStackTrace();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Please ensure all relative monetary fields have a valid numerical value ");
                alert.showAndWait();
            }
        } else if (isEditingShortTermPackage == true && shortTermpackageIdSelectedForEdit != null) {
            //  System.out.println("We are Editing");
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                String query = "UPDATE " + SHORT_TERM_PACKAGES_TABLE + " " +
                        "SET packagename = " + "'" + mShortTermPckgName.getText() + "' " + "," +
                        "packagefee = " + "'" + Float.parseFloat(mShortTermPckgFee.getText()) + "' " + "," +
                        "daysduration = " + "'" + Float.parseFloat(mShortTermPckgDuration.getValue()) + "' " +
                        "WHERE packageid = " + shortTermpackageIdSelectedForEdit + ";";


                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Package information Updated successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                clearShortTermPackageTextFields();
                loadAllShortTermPackages();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: " + throwables.getMessage());
                alert.showAndWait();
                throwables.printStackTrace();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Please ensure all relative monetary fields have a valid numerical value ");
                alert.showAndWait();
            }

        }

    }

    private void startAlterSubscriberService(SubscriptionModel subscriptionModel) {
        final AlterSubscriberService alterSubscriberService = new AlterSubscriberService(subscriptionModel);
        alterSubscriberService.start();
    }

    private boolean validateNameInput() {
        String val = mName.getText().toString();
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

    private void generateAccountNumber(String name, String surname, String idNum) {

        String query = "SELECT * FROM " + MEMBERSHIP_TABLE + " WHERE name = " + "'" + name + "'" +
                " AND surname = " + "'" + surname + "'" + " AND idnumber = " + "'" + idNum + "'" + ";";
        preparedStatement = null;
        MembershipModel membershipItem = null;

        try {
            membershipItem = getMembershipObject(query, preparedStatement, resultSet);

            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yy");
            Date date = new Date();
            String datestring = simpleDateFormat.format(date);
            int memberId = Integer.parseInt(membershipItem.getMemberuid());
            String formatedMemberUid = String.format("%06d", memberId);
            int mainMemberId = 1;
            int mainMember1Id = mainMemberId + 1;
            int mainMember2Id = mainMemberId + 2;
            String formatedMainMemberId = String.format("%02d", mainMemberId);
            String formatedMember1Id = String.format("%02d", mainMember1Id);
            String formatedMember2Id = String.format("%02d", mainMember2Id);
            String mainMemberAccountNum = datestring + formatedMemberUid + formatedMainMemberId;
            String member1AccountNUm = datestring + formatedMemberUid + formatedMember1Id;
            String member2AccountNum = datestring + formatedMemberUid + formatedMember2Id;

            String query1 = "SET NOCOUNT ON; UPDATE " + MEMBERSHIP_TABLE + " SET memberaccountnumber = " + "'" + mainMemberAccountNum + "'" +
                    "," + "  member1accountnumber = " + "'" + member1AccountNUm + "'" +
                    "," + "  member2accountnumber = " + "'" + member2AccountNum + "'" +
                    "," + "  contractnumber = " + "'" + formatedMemberUid + "'"
                    + " WHERE memberuid = " + "'" + membershipItem.getMemberuid() + "'" + ";";
            preparedStatement = null;
            resultSet = null;

            preparedStatement = connection.prepareStatement(query1);
            //resultSet = preparedStatement.executeQuery();
            preparedStatement.execute();

            //Create pdf
            createMembershipPdfForm(memberId);
            //Create Running account with paid amounts, monthly amounts and packages details
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        } catch (Exception e) {
            // createMembershipPdfForm(memberId);
            // System.out.println("Caught this troublesome error");
            e.printStackTrace();
        }
    }

    private void createMembershipPdfForm(int memberId) {
        String pdfquery = "SELECT * FROM " + MEMBERSHIP_TABLE + " WHERE memberuid = " + "'" + memberId + "'" + " ORDER BY 43";//changed from 41
        preparedStatement = null;
        resultSet = null;

        MembershipModel membershipModelObject = null;
        membershipModelObject = getMembershipObject(pdfquery, preparedStatement, resultSet);

        //create pdf document and Folder (New Directory)
        String membershipPdfDocPath = "C:\\Gym Proctor\\Membership Contracts\\" + membershipModelObject.getMemberaccountnumber()
                + " " + membershipModelObject.getName() + " " + membershipModelObject.getSurname() + ".pdf";
        String gymProctorFoldersPath = "C:\\Gym Proctor\\Membership Contracts";
        File file = new File(gymProctorFoldersPath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        try {
            // Get local project company logo Image and add to pdf
            // String defaultLogoImagepath = "src\\main\\resources\\logo_placeholder.png"; //todo make this a global variable
            String defaultLogoImagepath = "logo_placeholder.png";
            // String localImagepath = "src\\main\\resources\\logo.png";
            String localImagepath = "logo.png";
            com.itextpdf.layout.element.Image image = new com.itextpdf.layout.element.Image(ImageDataFactory.create(localImagepath));

            PdfWriter pdfWriter = new PdfWriter(membershipPdfDocPath);
            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            Document document = new Document(pdfDocument);

            //Column width for header table
            float headingTableClumnWidth[] = {240, 80, 140, 100};
            Table headingTable = new Table(headingTableClumnWidth);
            //ColumnWidth for body tables
            float bodyTablesColumnWidth[] = {75, 200, 1, 100, 174};
            Table membersTable = new Table(bodyTablesColumnWidth);
            membersTable.setBorder(new SolidBorder(1));
            float membTypeAndFeesColumnWidth[] = {70, 70, 70, 70, 140, 140};
            Table membershipTypeAndFessTable = new Table(membTypeAndFeesColumnWidth);
            membershipTypeAndFessTable.setBorder(new SolidBorder(1));

            //Backside tables
            float allTheLegalStuffColumnWidth[] = {900};
            Table allTheLegalStuffTable = new Table(allTheLegalStuffColumnWidth);
            allTheLegalStuffTable.setBackgroundColor(ColorConstants.LIGHT_GRAY);

            // Table membershipTypeAndFessTableHolder = new Table(540);
            //membershipTypeAndFessTableHolder.addCell(new Cell(1, 1).add(membershipTypeAndFessTable).setBorder(Border.NO_BORDER));
            //ColumnWidth for signatures table
            float siganturetableColumnWidth[] = {100, 180, 100, 180};
            Table signaturesTable = new Table(siganturetableColumnWidth);

            //ColumnWidth for Ts and Cs table
            float termsAndConditionsColumnWidth[] = {560};
            Table termsAndConditionsTable = new Table(termsAndConditionsColumnWidth);

            //ColumnWidth for rules and regulations table
            Table rulesAndRegulationsTable = new Table(termsAndConditionsColumnWidth);
            rulesAndRegulationsTable.setBackgroundColor(ColorConstants.LIGHT_GRAY);

            //ColumnWidth for Signatories
            float signatoriesColumnWidth[] = {116,116,116,116,116,116};
            Table signatoriesTable = new Table(signatoriesColumnWidth);
            signatoriesTable.setBackgroundColor(ColorConstants.LIGHT_GRAY);
            signatoriesTable.setBorder(new SolidBorder(1));

            //add cells to table
            //ROW 1 headingTable
            headingTable.addCell(new Cell().add(new Paragraph(MEMBERSHIP_FORM_MAIN_HEADING_a + " XPRESSIONS WELLNESS CENTRE " + MEMBERSHIP_FORM_MAIN_HEADING_b).setBold().setFontSize(8)).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell(4, 1).add(image.setAutoScale(true))).setBorder(Border.NO_BORDER);
            headingTable.addCell(new Cell(1, 1).add(new Paragraph("NEXT OF KIN: " + membershipModelObject.getNextofkin())).setBorder(Border.NO_BORDER)).setFontSize(7);
            headingTable.addCell(new Cell(1, 1).add(new Paragraph("CONTRACT NR: " + membershipModelObject.getContractnumber())).setBorder(Border.NO_BORDER)).setFontSize(7);
            //ROW 2 headingTable
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell(1, 1).add(new Paragraph("NEXT OF KIN CELL: " + membershipModelObject.getNextofkincell())).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell(1, 1).add(new Paragraph("CARD/ACCOUNT NR: " + membershipModelObject.getMemberaccountnumber())).setBorder(Border.NO_BORDER));
            //ROW 3 headingTable
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            //ROW 4 headingTable
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            //ROW 5 headingTable
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

            //________________________________________MEMBERS TABLE IN PDF BODY_________________________________
            //ROW 1 membersTable
            membersTable.addCell(new Cell(1, 2).add(new Paragraph(PRINCIPAL_MEMBER_HEADING)).setBold().setFontSize(8).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell(1, 2).add(new Paragraph(SEC_MEMBER_HEADING)).setBold().setFontSize(8).setBorder(Border.NO_BORDER));

            //ROW 2 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("TITLE:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getTitle())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("MEMBER 1 FULLNAME:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMember1name())).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 3 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("GENDER :")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getTitle())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("MEMBER 1 ID NUMBER:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMember2name())).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 4 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("NAME :")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getName())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("CARD NUMBER 1:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMember2accountnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 5 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("SURNAME:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getSurname())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 6 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("ID NUMBER :")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getIdnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("MEMBER 2 FULL NAME:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMember1idnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 7 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("ADDRESS :")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getAddress())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("MEMBER 2 ID NUMBER:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMember2idnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 8 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("CELL : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getCellnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("CARD NUMBER 2:")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMember1accountnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 9 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("TEL : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getCellnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));

            //ROW 10 membersTable
            //TODO CONITNUE FROM HERE#############################################################
            membersTable.addCell(new Cell().add(new Paragraph("OCCUPATION ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getOccupation())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW 11 membersTable
            membersTable.addCell(new Cell().add(new Paragraph("EMAIL ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getEmail())).setFontSize(7).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            membersTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membersTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            Paragraph membershiTypeandFeesHeading = new Paragraph("\n" + "MEMBERSHIP TYPE AND FEES" + "\n").setBold().setFontSize(8);

            //________________________________________MEMBERSHIP_TYPE_AND_FEES_TABLE_________________________________
            //ROW 1 membershipTypeAndFessTable
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("START DATE : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getStartdate())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("TOTAL AMOUNT RECEIVED")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getTotalreceived())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("MEMBERSHIP PACKAGE")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMembershipdescription())).setFontSize(7).setBorder(Border.NO_BORDER));

            //ROW 2 membershipTypeAndFessTable
            //todo add payment method
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("PAYMENT METHOD : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("Payment method")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("CARD FEE :")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getCardfee())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("MINIMUM DURATION")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getMinimumduration() + " MONTHS")).setFontSize(7).setBorder(Border.NO_BORDER));


            //ROW 3 membershipTypeAndFessTable
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("JOINING FEE : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getJoiningfee())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("UPFRONT PAYMENT :")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getUpfrontpayment())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            //ROW 4 membershipTypeAndFessTable
            membershipTypeAndFessTable.addCell(new Cell(1, 6).add(new Paragraph("\nPAYER DETAILS (Account holder if payer is different from member)\n")).setFontSize(8).setBorder(Border.NO_BORDER).setBold());
            //ROW 5 membershipTypeAndFessTable
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("NAME: ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getPayerdetails())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("ID NUMBER: ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getPayeridnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("CELL NUMBER: ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getPayercellnumber())).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW  membershipTypeAndFessTable
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("EMAIL: ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getPayeremail())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            //ROW  membershipTypeAndFessTable
            membershipTypeAndFessTable.addCell(new Cell(1, 6).add(new Paragraph("\nBANKING DETAILS: \n")).setFontSize(8).setBorder(Border.NO_BORDER).setBold().setUnderline());
            //ROW  membershipTypeAndFessTable
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("BANK NAME : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getBankname())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            //  membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("BRANCH CODE :")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            // membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getBranchcode())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("ACCOUNT NUMBER : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getBankaccountnumber())).setFontSize(7).setBorder(Border.NO_BORDER));

            //ROW  membershipTypeAndFessTable
            //todo add account type to database
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("ACCOUNT TYPE : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph(membershipModelObject.getBankaccounttype())).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            membershipTypeAndFessTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            Paragraph declaractionParagraph = new Paragraph("\n\n" + MEMBERSHIP_DECLARATION_a + " " + CO_NAME + " "
                    + MEMBERSHIP_DECLARATION_b + " " + CO_NAME + " " + MEMBERSHIP_DECLARATION_c + " " + CO_NAME + " " + MEMBERSHIP_DECLARATION_d + "\n\n\n\n").setBold().setFontSize(6);

            //________________________________________SIGNATURES_TABLE_________________________________
            //ROW 1 signaturesTable
            signaturesTable.addCell(new Cell().add(new Paragraph("Member's signature : ")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            signaturesTable.addCell(new Cell().add(new Paragraph("_____________________________________________")).setFontSize(7).setBorder(Border.NO_BORDER).setUnderline());
            signaturesTable.addCell(new Cell().add(new Paragraph("Payer's signature")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            signaturesTable.addCell(new Cell().add(new Paragraph("_____________________________________________")).setFontSize(7).setBorder(Border.NO_BORDER).setUnderline());


            //ROW terms and conditions Table
            termsAndConditionsTable.addCell(new Cell(1, 1).add(new Paragraph("TERMS AND CONDITIONS APPLY\n")).setFontSize(7).setBorder(Border.NO_BORDER).setBold());
            termsAndConditionsTable.addCell(new Cell(1, 1).add(new Paragraph(TERMS_AND_CONDITIONS)).setFontSize(7).setBold());
            document.add(headingTable);
            document.add(membersTable);
            document.add(membershiTypeandFeesHeading);
            document.add(membershipTypeAndFessTable);
            document.add(declaractionParagraph);
            document.add(signaturesTable);

            //Backside, Terms and conditions and Rules and Regulations
            document.add(termsAndConditionsTable);
            document.add(new AreaBreak(AreaBreakType.NEXT_PAGE));
            Paragraph allTheLegalStaffHeading = new Paragraph("ALL THE LEGAL STAFF YOU NEED TO KNOW\nTerms and conditions\n ").setBold().setFontSize(8);

            Paragraph allTheLegalStuffParagraph = new Paragraph(ALL_THE_LEGAL_STUFF_PARAGRAPH).setFontSize(7);
            allTheLegalStuffTable.addCell(new Cell().add(allTheLegalStaffHeading)).setBorder(Border.NO_BORDER);
            allTheLegalStuffTable.addCell(new Cell().add(allTheLegalStuffParagraph)).setBorder(Border.NO_BORDER);
            allTheLegalStuffTable.addCell(new Cell().add(new Paragraph("Signature :  __________________________\n\n"))
                    .setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.WHITE)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));
            document.add(allTheLegalStuffTable);

            Paragraph rulesAndRegulationsHeading = new Paragraph("RULES AND REGULATIONS").setFontSize(7);
            Paragraph rulesAndRegulationsParagraph = new Paragraph(RULES_AND_REGULATIONS_PARAGRAPH).setFontSize(7);
            rulesAndRegulationsTable.addCell(new Cell().add(rulesAndRegulationsHeading)).setBorder(Border.NO_BORDER);
            rulesAndRegulationsTable.addCell(new Cell().add(rulesAndRegulationsParagraph)).setBorder(Border.NO_BORDER);
            rulesAndRegulationsTable.addCell(new Cell().add(new Paragraph("Signature :  __________________________\n\n"))
                    .setBorder(Border.NO_BORDER).setBackgroundColor(ColorConstants.WHITE)
                    .setHorizontalAlignment(HorizontalAlignment.CENTER));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            signatoriesTable.addCell(new Cell().add(new Paragraph("MEMBER SIGNATURE: ")).setBorder(Border.NO_BORDER).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("CONSULTANT: ")).setBorder(Border.NO_BORDER).setFontSize(7).setTextAlignment(TextAlignment.RIGHT));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("DATE: ")).setBorder(Border.NO_BORDER).setFontSize(7).setTextAlignment(TextAlignment.RIGHT));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));


            signatoriesTable.addCell(new Cell().add(new Paragraph("LEGAL GUARDIAN: ")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("CAPACITY: ")).setFontSize(7).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("IDENTITY NO: ")).setFontSize(7).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.RIGHT));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));


            signatoriesTable.addCell(new Cell().add(new Paragraph("DATE: ")).setBorder(Border.NO_BORDER).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));


            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));


            signatoriesTable.addCell(new Cell().add(new Paragraph("SALES MANAGER: ")).setBorder(Border.NO_BORDER).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("SIGNATURE: ")).setBorder(Border.NO_BORDER).setFontSize(7).setTextAlignment(TextAlignment.RIGHT));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));
            signatoriesTable.addCell(new Cell().add(new Paragraph("DATE: ")).setBorder(Border.NO_BORDER).setFontSize(7).setTextAlignment(TextAlignment.RIGHT));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));

            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));
            signatoriesTable.addCell(new Cell().add(new Paragraph("")).setFontSize(7).setBorder(Border.NO_BORDER));


            document.add(rulesAndRegulationsTable);
            document.add(signatoriesTable);

            document.close();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Membership details saved successfully,Click ok to proceed to print");
            alert.showAndWait();

            openPdf(membershipPdfDocPath);
            // if (isEditingContract == false) {
            startAddSubscriberService(membershipModelObject);
            // }


        } catch (Exception e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("PDF error might  have occurred, " + e.getMessage());
            alert.showAndWait();

        }

        //Open document from path
    }

    public void printContract(ActionEvent event) {
        if (isEditingContract == false) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("No Membership account selected for this action. Please selected an account first " +
                    "before you can print");
            alert.showAndWait();
            return;
        } else {
            try {
                int selectedMemberUid = Integer.parseInt(globalVarOfSelcetectedMember.getMemberuid());
                createMembershipPdfForm(selectedMemberUid);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void startAddSubscriberService(MembershipModel memberlist) {
        System.out.println("Add subscriber service started!");

        getMembershipPackage();

        LocalDate startDate = LocalDate.parse(memberlist.getStartdate());
        LocalDate endDate = startDate.plusMonths(Long.parseLong(memberlist.getMinimumduration()));
        Long daysLeft = Duration.between(startDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
        LocalDate nowDate = LocalDate.now();
        LocalDate nextDueDate = nthDayOfFollowingMonth(Integer.parseInt(memberlist.getDebitorderdate()), nowDate);
        /*accountbalance = cardfee + joining fee + upfrontPayment +
        float accbalance = (((Float.parseFloat(memberlist.getCardfee())) + Float.parseFloat(memberlist.getJoiningfee()))
                - Float.parseFloat(memberlist.getUpfrontpayment()));*/


        SubscriptionModel newSubscriber = new SubscriptionModel(memberlist.getMemberuid(),
                memberlist.getName() + " " + memberlist.getSurname(), memberlist.getCellnumber(),
                memberlist.getIdnumber(), memberlist.getMember1idnumber(), memberlist.getMember2idnumber(),
                memberlist.getMemberaccountnumber(), memberlist.getMember1accountnumber(), memberlist.getMember2accountnumber(),
                selectedPackage.getPackagename(), selectedPackage.getPackagefee(), selectedPackage.getPackagefee1(),
                selectedPackage.getPackagefee2(), memberlist.getPaymentmethod(), memberlist.getDebitorderdate(), "0"
                , "0", "0", memberlist.getStartdate(), endDate.toString(), String.valueOf(daysLeft),
                memberlist.getDebitorderdate(), nextDueDate.toString(), "0", nowDate.toString(),
                memberlist.getAccountstatus(), memberlist.getProfilepicture(), memberlist.getPic1(), memberlist.getPic2(),
                memberlist.getMinimumduration(), "0",
                "0",
                memberlist.getUpfrontpayment(), "0"
        );

        final AddSubscriberService addSubscriberService = new AddSubscriberService(newSubscriber);
        addSubscriberService.start();
        // clearContractFields();//todo create method to clear fields

    }


    private void openPdf(String membershipPdfDocPath) {


        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(membershipPdfDocPath);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("No suitable program has been set as the default printing program for this computer" +
                        ". Please define a default program first.");
                alert.showAndWait();
                ex.printStackTrace();
            }
        }
    }

    private MembershipModel getMembershipObject(String query, PreparedStatement preparedStatement, ResultSet resultSet) {
        MembershipModel membershipModel = null;
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();

            while (resultSet.next()) {
                membershipModel = new MembershipModel(
                        resultSet.getString("memberuid"),
                        resultSet.getString("title"),
                        resultSet.getString("name"),
                        resultSet.getString("surname"),
                        resultSet.getString("idnumber"),
                        resultSet.getString("address"),
                        resultSet.getString("cellnumber"),
                        resultSet.getString("email"),
                        resultSet.getString("occupation"),
                        resultSet.getString("nextofkin"),
                        resultSet.getString("nextofkincell"),
                        resultSet.getString("memberaccountnumber"),
                        resultSet.getString("contractnumber"),
                        resultSet.getString("mc"),
                        resultSet.getString("member1name"),
                        resultSet.getString("member1idnumber"),
                        resultSet.getString("member1accountnumber"),
                        resultSet.getString("member2name"),
                        resultSet.getString("member2idnumber"),
                        resultSet.getString("member2accountnumber"),
                        resultSet.getString("startdate"),
                        resultSet.getString("cardfee"),
                        resultSet.getString("joiningfee"),
                        resultSet.getString("totalreceived"),
                        resultSet.getString("upfrontpayment"),
                        resultSet.getString("bankname"),
                        resultSet.getString("bankaccountnumber"),
                        resultSet.getString("bankaccounttype"),
                        resultSet.getString("debitorderdate"),
                        resultSet.getString("payerdetails"),
                        resultSet.getString("payeridnumber"),
                        resultSet.getString("payercellnumber"),
                        resultSet.getString("payeremail"),
                        resultSet.getString("membershipdescription"),
                        resultSet.getString("minimumduration"),
                        resultSet.getString("profilepicture"),
                        resultSet.getString("membercontractdoc"),
                        resultSet.getString("gender"),
                        resultSet.getString("telnumber"),
                        resultSet.getString("paymentmethod"),
                        resultSet.getString("accountstatus"),
                        resultSet.getString("pic1"),
                        resultSet.getString("pic2"),
                        resultSet.getString("paymenttype")

                );


            }
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return membershipModel;
    }


    public void generateIndemnityForm(ActionEvent event) {
        if (mDocumentsName.getText().isEmpty() && mDocumentsSurname.getText().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle("Selection Error:");
            alert1.setHeaderText(null);
            alert1.setContentText("Selected member class does not apply to this account, please select the appropriate member class");
            alert1.showAndWait();
            return;

        }

        //create pdf document and Folder (New Directory)

        String indemnityPdfPath = "C:\\Gym Proctor\\Indemnity Forms\\" + "m_" +
                mDocumentsName.getText().toString() + mDocumentsSurname.getText().toString() + ".pdf";

        String indemnityFolderPath = "C:\\Gym Proctor\\Indemnity Forms";
        File file = new File(indemnityFolderPath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: " + e.getMessage());
                alert.showAndWait();
                // System.out.println("Error creating Directory: " + e.getMessage());
            }
        }

        try {
            // Get local project company logo Image and add to pdf
            // String defaultLogoImagepath = "src\\main\\resources\\logo_placeholder.png";
            String defaultLogoImagepath = "logo_placeholder.png";
            //String defaultProfilePicImagepath = "src\\main\\resources\\user_icon.png";
            String defaultProfilePicImagepath = "user_icon.png";
            //String localImagepath = "src\\home\\resources\\logo.png";// todo change this to path to company logo stored in database
            //String localImagepath = "src\\main\\resources\\logo.png";
            String localImagepath = "logo.png";
            com.itextpdf.layout.element.Image image;
            com.itextpdf.layout.element.Image memberImage = null;
            //try using local image, if its null use default place holders
            try {
                image = new com.itextpdf.layout.element.Image(ImageDataFactory.create(localImagepath));
            } catch (NullPointerException e) {
                image = new com.itextpdf.layout.element.Image(ImageDataFactory.create(defaultLogoImagepath));

            }
            //try using local image, if its null use default place holders
            try {
                String memberClass = mIndemnitySelector.getValue().toString();
                switch (memberClass) {
                    case "MAIN MEMBER":
                        memberImage = new com.itextpdf.layout.element.Image(ImageDataFactory.create(globalVarProfPicImage.getUrl()));
                        break;
                    case "MEMBER 1":
                        memberImage = new com.itextpdf.layout.element.Image(ImageDataFactory.create(globalVarProfPicImage1.getUrl()));
                        break;
                    case "MEMBER 2":
                        memberImage = new com.itextpdf.layout.element.Image(ImageDataFactory.create(globalVarProfPicImage2.getUrl()));
                        break;
                    default:
                        // System.out.println("No Image found on the indemnity form!");
                        break;
                }

            } catch (NullPointerException e) {
                memberImage = new com.itextpdf.layout.element.Image(ImageDataFactory.create(defaultProfilePicImagepath));
            }

            PdfWriter pdfWriter = new PdfWriter(indemnityPdfPath);

            PdfDocument pdfDocument = new PdfDocument(pdfWriter);
            pdfDocument.addNewPage();

            //Column width for header table
            float headingTableC0lumnWidth[] = {140, 140, 140, 140};
            Table headingTable = new Table(headingTableC0lumnWidth);


            //add cells to table
            //ROW 1 headingTable
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("MEMBER INDEMNITY ").setBold().setFontSize(15)).setBorder(Border.NO_BORDER).setTextAlignment(TextAlignment.CENTER));
            //;; headingTable.addCell(new Cell(4, 1).add(image.setAutoScale(true))).setBorder(Border.NO_BORDER);

            //Empty lines
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

            //Add Member Profile picture if member and company logo
            headingTable.addCell(new Cell().add(image.setAutoScale(true))).setBorder(Border.NO_BORDER);
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(memberImage.setAutoScale(true))).setBorder(Border.NO_BORDER);

            //Empty Lines
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

            //Member Details
            headingTable.addCell(new Cell().add(new Paragraph("NAME :")).setBorder(Border.NO_BORDER).setBold());
            headingTable.addCell(new Cell().add(new Paragraph(mDocumentsName.getText().toUpperCase())).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("SURNAME :")).setBorder(Border.NO_BORDER).setBold());
            headingTable.addCell(new Cell().add(new Paragraph(mDocumentsSurname.getText().toUpperCase())).setBorder(Border.NO_BORDER));

            //Member Details
            headingTable.addCell(new Cell().add(new Paragraph("CELL :")).setBorder(Border.NO_BORDER).setBold());
            headingTable.addCell(new Cell().add(new Paragraph(mDocumentsCell.getText().toUpperCase())).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("DATE :")).setBorder(Border.NO_BORDER).setBold());
            headingTable.addCell(new Cell().add(new Paragraph(mDocumentsDate.getValue().toString())).setBorder(Border.NO_BORDER));


            //Empty Lines
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

            // Declaration Statement paragraph
            Paragraph paragraph1 = new Paragraph(INDEMNITY_PARAGRAPH_1 + INDEMNITY_PARAGRAPH_2 + INDEMNITY_PARAGRAPH_3 + INDEMNITY_PARAGRAPH_4 + INDEMNITY_PARAGRAPH_5);

            //Add declaration statement paragraph to table
            headingTable.addCell(new Cell(1, 4).add(paragraph1).setPadding(10));

            //Empty Lines
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell(1, 4).add(new Paragraph("\n")).setBorder(Border.NO_BORDER));

            //Member signature fields and Staff Member signature fields
            headingTable.addCell(new Cell().add(new Paragraph("Member Signature : ")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("_________________")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("STAFF MEMBER SIGNATURE")).setBorder(Border.NO_BORDER));
            headingTable.addCell(new Cell().add(new Paragraph("_________________")).setBorder(Border.NO_BORDER));


            Document document = new Document(pdfDocument);
            document.add(headingTable);
            document.close();

            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Membership details saved successfully, Click ok to print");
            alert.showAndWait();
            openIndemnityPdfForPrinting(indemnityPdfPath);

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Make sure all fields contain values ");
            alert.showAndWait();
            return;

        }


    }

    private void openIndemnityPdfForPrinting(String indemnityPdfPath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(indemnityPdfPath);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("No suitable program has been set as the default printing program for this computer" +
                        ". Please define a default program first.");
                alert.showAndWait();
                ex.printStackTrace();
            }
        }

    }

    public void assignFemaleGender(ActionEvent event) {
        isFemaleGender = true;
        isMaleGender = false;
        mMaleCheckBox.setSelected(false);

    }

    public void assignMaleGender(ActionEvent event) {
        isMaleGender = true;
        isFemaleGender = false;
        mFemaleCheckBox.setSelected(false);

    }

    public void generateMembershipCard(ActionEvent event) throws IOException {
        BufferedImage malebg = null;


        if (mDocumentsName.getText().isEmpty() && mDocumentsSurname.getText().isEmpty()) {
            Alert alert1 = new Alert(Alert.AlertType.WARNING);
            alert1.setTitle("Selection Error:");
            alert1.setHeaderText(null);
            alert1.setContentText("Selected member class does not apply to this account, please select the appropriate member class");
            alert1.showAndWait();
            return;

        }
        // System.out.println(globalVarOfSelcetectedMember.getMemberaccountnumber() + " " + globalVarOfSelcetectedMember.getName());
        Code128Bean code128Bean = new Code128Bean();
        code128Bean.setHeight(7f);//was 15f
        code128Bean.setModuleWidth(0.2);//was 0.3
        code128Bean.setQuietZone(5);//was 10
        code128Bean.doQuietZone(true);

        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        BitmapCanvasProvider canvasProvider = new BitmapCanvasProvider(byteArrayOutputStream, "image/x-png", 300, BufferedImage.TYPE_BYTE_BINARY, false, 0);
        try {
            if (mIndemnitySelector.getValue().equals("MAIN MEMBER")) {
                code128Bean.generateBarcode(canvasProvider, globalVarOfSelcetectedMember.getMemberaccountnumber());
            } else if (mIndemnitySelector.getValue().equals("MEMBER 1")) {
                code128Bean.generateBarcode(canvasProvider, globalVarOfSelcetectedMember.getMember1accountnumber());
            } else if (mIndemnitySelector.getValue().equals("MEMBER 2")) {
                code128Bean.generateBarcode(canvasProvider, globalVarOfSelcetectedMember.getMember2accountnumber());
            }
        } catch (NullPointerException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Please specify the member class in choice box above to proceed");
            alert.showAndWait();

        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("There was an error: code 1 : " + e.getMessage());
            alert.showAndWait();

        }


// Save as new image

        try {
            canvasProvider.finish();
        } catch (IOException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("There was an error: code 2 :  " + e.getMessage());
            alert.showAndWait();
        }

        String barcodesFolderPath = File.listRoots()[0] + File.separator + "Gym Proctor" + File.separator + "Membership Barcodes";
        File file = new File(barcodesFolderPath);
        if (!file.exists()) {
            try {
                file.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
                //  System.out.println("Error creating Directory: " + e.getMessage());
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR!");
                alert.setHeaderText(null);
                alert.setContentText("There was an error: code 3 :  " + e.getMessage());
                alert.showAndWait();
            }
        }

// Write to png
        try {
            FileOutputStream fileOutputStream = null;
            if (mIndemnitySelector.getValue().equals("MAIN MEMBER")) {
                fileOutputStream = new FileOutputStream(barcodesFolderPath + File.separator
                        + globalVarOfSelcetectedMember.getMemberaccountnumber() + "_barcode.png");
            } else if (mIndemnitySelector.getValue().equals("MEMBER 1")) {
                fileOutputStream = new FileOutputStream(barcodesFolderPath + File.separator
                        + globalVarOfSelcetectedMember.getMember1accountnumber() + "_barcode.png");
            } else if (mIndemnitySelector.getValue().equals("MEMBER 2")) {
                fileOutputStream = new FileOutputStream(barcodesFolderPath + File.separator
                        + globalVarOfSelcetectedMember.getMember2accountnumber() + "_barcode.png");
            }
            fileOutputStream.write(byteArrayOutputStream.toByteArray());
            fileOutputStream.flush();
            fileOutputStream.close();


            // getting buffered image todo reconsider this working code
            File path = new File("./src" + File.separator + "main" + File.separator + "resources");
            // File path =  new File(File.listRoots()[0]+File.separator+"Gym Proctor"+File.separator+"resources");
            // File path = new File(getClass());
            //File path = new File("./src");
            //BufferedImage barcodeImage = canvasProvider.getBufferedImage();
            BufferedImage barcodeImage = null;
            if (mIndemnitySelector.getValue().equals("MAIN MEMBER")) {
                barcodeImage = ImageIO.read((new File(barcodesFolderPath + File.separator
                        + globalVarOfSelcetectedMember.getMemberaccountnumber() + "_barcode.png")));
            } else if (mIndemnitySelector.getValue().equals("MEMBER 1")) {
                barcodeImage = ImageIO.read((new File(barcodesFolderPath + File.separator
                        + globalVarOfSelcetectedMember.getMember1accountnumber() + "_barcode.png")));

            } else if (mIndemnitySelector.getValue().equals("MEMBER 2")) {
                barcodeImage = ImageIO.read((new File(barcodesFolderPath + File.separator
                        + globalVarOfSelcetectedMember.getMember2accountnumber() + "_barcode.png")));

            }
            if (globalVarOfSelcetectedMember.getGender().equals("Female") ||
                    globalVarOfSelcetectedMember.getGender().equals("FEMALE") || isFemaleGender == true) {
                malebg = ImageIO.read(new File("fcode.png"));
            } else if (globalVarOfSelcetectedMember.getGender().equals("Male") ||
                    globalVarOfSelcetectedMember.getGender().equals("MALE") || isMaleGender == true) {
                // malebg = ImageIO.read(new BufferedImage(getClass().getResource("mcode.png"));
                malebg = ImageIO.read(new File("mcode.png"));

            }
            //int w = Math.max(barcodeImage.getWidth(), malebg.getWidth());
            //int h = Math.max(barcodeImage.getHeight(), malebg.getHeight());
            int barcodeHeight = barcodeImage.getHeight();
            int w = malebg.getWidth();
            int h = malebg.getHeight();
            BufferedImage combined = new BufferedImage(w, h, BufferedImage.TYPE_INT_ARGB);
            // paint both images, preserving the alpha channels
            Graphics g = combined.getGraphics();
            g.drawImage(malebg, 0, 0, null);//was 0,0
            g.drawImage(barcodeImage, 20, (h - 100), null);//was 0,0
            // System.out.println("Background width: " + malebg.getWidth());
            // System.out.println("Background height: " + malebg.getHeight());
            // System.out.println("Barcode width: " + barcodeImage.getWidth());
            // System.out.println("Background height: " + barcodeImage.getHeight());

            g.dispose();
            if (mIndemnitySelector.getValue().equals("MAIN MEMBER")) {
                ImageIO.write(combined, "PNG", new File(barcodesFolderPath, globalVarOfSelcetectedMember.getMemberaccountnumber() + "_IDCARD.png"));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Barcode Generated succesfully");
                alert.showAndWait();
                String outputImagePath = barcodesFolderPath + File.separator + globalVarOfSelcetectedMember.getMemberaccountnumber() + "_IDCARD.png";
                openOutputCardImageForPrinting(outputImagePath);
            } else if (mIndemnitySelector.getValue().equals("MEMBER 1")) {
                ImageIO.write(combined, "PNG", new File(barcodesFolderPath, globalVarOfSelcetectedMember.getMember1accountnumber() + "_IDCARD.png"));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Barcode Generated succesfully");
                alert.showAndWait();
                String outputImagePath = barcodesFolderPath + File.separator + globalVarOfSelcetectedMember.getMember1accountnumber() + "_IDCARD.png";
                openOutputCardImageForPrinting(outputImagePath);
            } else if (mIndemnitySelector.getValue().equals("MEMBER 2")) {
                ImageIO.write(combined, "PNG", new File(barcodesFolderPath, globalVarOfSelcetectedMember.getMember2accountnumber() + "_IDCARD.png"));
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Barcode Generated succesfully");
                alert.showAndWait();
                String outputImagePath = barcodesFolderPath + File.separator + globalVarOfSelcetectedMember.getMember2accountnumber() + "_IDCARD.png";
                openOutputCardImageForPrinting(outputImagePath);
            }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("There was an error: code 4 :  " + e.getMessage());
            alert.showAndWait();
        } catch (IOException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("There was an error: code 5 :  " + e.getMessage() + "CAUSE: " + e.getCause());
            alert.showAndWait();
            e.printStackTrace();
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("ERROR!");
            alert.setHeaderText(null);
            alert.setContentText("There was an error: code 6 :  " + e.getMessage());
            alert.showAndWait();
        }

        mMaleCheckBox.setSelected(false);
        mFemaleCheckBox.setSelected(false);


    }

    private void openOutputCardImageForPrinting(String outputImagePath) {
        if (Desktop.isDesktopSupported()) {
            try {
                File myFile = new File(outputImagePath);
                Desktop.getDesktop().open(myFile);
            } catch (IOException ex) {
                // no application registered for PDFs
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("No suitable program has been set as the default printing program for this computer" +
                        ". Please define a default program first.");
                alert.showAndWait();
                ex.printStackTrace();
            }
        }
    }

    public void saveMembershipPackage(ActionEvent event) {

        if (isEditingPackage == false && packageIdSelectedForEdit == null) {
            // System.out.println("Membership Package saveClicks");
            String query = "INSERT INTO " + PACKAGES_TABLE + " (packagename,packagefee,packagefee1,packagefee2) " +
                    " VALUES(?,?,?,?);";
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, mPackageName.getText().toString());
                preparedStatement.setFloat(2, Float.parseFloat(mAmount.getText().toString()));
                preparedStatement.setFloat(3, Float.parseFloat(mAmount1.getText().toString()));
                preparedStatement.setFloat(4, Float.parseFloat(mAmount2.getText().toString()));
                // preparedStatement.setString(6, "");

                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Package information added successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                loadAllPackages();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: " + throwables.getMessage());
                alert.showAndWait();
                throwables.printStackTrace();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Please ensure all relative monetary fields have a valid numerical value ");
                alert.showAndWait();
            }
        } else if (isEditingPackage == true && packageIdSelectedForEdit != null) {
            //  System.out.println("We are Editing");
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                String query = "UPDATE " + PACKAGES_TABLE + " " +
                        "SET packagename = " + "'" + mPackageName.getText() + "' " + "," +
                        "packagefee = " + "'" + Float.parseFloat(mAmount.getText()) + "' " + "," +
                        "packagefee1 = " + "'" + Float.parseFloat(mAmount1.getText()) + "' " + "," +
                        "packagefee2 = " + "'" + Float.parseFloat(mAmount2.getText()) + "' " +
                        "WHERE packageid = " + packageIdSelectedForEdit + ";";


                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Package information Updated successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                clearPackageTextFields();
                loadAllPackages();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: " + throwables.getMessage());
                alert.showAndWait();
                throwables.printStackTrace();
            } catch (NumberFormatException e) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Please ensure all relative monetary fields have a valid numerical value ");
                alert.showAndWait();
            }

        } /*else if (isEditingPackage == true && packageIdSelectedForEdit != null && isShortTermPackage == true) {
            try {
            connection = myDatabaseConnection.getDatabaseLinkConnection();
            String query = "UPDATE " + PACKAGES_TABLE + " " +
                    "SET packagename = " + "'" + mPackageName.getText() + "' " + "," +
                    // "monthsduration = " + "'" +"" + "' " + "," +
                    "packagefee = " + "'" + mAmount.getText() + "' " + "," +
                    "packagefee1 = " + "'" + Float.parseFloat("0") + "' " + "," +
                    "packagefee2 = " + "'" +Float.parseFloat("0") + "' " + "," +
                    "daysduration = " + "'" + Integer.parseInt(mShortTermDuration.getValue()) + "' " +
                    "WHERE packageid = " + packageIdSelectedForEdit + ";";


                preparedStatement = connection.prepareStatement(query);
                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Package information Updated successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                clearPackageTextFields();
                loadAllPackages();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: "+throwables.getMessage());
                alert.showAndWait();
                throwables.printStackTrace();
            }catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Please ensure all relative monetary fields have a valid numerical value ");
                alert.showAndWait();
            }

        } else if (isEditingPackage == false && packageIdSelectedForEdit == null && isShortTermPackage == true) {
            String query = "INSERT INTO " + PACKAGES_TABLE + " (packagename,packagefee,packagefee1,packagefee2,daysduration) " +
                    " VALUES(?,?,?,?,?);";
            try {
                connection = myDatabaseConnection.getDatabaseLinkConnection();
                preparedStatement = connection.prepareStatement(query);
                preparedStatement.setString(1, mPackageName.getText().toString());
                // preparedStatement.setString(2,"");
                preparedStatement.setFloat(2, Float.parseFloat(mAmount.getText().toString()));
                 preparedStatement.setFloat(3, Float.parseFloat("0"));
                preparedStatement.setFloat(4, Float.parseFloat("0"));
                preparedStatement.setInt(5, Integer.parseInt(mShortTermDuration.getValue()));

                preparedStatement.execute();
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Package information added successfully");
                alert.showAndWait();
                preparedStatement.close();
                connection.close();
                loadAllPackages();
            } catch (SQLException throwables) {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: "+throwables.getMessage());
                alert.showAndWait();
                throwables.printStackTrace();
            }catch (NumberFormatException e){
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("ERROR:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR: Please ensure all relative monetary fields have a valid numerical value and duration  is set");
                alert.showAndWait();
                return;
            }

        }*/
    }

    public void mRefreshPackagesnClear(ActionEvent event) {
        loadAllPackages();
        clearPackageTextFields();
        isEditingPackage = false;
        packageIdSelectedForEdit = null;
        isShortTermPackage = false;
    }

    private void clearPackageTextFields() {
        mPackageName.setText("");
        mAmount.setText("");
        mAmount1.setText("");
        mAmount2.setText("");
    }

    private void clearShortTermPackageTextFields() {
        mShortTermPckgName.setText("");
        mShortTermPckgFee.setText("");
        mShortTermPckgDuration.setValue(null);

    }

    public void deleteMembershipPackage(ActionEvent event) {
        // System.out.println("Membership Package deleteClicks");
        if (isEditingPackage == false) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("No package selected for deletion. Please select a package you want to delete");
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this record");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM " + PACKAGES_TABLE + " WHERE packageid =" + packageIdSelectedForEdit;
                    connection = myDatabaseConnection.getDatabaseLinkConnection();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();

                } catch (SQLException ex) {
                }
                clearPackageTextFields();
                loadAllPackages();
                return;
            }

        }

    }

    public void deleteShortTermPackage(ActionEvent event) {
        // System.out.println("Membership Package deleteClicks");
        if (isEditingShortTermPackage == false) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("No package selected for deletion. Please select a package you want to delete");
            alert.showAndWait();
            return;
        } else {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("Confirm?");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to delete this record");
            Optional<ButtonType> action = alert.showAndWait();
            if (action.get() == ButtonType.OK) {
                try {
                    String query = "DELETE FROM " + SHORT_TERM_PACKAGES_TABLE + " WHERE packageid =" + shortTermpackageIdSelectedForEdit;
                    connection = myDatabaseConnection.getDatabaseLinkConnection();
                    preparedStatement = connection.prepareStatement(query);
                    preparedStatement.execute();
                    preparedStatement.close();
                    connection.close();

                } catch (SQLException ex) {
                }
                clearShortTermPackageTextFields();
                loadAllShortTermPackages();
                return;
            }

        }

    }

    public void authenticateUserInPackagesTab() {
        if (mPackagesTab.isSelected()) {
            try {
                if (authlevel.equals("1") || authlevel.equals("2")) {
                    loadAllPackages();
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle("Info:");
                    alert.setHeaderText(null);
                    alert.setContentText("Access denied! You do not have user rights to access this module");
                    alert.showAndWait();
                    mPackagesTab.setContent(null);
                    return;

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                // logOutUser();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR! " + e.getMessage());
                alert.showAndWait();

            }
        }

    }

    public void authenticateUserInShortTCandPackages() {
        if (mShortTermContractsTab.isSelected()) {
            try {
                if (authlevel.equals("3")) {

                    mManageSTPackagesPane.setVisible(false);

                }
            } catch (NullPointerException e) {
                e.printStackTrace();
                // logOutUser();

                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR! " + e.getMessage());
                alert.showAndWait();

            }
        }

    }

    public void loadAllPackages() {
        String query = null;
        PreparedStatement preparedStatement = null;
        ResultSet resultSet = null;
        SystemUser systemUser = null;

        ObservableList<MembershipPackageModel> membershipPackageModelsList = FXCollections.observableArrayList();
        membershipPackageModelsList.clear();
        query = "SELECT * FROM " + PACKAGES_TABLE;
        connection = myDatabaseConnection.getDatabaseLinkConnection();
        try {
            preparedStatement = connection.prepareStatement(query);
            resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                membershipPackageModelsList.add(new MembershipPackageModel(
                        resultSet.getString("packageid"),
                        resultSet.getString("packagename"),
                        resultSet.getString("packagefee"),
                        resultSet.getString("packagefee1"),
                        resultSet.getString("packagefee2")

                ));
            }
            preparedStatement.close();
            resultSet.close();
            connection.close();

            //Initialize Table Columns
            mPackageNameColumn.setCellValueFactory(new PropertyValueFactory<>("packagename"));
            mAmountColumn.setCellValueFactory(new PropertyValueFactory<>("packagefee"));
            mAmount1Column.setCellValueFactory(new PropertyValueFactory<>("packagefee1"));
            mAmount2Column.setCellValueFactory(new PropertyValueFactory<>("packagefee2"));
            // mSh

            mPackagesTable.setItems(membershipPackageModelsList);

            mPackagesTable.setRowFactory(tv -> {
                TableRow<MembershipPackageModel> row = new TableRow<>();
                row.setOnMouseClicked(event -> {
                    if (event.getClickCount() == 1 && (!row.isEmpty())) {
                        // System.out.println("Row Selected");
                        //String rowData = row.getItem().getAddress().toString();
                        updatePackagesFieldsForEdit(row);
                    }
                });
                return row;
            });
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

    public void loadAllShortTermPackages() {
        try {
            String query = null;
            PreparedStatement preparedStatement = null;
            ResultSet resultSet = null;
            SystemUser systemUser = null;

            ObservableList<ShortTermPackageModel> shortTermPackageModelObservableList = FXCollections.observableArrayList();
            shortTermPackageModelObservableList.clear();
            query = "SELECT * FROM " + SHORT_TERM_PACKAGES_TABLE;
            connection = myDatabaseConnection.getDatabaseLinkConnection();
            try {
                preparedStatement = connection.prepareStatement(query);
                resultSet = preparedStatement.executeQuery();
                while (resultSet.next()) {
                    shortTermPackageModelObservableList.add(new ShortTermPackageModel(
                            resultSet.getString("packageid"),
                            resultSet.getString("packagename"),
                            resultSet.getString("packagefee"),
                            resultSet.getString("daysduration")

                    ));
                }
                preparedStatement.close();
                resultSet.close();
                connection.close();

                //Initialize Table Columns
                mShortTermPckgNameClmn.setCellValueFactory(new PropertyValueFactory<>("packagename"));
                mShortTermPckgFeesClmn.setCellValueFactory(new PropertyValueFactory<>("packagefee"));
                mShortTermDurationDaysClmn.setCellValueFactory(new PropertyValueFactory<>("daysduration"));

                mShortTermPackagesTable.setItems(shortTermPackageModelObservableList);

                mShortTermPackagesTable.setRowFactory(tv -> {
                    TableRow<ShortTermPackageModel> row = new TableRow<>();
                    row.setOnMouseClicked(event -> {
                        if (event.getClickCount() == 1 && (!row.isEmpty())) {
                            updateShortTermPackagesFieldsForEdit(row);
                        }
                    });
                    return row;
                });
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            } catch (Exception e) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("WARNING!");
                alert.setHeaderText(null);
                alert.setContentText("Error: " + e.getMessage());
            }
        } catch (Exception e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("WARNING!");
            alert.setHeaderText(null);
            alert.setContentText("Error: " + e.getMessage());
        }

    }

    private void updatePackagesFieldsForEdit(TableRow<MembershipPackageModel> row) {
        isEditingPackage = true;
        packageIdSelectedForEdit = row.getItem().getPackageid();
        mPackageName.setText(row.getItem().getPackagename());
        mAmount.setText(row.getItem().getPackagefee());
        mAmount1.setText(row.getItem().getPackagefee1());
        mAmount2.setText(row.getItem().getPackagefee2());


    }

    private void updateShortTermPackagesFieldsForEdit(TableRow<ShortTermPackageModel> row) {
        isEditingShortTermPackage = true;
        shortTermpackageIdSelectedForEdit = row.getItem().getPackageid();
        mShortTermPckgNameClmn.setText(row.getItem().getPackagename());
        mShortTermPckgFeesClmn.setText(row.getItem().getPackagefee());
        mShortTermDurationDaysClmn.setText(row.getItem().getDaysduration());

    }


}
