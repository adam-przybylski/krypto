package pl.crypto.viewproject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.HexFormat;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.binary.Hex;
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
    protected void onGenerateKeyButtonClick(){
        inputKeyTextField.setText(Des.generateKey());
    }


    @FXML
    protected void onEncryptButtonClick() throws IOException {
        String text = inputTextArea.getText();
        String key = inputKeyTextField.getText();

        String stringResult = Des.encryptText(text,key);

        outputTextArea.setText(stringResult);
    }

    @FXML
    protected void onDecryptButtonClick() throws IOException {
        byte[] byteTextArray = HexFormat.of().parseHex(outputTextArea.getText()) ;
        String stringKey = inputKeyTextField.getText();

        byte[][] byteBlocksArray = Des.createBlocksForDecryption(byteTextArray);


        for (int i = 0; i < byteBlocksArray.length; i++) {
            byteBlocksArray[i] = Des.decryptBlock(stringKey, byteBlocksArray[i]);
        }
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < byteBlocksArray.length-1; i++) {
            res.append(new String(byteBlocksArray[i], StandardCharsets.UTF_8));
        }
        String stringResult = res.toString();
//        String stringResult = new String(byteBlocksArray[0]);
//        String stringResult = new String(byteBlocksArray[0], StandardCharsets.UTF_8);
        inputTextArea.setText(stringResult);


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
