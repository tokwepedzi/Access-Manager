package home;

import java.sql.Connection;
import java.sql.DriverManager;
import java.util.prefs.Preferences;

public class DatabaseConnection {
    public Connection databaseLinkConnection;
    public Connection getDatabaseLinkConnection(){

        Preferences savedpref = Preferences.userRoot();
        String savedservername = savedpref.get("servername","");
        String savedlogin = savedpref.get("login","");
        String savedpassword = savedpref.get("password","");


        String databaseName = "xpressions_db";
        String databaseUser = "sa";
        String server="35.238.129.143";//DESKTOP-PU2PGO7\SQLEXPRESS12
        //String databaseUser1 = "sqlserver";//cahnge this to sqlserver for remote
        String databasePassword = "!@#$sql";
        //Changed integrated security to false to connect to remote instance
        //String url1 = "jdbc:sqlserver://"+server+";database="+databaseName+";integratedSecurity=false;"+"encrypt=true;trustServerCertificate=true";
        String url2 = "jdbc:sqlserver://"+savedservername+";database="+databaseName+";integratedSecurity=false;"+"encrypt=true;trustServerCertificate=true";

       // String url = "jdbc:sqlserver://DESKTOP-PU2PGO7\\SQLEXPRESS12;database="+databaseName+";integratedSecurity=false;"+"encrypt=true;trustServerCertificate=true";
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            databaseLinkConnection = DriverManager.getConnection(url2,savedlogin,savedpassword);
            System.out.println("Database connection successful");
        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLinkConnection;
    }
}
