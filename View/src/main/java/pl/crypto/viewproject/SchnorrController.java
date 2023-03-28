package pl.crypto.viewproject;

import java.io.IOException;
import java.math.BigInteger;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.FileChooser;
import javafx.stage.Stage;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import pl.crypto.model.Dao;
import pl.crypto.model.Schnorr;

public class SchnorrController implements Initializable {

    @FXML
    private Button backButton;

    @FXML
    private TextField qField;

    @FXML
    private TextField hField;

    @FXML
    private TextField publicKeyField;

    @FXML
    private TextField privateKeyField;

    @FXML
    private TextField pField;

    @FXML
    private TextArea inputTextArea;

    @FXML
    private TextArea outputTextArea;

    @FXML
    private TextField pathToSaveEncryptedFile;

    @FXML
    private TextField pathToSaveDecryptedFile;

    @FXML
    private TextField inputEncryptedFileTextField;
    private byte[] signedFileInBytes;
    private byte[] fileInBytes;

    @FXML
    private TextField inputFileTextField;


    @FXML
    protected void onBackButtonClick() throws IOException {
        Stage stage = (Stage) backButton.getScene().getWindow();
        HelloApplication.switchScene(stage, "hello-view.fxml");
    }

    @FXML
    protected void onGenerateKeysButtonClick() {
        Schnorr.generateKeys();
        qField.setText(Hex.encodeHexString(Schnorr.q.toByteArray()));
        hField.setText(Hex.encodeHexString(Schnorr.h.toByteArray()));
        publicKeyField.setText(Hex.encodeHexString(Schnorr.publicKey.toByteArray()));
        privateKeyField.setText(Hex.encodeHexString(Schnorr.privateKey.toByteArray()));
        pField.setText(Hex.encodeHexString(Schnorr.p.toByteArray()));
    }

    protected void setKeys() {
        Schnorr.q = new BigInteger(qField.getText(), 16);
        Schnorr.h = new BigInteger(hField.getText(), 16);
        Schnorr.publicKey = new BigInteger(publicKeyField.getText(), 16);
        Schnorr.privateKey = new BigInteger(privateKeyField.getText(), 16);
        Schnorr.p = new BigInteger(pField.getText(), 16);
    }


    @FXML
    protected void onSignButtonClick() {
        setKeys();
        BigInteger[] sign = Schnorr.sign(inputTextArea.getText().getBytes());
        outputTextArea.setText(
                Schnorr.bigInttoHexString(sign[0]) + "\n" + Schnorr.bigInttoHexString(sign[1]));
    }

    @FXML
    protected void onVerifyButtonClick() {
        setKeys();
        String[] sign = outputTextArea.getText().split("\n");
        try {
            if (Schnorr.verify(inputTextArea.getText().getBytes(),
                    Schnorr.hexStringToBigInt(sign[0]), Schnorr.hexStringToBigInt(sign[1]))) {
                CustomAlert.showAlert(Alert.AlertType.INFORMATION, "Weryfikacja",
                        "Podpis jest poprawny");
            } else {
                CustomAlert.showAlert(Alert.AlertType.ERROR, "Weryfikacja",
                        "Podpis jest niepoprawny");
            }
        } catch (DecoderException e) {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Weryfikacja", "Podpis jest niepoprawny");
        }

    }


    @FXML
    protected void onSignFileButtonClick() {
        setKeys();
        BigInteger[] sign = Schnorr.sign(fileInBytes);
        outputTextArea.setText(
                Schnorr.bigInttoHexString(sign[0]) + "\n" + Schnorr.bigInttoHexString(sign[1]));
        byte[] s1 = sign[0].toByteArray();
        byte[] s2 = sign[1].toByteArray();
        signedFileInBytes = new byte[s1.length + s2.length + 2];
        System.arraycopy(s1, 0, signedFileInBytes, 0, s1.length);
        signedFileInBytes[s1.length] = 0;
        signedFileInBytes[s1.length + 1] = 0;
        System.arraycopy(s2, 0, signedFileInBytes, s1.length + 2, s2.length);

    }


    @FXML
    protected void onVerifyFileButtonClick() {
        setKeys();
        int s1Length = 0;
        for (int i = 0; i <signedFileInBytes.length ; i++) {
            if(signedFileInBytes[i] == 0 && signedFileInBytes[i+1] == 0){
                s1Length = i;
                break;
            }
        }
        byte[] s1 = new byte[s1Length];
        byte[] s2 = new byte[signedFileInBytes.length - s1Length - 2];
        System.arraycopy(signedFileInBytes, 0, s1, 0, s1Length);
        System.arraycopy(signedFileInBytes, s1Length + 2, s2, 0, s2.length);
        BigInteger s1BigInt = new BigInteger(s1);
        BigInteger s2BigInt = new BigInteger(s2);
        if (Schnorr.verify(fileInBytes, s1BigInt, s2BigInt)) {
            CustomAlert.showAlert(Alert.AlertType.INFORMATION, "Weryfikacja",
                    "Podpis jest poprawny");
        } else {
            CustomAlert.showAlert(Alert.AlertType.ERROR, "Weryfikacja",
                    "Podpis jest niepoprawny");
        }
    }


    @FXML
    protected void onSaveSignFileButtonClick() throws IOException {
        Dao.writeFile(pathToSaveEncryptedFile.getText(), signedFileInBytes);
    }

    @FXML
    protected void onSaveFileButtonClick() throws IOException {
        Dao.writeFile(pathToSaveDecryptedFile.getText(), fileInBytes);
    }

    @FXML
    protected void onSelectPathToSaveSignButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showSaveDialog(backButton.getScene().getWindow()).getPath();
        pathToSaveEncryptedFile.setText(path);
    }


    @FXML
    protected void onSelectPathToSaveFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showSaveDialog(backButton.getScene().getWindow()).getPath();
        pathToSaveDecryptedFile.setText(path);
    }

    @FXML
    protected void onChooseSignFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showOpenDialog(backButton.getScene().getWindow()).getPath();
        inputEncryptedFileTextField.setText(path);
    }

    @FXML
    protected void onLoadSignFileButtonClick() {
        signedFileInBytes = Dao.readFile(inputEncryptedFileTextField.getText());

        String fileText = Hex.encodeHexString(signedFileInBytes);

        outputTextArea.setText(fileText);
    }


    @FXML
    protected void onChooseFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showOpenDialog(backButton.getScene().getWindow()).getPath();
        inputFileTextField.setText(path);

    }


    @FXML
    protected void onLoadPlainTextFileButtonClick() {
        fileInBytes = Dao.readFile(inputFileTextField.getText());

        String fileText = new String(fileInBytes);

        inputTextArea.setText(fileText);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onGenerateKeysButtonClick();
    }
}
