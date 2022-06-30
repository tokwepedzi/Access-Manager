package home.Models;

public class KeySubscriberDetailer {
    private String accountnumber, mainname, mainsurname, member1name,member2name,profpic,profpic1,profpic2;

    public KeySubscriberDetailer(String accountnumber, String mainname, String mainsurname,
                                 String member1name, String member2name,String profpic,String profpic1,String profpic2) {
        this.accountnumber = accountnumber;
        this.mainname = mainname;
        this.mainsurname = mainsurname;
        this.member1name = member1name;
        this.member2name = member2name;
        this.profpic = profpic;
        this.profpic1 = profpic1;
        this.profpic2 = profpic2;
    }

    public String getProfpic() {
        return profpic;
    }

    public void setProfpic(String profpic) {
        this.profpic = profpic;
    }

    public String getProfpic1() {
        return profpic1;
    }

    public void setProfpic1(String profpic1) {
        this.profpic1 = profpic1;
    }

    public String getProfpic2() {
        return profpic2;
    }

    public void setProfpic2(String profpic2) {
        this.profpic2 = profpic2;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getMainname() {
        return mainname;
    }

    public void setMainname(String mainname) {
        this.mainname = mainname;
    }

    public String getMainsurname() {
        return mainsurname;
    }

    public void setMainsurname(String mainsurname) {
        this.mainsurname = mainsurname;
    }

    public String getMember1name() {
        return member1name;
    }

    public void setMember1name(String member1name) {
        this.member1name = member1name;
    }

    public String getMember2name() {
        return member2name;
    }

    public void setMember2name(String member2name) {
        this.member2name = member2name;
    }
}
