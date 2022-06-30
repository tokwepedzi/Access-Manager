package home.Models;

public class DashboardInfoModel {
    private int currentmembers;
    private int expiringmembers;
    private int visitstoday;
    private int overridestoday;

    public DashboardInfoModel(int currentmembers, int expiringmembers, int visitstoday, int overridestoday) {
        this.currentmembers = currentmembers;
        this.expiringmembers = expiringmembers;
        this.visitstoday = visitstoday;
        this.overridestoday = overridestoday;
    }

    public int getCurrentmembers() {
        return currentmembers;
    }

    public void setCurrentmembers(int currentmembers) {
        this.currentmembers = currentmembers;
    }

    public int getExpiringmembers() {
        return expiringmembers;
    }

    public void setExpiringmembers(int expiringmembers) {
        this.expiringmembers = expiringmembers;
    }

    public int getVisitstoday() {
        return visitstoday;
    }

    public void setVisitstoday(int visitstoday) {
        this.visitstoday = visitstoday;
    }

    public int getOverridestoday() {
        return overridestoday;
    }

    public void setOverridestoday(int overridestoday) {
        this.overridestoday = overridestoday;
    }
}
