package home;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.time.YearMonth;

public class GlobalMethods {

    public static LocalDate nthDayOfFollowingMonth(
            int desiredDayOfMonth, LocalDate currentDate) {
        return YearMonth.from(currentDate)
                .plusMonths(1)
                .atDay(desiredDayOfMonth);
    }

    public static String getTodaysDateAsStringFromDb(){
        String todaysDateString = null;
        Connection dateConnection = new DatabaseConnection().getDatabaseLinkConnection();
        String dateQuery = "SELECT CAST(GETDATE() AS DATE) AS mydate";
        Statement dateStatement = null;
        try {
            dateStatement = dateConnection.createStatement();
            ResultSet dateResultSet = dateStatement.executeQuery(dateQuery);
            if (dateResultSet.next()) {
                todaysDateString = dateResultSet.getString("mydate");
            }
            dateStatement.close();
            dateConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return todaysDateString;
    }

    public static String getTodaysDateWithTimeStampAsStringFromDb(){
        String todaysDateString = null;
        Connection dateConnection = new DatabaseConnection().getDatabaseLinkConnection();
        String dateQuery = "SELECT (CAST(DATEPART(YYYY,GETDATE()) AS VARCHAR)+'-'" +
                "+CAST(DATEPART(MM,GETDATE()) AS VARCHAR)+'-'" +
                "+CAST(DATEPART(DD,GETDATE()) AS VARCHAR)" +
                "+' '+CAST(DATEPART(HH,GETDATE()) AS VARCHAR)" +
                "+':'+CAST(DATEPART(MI,GETDATE()) AS VARCHAR)) AS mydate";
        Statement dateStatement = null;
        try {
            dateStatement = dateConnection.createStatement();
            ResultSet dateResultSet = dateStatement.executeQuery(dateQuery);
            if (dateResultSet.next()) {
                todaysDateString = dateResultSet.getString("mydate");
            }
            dateStatement.close();
            dateConnection.close();
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }

        return todaysDateString;
    }

    public static  void delayWithSeconds(long timeinSeconds){
        try{
        Long convertedseconds = timeinSeconds*1000;
        Thread.sleep(convertedseconds);}
        catch (Exception e){
            e.printStackTrace();
        }
    }


}
