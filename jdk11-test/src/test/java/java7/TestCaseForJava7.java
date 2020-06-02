package java7;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaochun
 */
public class TestCaseForJava7 {
    public static void main(String[] args) {
        TestCaseForJava7 me = new TestCaseForJava7();
        System.out.println(me.test01_switch("山中送别"));
        me.test02_generic();
        me.test03_autoclose();
        me.test04_mutiException(0);
        me.test05_number();
        me.test06_newIO2();
    }

    private String test01_switch(String title) {
        switch (title) {
            case "鹿柴":
                return "空山不见人，但闻人语响。返景入深林，复照青苔上。";
            case "山中送别":
                return "山中相送罢，日暮掩柴扉。春草明年绿，王孙归不归。";
            case "渭城曲":
                return "渭城朝雨浥轻尘，客舍青青柳色新。劝君更尽一杯酒，西出阳关无故人。";
            default:
                return "";
        }
    }

    private void test02_generic() {
        List<String> tempList = new ArrayList<>();
        tempList.add("空山新雨后");
        tempList.add("天气晚来秋");
        tempList.add("明月松间照");
        tempList.add("清泉石上流");
        System.out.println(tempList.toString());
    }

    private void test03_autoclose() {
        String filePath = "/home/work/sources/jdk11-test/src/test/java/java7/TestCaseForJava7.java";
        try (FileInputStream fis = new FileInputStream(filePath);
             InputStreamReader isr = new InputStreamReader(fis, StandardCharsets.UTF_8);
             BufferedReader br = new BufferedReader(isr)) {
            String line;
            while ((line = br.readLine()) != null) {
                System.out.println(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void test04_mutiException(int n) {
        try {
            if (n < 0) {
                throw new FileNotFoundException();
            }
            if (n > 0) {
                throw new SQLException();
            }
            System.out.println("No Exceptions.");
        } catch (FileNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    private void test05_number() {
        int num1 = 1_000_000;
        System.out.println(num1);

        int num2 = 0b11;
        System.out.println(num2);
    }

    private void test06_newIO2() {
        Path path = Paths.get("/home/zhaochun/test");
        System.out.printf("Number of nodes: %s %n", path.getNameCount());
        System.out.printf("File name: %s %n", path.getFileName());
        System.out.printf("File root: %s %n", path.getRoot());
        System.out.printf("File parent: %s %n", path.getParent());

        try {
            Files.deleteIfExists(path);
            Files.createDirectory(path);
            watchFile(path);
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void watchFile(Path path) throws IOException, InterruptedException {
        WatchService service = FileSystems.getDefault().newWatchService();
        Path pathAbs = path.toAbsolutePath();
        pathAbs.register(service,
                StandardWatchEventKinds.ENTRY_CREATE,
                StandardWatchEventKinds.ENTRY_MODIFY,
                StandardWatchEventKinds.ENTRY_DELETE);
        while (true) {
            WatchKey key = service.take();
            for (WatchEvent<?> event : key.pollEvents()) {
                String fileName = event.context().toString();
                String kind = event.kind().name();

                System.out.println(String.format("%s : %s", fileName, kind));
                if ("end".equals(fileName) && "ENTRY_DELETE".equals(kind)) {
                    return;
                }
            }
            key.reset();
        }
    }
}
