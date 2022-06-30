package home.Models;

public class MemberSearchModel {
    private String title,name,surname,memberaccountnumber,cellnumber,idnumber,memberuid,accountstatus;

    public MemberSearchModel(String title, String name, String surname, String memberaccountnumber,
                             String cellnumber, String idnumber,String memberuid, String accountstatus) {
        this.title = title;
        this.name = name;
        this.surname = surname;
        this.memberaccountnumber = memberaccountnumber;
        this.cellnumber = cellnumber;
        this.idnumber = idnumber;
        this.memberuid = memberuid;
        this.accountstatus = accountstatus;
    }

    public String getAccountstatus() {
        return accountstatus;
    }

    public void setAccountstatus(String accountstatus) {
        this.accountstatus = accountstatus;
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

    public String getMemberaccountnumber() {
        return memberaccountnumber;
    }

    public void setMemberaccountnumber(String memberaccountnumber) {
        this.memberaccountnumber = memberaccountnumber;
    }

    public String getCellnumber() {
        return cellnumber;
    }

    public void setCellnumber(String cellnumber) {
        this.cellnumber = cellnumber;
    }

    public String getIdnumber() {
        return idnumber;
    }

    public void setIdnumber(String idnumber) {
        this.idnumber = idnumber;
    }
}
