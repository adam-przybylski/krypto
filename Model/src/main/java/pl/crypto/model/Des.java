package pl.crypto.model;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;

public class Des {
    private static final char[] HEX_ARRAY = "0123456789ABCDEF".toCharArray();

    private static final int[] InitialPerm = {
            58, 50, 42, 34, 26, 18, 10, 2,
            60, 52, 44, 36, 28, 20, 12, 4,
            62, 54, 46, 38, 30, 22, 14, 6,
            64, 56, 48, 40, 32, 24, 16, 8,
            57, 49, 41, 33, 25, 17, 9, 1,
            59, 51, 43, 35, 27, 19, 11, 3,
            61, 53, 45, 37, 29, 21, 13, 5,
            63, 55, 47, 39, 31, 23, 15, 7
    };

    private static final int[] KeyPerm1 = {
            57, 49, 41, 33, 25, 17, 9,
            1, 58, 50, 42, 34, 26, 18,
            10, 2, 59, 51, 43, 35, 27,
            19, 11, 3, 60, 52, 44, 36,
            63, 55, 47, 39, 31, 23, 15,
            7, 62, 54, 46, 38, 30, 22,
            14, 6, 61, 53, 45, 37, 29,
            21, 13, 5, 28, 20, 12, 4
    };

    private static final int[] KeyPerm2 = {
            14, 17, 11, 24, 1,  5,
            3,  28, 15, 6,  21, 10,
            23, 19, 12, 4,  26, 8,
            16, 7,  27, 20, 13, 2,
            41, 52, 31, 37, 47, 55,
            30, 40, 51, 45, 33, 48,
            44, 49, 39, 56, 34, 53,
            46, 42, 50, 36, 29, 32
    };

    private static final int[] ExpansionTable = {
            32, 1, 2, 3, 4, 5,
            4, 5, 6, 7, 8, 9,
            8, 9, 10, 11, 12, 13,
            12, 13, 14, 15, 16, 17,
            16, 17, 18, 19, 20, 21,
            20, 21, 22, 23, 24, 25,
            24, 25, 26, 27, 28, 29,
            28, 29, 30, 31, 32, 1
    };

    private static final int[] Rotations = {
            1, 1, 2, 2, 2, 2, 2, 2, 1, 2, 2, 2, 2, 2, 2, 1
    };


