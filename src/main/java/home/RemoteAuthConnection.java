package home;

import java.sql.Connection;
import java.sql.DriverManager;

import static home.SuperCon.*;

public class RemoteAuthConnection {
    public Connection databaseLinkConnection;
    public Connection getDatabaseLinkConnection(){

       // Preferences savedpref = Preferences.userRoot();
       // String remoteservername = "35.238.129.143";
        /*String remotlogin = "sqlserver";
        String remotpassword ="!@#$sql";
        String databaseName = "xpressions_db";*/

        //String databaseUser = "sa";
       // String databaseUser1 = "sqlserver";//cahnge this to sqlserver for remote
       // String databasePassword = "!@#$sql";
        //Changed integrated security to false to connect to remote instance
        //String url =  "jdbc:mysql://sql6.freesqldatabase.com:3306/sql6511605";
        String url = "jdbc:sqlserver://chihwa.database.windows.net:1433;database=transactafrica_db;encrypt=true;trustServerCertificate=false;hostNameInCertificate=*.database.windows.net;loginTimeout=60";
       // String url = "jdbc:sqlserver://"+REMOTE_SERVER+":3306"+";database="+DB_NAME+";integratedSecurity=false;"+"encrypt=true;trustServerCertificate=true";
       // String url = "jdbc:sqlserver://DESKTOP-PU2PGO7\\SQLEXPRESS12;database="+databaseName+";integratedSecurity=true;"+"encrypt=true;trustServerCertificate=true";
        try{
            Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
            databaseLinkConnection = DriverManager.getConnection(url,REMOTE_LOGIN,REMOTE_PW);
            System.out.println("Remote Database connection successful!");
        }
        catch (Exception e){
            e.printStackTrace();
            e.getCause();
        }
        return databaseLinkConnection;
    }
}
