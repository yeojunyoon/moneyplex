package com.kakaopay.moneyplex.util;

public class RandomUtil {
    private static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHJIKLMNOPQRSTUVWXYZ";

    public static String getRandomString(int length){
        StringBuilder sb = new StringBuilder();
        for(int i=0; i<length; i++){
            sb.append(ALPHABET.charAt(getRandInt(ALPHABET.length())));
        }
        return sb.toString();
    }

    public static long getRandLong(long maxValue){
        return Math.round(Math.random() * maxValue) % maxValue;
    }

    public static int getRandInt(int maxValue){
        return (int) Math.round(Math.random() * maxValue) % maxValue;
    }
}
