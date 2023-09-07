package nl.eric.utils;

import java.util.Random;

public class ThreadUtil {

    public static void wait(int i) throws InterruptedException {

        int millis = i + new Random().nextInt(200 + 200) - 200;

        Thread.sleep(millis);
    }
}
