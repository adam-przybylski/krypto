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
    private TextField pathOfKeysFile;

    @FXML
    private TextField pathToSaveKeyFile;


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
        signedFileInBytes = new byte[s1.length + s2.length + 1];
        System.arraycopy(s1, 0, signedFileInBytes, 0, s1.length);
//        signedFileInBytes[s1.length] = 0;
//        signedFileInBytes[s1.length + 1] = 0;
        System.arraycopy(s2, 0, signedFileInBytes, s1.length, s2.length);
        signedFileInBytes[s1.length+s2.length] = (byte) s2.length;

    }


    @FXML
    protected void onVerifyFileButtonClick() {
        setKeys();
        int s2Length = signedFileInBytes[signedFileInBytes.length-1];
//        for (int i = 0; i < signedFileInBytes.length; i++) {
//            if (signedFileInBytes[i] == 0 && signedFileInBytes[i + 1] == 0) {
//                s2Length = i;
//                break;
//            }
//        }
        byte[] s1 = new byte[signedFileInBytes.length-s2Length-1];
        byte[] s2 = new byte[s2Length];
        System.arraycopy(signedFileInBytes, 0, s1, 0, s1.length);
        System.arraycopy(signedFileInBytes, s1.length, s2, 0, s2Length);
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


    @FXML
    protected void onLoadKeyFileButtonClick() {
        byte[] keyFileInBytes = Dao.readFile(pathOfKeysFile.getText());
        String keyFile = new String(keyFileInBytes);
        String[] keys = keyFile.split("\n");
        qField.setText(keys[0]);
        pField.setText(keys[1]);
        publicKeyField.setText(keys[2]);
        privateKeyField.setText(keys[3]);
        hField.setText(keys[4]);
    }


    @FXML
    protected void onChooseKeyFileButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showOpenDialog(backButton.getScene().getWindow()).getPath();
        pathOfKeysFile.setText(path);
    }


    @FXML
    protected void onSaveKeyFileButtonClick() throws IOException {
        String q = qField.getText() + "\n";
        String p = pField.getText() + "\n";
        String publicKey = publicKeyField.getText() + "\n";
        String privateKey = privateKeyField.getText() + "\n";
        String h = hField.getText() + "\n";
        String keyFile = q + p + publicKey + privateKey + h;
        byte[] keyFileInBytes = keyFile.getBytes();
        Dao.writeFile(pathToSaveKeyFile.getText(), keyFileInBytes);
    }


    @FXML
    protected void onSelectPathToSaveKeyButtonClick() {
        FileChooser fileChooser = new FileChooser();
        String path = fileChooser.showSaveDialog(backButton.getScene().getWindow()).getPath();
        pathToSaveKeyFile.setText(path);
    }


    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        onGenerateKeysButtonClick();
    }
}
