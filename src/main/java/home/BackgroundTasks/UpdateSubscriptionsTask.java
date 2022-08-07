package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.KeySubscriberDetailer;
import home.Models.SubscriptionModel;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;

import java.sql.*;
import java.time.Duration;
import java.time.LocalDate;
import java.time.Month;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;

import static home.Constants.MEMBERSHIP_TABLE;
import static home.Constants.SUBSCRIPTIONS_TABLE;
import static home.GlobalMethods.getTodaysDateAsStringFromDb;
import static home.GlobalMethods.nthDayOfFollowingMonth;

public class UpdateSubscriptionsTask extends Task<ObservableList<SubscriptionModel>> {

    ObservableList<SubscriptionModel> subscriptionModelObservableList = FXCollections.observableArrayList();


    public UpdateSubscriptionsTask(ObservableList<SubscriptionModel> subscriptionModelObservableList) {
        this.subscriptionModelObservableList = subscriptionModelObservableList;

    }


    @Override
    protected ObservableList<SubscriptionModel> call() {
        try {


            ArrayList<String> threeMembersaccountsList = new ArrayList<>();
            ArrayList<String> twoMembersaccountsList = new ArrayList<>();
            ArrayList<String> oneMembersaccountsList = new ArrayList<>();
            ArrayList<SubscriptionModel> threeMembersSubscriptionModelList = new ArrayList<>();
            ArrayList<SubscriptionModel> twoMembersSubscriptionModelList = new ArrayList<>();
            ArrayList<SubscriptionModel> oneMembersSubscriptionModelList = new ArrayList<>();


            System.out.println("Update Subscriptions Task started successfully");
            String todaysDateString = getTodaysDateAsStringFromDb();

            //Getting all active account numbers from DB to make 3 lists of them, one with all three members active ,
            // one with only two active and one with one member active

            Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
           // String query = "SELECT  * FROM " + MEMBERSHIP_TABLE + " WHERE accountstatus = 'ACTIVE' ";
            String query = "SELECT  * FROM " + MEMBERSHIP_TABLE ;
            Statement statement = null;
            KeySubscriberDetailer keySubscriberDetailer;
            try {
                statement = connection.createStatement();
                ResultSet resultSet = statement.executeQuery(query);
                while (resultSet.next()) {
                    keySubscriberDetailer = (new KeySubscriberDetailer(
                            resultSet.getString("memberaccountnumber"), resultSet.getString("name"),
                            resultSet.getString("surname"), resultSet.getString("member1name"),
                            resultSet.getString("member2name"), resultSet.getString("profilepicture"),
                            resultSet.getString("pic1"), resultSet.getString("pic2")
                    ));
                    // String accountnumber = resultSet.getString("memberaccountnumber");
                    //get accounts with all three members subscribing and add to threeMember list
                    if (!(keySubscriberDetailer.getMainname().isEmpty() || keySubscriberDetailer.getMainsurname().isEmpty())
                            && (!keySubscriberDetailer.getMember1name().isEmpty()) && (!keySubscriberDetailer.getMember2name().isEmpty())) {
                        threeMembersaccountsList.add(keySubscriberDetailer.getAccountnumber());
                    }
                    //get accounts with two members subscribing and add to twoMember list
                    else if (keySubscriberDetailer.getMember1name().isEmpty() && !(keySubscriberDetailer.getMember2name().isEmpty())) {
                        twoMembersaccountsList.add(keySubscriberDetailer.getAccountnumber());
                    }
                    //get accounts with two members subscribingand add to twoMember list
                    else if ((!keySubscriberDetailer.getMember1name().isEmpty()) && keySubscriberDetailer.getMember2name().isEmpty()) {
                        twoMembersaccountsList.add(keySubscriberDetailer.getAccountnumber());
                    }
                    //get accounts with one members subscribing and add to oneMember list
                    else if ((!keySubscriberDetailer.getMainname().isEmpty()) || (!keySubscriberDetailer.getMainsurname().isEmpty())
                            && (keySubscriberDetailer.getMember1name().isEmpty()) && keySubscriberDetailer.getMember2name().isEmpty()) {
                        oneMembersaccountsList.add(keySubscriberDetailer.getAccountnumber());
                    }
                }
                statement.close();
                connection.close();
                resultSet.close();
                //this enables sending back the observablelist to the service's listener (service.valueProperty.addListener)
                //updateValue(subscriptionModelObservableList);
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }

            //This prints out just to visualize and verify the results are correct
           /* System.out.println("These are accounts Three members active");
            for (int i = 0; i < threeMembersaccountsList.size(); i++) {
                System.out.println(threeMembersaccountsList.get(i));
            }
            System.out.println("These are accounts Two members active");
            for (int i = 0; i < twoMembersaccountsList.size(); i++) {
                System.out.println(twoMembersaccountsList.get(i));
            }
            System.out.println("These are accounts One members active");
            for (int i = 0; i < oneMembersaccountsList.size(); i++) {
                System.out.println(oneMembersaccountsList.get(i));
            }*/


            //Iterate through each list of accounts and fetch the respective Subscriber model object and add it the  the Object model list
            //Get two member Object List
            Connection connection1 = new DatabaseConnection().getDatabaseLinkConnection();
            for (int i = 0; i < threeMembersaccountsList.size(); i++) {
                String query1 = "SELECT * FROM " + SUBSCRIPTIONS_TABLE + " WHERE accountnum = " + "'" + threeMembersaccountsList.get(i) + "'";
                Statement statement1 = null;
                SubscriptionModel subscriptionModel;

                try {
                    statement1 = connection1.createStatement();
                    ResultSet resultSet1 = statement1.executeQuery(query1);

                    while (resultSet1.next()) {
                        subscriptionModel = (new SubscriptionModel(
                                resultSet1.getString("memberuid"), resultSet1.getString("accountname"),
                                resultSet1.getString("cellnum"), resultSet1.getString("idnumber"),
                                resultSet1.getString("idnumber1"), resultSet1.getString("idnumber2"),
                                resultSet1.getString("accountnum"), resultSet1.getString("subaccount1"),
                                resultSet1.getString("subaccount2"), resultSet1.getString("packagename"),
                                resultSet1.getString("subscriptionfee"), resultSet1.getString("subscriptionfee1"),
                                resultSet1.getString("subscriptionfee2"), resultSet1.getString("paymethod"),
                                resultSet1.getString("dueday"), resultSet1.getString("accesscount"),
                                resultSet1.getString("accesscount1"), resultSet1.getString("accesscount2"),
                                resultSet1.getString("startdate"), resultSet1.getString("enddate"),
                                resultSet1.getString("daysleft"), resultSet1.getString("debitorderday"),
                                resultSet1.getString("nextduedate"), resultSet1.getString("accountbalance"),
                                resultSet1.getString("adjustmentdate"), resultSet1.getString("accountstatus"),
                                resultSet1.getString("profpic"), resultSet1.getString("profpic1"),
                                resultSet1.getString("profpic2"),resultSet1.getString("monthsduration"),
                                resultSet1.getString("monthselapsed"),resultSet1.getString("contractvalue"),
                                resultSet1.getString("totalpaid"),resultSet1.getString("elapsedamount")
                        ));

                        threeMembersSubscriptionModelList.add(subscriptionModel);
                    }


                } catch (SQLException throwables) {

                }
            }


            //Get two member Object List
            Connection connection2 = new DatabaseConnection().getDatabaseLinkConnection();
            for (int i = 0; i < twoMembersaccountsList.size(); i++) {
                String query2 = "SELECT * FROM " + SUBSCRIPTIONS_TABLE + " WHERE accountnum = " + "'" + twoMembersaccountsList.get(i) + "'";
                Statement statement2 = null;
                SubscriptionModel subscriptionModel2;

                try {
                    statement2 = connection2.createStatement();
                    ResultSet resultSet2 = statement2.executeQuery(query2);

                    while (resultSet2.next()) {
                        subscriptionModel2 = (new SubscriptionModel(
                                resultSet2.getString("memberuid"), resultSet2.getString("accountname"),
                                resultSet2.getString("cellnum"), resultSet2.getString("idnumber"),
                                resultSet2.getString("idnumber1"), resultSet2.getString("idnumber2"),
                                resultSet2.getString("accountnum"), resultSet2.getString("subaccount1"),
                                resultSet2.getString("subaccount2"), resultSet2.getString("packagename"),
                                resultSet2.getString("subscriptionfee"), resultSet2.getString("subscriptionfee1"),
                                resultSet2.getString("subscriptionfee2"), resultSet2.getString("paymethod"),
                                resultSet2.getString("dueday"), resultSet2.getString("accesscount"),
                                resultSet2.getString("accesscount1"), resultSet2.getString("accesscount2"),
                                resultSet2.getString("startdate"), resultSet2.getString("enddate"),
                                resultSet2.getString("daysleft"), resultSet2.getString("debitorderday"),
                                resultSet2.getString("nextduedate"), resultSet2.getString("accountbalance"),
                                resultSet2.getString("adjustmentdate"), resultSet2.getString("accountstatus"),
                                resultSet2.getString("profpic"), resultSet2.getString("profpic1"),
                                resultSet2.getString("profpic2"),resultSet2.getString("monthsduration"),
                                resultSet2.getString("monthselapsed"),resultSet2.getString("contractvalue"),
                                resultSet2.getString("totalpaid"), resultSet2.getString("elapsedamount")
                        ));

                        twoMembersSubscriptionModelList.add(subscriptionModel2);
                    }


                } catch (SQLException throwables) {

                }
            }


            //Get One member Object List
            Connection connection3 = new DatabaseConnection().getDatabaseLinkConnection();
            for (int i = 0; i < oneMembersaccountsList.size(); i++) {
                String query3 = "SELECT * FROM " + SUBSCRIPTIONS_TABLE + " WHERE accountnum = " + "'" + oneMembersaccountsList.get(i) + "'";
                Statement statement3 = null;
                SubscriptionModel subscriptionModel3;

                try {
                    statement3 = connection3.createStatement();
                    ResultSet resultSet3 = statement3.executeQuery(query3);

                    while (resultSet3.next()) {
                        subscriptionModel3 = (new SubscriptionModel(
                                resultSet3.getString("memberuid"), resultSet3.getString("accountname"),
                                resultSet3.getString("cellnum"), resultSet3.getString("idnumber"),
                                resultSet3.getString("idnumber1"), resultSet3.getString("idnumber2"),
                                resultSet3.getString("accountnum"), resultSet3.getString("subaccount1"),
                                resultSet3.getString("subaccount2"), resultSet3.getString("packagename"),
                                resultSet3.getString("subscriptionfee"), resultSet3.getString("subscriptionfee1"),
                                resultSet3.getString("subscriptionfee2"), resultSet3.getString("paymethod"),
                                resultSet3.getString("dueday"), resultSet3.getString("accesscount"),
                                resultSet3.getString("accesscount1"), resultSet3.getString("accesscount2"),
                                resultSet3.getString("startdate"), resultSet3.getString("enddate"),
                                resultSet3.getString("daysleft"), resultSet3.getString("debitorderday"),
                                resultSet3.getString("nextduedate"), resultSet3.getString("accountbalance"),
                                resultSet3.getString("adjustmentdate"), resultSet3.getString("accountstatus"),
                                resultSet3.getString("profpic"), resultSet3.getString("profpic1"),
                                resultSet3.getString("profpic2"),resultSet3.getString("monthsduration"),
                                resultSet3.getString("monthselapsed"),resultSet3.getString("contractvalue"),
                                resultSet3.getString("totalpaid"),resultSet3.getString("elapsedamount")
                        ));

                        oneMembersSubscriptionModelList.add(subscriptionModel3);
                    }


                } catch (SQLException throwables) {

                }
            }


           /* System.out.println("THREE Membered List");
            for (int j = 0; j < threeMembersSubscriptionModelList.size(); j++) {
                System.out.println(threeMembersSubscriptionModelList.get(j).getAccountnum() + "NAME: " + threeMembersSubscriptionModelList.get(j).getAccountname());
            }
            System.out.println("TWO Membered List");

            for (int j = 0; j < twoMembersSubscriptionModelList.size(); j++) {
                System.out.println(twoMembersSubscriptionModelList.get(j).getAccountnum() + "NAME: " + twoMembersSubscriptionModelList.get(j).getAccountname());
            }

            System.out.println("ONE Membered List");
            for (int j = 0; j < oneMembersSubscriptionModelList.size(); j++) {
                System.out.println(oneMembersSubscriptionModelList.get(j).getAccountnum() + "NAME: " + oneMembersSubscriptionModelList.get(j).getAccountname());
            }*/

            //Now Calculate due dates and adjust amounts accordingly for 3 Member Object list
            for (int i = 0; i < threeMembersSubscriptionModelList.size(); i++) {
                String accountstatus = threeMembersSubscriptionModelList.get(i).getAccountstatus();
                LocalDate todaysDate = LocalDate.parse(todaysDateString);
                LocalDate startDate = LocalDate.parse(threeMembersSubscriptionModelList.get(i).getStartdate());
                LocalDate endDate = LocalDate.parse(threeMembersSubscriptionModelList.get(i).getEnddate());
                LocalDate lastAdjustmentDate = LocalDate.parse(threeMembersSubscriptionModelList.get(i).getAdjustmentdate());
                LocalDate nextDueDate = LocalDate.parse(threeMembersSubscriptionModelList.get(i).getNextduedate());
                Long daysLeft = Duration.between(todaysDate.atStartOfDay(), endDate.atStartOfDay()).toDays(); //todo if days left is less than zero set account to paused in DB
               // System.out.println("Days Left = " + String.valueOf(daysLeft));
                Long daysSinceLastAdjustment = Duration.between(lastAdjustmentDate.atStartOfDay(), todaysDate.atStartOfDay()).toDays();
                //get Months Elapsed since beginning of contract
                Long monthsElapsed = ChronoUnit.MONTHS.between(startDate.atStartOfDay(),todaysDate.atStartOfDay());
               // System.out.println("DAYS SINCE LAST ADJ " + String.valueOf(daysSinceLastAdjustment));
               // long multiplicationFactor = daysSinceLastAdjustment / 30;
                //update contract value
                float contractvalue = Float.parseFloat(threeMembersSubscriptionModelList.get(i).getMonthsduration())*
                        ( Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee())+
                        Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee1())+
                        Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee2()));

