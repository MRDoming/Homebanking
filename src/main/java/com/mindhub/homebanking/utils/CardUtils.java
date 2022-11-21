package com.mindhub.homebanking.utils;

public final class CardUtils {

    private CardUtils(){};

    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
    }

    public static String getCardNumber() {
        String cardNumber = getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000) + "-" + getRandomNumber(1000, 10000);
        return cardNumber;
    }

    public static int getCvv() {
        int cvv = getRandomNumber(100, 1000);
        return cvv;
    }
}
