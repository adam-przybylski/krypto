package pl.crypto.viewproject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.Arrays;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import pl.crypto.model.Des;

public class DesController implements Initializable {
    @FXML
    private TextArea inputTextArea;
    @FXML
    private TextField inputKeyTextField;

    @FXML
    private Button encryptButton;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private Button backButton;

    @FXML
    private Button decryptButton;

    @FXML
    private TextField inputFileTextField;

    @FXML
    protected void onEncryptButtonClick() throws IOException {
        byte[] byteTextArray = inputTextArea.getText().getBytes();
        String stringKey = inputKeyTextField.getText();

        byte[][] byteBlocksArray = Des.createBlocks(byteTextArray);


        for (int i = 0; i < byteBlocksArray.length; i++) {
            byteBlocksArray[i] = Des.encrypt(stringKey, byteBlocksArray[i]);
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < byteBlocksArray.length; i++) {
            res.append(byteBlocksArray[i]);
        }
        String stringResult = res.toString();

        outputTextArea.setText(stringResult);
    }

    @FXML
    protected void onDecryptButtonClick() throws IOException {
        byte[] byteTextArrray = outputTextArea.getText().getBytes();
        String byteKeyArrray = inputKeyTextField.getText();
        //inputTextArea.setText(new String(Des.encrypt(byteTextArrray, byteKeyArrray)));

        BigInteger b = new BigInteger("1");
        BigInteger b1 = b.flipBit(0);
        BigInteger b2 = b.flipBit(1);
    }

    @FXML
    protected void onChooseFileButtonClick() throws IOException {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showOpenDialog(backButton.getScene().getWindow()).getPath();
        inputFileTextField.setText(path);

        File file = new File(inputFileTextField.getText());
        byte[] bytes = new byte[(int) file.length()];

        try(FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String fileText = new String(bytes);

        inputTextArea.setText(fileText);

        File newFile = new File("new.pdf");
        newFile.createNewFile();
        try(FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

    }

    @FXML
    protected void onEncryptFileButtonClick(){

    }

    @FXML
    protected void onDecryptFileButtonClick(){

    }

    @FXML
    protected void onBackButtonClick() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        HelloApplication.switchScene(stage, "hello-view.fxml");
    }

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {

    }
}
