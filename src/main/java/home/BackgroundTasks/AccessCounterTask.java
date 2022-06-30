package home.BackgroundTasks;

import home.DatabaseConnection;
import javafx.concurrent.Task;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;

import static home.Constants.SUBSCRIPTIONS_TABLE;

public class AccessCounterTask extends Task<Long> {

    private final String accountnumber;
    private final int memberIdentifier;

    public AccessCounterTask(String accountnumber, int memberIdentifier) {
        this.accountnumber = accountnumber;
        this.memberIdentifier = memberIdentifier;
    }

    @Override
    protected Long call() throws Exception {

        int accoutnId = memberIdentifier;
        boolean alreadyExecuted = false;
        switch (accoutnId) {
            case 1: {
                try {
                    String query = "UPDATE " + SUBSCRIPTIONS_TABLE + " SET accesscount = accesscount+1 WHERE accountnum = "
                            +"'"+ accountnumber +"'"+ ";";
                    Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
                    PreparedStatement statement = null;
                    statement = connection.prepareStatement(query);
                    statement.execute();
                    connection.close();
                    statement.close();
                    alreadyExecuted = true;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            break;
            case 2: {
                try {
                    String query = "UPDATE " + SUBSCRIPTIONS_TABLE + " SET accesscount1 = accesscount1+1 WHERE subaccount1 = "
                            + accountnumber + ";";
                    Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
                    PreparedStatement statement = null;
                    statement = connection.prepareStatement(query);
                    statement.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            break;
            case 3: {
                try {
                    String query = "UPDATE " + SUBSCRIPTIONS_TABLE + " SET accesscount2 = accesscount2+1 WHERE subaccount2 = "
                            + accountnumber + ";";
                    Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
                    PreparedStatement statement = null;
                    statement = connection.prepareStatement(query);
                    statement.execute();
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }

            }
            break;
            default: {

            }
            break;
        }
        return null;
    }
}
