package com.nexorel.et.util;

import java.util.Random;

public class SomeFunctions {

    public static int generateRandomIntBetweenRange(int min, int max, Random random) {
        return random.nextInt((max - min) + 1) + min;
    }

    public static double interpolate_with_clipping(double x, double x1, double x2, double y1, double y2) {
        if (x1 > x2) {
            double temp = x1;
            x1 = x2;
            x2 = temp;
            temp = y1;
            y1 = y2;
            y2 = temp;
        }

        if (x <= x1) return y1;
        if (x >= x2) return y2;
        double xFraction = (x - x1) / (x2 - x1);
        return y1 + xFraction * (y2 - y1);
    }

}
