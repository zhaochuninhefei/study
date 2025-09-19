package com.czhao.test.jdk11.websocket;

import com.czhao.test.jdk11.db.po.TbEmployee;
import com.czhao.test.jdk11.db.po.TbOrg;
import com.czhao.test.jdk11.service.Jdk11TestService;
import com.czhao.test.jdk11.utils.SpringUtil;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * Websocket测试监听器
 *
 * @author zhaochun
 */
@Component
@ServerEndpoint(value = "/ws/test")
@Slf4j
public class WebsocketTestListener {

    /**
     * 在线连接数
     */
    private static AtomicInteger onlineCount = new AtomicInteger(0);

    /**
     * Websocket测试监听器集合
     */
    private static CopyOnWriteArraySet<WebsocketTestListener> listenerSet = new CopyOnWriteArraySet<>();

    /**
     * 客户端连接会话
     */
    private Session session;

    /**
     * 手动注入业务处理bean 因为需要动态注入，不能利用@Autowired等注解
     */
    private Jdk11TestService jdk11TestService = SpringUtil.getBean(Jdk11TestService.class);

    /**
     * 连接建立成功处理
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
        // 加入 Websocket测试监听器集合
        listenerSet.add(this);
        // 在线数加1
        onlineCount.incrementAndGet();
        log.debug("WebsocketTestListener 新增了一个连接！ 当前在线连接数为: " + getOnlineCount());
        try {
            // 取得当前Svnserv的状态
            sendMessage("status", "连接成功");
        } catch (IOException e) {
            log.error("WebsocketTestListener IO异常", e);
        }
    }

    /**
     * 连接关闭处理
     */
    @OnClose
    public void onClose() {
        // 从Websocket测试监听器集合中删除
        listenerSet.remove(this);
        // 在线数减1
        onlineCount.decrementAndGet();
        log.debug("WebsocketTestListener 移除了一个连接！ 当前在线连接数为: " + getOnlineCount());
    }

    /**
     * 收到客户端消息处理
     *
     * @param message
     *            客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
        log.debug("WebsocketTestListener 收到来自客户端的消息:" + message);

        try {
            ObjectMapper objectMapper = new ObjectMapper();
            switch (message) {
                case "org_all_request":
                    List<TbOrg> orgList = jdk11TestService.getAllOrgs();
                    sendInfo("org_all_response", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(orgList));
                    break;

                case "employee_all_request":
                    List<TbEmployee> employeeList = jdk11TestService.getAllEmployees();
                    sendInfo("employee_all_response", objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(employeeList));
                    break;

                default:
                    break;
            }
        } catch (JsonProcessingException e) {
            log.error("数据序列化发生异常", e);
        }

    }

    /**
     * 发生错误时调用
     */
    @OnError
    public void onError(Session session, Throwable error) {
        log.error("WebsocketTestListener 发生错误", error);
    }

    private void sendMessage(String key, String message) throws IOException {
        this.session.getBasicRemote().sendText(key + ":" + message);
        // this.session.getAsyncRemote().sendText(message);
    }

    /**
     * 群发自定义消息
     */
    public static void sendInfo(String key, String message) {
        for (var item : listenerSet) {
            try {
                item.sendMessage(key, message);
            } catch (IOException e) {
                log.error("SvnservListener.sendInfo 发生错误", e);
            }
        }
    }

    private static AtomicInteger getOnlineCount() {
        return onlineCount;
    }
}
