package pl.crypto.viewproject;


import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
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
    private TextField pathToSaveEncryptedFile;

    @FXML
    private TextField inputEncryptedFileTextField;

    private byte[] fileInBytes;

    private byte[] encryptedFileInBytes;


    @FXML
    protected void onGenerateKeyButtonClick() {
        inputKeyTextField.setText(Des.generateKey());
    }


    @FXML
    protected void onEncryptButtonClick() {
        String text = inputTextArea.getText();
        String key = inputKeyTextField.getText();

        String stringResult = Des.encryptText(text, key);

        outputTextArea.setText(stringResult);
    }

    @FXML
    protected void onDecryptButtonClick() {
        String outputText = outputTextArea.getText();
        String stringKey = inputKeyTextField.getText();

        String result = Des.decryptText(outputText, stringKey);

        inputTextArea.setText(result);


    }

    @FXML
    protected void onChooseFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showOpenDialog(backButton.getScene().getWindow()).getPath();
        inputFileTextField.setText(path);

//        File file = new File(inputFileTextField.getText());
//        byte[] bytes = new byte[(int) file.length()];
//
//        try(FileInputStream fis = new FileInputStream(file)) {
//            fis.read(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        String fileText = new String(bytes);
//
//        inputTextArea.setText(fileText);
//
//        File newFile = new File("new.pdf");
//        newFile.createNewFile();
//        try(FileOutputStream fos = new FileOutputStream(newFile)) {
//            fos.write(bytes);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

    }


    @FXML
    protected void onLoadPlainTextFileButtonClick() {
        File file = new File(inputFileTextField.getText());
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        fileInBytes = bytes;

        String fileText = new String(bytes);

        inputTextArea.setText(fileText);
    }

    @FXML
    protected void onEncryptFileButtonClick() {
        String key = inputKeyTextField.getText();

        encryptedFileInBytes = Des.encryptFile(fileInBytes, key);

        String encryptedFile = Hex.encodeHexString(encryptedFileInBytes);

        outputTextArea.setText(encryptedFile);

    }

    @FXML
    protected void onDecryptFileButtonClick() {
        String key = inputKeyTextField.getText();

        fileInBytes = Des.decryptFile(encryptedFileInBytes, key);

        String result = new String(fileInBytes);

        inputTextArea.setText(result);

    }

    @FXML
    protected void onSaveEncryptedFileButtonClick() throws IOException {
        File newFile = new File(pathToSaveEncryptedFile.getText());
        newFile.createNewFile();
        try (FileOutputStream fos = new FileOutputStream(newFile)) {
            fos.write(encryptedFileInBytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @FXML
    protected void onSelectPathToSaveEncryptedButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showSaveDialog(backButton.getScene().getWindow()).getPath();
        pathToSaveEncryptedFile.setText(path);
    }

    @FXML
    protected void onChooseEncryptedFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showOpenDialog(backButton.getScene().getWindow()).getPath();
        inputEncryptedFileTextField.setText(path);
    }

    @FXML
    protected void onLoadEncryptedFileButtonClick() {
        File file = new File(inputEncryptedFileTextField.getText());
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        encryptedFileInBytes = bytes;

        String fileText = Hex.encodeHexString(encryptedFileInBytes);

        outputTextArea.setText(fileText);
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
