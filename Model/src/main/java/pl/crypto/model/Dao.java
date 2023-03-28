package pl.crypto.model;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;

public class Dao implements AutoCloseable {

    public static byte[] readFile(String name) {
        File file = new File(name);
        byte[] bytes = new byte[(int) file.length()];

        try (FileInputStream fis = new FileInputStream(file)) {
            fis.read(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return bytes;
    }

    public static void writeFile(String name, byte[] bytes) {
        File file = new File(name);
        try (FileOutputStream fos = new FileOutputStream(file)) {
            fos.write(bytes);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void close() throws Exception {

    }
}
