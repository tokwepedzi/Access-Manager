package home.Models;

public class PaymentModelObject {
    private String transactionid, paymentdate, paymentmothdate, paymentamount, monthlypayablesubscriptionssum,
            accountname, accountnum, bankaccountnum, idnum, packagename, startdate, enddate, monthsduration, monthselapsed,
            payableelapsed, amountpaidtodate, contractvalue, accountbalancebefore,accountbalanceafter, description;

    public PaymentModelObject() {
    }

    public PaymentModelObject(String transactionid, String paymentdate, String paymentmothdate, String paymentamount,
                              String monthlypayablesubscriptionssum, String accountname, String accountnum,
                              String bankaccountnum, String idnum, String packagename, String startdate,
                              String enddate, String monthsduration, String monthselapsed, String payableelapsed,
                              String amountpaidtodate, String contractvalue, String accountbalancebefore,
                              String accountbalanceafter, String description) {
        this.transactionid = transactionid;
        this.paymentdate = paymentdate;
        this.paymentmothdate = paymentmothdate;
        this.paymentamount = paymentamount;
        this.monthlypayablesubscriptionssum = monthlypayablesubscriptionssum;
        this.accountname = accountname;
        this.accountnum = accountnum;
        this.bankaccountnum = bankaccountnum;
        this.idnum = idnum;
        this.packagename = packagename;
        this.startdate = startdate;
        this.enddate = enddate;
        this.monthsduration = monthsduration;
        this.monthselapsed = monthselapsed;
        this.payableelapsed = payableelapsed;
        this.amountpaidtodate = amountpaidtodate;
        this.contractvalue = contractvalue;
        this.accountbalancebefore = accountbalancebefore;
        this.accountbalanceafter = accountbalanceafter;
        this.description = description;
    }

    public String getTransactionid() {
        return transactionid;
    }

    public void setTransactionid(String transactionid) {
        this.transactionid = transactionid;
    }

    public String getPaymentdate() {
        return paymentdate;
    }

    public void setPaymentdate(String paymentdate) {
        this.paymentdate = paymentdate;
    }

    public String getPaymentmothdate() {
        return paymentmothdate;
    }

    public void setPaymentmothdate(String paymentmothdate) {
        this.paymentmothdate = paymentmothdate;
    }

    public String getPaymentamount() {
        return paymentamount;
    }

    public void setPaymentamount(String paymentamount) {
        this.paymentamount = paymentamount;
    }

    public String getMonthlypayablesubscriptionssum() {
        return monthlypayablesubscriptionssum;
    }

    public void setMonthlypayablesubscriptionssum(String monthlypayablesubscriptionssum) {
        this.monthlypayablesubscriptionssum = monthlypayablesubscriptionssum;
    }

    public String getAccountname() {
        return accountname;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public String getBankaccountnum() {
        return bankaccountnum;
    }

    public void setBankaccountnum(String bankaccountnum) {
        this.bankaccountnum = bankaccountnum;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getEnddate() {
        return enddate;
    }

    public void setEnddate(String enddate) {
        this.enddate = enddate;
    }

    public String getMonthsduration() {
        return monthsduration;
    }

    public void setMonthsduration(String monthsduration) {
        this.monthsduration = monthsduration;
    }

    public String getMonthselapsed() {
        return monthselapsed;
    }

    public void setMonthselapsed(String monthselapsed) {
        this.monthselapsed = monthselapsed;
    }

    public String getPayableelapsed() {
        return payableelapsed;
    }

    public void setPayableelapsed(String payableelapsed) {
        this.payableelapsed = payableelapsed;
    }

    public String getAmountpaidtodate() {
        return amountpaidtodate;
    }

    public void setAmountpaidtodate(String amountpaidtodate) {
        this.amountpaidtodate = amountpaidtodate;
    }

    public String getContractvalue() {
        return contractvalue;
    }

    public void setContractvalue(String contractvalue) {
        this.contractvalue = contractvalue;
    }

    public String getAccountbalancebefore() {
        return accountbalancebefore;
    }

    public void setAccountbalancebefore(String accountbalancebefore) {
        this.accountbalancebefore = accountbalancebefore;
    }
    public String getAccountbalanceafter() {
        return accountbalanceafter;
    }

    public void setAccountbalanceafter(String accountbalanceafter) {
        this.accountbalanceafter = accountbalanceafter;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
