package jdk11;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.net.http.WebSocket;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionStage;
import java.util.concurrent.TimeUnit;

/**
 * <h2>HttpClient</h2>
 *
 * <p>Java 9开始引入HttpClient API来处理HTTP请求。 从Java 11开始，这个API正式进入标准库包。</p>
 * <p>参考网址 : http://openjdk.java.net/groups/net/httpclient/intro.html </p>
 * <p>
 * HttpClient同时支持 HTTP1.1 和 HTTP2 协议，并支持 websocket;
 * 它同时支持同步和异步编程模型;
 * 它将请求和响应主体作为响应式流(reactive-streams)处理，并使用构建器模式。
 * </p>
 *
 * <h3>HttpClient</h3>
 * <p>要发送http请求，首先要使用其构建器创建一个HttpClient。这个构建器能够配置每个客户端的状态：</p>
 * <ul>
 * <li>首选协议版本 ( HTTP/1.1 或 HTTP/2 )</li>
 * <li>是否跟随重定向</li>
 * <li>代理</li>
 * <li>身份验证</li>
 * </ul>
 * <p>一旦构建完成，就可以使用HttpClient发送多个请求。</p>
 *
 * <h3>HttpRequest</h3>
 * <p>HttpRequest是由它的构建器创建的。请求的构建器可用于设置:</p>
 * <ul>
 * <li>请求URI</li>
 * <li>请求Method ( GET, PUT, POST )</li>
 * <li>请求主体(如果有)</li>
 * <li>超时是时间</li>
 * <li>请求头</li>
 * </ul>
 * <p>HttpRequest构建之后是不可变的，但可以发送多次。</p>
 *
 * <h3>Synchronous or Asynchronous</h3>
 * <p>请求既可以同步发送，也可以异步发送。当然同步的API会导致线程阻塞直到HttpResponse可用。</p>
 * <p>异步API立即返回一个CompletableFuture，当HttpResponse可用时，它将获取HttpResponse并执行后续处理。</p>
 * <p>CompletableFuture是Java 8添加的新特性，用于可组合的异步编程。</p>
 *
 * <h3>Data as reactive-streams</h3>
 * <p>
 * 请求和响应的主体作为响应式流(具有非阻塞背压的异步数据流)供外部使用。
 * HttpClient实际上是请求正文的订阅者和响应正文字节的发布者。
 * BodyHandler接口允许在接收实际响应体之前检查响应代码和报头，并负责创建响应BodySubscriber。
 * </p>
 * <p>
 * HttpRequest和HttpResponse类型提供了许多便利的工厂方法，用于创建请求发布者和响应订阅者，以处理常见的主体类型，如文件、字符串和字节。
 * 这些便利的实现要么累积数据，直到可以创建更高级别的Java类型（如String），要么就文件流传输数据。
 * BodySubscriber和BodyPublisher接口可以实现为自定义反应流处理数据。
 * </p>
 * <p>
 * HttpRequest和HttpResponse还提供了转换器，
 * 用于将 java.util.concurrent.Flow 的 Publisher/Subscriber 类型转换为 HTTP Client的 BodyPublisher/BodySubscriber 类型。
 * </p>
 *
 * <h3>HTTP/2</h3>
 * <p>
 * Java HTTP Client支持 HTTP/1.1 和 HTTP/2。默认情况下，客户端将使用 HTTP/2 发送请求。
 * 发送到尚不支持 HTTP/2 的服务器的请求将自动降级为 HTTP/1.1。
 * 以下是HTTP/2带来的主要改进:
 * </p>
 * <ul>
 * <li>标头压缩。 HTTP/2 使用 HPACK 压缩，从而减少了开销。</li>
 * <li>与服务器的单一连接减少了建立多个TCP连接所需的往返次数。</li>
 * <li>多路复用。 在同一连接上，同时允许多个请求。</li>
 * <li>服务器推送。 可以将其他将来需要的资源发送给客户端。</li>
 * <li>二进制格式。 更紧凑。</li>
 * </ul>
 * <p>由于HTTP/2是默认的首选协议，并且在需要的地方无缝地实现回退到HTTP/1.1，那么当HTTP/2被更广泛地部署时，Java HTTP客户端就可以很好地为将来做好准备了。</p>
 *
 * <h3>References</h3>
 * <a>https://docs.oracle.com/en/java/javase/11/docs/api/java.net.http/java/net/http/package-summary.html</a>
 *
 * @author zhaochun
 */
public class TestCase02HttpClient {
    public static void main(String[] args) throws Exception {
        TestCase02HttpClient me = new TestCase02HttpClient();
//        me.testHttpClientGetSync();
//        me.testHttpClientGetAsync();
//        me.testHttpClientPost();

        // 同一个HttpClient先登录网站获取token，再请求受限制资源，从而爬取需要认证的资源
//        me.testLogin();

        // TODO: 20-5-13 HttpClient支持websocket
        me.testWebsocket();
    }

