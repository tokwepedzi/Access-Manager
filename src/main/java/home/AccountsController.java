package home;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.ProgressBar;
import javafx.scene.image.ImageView;
import javafx.stage.FileChooser;

import javax.swing.filechooser.FileNameExtensionFilter;
import java.io.File;
import java.io.FilenameFilter;
import java.net.URL;
import java.util.ResourceBundle;

public class AccountsController implements Initializable {
    @FXML
    private Button mImportBtn;
    @FXML
    private ProgressBar mImportProgressBar;
    @FXML
    private Label mImportNotes;
    @FXML
    private ImageView mExcelIcon;


    private File selectedFile;

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        mExcelIcon.setVisible(false);

    }

    public void importPaymentsfromExcel(ActionEvent actionEvent){
        System.out.println("Import button Clicked!");
        try{
            FileChooser fileChooser = new FileChooser();
            selectedFile = fileChooser.showOpenDialog(null);

            //get file URL
            URL url = selectedFile.toURI().toURL();
            System.out.println("FILE URL: "+url+" NAME:"+selectedFile.getName());
            mImportNotes.setText(selectedFile.getPath());
           // if(FilenameUtils.getExtension(selectedFile))
            mExcelIcon.setVisible(true);

        }catch (Exception e){

        }

    }
}