                //Apply subscription fees for 3 members
                if (accountstatus.equals("ACTIVE")) {
                   /* float totalfees = (Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee())
                            + Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee1()) +
                            Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee2())) * multiplicationFactor;
                    float newbalance = totalfees + Float.parseFloat(threeMembersSubscriptionModelList.get(i).getAccountbalance());*/
                    //The total amount of payments made to date so far on this account
                    float totalpaid = Float.parseFloat(threeMembersSubscriptionModelList.get(i).getTotalpaid());
                    float elapsedamount = (Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee())
                            + Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee1()) +
                            Float.parseFloat(threeMembersSubscriptionModelList.get(i).getSubscriptionfee2())) * monthsElapsed;
                    float accountbalance =  elapsedamount-totalpaid;

                    LocalDate newNextDueDate = nthDayOfFollowingMonth(Integer.parseInt(threeMembersSubscriptionModelList.get(i).getDebitorderday()), todaysDate);


                    //set updated values
                    threeMembersSubscriptionModelList.get(i).setDaysleft(String.valueOf(daysLeft));
                    threeMembersSubscriptionModelList.get(i).setNextduedate(newNextDueDate.toString());
                    threeMembersSubscriptionModelList.get(i).setAccountbalance(String.valueOf(accountbalance));
                    threeMembersSubscriptionModelList.get(i).setAdjustmentdate(todaysDate.toString());
                    threeMembersSubscriptionModelList.get(i).setMonthselapsed(String.valueOf(monthsElapsed));
                    threeMembersSubscriptionModelList.get(i).setElapsedamount(String.valueOf(elapsedamount));
                    threeMembersSubscriptionModelList.get(i).setContractvalue(String.valueOf(contractvalue));

                    String updateQuery = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                            "SET daysleft = " + "'" + threeMembersSubscriptionModelList.get(i).getDaysleft() + "' " + "," +
                            "nextduedate = " + "'" + threeMembersSubscriptionModelList.get(i).getNextduedate() + "' " + "," +
                            "accountbalance = " + "'" + threeMembersSubscriptionModelList.get(i).getAccountbalance() + "' " + "," +
                            "adjustmentdate = " + "'" + threeMembersSubscriptionModelList.get(i).getAdjustmentdate() + "' " + "," +
                            "monthselapsed = " + "'" + threeMembersSubscriptionModelList.get(i).getMonthselapsed() + "' " + "," +
                            "elapsedamount = " + "'" + threeMembersSubscriptionModelList.get(i).getElapsedamount() + "' " + "," +
                            "contractvalue = " + "'" + threeMembersSubscriptionModelList.get(i).getContractvalue() + "' " +
                            "WHERE accountnum = " + threeMembersSubscriptionModelList.get(i).getAccountnum() + ";";

                    try {
                        Connection connection4 = new DatabaseConnection().getDatabaseLinkConnection();
                        PreparedStatement preparedStatement4 = connection4.prepareStatement(updateQuery);
                        preparedStatement4.execute();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }
                  //  System.out.println("ADJUSTMENTS EFFECTED SUCCESSFULLY ::::: OPERATION DONE");
                }


            }


            //Now Calculate due dates and adjust amounts accordingly for 2 Member Object list
            for (int i = 0; i < twoMembersSubscriptionModelList.size(); i++) {
                String accountstatus = twoMembersSubscriptionModelList.get(i).getAccountstatus();
                LocalDate todaysDate = LocalDate.parse(todaysDateString);
                LocalDate startDate = LocalDate.parse(twoMembersSubscriptionModelList.get(i).getStartdate());
                LocalDate endDate = LocalDate.parse(twoMembersSubscriptionModelList.get(i).getEnddate());
                LocalDate lastAdjustmentDate = LocalDate.parse(twoMembersSubscriptionModelList.get(i).getAdjustmentdate());
                LocalDate nextDueDate = LocalDate.parse(twoMembersSubscriptionModelList.get(i).getNextduedate());
                Long daysLeft = Duration.between(todaysDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
               // System.out.println("Days Left = " + String.valueOf(daysLeft));
                Long daysSinceLastAdjustment = Duration.between(lastAdjustmentDate.atStartOfDay(), todaysDate.atStartOfDay()).toDays();
                //get Months Elapsed since beginning of contract
                Long monthsElapsed = ChronoUnit.MONTHS.between(startDate.atStartOfDay(),todaysDate.atStartOfDay());
                //System.out.println("DAYS SINCE LAST ADJ " + String.valueOf(daysSinceLastAdjustment));
              //  long multiplicationFactor = daysSinceLastAdjustment / 30;
                //update contract value
                float contractvalue = Float.parseFloat(twoMembersSubscriptionModelList.get(i).getMonthsduration())*
                        ( Float.parseFloat(twoMembersSubscriptionModelList.get(i).getSubscriptionfee())+
                                Float.parseFloat(twoMembersSubscriptionModelList.get(i).getSubscriptionfee1()));

                //Apply subscription fees for 3 members
                if (accountstatus.equals("ACTIVE")) {
                   /* float totalfees = (Float.parseFloat(twoMembersSubscriptionModelList.get(i).getSubscriptionfee())
                            + Float.parseFloat(twoMembersSubscriptionModelList.get(i).getSubscriptionfee1())) * multiplicationFactor;
                    float newbalance = totalfees + Float.parseFloat(twoMembersSubscriptionModelList.get(i).getAccountbalance());*/

                    float totalpaid = Float.parseFloat(twoMembersSubscriptionModelList.get(i).getTotalpaid());
                    float elapsedamount = (Float.parseFloat(twoMembersSubscriptionModelList.get(i).getSubscriptionfee())
                            + Float.parseFloat(twoMembersSubscriptionModelList.get(i).getSubscriptionfee1()))* monthsElapsed;
                    float accountbalance =  elapsedamount-totalpaid;

                    LocalDate newNextDueDate = nthDayOfFollowingMonth(Integer.parseInt(twoMembersSubscriptionModelList.get(i).getDebitorderday()), todaysDate);


                    //set updated values
                    twoMembersSubscriptionModelList.get(i).setDaysleft(String.valueOf(daysLeft));
                    twoMembersSubscriptionModelList.get(i).setNextduedate(newNextDueDate.toString());
                    twoMembersSubscriptionModelList.get(i).setAccountbalance(String.valueOf(accountbalance));
                    twoMembersSubscriptionModelList.get(i).setAdjustmentdate(todaysDate.toString());
                    twoMembersSubscriptionModelList.get(i).setMonthselapsed(String.valueOf(monthsElapsed));
                    twoMembersSubscriptionModelList.get(i).setElapsedamount(String.valueOf(elapsedamount));
                    twoMembersSubscriptionModelList.get(i).setContractvalue(String.valueOf(contractvalue));


                    String updateQuery = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                            "SET daysleft = " + "'" + twoMembersSubscriptionModelList.get(i).getDaysleft() + "' " + "," +
                            "nextduedate = " + "'" + twoMembersSubscriptionModelList.get(i).getNextduedate() + "' " + "," +
                            "accountbalance = " + "'" + twoMembersSubscriptionModelList.get(i).getAccountbalance() + "' " + "," +
                            "adjustmentdate = " + "'" + twoMembersSubscriptionModelList.get(i).getAdjustmentdate() + "' " + "," +
                            "monthselapsed = " + "'" + twoMembersSubscriptionModelList.get(i).getMonthselapsed() + "' " + "," +
                            "elapsedamount = " + "'" + twoMembersSubscriptionModelList.get(i).getElapsedamount() + "' " + "," +
                            "contractvalue = " + "'" + twoMembersSubscriptionModelList.get(i).getContractvalue() + "' " +
                            "WHERE accountnum = " + twoMembersSubscriptionModelList.get(i).getAccountnum() + ";";

                    try {
                        Connection connection4 = new DatabaseConnection().getDatabaseLinkConnection();
                        PreparedStatement preparedStatement4 = connection4.prepareStatement(updateQuery);
                        preparedStatement4.execute();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }


            }


            //Now Calculate due dates and adjust amounts accordingly for 1 Member Object list
            for (int i = 0; i < oneMembersSubscriptionModelList.size(); i++) {
                String accountstatus = oneMembersSubscriptionModelList.get(i).getAccountstatus();
                LocalDate todaysDate = LocalDate.parse(todaysDateString);
                LocalDate startDate = LocalDate.parse(oneMembersSubscriptionModelList.get(i).getStartdate());
                LocalDate endDate = LocalDate.parse(oneMembersSubscriptionModelList.get(i).getEnddate());
                LocalDate lastAdjustmentDate = LocalDate.parse(oneMembersSubscriptionModelList.get(i).getAdjustmentdate());
                LocalDate nextDueDate = LocalDate.parse(oneMembersSubscriptionModelList.get(i).getNextduedate());
                Long daysLeft = Duration.between(todaysDate.atStartOfDay(), endDate.atStartOfDay()).toDays();
               // System.out.println("Days Left = " + String.valueOf(daysLeft));
                Long daysSinceLastAdjustment = Duration.between(lastAdjustmentDate.atStartOfDay(), todaysDate.atStartOfDay()).toDays();
                //get Months Elapsed since beginning of contract
                Long monthsElapsed = ChronoUnit.MONTHS.between(startDate.atStartOfDay(),todaysDate.atStartOfDay());
               // System.out.println("DAYS SINCE LAST ADJ " + String.valueOf(daysSinceLastAdjustment));
               // long multiplicationFactor = daysSinceLastAdjustment / 30;
                //update contract value
                float contractvalue = Float.parseFloat(oneMembersSubscriptionModelList.get(i).getMonthsduration())*
                        ( Float.parseFloat(oneMembersSubscriptionModelList.get(i).getSubscriptionfee()));

                //Apply subscription fees for 3 members
                if (accountstatus.equals("ACTIVE")) {
                   /* float totalfees = (Float.parseFloat(oneMembersSubscriptionModelList.get(i).getSubscriptionfee())) * multiplicationFactor;
                    float newbalance = totalfees + Float.parseFloat(oneMembersSubscriptionModelList.get(i).getAccountbalance());*/
                    //The total amount of payments made to date so far on this account
                    float totalpaid = Float.parseFloat(oneMembersSubscriptionModelList.get(i).getTotalpaid());
                    float elapsedamount = (Float.parseFloat(oneMembersSubscriptionModelList.get(i).getSubscriptionfee()))* monthsElapsed;
                    float accountbalance =  elapsedamount-totalpaid;

                    LocalDate newNextDueDate = nthDayOfFollowingMonth(Integer.parseInt(oneMembersSubscriptionModelList.get(i).getDebitorderday()), todaysDate);


                    //set updated values
                    oneMembersSubscriptionModelList.get(i).setDaysleft(String.valueOf(daysLeft));
                    oneMembersSubscriptionModelList.get(i).setNextduedate(newNextDueDate.toString());
                    oneMembersSubscriptionModelList.get(i).setAccountbalance(String.valueOf(accountbalance));
                    oneMembersSubscriptionModelList.get(i).setAdjustmentdate(todaysDate.toString());
                    oneMembersSubscriptionModelList.get(i).setMonthselapsed(String.valueOf(monthsElapsed));
                    oneMembersSubscriptionModelList.get(i).setElapsedamount(String.valueOf(elapsedamount));
                    oneMembersSubscriptionModelList.get(i).setContractvalue(String.valueOf(contractvalue));

                    String updateQuery = "UPDATE " + SUBSCRIPTIONS_TABLE + " " +
                            "SET daysleft = " + "'" + oneMembersSubscriptionModelList.get(i).getDaysleft() + "' " + "," +
                            "nextduedate = " + "'" + oneMembersSubscriptionModelList.get(i).getNextduedate() + "' " + "," +
                            "accountbalance = " + "'" + oneMembersSubscriptionModelList.get(i).getAccountbalance() + "' " + "," +
                            "adjustmentdate = " + "'" + oneMembersSubscriptionModelList.get(i).getAdjustmentdate() + "' " + "," +
                            "monthselapsed = " + "'" + oneMembersSubscriptionModelList.get(i).getMonthselapsed() + "' " + "," +
                            "elapsedamount = " + "'" + oneMembersSubscriptionModelList.get(i).getMonthselapsed() + "' " +"," +
                            "contractvalue = " + "'" + oneMembersSubscriptionModelList.get(i).getContractvalue() + "' " +
                            "WHERE accountnum = " + oneMembersSubscriptionModelList.get(i).getAccountnum() + ";";

                    try {
                        Connection connection4 = new DatabaseConnection().getDatabaseLinkConnection();
                        PreparedStatement preparedStatement4 = connection4.prepareStatement(updateQuery);
                        preparedStatement4.execute();
                    } catch (SQLException throwables) {
                        throwables.printStackTrace();
                    }

                }


            }


            //Get Subscription Model Objects from all  three lists and add to observable list for returning

            //subscriptionModelObservableList.clear();


            //subscriptionModelObservableList.addAll(threeMembersSubscriptionModelList);
            // updateValue(subscriptionModelObservableList);
            for (int i = 0; i < threeMembersSubscriptionModelList.size(); i++) {
                subscriptionModelObservableList.add(threeMembersSubscriptionModelList.get(i));
                updateValue(subscriptionModelObservableList);
            }
            // subscriptionModelObservableList.addAll(twoMembersSubscriptionModelList);
            // updateValue(subscriptionModelObservableList);
            for (int i = 0; i < twoMembersSubscriptionModelList.size(); i++) {
                subscriptionModelObservableList.add(twoMembersSubscriptionModelList.get(i));
                updateValue(subscriptionModelObservableList);
            }
            // subscriptionModelObservableList.addAll(oneMembersSubscriptionModelList);
            // updateValue(subscriptionModelObservableList);
            for (int i = 0; i < oneMembersSubscriptionModelList.size(); i++) {
                subscriptionModelObservableList.add(oneMembersSubscriptionModelList.get(i));
                updateValue(subscriptionModelObservableList);
            }


            //Show the contents of  the oservable list before returning it //todo change this its not working

           /* for (int m = 0; m < subscriptionModelObservableList.size(); m++) {
                System.out.println(" Item in List :" + subscriptionModelObservableList.get(m).getAccountnum() +
                        "BALANCE: " + subscriptionModelObservableList.get(m).getAccountbalance() + " llll "
                        + subscriptionModelObservableList.size());
            }*/


            // updateValue(subscriptionModelObservableList);
        }
        catch (Exception e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Info:");
            alert.setHeaderText(null);
            alert.setContentText("ERROR WARMING! "+e.getMessage());
            alert.showAndWait();
        }
        return subscriptionModelObservableList;
    }
}
