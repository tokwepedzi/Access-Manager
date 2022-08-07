package home.Models;

public class PaymentsModel {
    private String idnumber,paymentdate,paymentamount,accountnumber,contractstartdate,contractenddate,monthsduration,
            contractvalue,monthselapsed,elapsedamount,description;

    public PaymentsModel(String idnumber, String paymentdate, String paymentamount, String accountnumber,
                         String contractstartdate, String contractenddate, String monthsduration, String contractvalue,
                         String monthselapsed, String elapsedamount,String description) {
        this.idnumber = idnumber;
        this.paymentdate = paymentdate;
        this.paymentamount = paymentamount;
        this.accountnumber = accountnumber;
        this.contractstartdate = contractstartdate;
        this.contractenddate = contractenddate;
        this.monthsduration = monthsduration;
        this.contractvalue = contractvalue;
        this.monthselapsed = monthselapsed;
        this.elapsedamount = elapsedamount;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getPaymentamount() {
        return paymentamount;
    }

    public void setPaymentamount(String paymentamount) {
        this.paymentamount = paymentamount;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getContractstartdate() {
        return contractstartdate;
    }

    public void setContractstartdate(String contractstartdate) {
        this.contractstartdate = contractstartdate;
    }

    public String getContractenddate() {
        return contractenddate;
    }

    public void setContractenddate(String contractenddate) {
        this.contractenddate = contractenddate;
    }

    public String getMonthsduration() {
        return monthsduration;
    }

    public void setMonthsduration(String monthsduration) {
        this.monthsduration = monthsduration;
    }

    public String getContractvalue() {
        return contractvalue;
    }

    public void setContractvalue(String contractvalue) {
        this.contractvalue = contractvalue;
    }

    public String getMonthselapsed() {
        return monthselapsed;
    }

    public void setMonthselapsed(String monthselapsed) {
        this.monthselapsed = monthselapsed;
    }

    public String getElapsedamount() {
        return elapsedamount;
    }

    public void setElapsedamount(String elapsedamount) {
        this.elapsedamount = elapsedamount;
    }
}
