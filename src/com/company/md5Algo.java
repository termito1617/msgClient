package com.company;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by vlad on 21.02.2017.
 */
public class md5Algo {
    public static String md5(String pass) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("MD5");
        md.update(pass.getBytes());

        byte byteData[] = md.digest();


        StringBuffer sb = new StringBuffer();
        for (byte aByteData : byteData) {
            sb.append(Integer.toString((aByteData & 0xff) + 0x100, 16).substring(1));
        }
        return sb.toString();
    }
}
