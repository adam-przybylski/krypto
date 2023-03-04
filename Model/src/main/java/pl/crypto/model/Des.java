package pl.crypto.model;

import java.io.ByteArrayOutputStream;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.jar.JarException;

public class Des {
    public static byte[] encrypt(byte[] key, byte[] text) throws java.io.IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        outputStream.write(key);
        outputStream.write(text);

        return outputStream.toByteArray();
    }

    public static BigInteger[] createBlocks(byte[] input) {
        BigInteger bigInteger = new BigInteger(input);

        int inputLength = bigInteger.bitLength();
        int fullBlocks = inputLength / 8;
        int bitPadding = inputLength - fullBlocks * 8;
        BigInteger[] result = new BigInteger[fullBlocks + 2];

        for (int i = 0; i < fullBlocks; i++) {
            byte[] temp = Arrays.copyOfRange(input, i, i+1);
            result[i] = new BigInteger(temp);
        }
        // TODO fix last two BigInts
        byte[] last = Arrays.copyOfRange(input, fullBlocks + 1, fullBlocks + 2);
        BigInteger lastBI = new BigInteger(last);
        lastBI.shiftLeft(64-lastBI.bitLength());

        result[fullBlocks] = lastBI;
        byte[] tempPad = new byte[1];
        tempPad[0] = (byte) bitPadding;
        BigInteger paddingNumberBI = new BigInteger(tempPad);

        result[fullBlocks + 1] = paddingNumberBI;

        System.out.println("in bits: " + inputLength + " full block: " + fullBlocks + " padding " +  bitPadding);
        //TODO [n][8]
        return result;
    }
}
