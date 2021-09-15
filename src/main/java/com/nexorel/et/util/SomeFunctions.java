package com.nexorel.et.util;

import java.util.Random;

public class SomeFunctions {

    public static int generateRandomIntBetweenRange(int min, int max, Random random) {
        return random.nextInt((max - min) + 1) + min;
    }

}
