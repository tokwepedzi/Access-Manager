package home.Models;

public class OverrideReasonModel {
    private String reasonid,overridereason;

    public OverrideReasonModel(String reasonid, String overridereason) {
        this.reasonid = reasonid;
        this.overridereason = overridereason;
    }

    public String getReasonid() {
        return reasonid;
    }

    public void setReasonid(String reasonid) {
        this.reasonid = reasonid;
    }

    public String getOverridereason() {
        return overridereason;
    }

    public void setOverridereason(String overridereason) {
        this.overridereason = overridereason;
    }
}
