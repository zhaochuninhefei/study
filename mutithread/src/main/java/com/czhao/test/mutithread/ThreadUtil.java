package com.czhao.test.mutithread;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;

/**
 * @author zhaochun
 */
public class ThreadUtil {

    public static final String RANDOM_FILES_PATH = "./randomFiles";

    public static void writeRandomNumberFile(int randomNumber, String threadName) {
        LocalDateTime now = LocalDateTime.now();
        String randomContent = threadName + "." + randomNumber + "." + now;

        File dir = new File(RANDOM_FILES_PATH);
        if (!dir.exists()) {
            dir.mkdirs();
        }
        File newFile = new File(RANDOM_FILES_PATH + "/" + randomContent + ".txt");
        try (BufferedWriter bw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(newFile), StandardCharsets.UTF_8))) {
            for (int i = 0; i < 10000; i++) {
                bw.write(randomContent);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
