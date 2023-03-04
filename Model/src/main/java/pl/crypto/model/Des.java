package pl.crypto.model;

import java.io.ByteArrayOutputStream;
import java.util.jar.JarException;

public class Des {
    public static byte[] encrypt(byte[] key, byte[] text) throws java.io.IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(key);
        outputStream.write(text);

        return outputStream.toByteArray();
    }
}
