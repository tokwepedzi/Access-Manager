package home.Models;

public class MembershipModel {
    private String memberuid,title, name, surname, idnumber, address, cellnumber, email, occupation, nextofkin, nextofkincell,
            memberaccountnumber, contractnumber, mc, member1name, member1idnumber, member1accountnumber, member2name,
            member2idnumber, member2accountnumber, startdate, cardfee, joiningfee, totalreceived, upfrontpayment, bankname
            , bankaccountnumber, bankaccounttype, debitorderdate, payerdetails, payeridnumber,
            payercellnumber, payeremail, membershipdescription, minimumduration, profilepicture,membercontractdoc,gender
            ,telnumber,paymentmethod, accountstatus,pic1,pic2,paymentype;


    public MembershipModel(String memberuid, String title, String name, String surname, String idnumber, String address,
                           String cellnumber, String email, String occupation, String nextofkin,
                           String nextofkincell, String memberaccountnumber, String contractnumber,
                           String mc, String member1name, String member1idnumber, String member1accountnumber,
                           String member2name, String member2idnumber, String member2accountnumber,
                           String startdate, String cardfee, String joiningfee, String totalreceived,
                           String upfrontpayment, String bankname, String bankaccountnumber,
                           String bankaccounttype, String debitorderdate, String payerdetails,
                           String payeridnumber, String payercellnumber, String payeremail,
                           String membershipdescription, String minimumduration, String profilepicture, String membercontractdoc,
                           String gender, String telnumber, String paymentmethod, String accountstatus, String pic1, String pic2,
                           String paymentype) {
        this.memberuid = memberuid;
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.idnumber = idnumber;
        this.address = address;
        this.cellnumber = cellnumber;
        this.email = email;
        this.occupation = occupation;
        this.nextofkin = nextofkin;
        this.nextofkincell = nextofkincell;
        this.memberaccountnumber = memberaccountnumber;
        this.contractnumber = contractnumber;
        this.mc = mc;
        this.member1name = member1name;
        this.member1idnumber = member1idnumber;
        this.member1accountnumber = member1accountnumber;
        this.member2name = member2name;
        this.member2idnumber = member2idnumber;
        this.member2accountnumber = member2accountnumber;
        this.startdate = startdate;
        this.cardfee = cardfee;
        this.joiningfee = joiningfee;
        this.totalreceived = totalreceived;
        this.upfrontpayment = upfrontpayment;
        this.bankname = bankname;
        this.bankaccountnumber = bankaccountnumber;
        this.bankaccounttype = bankaccounttype;
        this.debitorderdate = debitorderdate;
        this.payerdetails = payerdetails;
        this.payeridnumber = payeridnumber;
        this.payercellnumber = payercellnumber;
        this.payeremail = payeremail;
        this.membershipdescription = membershipdescription;
        this.minimumduration = minimumduration;
        this.profilepicture = profilepicture;
        this.membercontractdoc = membercontractdoc;
        this.gender = gender;
        this.telnumber = telnumber;
        this.paymentmethod = paymentmethod;
        this.accountstatus = accountstatus;
        this.pic1 = pic1;
        this.pic2 = pic2;
        this.paymentype = paymentype;
    }

    public String getPaymentype() {
        return paymentype;
    }

    public void setPaymentype(String paymentype) {
        this.paymentype = paymentype;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
    }

    public String getMembercontractdoc() {
        return membercontractdoc;
    }

    public void setMembercontractdoc(String membercontractdoc) {
        this.membercontractdoc = membercontractdoc;
    }

    public String getPaymentmethod() {
        return paymentmethod;
    }

    public void setPaymentmethod(String paymentmethod) {
        this.paymentmethod = paymentmethod;
    }

    public String getTelnumber() {
        return telnumber;
    }

