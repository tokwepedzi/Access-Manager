package home.Models;

public class SecurityClearanceEventModel {
    private String event, eventowner,accountnum,timestamp,date,attributes,comments;

    public SecurityClearanceEventModel(String event, String eventowner, String accountnum, String timestamp,String date,
                                       String attributes, String comments) {
        this.event = event;
        this.eventowner = eventowner;
        this.accountnum = accountnum;
        this.timestamp = timestamp;
        this.date = date;
        this.attributes = attributes;
        this.comments = comments;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEvent() {
        return event;
    }

    public void setEvent(String event) {
        this.event = event;
    }

    public String getEventowner() {
        return eventowner;
    }

    public void setEventowner(String eventowner) {
        this.eventowner = eventowner;
    }

    public String getAccountnum() {
        return accountnum;
    }

    public void setAccountnum(String accountnum) {
        this.accountnum = accountnum;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    public String getAttributes() {
        return attributes;
    }

    public void setAttributes(String attributes) {
        this.attributes = attributes;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }
}
