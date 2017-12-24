package com.artf.poloa.data.utility;

import java.io.UnsupportedEncodingException;
import java.math.BigInteger;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Set;

import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;

public class Utility {

    public static String getUri(HashMap<String, String> args) {
        Set<String> keys = args.keySet();
        String postData = "";
        for (String key : keys) {
            if (postData.length() > 0) {
                postData += "&";
            }
            postData += key + "=" + args.get(key);
        }
        return postData;
    }

    public static String getSignature(String secretKey, String postData) {
        String signature = "";
        try {
            SecretKeySpec keySpec = new SecretKeySpec(secretKey.getBytes(Constant.DEFAULT_ENCODING), Constant.HMAC_SHA512);
            Mac mac = Mac.getInstance(Constant.HMAC_SHA512);
            mac.init(keySpec);
            signature = toHex(mac.doFinal(postData.getBytes(Constant.DEFAULT_ENCODING)));
        } catch (InvalidKeyException | UnsupportedEncodingException | NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return signature;
    }

    private static String toHex(byte[] b) {
        return String.format("%040x", new BigInteger(1, b));
    }

}