    // Substitution Box
    private static final byte[][] SubstitutionTable = {{
            14, 4, 13, 1, 2, 15, 11, 8, 3, 10, 6, 12, 5, 9, 0, 7,
            0, 15, 7, 4, 14, 2, 13, 1, 10, 6, 12, 11, 9, 5, 3, 8,
            4, 1, 14, 8, 13, 6, 2, 11, 15, 12, 9, 7, 3, 10, 5, 0,
            15, 12, 8, 2, 4, 9, 1, 7, 5, 11, 3, 14, 10, 0, 6, 13
    }, {
            15, 1, 8, 14, 6, 11, 3, 4, 9, 7, 2, 13, 12, 0, 5, 10,
            3, 13, 4, 7, 15, 2, 8, 14, 12, 0, 1, 10, 6, 9, 11, 5,
            0, 14, 7, 11, 10, 4, 13, 1, 5, 8, 12, 6, 9, 3, 2, 15,
            13, 8, 10, 1, 3, 15, 4, 2, 11, 6, 7, 12, 0, 5, 14, 9
    }, {
            10, 0, 9, 14, 6, 3, 15, 5, 1, 13, 12, 7, 11, 4, 2, 8,
            13, 7, 0, 9, 3, 4, 6, 10, 2, 8, 5, 14, 12, 11, 15, 1,
            13, 6, 4, 9, 8, 15, 3, 0, 11, 1, 2, 12, 5, 10, 14, 7,
            1, 10, 13, 0, 6, 9, 8, 7, 4, 15, 14, 3, 11, 5, 2, 12
    }, {
            7, 13, 14, 3, 0, 6, 9, 10, 1, 2, 8, 5, 11, 12, 4, 15,
            13, 8, 11, 5, 6, 15, 0, 3, 4, 7, 2, 12, 1, 10, 14, 9,
            10, 6, 9, 0, 12, 11, 7, 13, 15, 1, 3, 14, 5, 2, 8, 4,
            3, 15, 0, 6, 10, 1, 13, 8, 9, 4, 5, 11, 12, 7, 2, 14
    }, {
            2, 12, 4, 1, 7, 10, 11, 6, 8, 5, 3, 15, 13, 0, 14, 9,
            14, 11, 2, 12, 4, 7, 13, 1, 5, 0, 15, 10, 3, 9, 8, 6,
            4, 2, 1, 11, 10, 13, 7, 8, 15, 9, 12, 5, 6, 3, 0, 14,
            11, 8, 12, 7, 1, 14, 2, 13, 6, 15, 0, 9, 10, 4, 5, 3
    }, {
            12, 1, 10, 15, 9, 2, 6, 8, 0, 13, 3, 4, 14, 7, 5, 11,
            10, 15, 4, 2, 7, 12, 9, 5, 6, 1, 13, 14, 0, 11, 3, 8,
            9, 14, 15, 5, 2, 8, 12, 3, 7, 0, 4, 10, 1, 13, 11, 6,
            4, 3, 2, 12, 9, 5, 15, 10, 11, 14, 1, 7, 6, 0, 8, 13
    }, {
            4, 11, 2, 14, 15, 0, 8, 13, 3, 12, 9, 7, 5, 10, 6, 1,
            13, 0, 11, 7, 4, 9, 1, 10, 14, 3, 5, 12, 2, 15, 8, 6,
            1, 4, 11, 13, 12, 3, 7, 14, 10, 15, 6, 8, 0, 5, 9, 2,
            6, 11, 13, 8, 1, 4, 10, 7, 9, 5, 0, 15, 14, 2, 3, 12
    }, {
            13, 2, 8, 4, 6, 15, 11, 1, 10, 9, 3, 14, 5, 0, 12, 7,
            1, 15, 13, 8, 10, 3, 7, 4, 12, 5, 6, 11, 0, 14, 9, 2,
            7, 11, 4, 1, 9, 12, 14, 2, 0, 6, 10, 13, 15, 3, 5, 8,
            2, 1, 14, 7, 4, 10, 8, 13, 15, 12, 9, 0, 3, 5, 6, 11
    }};

    private static final int[] P = {
            16, 7, 20, 21,
            29, 12, 28, 17,
            1, 15, 23, 26,
            5, 18, 31, 10,
            2, 8, 24, 14,
            32, 27, 3, 9,
            19, 13, 30, 6,
            22, 11, 4, 25
    };

    //    private static final int[]
    //  Index w BigInt oznacza którą pozycje od prawej stronu ma bit np:
    // 1 0 1 0 1 1 0 1 1
    // 8 7 6 5 4 3 2 1 0
    private static long permute(int[] table, int srcWidth, long src) {
        long dst = 0;
        for (int i = 0; i < table.length; i++) {
            int srcPos = srcWidth - table[i];
            // przesuwa "stare" bity w lewo i ustawia pojedynczy bit na nowej pozycji
            dst = (dst << 1) | (src >> srcPos & 0x01);
        }
        return dst;
    }

    private static long getLongFromBytes(byte[] ba, int offset) {
        long l = 0;
        for (int i = 0; i < 8; i++) {
            byte value;
            if ((offset + i) < ba.length) {
                value = ba[offset + i];
            } else {
                value = 0;
            }
            l = l << 8 | (value & 0xFFL);
        }
        return l;
    }

    private static void getBytesFromLong(byte[] ba, int offset, long l) {
        for (int i = 7; i >= 0; i--) {
            if ((offset + i) < ba.length) {
                ba[offset + i] = (byte) (l & 0xFF);
                l = l >> 8;
            } else {
                break;
            }
        }
    }

