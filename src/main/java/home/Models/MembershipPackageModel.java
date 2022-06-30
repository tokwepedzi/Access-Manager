package home.Models;

public class MembershipPackageModel {

    private String packageid,packagename, packagefee,packagefee1,packagefee2;

    public MembershipPackageModel() {
    }

    public MembershipPackageModel(String packageid, String packagename, String packagefee, String packagefee1,
                                  String packagefee2) {
        this.packageid = packageid;
        this.packagename = packagename;
        this.packagefee = packagefee;
        this.packagefee1 = packagefee1;
        this.packagefee2 = packagefee2;
    }

    public String getPackageid() {
        return packageid;
    }

    public void setPackageid(String packageid) {
        this.packageid = packageid;
    }

    public String getPackagename() {
        return packagename;
    }

    public void setPackagename(String packagename) {
        this.packagename = packagename;
    }

    public String getPackagefee() {
        return packagefee;
    }

    public void setPackagefee(String packagefee) {
        this.packagefee = packagefee;
    }

    public String getPackagefee1() {
        return packagefee1;
    }

    public void setPackagefee1(String packagefee1) {
        this.packagefee1 = packagefee1;
    }

    public String getPackagefee2() {
        return packagefee2;
    }

    public void setPackagefee2(String packagefee2) {
        this.packagefee2 = packagefee2;
    }
}
