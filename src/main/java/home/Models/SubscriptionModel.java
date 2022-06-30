package home.Models;

public class SubscriptionModel {
    private String memberuid,accountname, cellnum, idnumber, idnumber1, idnumber2, accountnum, subaccount1, subaccount2, packagename,
            subscriptionfee, subscriptionfee1, subscriptionfee2, paymethod, dueday, accesscount, accesscount1, accesscount2,
            startdate, enddate, daysleft, debitorderday, nextduedate, accountbalance,adjustmentdate,accountstatus,profilepic,profilepic1,profilepic2;

    public SubscriptionModel(String memberuid, String accountname, String cellnum, String idnumber, String idnumber1, String idnumber2,
                             String accountnum, String subaccount1, String subaccount2, String packagename,
                             String subscriptionfee, String subscriptionfee1, String subscriptionfee2,
                             String paymethod, String dueday, String accesscount, String accesscount1,
                             String accesscount2, String startdate, String enddate, String daysleft,
                             String debitorderday, String nextduedate, String accountbalance, String adjustmentdate,
                             String accountstatus,String profilepic, String profilepic1, String profilepic2) {
        this.memberuid = memberuid;
        this.accountname = accountname;
        this.cellnum = cellnum;
        this.idnumber = idnumber;
        this.idnumber1 = idnumber1;
        this.idnumber2 = idnumber2;
        this.accountnum = accountnum;
        this.subaccount1 = subaccount1;
        this.subaccount2 = subaccount2;
        this.packagename = packagename;
        this.subscriptionfee = subscriptionfee;
        this.subscriptionfee1 = subscriptionfee1;
        this.subscriptionfee2 = subscriptionfee2;
        this.paymethod = paymethod;
        this.dueday = dueday;
        this.accesscount = accesscount;
        this.accesscount1 = accesscount1;
        this.accesscount2 = accesscount2;
        this.startdate = startdate;
        this.enddate = enddate;
        this.daysleft = daysleft;
        this.debitorderday = debitorderday;
        this.nextduedate = nextduedate;
        this.accountbalance = accountbalance;
        this.adjustmentdate = adjustmentdate;
        this.accountstatus = accountstatus;
        this.profilepic = profilepic;
        this.profilepic1 = profilepic1;
        this.profilepic2 = profilepic2;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
    }

    public String getAdjustmentdate() {
        return adjustmentdate;
    }

    public void setAdjustmentdate(String adjustmentdate) {
        this.adjustmentdate = adjustmentdate;
    }

    public String getAccountname() {
        return accountname;
    }

    public String getMemberuid() {
        return memberuid;
    }

    public void setMemberuid(String memberuid) {
        this.memberuid = memberuid;
    }

    public void setAccountname(String accountname) {
        this.accountname = accountname;
    }

    public String getCellnum() {
        return cellnum;
    }

    public void setCellnum(String cellnum) {
        this.cellnum = cellnum;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getIdnumber1() {
        return idnumber1;
    }

    public void setIdnumber1(String idnumber1) {
        this.idnumber1 = idnumber1;
    }

    public String getIdnumber2() {
        return idnumber2;
    }

    public void setIdnumber2(String idnumber2) {
        this.idnumber2 = idnumber2;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public String getSubaccount1() {
        return subaccount1;
    }

    public void setSubaccount1(String subaccount1) {
        this.subaccount1 = subaccount1;
    }

    public String getSubaccount2() {
        return subaccount2;
    }

    public void setSubaccount2(String subaccount2) {
        this.subaccount2 = subaccount2;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getSubscriptionfee() {
        return subscriptionfee;
    }

    public void setSubscriptionfee(String subscriptionfee) {
        this.subscriptionfee = subscriptionfee;
    }

    public String getSubscriptionfee1() {
        return subscriptionfee1;
    }

    public void setSubscriptionfee1(String subscriptionfee1) {
        this.subscriptionfee1 = subscriptionfee1;
    }

    public String getSubscriptionfee2() {
        return subscriptionfee2;
    }

    public void setSubscriptionfee2(String subscriptionfee2) {
        this.subscriptionfee2 = subscriptionfee2;
    }

    public String getPaymethod() {
        return paymethod;
    }

    public void setPaymethod(String paymethod) {
        this.paymethod = paymethod;
    }

    public String getDueday() {
        return dueday;
    }

    public void setDueday(String dueday) {
        this.dueday = dueday;
    }

    public String getAccesscount() {
        return accesscount;
    }

    public void setAccesscount(String accesscount) {
        this.accesscount = accesscount;
    }

    public String getAccesscount1() {
        return accesscount1;
    }

    public void setAccesscount1(String accesscount1) {
        this.accesscount1 = accesscount1;
    }

    public String getAccesscount2() {
        return accesscount2;
    }

    public void setAccesscount2(String accesscount2) {
        this.accesscount2 = accesscount2;
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

    public String getDaysleft() {
        return daysleft;
    }

    public void setDaysleft(String daysleft) {
        this.daysleft = daysleft;
    }

    public String getDebitorderday() {
        return debitorderday;
    }

    public void setDebitorderday(String debitorderday) {
        this.debitorderday = debitorderday;
    }

    public String getNextduedate() {
        return nextduedate;
    }

    public void setNextduedate(String nextduedate) {
        this.nextduedate = nextduedate;
    }

    public String getAccountbalance() {
        return accountbalance;
    }

    public String getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(String profilepic) {
        this.profilepic = profilepic;
    }

    public String getProfilepic1() {
        return profilepic1;
    }

    public void setProfilepic1(String profilepic1) {
        this.profilepic1 = profilepic1;
    }

    public String getProfilepic2() {
        return profilepic2;
    }

    public void setProfilepic2(String profilepic2) {
        this.profilepic2 = profilepic2;
    }

    public void setAccountbalance(String accountbalance) {
        this.accountbalance = accountbalance;
    }
}
