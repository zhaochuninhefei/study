package com.czhao.test.jdk18;

import com.sun.net.httpserver.SimpleFileServer;

import java.net.InetSocketAddress;
import java.nio.file.Path;

/**
 * @author zhaochun
 */
public class TestSimpleWebServer {


    /*
        启动简易网络服务器:
        $ cd ${JDK21_HOME}/bin
        $ ./jwebserver
        $ 默认情况下绑定到环回。如果要表示所有接口，请使用 "-b 0.0.0.0" 或 "-b ::"。
        $ 为 127.0.0.1 端口 8000 上的 /usr/java/jdk-21.0.1+12/bin 及子目录提供服务
        $ URL http://127.0.0.1:8000/

        默认情况下，服务器在前台运行并绑定到回环地址和端口8000。可以使用`-b`和`-p`选项进行更改。例如，要在端口9000上运行服务器，请使用：
        $ jwebserver -p 9000

        访问URL: http://localhost:9000/
     */


    public static void main(String[] args) {
        TestSimpleWebServer me = new TestSimpleWebServer();
        me.startSimpleServer();
    }

    private void startSimpleServer() {
        var server = SimpleFileServer.createFileServer(new InetSocketAddress(9000),
                Path.of("/home/zhaochun/work/sources/github.com/zhaochuninhefei/study/jdk21-test/resource/simpleWeb/files"),
                SimpleFileServer.OutputLevel.VERBOSE);
        server.start();

        // 启动后可访问: http://localhost:9000/index.html
    }
}
