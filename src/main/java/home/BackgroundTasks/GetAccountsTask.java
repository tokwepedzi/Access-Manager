package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.MemberSearchModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import static home.Constants.MEMBERSHIP_TABLE;

public class GetAccountsTask extends Task<ObservableList<MemberSearchModel>> {
    @Override
    protected ObservableList<MemberSearchModel> call() throws Exception {
        Connection getAccconnection = new DatabaseConnection().getDatabaseLinkConnection();
        String getAccountsQuery = " SELECT COUNT(*) as mycount from "+ MEMBERSHIP_TABLE+" ;";
        //connection = myDatabaseConnection.getDatabaseLinkConnection();
        Statement getAccstatement = getAccconnection.createStatement();
        ResultSet countResultset = getAccstatement.executeQuery(getAccountsQuery);
        int count = 0;

        if (countResultset.next()) {
             count = countResultset.getInt("mycount");

        }

        countResultset.close();
        getAccconnection.close();

        Connection connection = new DatabaseConnection().getDatabaseLinkConnection();// TODO change to private final
        // todo Connection connection and move outside override method and test
        ObservableList<MemberSearchModel> observableList = FXCollections.observableArrayList();
        String searchMembersQuery = "SELECT title, name, surname, memberaccountnumber,cellnumber, idnumber,memberuid,accountstatus from membership_tb ORDER BY name asc;";
        try {
            //connection = myDatabaseConnection.getDatabaseLinkConnection();
            Statement statement = connection.createStatement();
            ResultSet searchMemberQueryOutput = statement.executeQuery(searchMembersQuery);


            while (searchMemberQueryOutput.next()) {

                String queryTitle = searchMemberQueryOutput.getString("title");
                String queryName = searchMemberQueryOutput.getString("name");
                String querySurname = searchMemberQueryOutput.getString("surname");
                String queryAccountNum = searchMemberQueryOutput.getString("memberaccountnumber");
                String queryCellNum = searchMemberQueryOutput.getString("cellnumber");
                String queryIdNum = searchMemberQueryOutput.getString("idnumber");
                String queryUid = searchMemberQueryOutput.getString("memberuid");
                String queryAccStatus = searchMemberQueryOutput.getString("accountstatus");
                //Populate Observable List
                observableList.add(new MemberSearchModel(queryTitle, queryName, querySurname, queryAccountNum, queryCellNum, queryIdNum, queryUid, queryAccStatus));
            updateProgress(observableList.size(),count);
            updateValue(observableList);
            //use Thread.sleep only to emulate heavy tasks
            //Thread.sleep(100);
            }
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return observableList;
    }
}
