package pl.crypto.model;

import java.io.ByteArrayOutputStream;
import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class Des {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static final int[] IP ={
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9,  1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

//    private static final int[]
    //  Index w BigInt oznacza którą pozycje od prawej stronu ma bit np:
    // 1 0 1 0 1 1 0 1 1
    // 8 7 6 5 4 3 2 1 0
    private static long permute(int[] table, int srcWidth, long src) {
        long dst = 0;
        for (int i=0; i<table.length; i++) {
            int srcPos = srcWidth - table[i];
            dst = (dst<<1) | (src>>srcPos & 0x01);
        }
        return dst;
    }

    private static long getLongFromBytes(byte[] ba, int offset) {
        long l = 0;
        for (int i=0; i<8; i++) {
            byte value;
            if ((offset+i) < ba.length) {
                value = ba[offset+i];
            } else {
                value = 0;
            }
            l = l<<8 | (value & 0xFFL);
        }
        return l;
    }

    private static void getBytesFromLong(byte[] ba, int offset, long l) {
        for (int i=7; i>=0; i--) {
            if ((offset+i) < ba.length) {
                ba[offset+i] = (byte) (l & 0xFF);
                l = l >> 8;
            } else {
                break;
            }
        }
    }

    private static long getLongFromBytes(byte[] ba) {
        return getLongFromBytes(ba, 0);
    }


    public static byte[] encrypt(byte[] key, byte[] text) throws java.io.IOException {
        //tworzenie bloków
        byte[][] blocks = createBlocks(text);
        //tworzenie longów do permutacji
        long[] lBlocks = new long[blocks.length];
        for (int i = 0; i <lBlocks.length ; i++) {
            lBlocks[i] = getLongFromBytes(blocks[i]);
        }
        //permutacja początkowa
        for (int i = 0; i <lBlocks.length ; i++) {
            lBlocks[i] = permute(IP,64,lBlocks[i]);
        }
        int[] leftBlocks = new int[lBlocks.length];
        int[] rightBlocks = new int[lBlocks.length];
        for (int i = 0; i <lBlocks.length ; i++) {
            leftBlocks[i] = (int)lBlocks[i] >> 32;
            rightBlocks[i] = (int) ((int)lBlocks[i]&0xFFFFFFFFL); //32 jedynki
        }
    }


    public static BigInteger[] byteBlockArrayToBigIntArray(byte[][] input) {
        BigInteger[] bigIntArray = new BigInteger[input.length];
        for (int i = 0; i < input.length ; i++) {
            bigIntArray[i] = new BigInteger(input[i]);
        }
        return bigIntArray;
    }

    public static byte[][] bigIntArrayToByteBlockArray(BigInteger[] input) {
        byte[][] byteBlockArray = new byte[input.length][8];
        for (int i = 0; i < input.length ; i++) {
            byteBlockArray[i] = input[i].toByteArray();
        }
        return byteBlockArray;
    }

    public static String byteBlockArrayToString(byte[][] input) {
        StringBuilder builder = new StringBuilder();

        for (int i = 0; i < input.length - 1; i++) {
            builder.append(new String(input[i]));
        }

        int numberOfZeros = input[input.length - 1][7];

        //obcięcie zer i dodanie pozostałych bajtów
        byte[] lastBytes = new byte[8-numberOfZeros];

        for (int i = 0; i < 8 - numberOfZeros; i++) {
            lastBytes[i] = input[input.length-1][i];
        }

        builder.append(new String(lastBytes));
        return builder.toString();
    }


    public static byte[][] createBlocks(byte[] input) {
        int inputLen = input.length;
        // We need blocks to fit all bytes
        int blockAmount = inputLen / 8 + 1;
        byte[][] result = new byte[blockAmount][8];

        for (int i = 0; i < blockAmount - 1; i++) {
            for (int j = 0; j < 8; j++) {
                result[i][j] = input[8 * i + j];
            }
        }

        for (int i = 0; i < inputLen % 8; i++) {
            result[blockAmount - 1][i] = input[(blockAmount - 1) * 8 + i];
        }

        // W ostatnim bajcie dopisujemy ile bajtów paddingu dodaliśmy
        result[blockAmount - 1][7] = (byte) (8 - (inputLen % 8));
        return result;
    }


    //konwertuje stringa na BigIntegera
    public static BigInteger stringToBigInt(String str) {
        byte[] tab = new byte[str.length()];
        for (int i = 0; i < tab.length; i++) {
            tab[i] = (byte) str.charAt(i);
        }
        return new BigInteger(1, tab);
    }

    //konwertuje BigIntegera na string
    public static String bigIntToString(BigInteger n) {
        byte[] tab = n.toByteArray();
        StringBuffer sb = new StringBuffer();
        for (int i = 0; i < tab.length; i++) {
            sb.append((char) tab[i]);
        }
        return sb.toString();
    }

    public static byte[] hexStringToByteArray(String s) throws UnsupportedEncodingException {
        if (s == null) {
            return null;
        } else if (s.length() < 2) {
            return null;
        } else {
            if (s.length() % 2 != 0) {
                s += '0';
            }
            int dl = s.length() / 2;
            byte[] wynik = new byte[dl];
            for (int i = 0; i < dl; i++) {
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

        for (int i = 0; i < rawData.length; i++) {
            int positiveValue = rawData[i] & 0x000000FF;
            initialHex = Integer.toHexString(positiveValue);
            initHexLength = initialHex.length();
            while (initHexLength++ < 2) {
                hexText.append("0");
            }
            hexText.append(initialHex);
        }
        return hexText.toString();
    }
}
