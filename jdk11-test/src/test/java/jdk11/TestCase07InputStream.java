package jdk11;

import java.io.*;

/**
 * InputStream增强
 *
 * @author zhaochun
 */
public class TestCase07InputStream {
    public static void main(String[] args) {
        TestCase07InputStream me = new TestCase07InputStream();
        me.test01_transferTo();
    }

    private void test01_transferTo() {
        var filePath = "/home/work/sources/test/jdk11-test/src/main/resources/application.yml";
        var tmpFilePath = "/home/work/sources/test/jdk11-test/src/main/resources/application.yml.bk";

        File tmpFile = new File(tmpFilePath);
        if (tmpFile.exists() && tmpFile.isFile()) {
            tmpFile.delete();
        }

        try(InputStream inputStream = new FileInputStream(filePath);
            OutputStream outputStream = new FileOutputStream(tmpFilePath)) {
            // transferTo将 InputStream 的数据直接传输给 OutputStream
            inputStream.transferTo(outputStream);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
