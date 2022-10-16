package home.Services;

import home.BackgroundTasks.UploadPaymentsTask;
import home.Models.PaymentModelObject;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;
import javafx.concurrent.Service;
import javafx.concurrent.Task;

public class UploadPaymentsService extends Service<ObservableList<PaymentModelObject>> {

    private final StringProperty path = new SimpleStringProperty(this, "clusterId");
    private  int lastRowNum ;

    public final void setPath(String path) {
        this.path.set(path);
    }

    public final String getPath() {
        return path.get();
    }

    public final void setLastRowNum(int lastRowNum) {
        this.lastRowNum = lastRowNum;
    }

    public final int getLastRowNum() {
        return lastRowNum;
    }



    public UploadPaymentsService() {
    }


    public UploadPaymentsService(String path) {
        setPath(path);
    }

    @Override
    protected Task<ObservableList<PaymentModelObject>> createTask() {
        final String path = getPath(); // cache configuration
        final int lastNum = getLastRowNum();
        return new UploadPaymentsTask(path,lastNum);
    }
}