    private static long getLongFromBytes(byte[] ba) {
        return getLongFromBytes(ba, 0);
    }


    public static byte[] encrypt(byte[] key, byte[] block) throws java.io.IOException {
        //tworzenie longów do permutacji
        long lBlock = getLongFromBytes(block);
        //permutacja początkowa
        lBlock = permute(InitialPerm, 64, lBlock);
        int leftBlock = (int) (lBlock >> 32);
        int rightBlock = (int) (lBlock & 0xFFFFFFFFL); //32 jedynki

        return null;
    }

    private static byte SubBoxValue(int boxNumber, byte value) {
        // 0x20 lewy skrajny bit 0x01 prawy skrajny bit 0x1E środkowe bity
        // ta operacja daje nam indeks w jednowymiarowej tabeli który potrzebujemy !!!
        byte index = (byte) (value & 0x20 | ((value & 0x01) << 4) | ((value & 0x1E) >> 1));
        return SubstitutionTable[boxNumber - 1][index];
    }

    private static int round(int halfBlock, long key) {
        // Z 32 bitowej połowy bloku robimy 48 bitowe pole
        // bitowa maniupulacja aby rzutować na longa i mieć dobre bity w pamięci
        long expandedBlock = permute(ExpansionTable, 32, halfBlock & 0xFFFFFFFFL);

        long xored = expandedBlock ^ key;

        // Substytucja po której z 48 bitów wejściowych otrzymamy 32 bity wyjściowe
        int returnHalfBlock = 0;
        // najpierw zapisujemy z "lewej strony inta, przsuwamy o 4 bity w lewo i dodajemy kolejne bity
        for (int i = 0; i < 8; i++) {
            // 3 > ponieważ nie chcemy przesuwać znaku
            returnHalfBlock >>>= 4;
            // nauczyć się jak to dokładnie działa
            // 3f = 0011111 czyli 6 "prawych bitów"
            int substituted = SubBoxValue(8 - 1, (byte) (xored & 0x3F));
            returnHalfBlock |= substituted << 28;
            xored >>= 6; //Przesuwamy liczbę o bity które użyliśmy
        }

        int lastPermutation = (int) permute(P, 32, returnHalfBlock);

        return lastPermutation;
    }

    private static long[] createSubKeys(String key) {
        byte[] byteKey = key.getBytes();
        long lKey = getLongFromBytes(byteKey);
        //permutacja klucza; wyrzucenie bitów parzystości
        lKey = permute(KeyPerm1, 64, lKey);
        long[] subKeys = new long[16];
        int l = (int) (lKey >> 28);
        int r = (int) (lKey & 0xFFFFFFF);

        for (int i = 0; i < 16; i++) {
            if (Rotations[i] == 1) {
                l = ((l << 1) & 0x0FFFFFF) | (l >> 27);
                r = ((r << 1) & 0x0FFFFFF) | (r >> 27);
            } else {
                l = ((l << 2) & 0x0FFFFFF) | (l >> 26);
                r = ((r << 2) & 0x0FFFFFF) | (r >> 26);
            }
            long subkey = (l & 0xFFFFFFFFL) << 28 | (r & 0xFFFFFFFFL);

            subKeys[i] = permute(KeyPerm2, 56, subkey);
        }

        return subKeys;
    }

    public static BigInteger[] byteBlockArrayToBigIntArray(byte[][] input) {
        BigInteger[] bigIntArray = new BigInteger[input.length];
        for (int i = 0; i < input.length; i++) {
            bigIntArray[i] = new BigInteger(input[i]);
        }
        return bigIntArray;
    }

    public static byte[][] bigIntArrayToByteBlockArray(BigInteger[] input) {
        byte[][] byteBlockArray = new byte[input.length][8];
        for (int i = 0; i < input.length; i++) {
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
        byte[] lastBytes = new byte[8 - numberOfZeros];

        for (int i = 0; i < 8 - numberOfZeros; i++) {
            lastBytes[i] = input[input.length - 1][i];
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
