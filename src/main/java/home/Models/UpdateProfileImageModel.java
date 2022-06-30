package home.Models;

public class UpdateProfileImageModel {
    private String accountnumber,pathtopic;
    private int memberidentifier;

    public UpdateProfileImageModel(String accountnumber, String pathtopic, int memberidentifier) {
        this.accountnumber = accountnumber;
        this.pathtopic = pathtopic;
        this.memberidentifier = memberidentifier;
    }

    public String getAccountnumber() {
        return accountnumber;
    }

    public void setAccountnumber(String accountnumber) {
        this.accountnumber = accountnumber;
    }

    public String getPathtopic() {
        return pathtopic;
    }

    public void setPathtopic(String pathtopic) {
        this.pathtopic = pathtopic;
    }

    public int getMemberidentifier() {
        return memberidentifier;
    }

    public void setMemberidentifier(int memberidentifier) {
        this.memberidentifier = memberidentifier;
    }
}