    public void setTelnumber(String telnumber) {
        this.telnumber = telnumber;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getMemberuid() {
        return memberuid;
    }

    public void setMemberuid(String memberuid) {
        this.memberuid = memberuid;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCellnumber() {
        return cellnumber;
    }

    public void setCellnumber(String cellnumber) {
        this.cellnumber = cellnumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getOccupation() {
        return occupation;
    }

    public void setOccupation(String occupation) {
        this.occupation = occupation;
    }

    public String getNextofkin() {
        return nextofkin;
    }

    public void setNextofkin(String nextofkin) {
        this.nextofkin = nextofkin;
    }

    public String getNextofkincell() {
        return nextofkincell;
    }

    public void setNextofkincell(String nextofkincell) {
        this.nextofkincell = nextofkincell;
    }

    public String getMemberaccountnumber() {
        return memberaccountnumber;
    }

    public void setMemberaccountnumber(String memberaccountnumber) {
        this.memberaccountnumber = memberaccountnumber;
    }

    public String getContractnumber() {
        return contractnumber;
    }

    public void setContractnumber(String contractnumber) {
        this.contractnumber = contractnumber;
    }

    public String getMc() {
        return mc;
    }

    public void setMc(String mc) {
        this.mc = mc;
    }

    public String getMember1name() {
        return member1name;
    }

    public void setMember1name(String member1name) {
        this.member1name = member1name;
    }

    public String getMember1idnumber() {
        return member1idnumber;
    }

    public void setMember1idnumber(String member1idnumber) {
        this.member1idnumber = member1idnumber;
    }

    public String getMember1accountnumber() {
        return member1accountnumber;
    }

    public void setMember1accountnumber(String member1accountnumber) {
        this.member1accountnumber = member1accountnumber;
    }

    public String getMember2name() {
        return member2name;
    }

    public void setMember2name(String member2name) {
        this.member2name = member2name;
    }

    public String getMember2idnumber() {
        return member2idnumber;
    }

    public void setMember2idnumber(String member2idnumber) {
        this.member2idnumber = member2idnumber;
    }

    public String getMember2accountnumber() {
        return member2accountnumber;
    }

    public void setMember2accountnumber(String member2accountnumber) {
        this.member2accountnumber = member2accountnumber;
    }

    public String getStartdate() {
        return startdate;
    }

    public void setStartdate(String startdate) {
        this.startdate = startdate;
    }

    public String getCardfee() {
        return cardfee;
    }

    public void setCardfee(String cardfee) {
        this.cardfee = cardfee;
    }

    public String getJoiningfee() {
        return joiningfee;
    }

    public void setJoiningfee(String joiningfee) {
        this.joiningfee = joiningfee;
    }

    public String getTotalreceived() {
        return totalreceived;
    }

    public void setTotalreceived(String totalreceived) {
        this.totalreceived = totalreceived;
    }

    public String getUpfrontpayment() {
        return upfrontpayment;
    }

    public void setUpfrontpayment(String upfrontpayment) {
        this.upfrontpayment = upfrontpayment;
    }

    public String getBankname() {
        return bankname;
    }

    public void setBankname(String bankname) {
        this.bankname = bankname;
    }


    public String getBankaccountnumber() {
        return bankaccountnumber;
    }

    public void setBankaccountnumber(String bankaccountnumber) {
        this.bankaccountnumber = bankaccountnumber;
    }

    public String getBankaccounttype() {
        return bankaccounttype;
    }

    public void setBankaccounttype(String bankaccounttype) {
        this.bankaccounttype = bankaccounttype;
    }

    public String getDebitorderdate() {
        return debitorderdate;
    }

    public void setDebitorderdate(String debitorderdate) {
        this.debitorderdate = debitorderdate;
    }

    public String getPayerdetails() {
        return payerdetails;
    }

    public void setPayerdetails(String payerdetails) {
        this.payerdetails = payerdetails;
    }

    public String getPayeridnumber() {
        return payeridnumber;
    }

    public void setPayeridnumber(String payeridnumber) {
        this.payeridnumber = payeridnumber;
    }

    public String getPayercellnumber() {
        return payercellnumber;
    }

    public void setPayercellnumber(String payercellnumber) {
        this.payercellnumber = payercellnumber;
    }

    public String getPayeremail() {
        return payeremail;
    }

    public void setPayeremail(String payeremail) {
        this.payeremail = payeremail;
    }

    public String getMembershipdescription() {
        return membershipdescription;
    }

    public void setMembershipdescription(String membershipdescription) {
        this.membershipdescription = membershipdescription;
    }

    public String getMinimumduration() {
        return minimumduration;
    }

    public void setMinimumduration(String minimumduration) {
        this.minimumduration = minimumduration;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }
}
