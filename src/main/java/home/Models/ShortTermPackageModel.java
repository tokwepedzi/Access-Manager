package home.Models;

public class ShortTermPackageModel {
    private String packageid,packagename, packagefee,daysduration;

    public ShortTermPackageModel(String packageid, String packagename, String packagefee, String daysduration) {
        this.packageid = packageid;
        this.packagename = packagename;
        this.packagefee = packagefee;
        this.daysduration = daysduration;
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

    public String getDaysduration() {
        return daysduration;
    }

    public void setDaysduration(String daysduration) {
        this.daysduration = daysduration;
    }


}
