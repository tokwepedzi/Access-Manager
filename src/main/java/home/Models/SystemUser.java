package home.Models;

public class SystemUser {
    private String fullname, cellnumber, idnumber, dob, address, gender, email, authlevel, password,uid,profilepicture;


    public SystemUser() {

    }

    public SystemUser(String uid, String fullname, String cellnumber, String idnumber, String dob, String address,
                      String gender, String email, String authlevel, String password, String profilepicture) {
        this.fullname = fullname;
        this.cellnumber = cellnumber;
        this.idnumber = idnumber;
        this.dob = dob;
        this.address = address;
        this.gender = gender;
        this.email = email;
        this.authlevel = authlevel;
        this.password = password;
        this.uid = uid;
        this.profilepicture = profilepicture;
    }

    public String getProfilepicture() {
        return profilepicture;
    }

    public void setProfilepicture(String profilepicture) {
        this.profilepicture = profilepicture;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getUid() {
        return uid;
    }

    public void setUid(String uid) {
        this.uid = uid;
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

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAuthlevel() {
        return authlevel;
    }

    public void setAuthlevel(String authlevel) {
        this.authlevel = authlevel;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
