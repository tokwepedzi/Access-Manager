package home.Models;

public class ImportedPaymentModel extends PaymentModelObject {
    private String date, idnum, paymentamount;

    public ImportedPaymentModel(String date, String idnum, String paymentamount) {
        this.date = date;
        this.idnum = idnum;
        this.paymentamount = paymentamount;
    }

    public ImportedPaymentModel() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getIdnum() {
        return idnum;
    }

    public void setIdnum(String idnum) {
        this.idnum = idnum;
    }

    public String getPaymentamount() {
        return paymentamount;
    }

    public void setPaymentamount(String paymentamount) {
        this.paymentamount = paymentamount;
    }
}
