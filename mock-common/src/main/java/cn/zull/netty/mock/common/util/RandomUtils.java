package cn.zull.netty.mock.common.util;

import java.util.Random;

/**
 * @author zurun
 * @date 2018/11/26 21:14:33
 */
public class RandomUtils {
    private static Random RANDOM = new Random();

    /**
     * 0-max 之间的随机整数
     *
     * @param max
     * @return
     */
    public static int randomNumber(int max) {
        return RANDOM.nextInt(max);
    }

    public static int randomNumber(int min, int max) {
        return RANDOM.nextInt(max - min + 1) + min;
    }


    public static String getRandom(int count) {
        return Math.round(Math.random() * (count)) + "f";
    }

    public static String randomNum(int length) {
        String val = "";
        Random random = new Random();
        for (int i = 0; i < length; i++) {
            val += String.valueOf(random.nextInt(10));
        }
        return val;
    }

    public static void main(String[] args) {
        System.out.println(randomNum(6));
        System.out.println(randomNumber(1, 6));

        for (int i = 0; i < 100; i++) {
            System.out.println(randomNumber(1, 5));
        }
    }
}
