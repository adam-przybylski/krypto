package pl.crypto.model;

import java.math.BigInteger;
import java.util.HexFormat;
import java.util.Random;
import org.apache.commons.codec.DecoderException;
import org.apache.commons.codec.binary.Hex;
import org.apache.commons.codec.digest.DigestUtils;

public class Schnorr {

    private static final int qBits = 140;
    private static final int keyBits = 512;

    public static BigInteger q;

    public static BigInteger h;

    public static BigInteger publicKey;

    public static BigInteger privateKey;

    public static BigInteger p;


    public static void generateKeys() {
        q = BigInteger.probablePrime(qBits, new Random());
        BigInteger p1, p2;
        do {
            p1 = BigInteger.probablePrime(keyBits, new Random());
            p2 = p1.subtract(BigInteger.ONE);
            p1 = p1.subtract(p2.remainder(q));
        } while (!p1.isProbablePrime(2));
        p = p1;
        h = new BigInteger(keyBits, new Random());
        h = h.modPow(p.subtract(BigInteger.ONE).divide(q), p);
        do {
            privateKey = new BigInteger(qBits, new Random());
        } while (privateKey.compareTo(BigInteger.ONE) < 1);
        publicKey = h.modPow(privateKey, p);
        publicKey = publicKey.modInverse(p);
    }


    public static void setKeys(BigInteger q, BigInteger h, BigInteger publicKey,
                               BigInteger privateKey, BigInteger p) {
        Schnorr.q = q;
        Schnorr.h = h;
        Schnorr.publicKey = publicKey;
        Schnorr.privateKey = privateKey;
        Schnorr.p = p;
    }


    public static BigInteger[] sign(byte[] plainText) {
        BigInteger r;
        do {
            r = new BigInteger(qBits, new Random());
        } while (r.compareTo(q) > 0);
        BigInteger x = h.modPow(r, p);
        byte[] xBytes = x.toByteArray();
        byte[] mx = new byte[xBytes.length + plainText.length];
        System.arraycopy(plainText, 0, mx, 0, plainText.length);
        System.arraycopy(xBytes, 0, mx, plainText.length, xBytes.length);
        byte[] eBytes = DigestUtils.sha256(mx);
        BigInteger[] signature = new BigInteger[2];
        signature[0] = new BigInteger(1, eBytes);
        signature[1] = r.add(privateKey.multiply(signature[0]));
        signature[1] = signature[1].mod(q);
        return signature;
    }

    public static boolean verify(byte[] plainText, BigInteger s1, BigInteger s2) {
        BigInteger z = h.modPow(s2, p).multiply(publicKey.modPow(s1, p)).mod(p);
        byte[] zBytes = z.toByteArray();
        byte[] mz = new byte[zBytes.length + plainText.length];
        System.arraycopy(plainText, 0, mz, 0, plainText.length);
        System.arraycopy(zBytes, 0, mz, plainText.length, zBytes.length);
        byte[] eBytes = DigestUtils.sha256(mz);
        BigInteger e = new BigInteger(1, eBytes);
        return e.equals(s1);
    }



    public static String bigInttoHexString(BigInteger bigInteger) {
        return Hex.encodeHexString(bigInteger.toByteArray());
    }

    public static BigInteger hexStringToBigInt(String hexString) throws DecoderException {
        return new BigInteger(Hex.decodeHex(hexString));
    }


}