    private void testHttpClientGetSync() {
        var url = "https://openjdk.java.net/";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        var client = HttpClient.newHttpClient();
        try {
            System.out.println(String.format("send begin at %s", LocalDateTime.now()));
            // 同步请求
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(String.format("send end at %s", LocalDateTime.now()));
            System.out.println(String.format("receive response : %s", response.body().substring(0, 10)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testHttpClientGetAsync() {
        var url = "https://openjdk.java.net/";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .GET()
                .build();
        var client = HttpClient.newHttpClient();
        try {
            System.out.println(String.format("sendAsync begin at %s", LocalDateTime.now()));
            // 异步请求
            client.sendAsync(request, HttpResponse.BodyHandlers.ofString())
                    .thenApply(stringHttpResponse -> {
                        System.out.println(String.format("receive response at %s", LocalDateTime.now()));
                        return stringHttpResponse.body();
                    })
                    .thenAccept(s -> System.out.println(String.format("receive response : %s at %s", s.substring(0, 10), LocalDateTime.now())));
            System.out.println(String.format("sendAsync end at %s", LocalDateTime.now()));

            // 为了防止异步请求尚未返回主线程就结束(jvm会退出)，这里让主线程sleep 10秒
            System.out.println("Main Thread sleep 10 seconds start...");
            Thread.sleep(10000);
            System.out.println("Main Thread sleep 10 seconds stop...");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testHttpClientPost() {
        var url = "http://localhost:30001/jdk11/test/helloByPost";
        var request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .header("Content-Type", "text/plain")
                .POST(HttpRequest.BodyPublishers.ofString("zhangsan"))
                .build();
        var client = HttpClient.newHttpClient();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            System.out.println(response.statusCode());
            System.out.println(response.body());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void testLogin() throws Exception {
        var client = HttpClient.newHttpClient();
        // 测试环境登录URL
        var urlLogin = "http://<host>:<port>/xxx/login";
        var requestObj = new HashMap<String, Object>();
        requestObj.put("username", "xxx");
        requestObj.put("password", "xxxxxxxxxx");
        var objectMapper = new ObjectMapper();
        var requestBodyJson = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(requestObj);
        var requestLogin = HttpRequest.newBuilder()
                .uri(URI.create(urlLogin))
                .header("Content-Type", "application/json;charset=UTF-8")
                .POST(HttpRequest.BodyPublishers.ofString(requestBodyJson))
                .build();
        HttpResponse<String> responseLogin = client.send(requestLogin, HttpResponse.BodyHandlers.ofString());
        // 这里的登录网站使用token，而没有使用session，因此我们需要从返回的报文主体中查找token信息；
        // 如果是使用session的网站，这里需要从响应的headers中查找"set-cookie"从而获取session id，并在后续请求中，将sid设置到header的Cookie中。
        // 如： responseLogin.headers().map().get("set-cookie")获取cookies，再从中查找sid。
        var loginResponse = responseLogin.body();
        var mpLoginResponse = objectMapper.readValue(loginResponse, Map.class);
        var dataLogin = (Map<String, Object>) mpLoginResponse.get("data");
        var token = dataLogin.get("token").toString();
        // 测试环境资源访问URL
        var urlGetResource = "http://<host>:<port>/xxx/xxx";
        var requestRes = HttpRequest.newBuilder()
                .uri(URI.create(urlGetResource))
                .header("Content-Type", "application/json;charset=UTF-8")
                // 注意，token并非一定设置到header的Authorization中，这取决于网站验证的方式，也有可能token也放到cookie里。
                // 但对于使用session的网站，sid都是设置在cookie里的。如: .header("Cookie", "JSESSIONID=" + sid)
                .header("Authorization", token)
                .GET()
                .build();
        HttpResponse<String> responseResource = client.send(requestRes, HttpResponse.BodyHandlers.ofString());
        var response = responseResource.body();
        System.out.println(response);
    }

    private void testWebsocket() {
        var wsUrl = "ws://localhost:30001/ws/test";
        var httpClient = HttpClient.newHttpClient();
        WebSocket websocketClient = httpClient.newWebSocketBuilder()
                .buildAsync(URI.create(wsUrl), new WebSocket.Listener() {
                    @Override
                    public void onOpen(WebSocket webSocket) {
                        System.out.println("onOpen : webSocket opened.");
                        webSocket.request(1);
                    }

                    @Override
                    public CompletionStage<?> onText(WebSocket webSocket, CharSequence data, boolean last) {
                        System.out.println("onText");
                        webSocket.request(1);
                        return CompletableFuture.completedFuture(data)
                                .thenAccept(System.out::println);
                    }

                    @Override
                    public CompletionStage<?> onClose(WebSocket webSocket, int statusCode, String reason) {
                        System.out.println("ws closed with status(" + statusCode + "). cause:" + reason);
                        webSocket.sendClose(statusCode, reason);
                        return null;
                    }

                    @Override
                    public void onError(WebSocket webSocket, Throwable error) {
                        System.out.println("error: " + error.getLocalizedMessage());
                        webSocket.abort();
                    }
                }).join();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // last参数用于指示websocketClient，本次发送的数据是否是完整消息的最后部分。
        // 如果是false，则websocketClient不会把消息发送给websocket后台的listener，只会把数据缓存起来；
        // 当传入true时，会将之前缓存的数据和这次的数据拼接起来一起发送给websocket后台的listener。
        websocketClient.sendText("test1", false);
        websocketClient.sendText("test2", true);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        websocketClient.sendText("org_all_request", true);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        websocketClient.sendText("employee_all_request", true);

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        websocketClient.sendClose(WebSocket.NORMAL_CLOSURE, "Happy ending.");
    }
}
