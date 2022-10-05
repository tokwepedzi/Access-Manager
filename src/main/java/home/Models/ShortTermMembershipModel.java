package home.Models;

public class WeeklyMemberModel {
    private String fullname,idnum,cellnum,startdate,enddate;

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getCellnum() {
        return cellnum;
    }

    public void setCellnum(String cellnum) {
        this.cellnum = cellnum;
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

    public WeeklyMemberModel(String fullname, String idnum, String cellnum, String startdate, String enddate) {
        this.fullname = fullname;
        this.idnum = idnum;
        this.cellnum = cellnum;
        this.startdate = startdate;
        this.enddate = enddate;
    }
}
