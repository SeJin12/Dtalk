package com.example.capdtalk.SHA256;

import java.math.BigInteger;
import java.security.MessageDigest;

public class SHA256 {

    public static String getSHA256(String input){
        String convertValue = null;
        try{
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            digest.reset();
            digest.update(input.getBytes("utf8"));
            convertValue = String.format("%040x",new BigInteger(1,digest.digest()));
        }catch (Exception e){
            e.printStackTrace();
        }
        return convertValue;
    }

}
