package home;

import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;

import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.prefs.Preferences;

public class SettingsController implements Initializable {

    @FXML
    private TextField mServerName,mDatabaseName,mLogin,mPassword;
    @FXML
    private RadioButton mSqlServerAuth,mWindowsAuth,mAzurActive,mAzurActive1,mAzurActive2;
    @FXML
    private Button mTestConnection,mSaveDatabaseConnectionSettings;

    private String servername,databasename,authtype,login,password;
    public Connection databaseLinkConnection;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        // Check to see if settings already exist
        databasename = "xpressions_db";

        Preferences savedpref = Preferences.userRoot();
        String savedservername = savedpref.get("servername","");
        String savedlogin = savedpref.get("login","");
        String savedpassword = savedpref.get("password","");


        mServerName.setText(savedservername);
        mLogin.setText(savedlogin);
        mPassword.setText(savedpassword);
        setAuthType();



    }

    public  void setAuthType(){
       // System.out.println("Setting Auth Type");
        if(mSqlServerAuth.isSelected()){
            authtype = "sqlserver";
        }else if(mWindowsAuth.isSelected() || mAzurActive.isSelected() || mAzurActive1.isSelected()
         || mAzurActive2.isSelected()){
            authtype= null;
        }
    }

    public  void testDatabaseConnection(){
        if( authtype ==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Database Connection ERROR! Authentication type not supported ");
            alert.showAndWait();
            return;

        }

        Preferences savedpref = Preferences.userRoot();
        String savedservername = savedpref.get("servername","");
        String savedlogin = savedpref.get("login","");
        String savedpassword = savedpref.get("password","");

//        System.out.println("testing Database Connection");
//        servername = mServerName.getText().toString();
//        login = mLogin.getText().toString();
//        password = mPassword.getText().toString();
        String url1 = "jdbc:sqlserver://"+savedservername+";database="+databasename+";integratedSecurity=false;"+"encrypt=true;trustServerCertificate=true";
         //String url = "jdbc:sqlserver://DESKTOP-PU2PGO7\\SQLEXPRESS12;database="+databaseName+";integratedSecurity=true;"+"encrypt=true;trustServerCertificate=true";
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            databaseLinkConnection = DriverManager.getConnection(url1,savedlogin,savedpassword);


            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Database Connection Successful with details: "+"\nSERVERNAME: "+
                    savedservername+"\nLOGIN: "+savedlogin+"\n PASSWORD: "+savedpassword);
            alert.showAndWait();
        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Database Connection ERROR! "+e.getMessage());
            alert.showAndWait();
        }
    }


    public  void saveDatabaseConnectionSettings(){

        if( authtype ==null){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("Database Connection ERROR! Authentication type not supported ");
            alert.showAndWait();
            return;

        }
      //  System.out.println("saving Database Connection Settings");
        servername = mServerName.getText().toString();
        login = mLogin.getText().toString();
        password = mPassword.getText().toString();

        Preferences preferences = Preferences.userRoot();
        preferences.put("servername",servername);
        preferences.put("login",login);
        preferences.put("password",password);

        try{
            Preferences savedpref = Preferences.userRoot();
            String savedservername = savedpref.get("servername","");
            String savedlogin = savedpref.get("login","");
            String savedpassword = savedpref.get("password","");
            if(!savedservername.isEmpty() && !savedlogin.isEmpty() && !savedpassword.isEmpty()){
                Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("Connection details saved succsessfuly as"+"\nSERVERNAME: "+
                        savedservername+"\nLOGIN: "+savedlogin+"\n PASSWORD: "+savedpassword);
                alert.showAndWait();
            }else {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle("Info:");
                alert.setHeaderText(null);
                alert.setContentText("ERROR saving details! ");
                alert.showAndWait();
            }
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR saving details! "+e.getMessage());
            alert.showAndWait();
        }
    }


}
