package pl.crypto.model;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.jar.JarException;

public class Des {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    public static byte[] encrypt(byte[] key, byte[] text) throws java.io.IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(key);
        outputStream.write(text);

        return outputStream.toByteArray();
    }

    public static byte[] hexStringToByteArray(String s) throws UnsupportedEncodingException {
            if (s == null) { return null;}
            else if (s.length() < 2) { return null;}
            else { if (s.length()%2!=0)s+='0';
                int dl = s.length() / 2;
                byte[] wynik = new byte[dl];
                for (int i = 0; i < dl; i++)
                { 
                    wynik[i] = (byte) Integer.parseInt(s.substring(i * 2, i * 2 + 2), 16);
                }
                return wynik;
            }
    }

    public static String bytesToHex(byte[] bytes) {
        byte rawData[] = bytes;
        StringBuilder hexText = new StringBuilder();
        String initialHex = null;
        int initHexLength = 0;

        for (int i = 0; i < rawData.length; i++)
        {
            int positiveValue = rawData[i] & 0x000000FF;
            initialHex = Integer.toHexString(positiveValue);
            initHexLength = initialHex.length();
            while (initHexLength++ < 2)
            {
                hexText.append("0");
            }
            hexText.append(initialHex);
        }
        return hexText.toString();
    }


    //konwertuje stringa na BigIntegera
    public static BigInteger stringToBigInt(String str)
    {
        byte[] tab = new byte[str.length()];
        for (int i = 0; i < tab.length; i++)
            tab[i] = (byte)str.charAt(i);
        return new BigInteger(1,tab);
    }

    //konwertuje BigIntegera na string
    public static String bigIntToString(BigInteger n)
    {
        byte[] tab = n.toByteArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tab.length; i++)
            sb.append((char)tab[i]);
        return sb.toString();
    }

    public static byte[][] createBlocks(byte[] input) {
        int inputLen = input.length;
        // We need blocks to fit all bytes
        int blockAmount = inputLen / 8 + 1;
        byte[][] result = new byte[blockAmount][8];
        for (int i = 0; i < blockAmount; i++) {
            // Trzeba dokończyć TODO kopiowanie bloków
        }

        // W ostatnim bajcie dopisujemy ile bajtów paddingu dodaliśmy
        result[blockAmount-1][7] =  (byte) (8 - (inputLen % 8));
        return result;
    }
}