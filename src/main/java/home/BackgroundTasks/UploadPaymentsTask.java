package home.BackgroundTasks;

import home.DatabaseConnection;
import home.Models.*;
import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.concurrent.Task;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.net.URL;
import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static home.Constants.*;

public class UploadPaymentsTask extends Task<ObservableList<PaymentModelObject>> {

    private final String path;
    private int lastRowNum;

    public UploadPaymentsTask(String path, int lastNum) {
        this.path = path;
        this.lastRowNum = lastNum;
    }

    // ObservableList<PaymentModelObject> observableList = FXCollections.observableArrayList();
    ObservableList<PaymentModelObject> paymentModelObjects = FXCollections.observableArrayList();
    private DatabaseConnection myDatabaseConnection = new DatabaseConnection();
    private Connection connection = myDatabaseConnection.getDatabaseLinkConnection();
    PreparedStatement preparedStatement = null;
    private MembershipModel globalMembershipModel;
    private SubscriptionModel globalSubscriptionModel;

    @Override
    protected ObservableList<PaymentModelObject> call() {
        try {
            try {
                // System.out.println("PRINTING THE PATH FROM TASK: " + path);
                paymentModelObjects.clear();
                String query = "INSERT INTO " + IMPORTED_PAYMENTS_TABLE + " (paymentdate,idnum,paymentamount) " +
                        "VALUES (?,?,?);";
                preparedStatement = connection.prepareStatement(query);


                //read columns from the excel file
                URL url = new URL(path);
                File file = new File(url.getFile());
                FileInputStream fileInputStream = new FileInputStream(new File(file.getAbsolutePath()));
                XSSFWorkbook xssfWorkbook = new XSSFWorkbook(fileInputStream);
                XSSFSheet xssfSheet = xssfWorkbook.getSheetAt(0);
                XSSFRow row = null;
                XSSFCell xssfCell = null;
                // start i from 1, column 0 is the headings
                for (int i = 1; i <= lastRowNum; i++) {
                    row = xssfSheet.getRow(i);
                    xssfCell = row.getCell(0);
                    if (xssfCell.getCellType() != null) {
                        preparedStatement.setString(1, row.getCell(0).getStringCellValue().toString());
                        preparedStatement.setString(2, row.getCell(1).getStringCellValue());
                        preparedStatement.setDouble(3, row.getCell(2).getNumericCellValue());
                        preparedStatement.execute();
                    }
                }
                xssfWorkbook.close();
                fileInputStream.close();
                preparedStatement.close();
                connection.close();
            } catch (FileNotFoundException e) {
                System.out.println("Exception : 1");
                Platform.runLater(() -> {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 1: " + e.getMessage(), ButtonType.OK);
                    dialog.show();
                });
                e.printStackTrace();
                e.getMessage();
            } catch (SQLException e) {
                System.out.println("Exception : 2");
                Platform.runLater(() -> {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 2: " + e.getMessage(), ButtonType.OK);
                    dialog.show();
                });
                e.printStackTrace();
                e.getMessage();
            } catch (Exception e) {
                System.out.println("Exception : 3");
                Platform.runLater(() -> {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 3: " + e.getMessage(), ButtonType.OK);
                    dialog.show();
                });
                e.printStackTrace();
                e.getMessage();
            }
            System.out.println("LAST ROWN NUMBER IS  " + lastRowNum);
            // System.out.println("TASK DONE DONE! " + path);

            String importedPaymentsQuery = "SELECT*FROM " + IMPORTED_PAYMENTS_TABLE;
            Connection connection = new DatabaseConnection().getDatabaseLinkConnection();
            ResultSet resultSet = null;
            System.out.println("TASK DONE DONE! ??");
            try {
                PreparedStatement preparedStatement1 = connection.prepareStatement(importedPaymentsQuery);
                resultSet = preparedStatement1.executeQuery();

                System.out.println("QUERY BEGIN ??");


                while (resultSet.next()) {
                    ImportedPaymentModel importedPaymentModel = new ImportedPaymentModel(
                            resultSet.getString("paymentdate"),
                            resultSet.getString("idnum"),
                            resultSet.getString("paymentamount")
                    );
                    System.out.println("QUERY END ??");
                    //Get corresponding Membershipmodel
                    //Get the Member the membership object from the database (membership table)
                    String membershipModelQuery = "SELECT * FROM " + MEMBERSHIP_TABLE + " WHERE idnumber = " + "'" + importedPaymentModel.getIdnum() + "'";
                    //DatabaseConnection databaseConnection = new DatabaseConnection();
                    Connection membershipModelConnection = new DatabaseConnection().getDatabaseLinkConnection();
                    PreparedStatement memberModelPreparedStatement = null;
                    ResultSet membershipModelResultSet = null;

                    try {
                        // membershipModelConnection = databaseConnection.getDatabaseLinkConnection();
                        memberModelPreparedStatement = membershipModelConnection.prepareStatement(membershipModelQuery);
                        membershipModelResultSet = memberModelPreparedStatement.executeQuery();
                        MembershipModel membershipModel = null;
                        while (membershipModelResultSet.next()) {
                            membershipModel = new MembershipModel(
                                    membershipModelResultSet.getString("memberuid"),
                                    membershipModelResultSet.getString("title"), membershipModelResultSet.getString("name"),
                                    membershipModelResultSet.getString("surname"), membershipModelResultSet.getString("idnumber"),
                                    membershipModelResultSet.getString("address"), membershipModelResultSet.getString("cellnumber"),
                                    membershipModelResultSet.getString("email"), membershipModelResultSet.getString("occupation"),
                                    membershipModelResultSet.getString("nextofkin"), membershipModelResultSet.getString("nextofkincell"),
                                    membershipModelResultSet.getString("memberaccountnumber"), membershipModelResultSet.getString("contractnumber"),
                                    membershipModelResultSet.getString("mc"), membershipModelResultSet.getString("member1name"),
                                    membershipModelResultSet.getString("member1idnumber"), membershipModelResultSet.getString("member1accountnumber"),
                                    membershipModelResultSet.getString("member2name"), membershipModelResultSet.getString("member2idnumber"),
                                    membershipModelResultSet.getString("member2accountnumber"), membershipModelResultSet.getString("startdate"),
                                    membershipModelResultSet.getString("cardfee"), membershipModelResultSet.getString("joiningfee"),
                                    membershipModelResultSet.getString("totalreceived"), membershipModelResultSet.getString("upfrontpayment"),
                                    membershipModelResultSet.getString("bankname"), membershipModelResultSet.getString("bankaccountnumber"),
                                    membershipModelResultSet.getString("bankaccounttype"), membershipModelResultSet.getString("debitorderdate"),
                                    membershipModelResultSet.getString("payerdetails"),
                                    membershipModelResultSet.getString("payeridnumber"), membershipModelResultSet.getString("payercellnumber"),
                                    membershipModelResultSet.getString("payeremail"), membershipModelResultSet.getString("membershipdescription"),
                                    membershipModelResultSet.getString("minimumduration"), membershipModelResultSet.getString("profilepicture"),
                                    membershipModelResultSet.getString("membercontractdoc"), membershipModelResultSet.getString("gender"), membershipModelResultSet.getString("telnumber"),
                                    membershipModelResultSet.getString("paymentmethod"), membershipModelResultSet.getString("accountstatus"),
                                    membershipModelResultSet.getString("pic1"), membershipModelResultSet.getString("pic2"), membershipModelResultSet.getString("paymenttype")
                            );
                            globalMembershipModel = membershipModel;
                        }

                        //MembershipModel object obtained from DB


                        //Get SubscriptionModel from DB

                        Connection subscriptionModelConnection = new DatabaseConnection().getDatabaseLinkConnection();
                        String subscriptionModelQuery = "SELECT * FROM " + SUBSCRIPTIONS_TABLE + " WHERE idnumber = " + "'" + importedPaymentModel.getIdnum() + "'";
                        Statement subscriptionStatement = null;
                        SubscriptionModel subscriptionModel = null;

                        try {
                            subscriptionStatement = subscriptionModelConnection.createStatement();
                            ResultSet subscriptionResultSet = subscriptionStatement.executeQuery(subscriptionModelQuery);

                            while (subscriptionResultSet.next()) {
                                subscriptionModel = (new SubscriptionModel(
                                        subscriptionResultSet.getString("memberuid"), subscriptionResultSet.getString("accountname"),
                                        subscriptionResultSet.getString("cellnum"), subscriptionResultSet.getString("idnumber"),
                                        subscriptionResultSet.getString("idnumber1"), subscriptionResultSet.getString("idnumber2"),
                                        subscriptionResultSet.getString("accountnum"), subscriptionResultSet.getString("subaccount1"),
                                        subscriptionResultSet.getString("subaccount2"), subscriptionResultSet.getString("packagename"),
                                        subscriptionResultSet.getString("subscriptionfee"), subscriptionResultSet.getString("subscriptionfee1"),
                                        subscriptionResultSet.getString("subscriptionfee2"), subscriptionResultSet.getString("paymethod"),
                                        subscriptionResultSet.getString("dueday"), subscriptionResultSet.getString("accesscount"),
                                        subscriptionResultSet.getString("accesscount1"), subscriptionResultSet.getString("accesscount2"),
                                        subscriptionResultSet.getString("startdate"), subscriptionResultSet.getString("enddate"),
                                        subscriptionResultSet.getString("daysleft"), subscriptionResultSet.getString("debitorderday"),
                                        subscriptionResultSet.getString("nextduedate"), subscriptionResultSet.getString("accountbalance"),
                                        subscriptionResultSet.getString("adjustmentdate"), subscriptionResultSet.getString("accountstatus"),
                                        subscriptionResultSet.getString("profpic"), subscriptionResultSet.getString("profpic1"),
                                        subscriptionResultSet.getString("profpic2"), subscriptionResultSet.getString("monthsduration"),
                                        subscriptionResultSet.getString("monthselapsed"), subscriptionResultSet.getString("contractvalue"),
                                        subscriptionResultSet.getString("totalpaid"), subscriptionResultSet.getString("elapsedamount")
                                ));

                                globalSubscriptionModel = subscriptionModel;
                            }


                        } catch (SQLException throwables) {
                            System.out.println("Exception : 4");
                            Platform.runLater(() -> {
                                Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 4: " + throwables.getMessage(), ButtonType.OK);
                                dialog.show();
                            });
                            throwables.printStackTrace();

                        }


                        String date = importedPaymentModel.getDate();
                        DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");

                        String month = LocalDate.parse(date, dateTimeFormatter).getMonth().toString();
                        int year = LocalDate.parse(date, dateTimeFormatter).getYear();
                        //get individual subscription fees
                        double primaryFee = Double.parseDouble(globalSubscriptionModel.getSubscriptionfee());
                        double secondaryFee1 = Double.parseDouble(globalSubscriptionModel.getSubscriptionfee1());
                        double secondaryFee2 = Double.parseDouble(globalSubscriptionModel.getSubscriptionfee2());
                        double total = primaryFee + secondaryFee1 + secondaryFee2;


                        PaymentModelObject paymentModelObject = new PaymentModelObject("", date, month + " " + year,
                                importedPaymentModel.getPaymentamount(), Double.toString(total), globalSubscriptionModel.getAccountname(),
                                globalSubscriptionModel.getAccountnum(), globalMembershipModel.getBankaccountnumber(), importedPaymentModel.getIdnum(),
                                globalSubscriptionModel.getPackagename(), globalSubscriptionModel.getStartdate(),
                                globalSubscriptionModel.getEnddate(), globalSubscriptionModel.getMonthsduration(),
                                globalSubscriptionModel.getMonthselapsed(), globalSubscriptionModel.getElapsedamount(),
                                Double.toString(Double.parseDouble(globalSubscriptionModel.getTotalpaid()) + Double.parseDouble(importedPaymentModel.getPaymentamount())), globalSubscriptionModel.getContractvalue(),
                                globalSubscriptionModel.getAccountbalance(), Double.toString(Double.parseDouble(globalSubscriptionModel.getAccountbalance()) - Double.parseDouble(importedPaymentModel.getPaymentamount())),
                                "BULK IMPORT PAYMENT"

                        );


                        //Insert transaction into DB payments table
                        String paymentsQuery = "INSERT INTO " + PAYMENTS_TABLE + "(paymentdate,paymentmonthdate,paymentamount," +
                                "monthlypayablesubscriptionssum,accountname,accountnum,bankaccountnum,idnum,packagename," +
                                "startdate,enddate,monthsduration,monthselapsed,payableelapsed,amountpaidtodate,contractvalue," +
                                "accountbalancebefore,accountbalanceafter,description)" +
                                "VALUES(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?);";


                        //ResultSet resultSet1 = null;
                        System.out.println("Prepare to submit");

                        try {
                            //DatabaseConnection paymentsConnection = new DatabaseConnection();
                            Connection paymentsConnection = new DatabaseConnection().getDatabaseLinkConnection();
                            PreparedStatement paymentsPreparedStatement = null;
                            System.out.print("Submission started");
                            paymentsPreparedStatement = paymentsConnection.prepareStatement(paymentsQuery);
                            paymentsPreparedStatement.setString(1, paymentModelObject.getPaymentdate());
                            paymentsPreparedStatement.setString(2, paymentModelObject.getPaymentmonthdate());
                            paymentsPreparedStatement.setDouble(3, Double.parseDouble(paymentModelObject.getPaymentamount()));
                            paymentsPreparedStatement.setDouble(4, Double.parseDouble(paymentModelObject.getMonthlypayablesubscriptionssum()));
                            paymentsPreparedStatement.setString(5, paymentModelObject.getAccountname());
                            paymentsPreparedStatement.setString(6, paymentModelObject.getAccountnum());
                            paymentsPreparedStatement.setString(7, paymentModelObject.getBankaccountnum());
                            paymentsPreparedStatement.setString(8, paymentModelObject.getIdnum());
                            paymentsPreparedStatement.setString(9, paymentModelObject.getPackagename());
                            paymentsPreparedStatement.setString(10, paymentModelObject.getStartdate());
                            paymentsPreparedStatement.setString(11, paymentModelObject.getEnddate());
                            paymentsPreparedStatement.setInt(12, Integer.parseInt(paymentModelObject.getMonthsduration()));
                            paymentsPreparedStatement.setInt(13, Integer.parseInt(paymentModelObject.getMonthselapsed()));
                            paymentsPreparedStatement.setDouble(14, Double.parseDouble(paymentModelObject.getPayableelapsed()));
                            paymentsPreparedStatement.setDouble(15, Double.parseDouble(paymentModelObject.getAmountpaidtodate()));
                            paymentsPreparedStatement.setDouble(16, Double.parseDouble(paymentModelObject.getContractvalue()));
                            paymentsPreparedStatement.setDouble(17, Double.parseDouble(paymentModelObject.getAccountbalancebefore()));
                            paymentsPreparedStatement.setDouble(18, Double.parseDouble(paymentModelObject.getAccountbalanceafter()));
                            paymentsPreparedStatement.setString(19, paymentModelObject.getDescription());
                            paymentsPreparedStatement.execute();

                            System.out.println("Submission completed");
                            paymentModelObjects.add(paymentModelObject);

                        } catch (SQLException e) {
                            System.out.println("Exception : 5");
                            Platform.runLater(() -> {
                                Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 5: " + e.getMessage(), ButtonType.OK);
                                dialog.show();
                            });
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }

                   /* try {
                       // connection.close();
                        //preparedStatement1.close();

                    } catch (SQLException e) {
                        e.printStackTrace();
                        throw new RuntimeException(e);
                    }*/
                        System.out.println("End submit");

                        //Update Subscriptions_tb with new total paid (and new account balance)? if the subscriptions service doe not auto calc balance using
                        String newTotalPaidSoFar = Double.toString(Double.parseDouble(globalSubscriptionModel.getTotalpaid()) + Double.parseDouble(importedPaymentModel.getPaymentamount()));
                        String subscriptionsQuery2 = "UPDATE " + SUBSCRIPTIONS_TABLE + " SET totalpaid = " + "'" + newTotalPaidSoFar + "'" + "WHERE idnumber = " + importedPaymentModel.getIdnum() + ";";
                        Connection subscriptionsConnection2 = new DatabaseConnection().getDatabaseLinkConnection();
                        PreparedStatement subscriptionsPreparedStatement2 = null;
                        System.out.print("Update started");
                        try {
                            subscriptionsPreparedStatement2 = subscriptionsConnection2.prepareStatement(subscriptionsQuery2);
                            subscriptionsPreparedStatement2.execute();
                            System.out.print("Update ended");
                        } catch (SQLException e) {
                            System.out.println("Exception : 6");
                            Platform.runLater(() -> {
                                Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 6: " + e.getMessage(), ButtonType.OK);
                                dialog.show();
                            });
                            e.printStackTrace();
                            throw new RuntimeException(e);
                        }


                    } catch (SQLException throwables) {
                        System.out.println("Exception : 7");
                        Platform.runLater(() -> {
                            Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 7: " + throwables.getMessage(), ButtonType.OK);
                            dialog.show();
                        });
                        throwables.printStackTrace();
                        throw new RuntimeException(throwables);
                    }

                    /*  try {
                     *//* connection.close();
                    preparedStatement.close();*//*
                    //resultSet.close();
                    System.out.println("DONE 1 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }*/
                    System.out.println("DONE 1 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

                }
            } catch (SQLException e) {
                System.out.println("Exception : 8");
                Platform.runLater(() -> {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 8 : " + e.getMessage(), ButtonType.OK);
                    dialog.show();
                });
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("Exception : 9");
                Platform.runLater(() -> {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 9: " + e.getMessage(), ButtonType.OK);
                    dialog.show();
                });
            }
            System.out.println("DONE 2 !!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");

            //Delete imported payments from table when done
            Connection deleteImportedPaymentsConnection = new DatabaseConnection().getDatabaseLinkConnection();
            String deleteImportedPaymentsQuery = "DELETE FROM " + IMPORTED_PAYMENTS_TABLE + ";";
            try {
                PreparedStatement deleteImportedPaymentsPreparedStatement = deleteImportedPaymentsConnection.prepareStatement(deleteImportedPaymentsQuery);
                deleteImportedPaymentsPreparedStatement.execute();
                System.out.println("IMPORTED PAYMENTS TABLE DELETED");
            } catch (SQLException e) {
                System.out.println("Exception : 10");
                Platform.runLater(() -> {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 10: " + e.getMessage(), ButtonType.OK);
                    dialog.show();
                });
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (Exception e) {
                System.out.println("Exception : 11");
                Platform.runLater(() -> {
                    Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 11: " + e.getMessage(), ButtonType.OK);
                    dialog.show();
                });
            }
            //return observableList;

        }catch (Exception e){
            System.out.println("Exception : 12");
            Platform.runLater(() -> {
                Alert dialog = new Alert(Alert.AlertType.ERROR, "Error 12: " + e.getMessage(), ButtonType.OK);
                dialog.show();
            });

        }
            return paymentModelObjects;

    }


}
