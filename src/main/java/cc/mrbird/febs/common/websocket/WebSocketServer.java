package cc.mrbird.febs.common.websocket;

import cc.mrbird.febs.common.exception.FebsException;
import cc.mrbird.febs.common.i18n.MessageUtils;
import com.alibaba.fastjson.JSONObject;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.websocket.*;
import javax.websocket.server.PathParam;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.util.concurrent.ConcurrentHashMap;

@ServerEndpoint("/websocket/{userId}")
@Component
public class WebSocketServer {
    static Logger logger = LoggerFactory.getLogger(WebSocketServer.class);
    /**
     * @ServerEndpoint(value= "/${websocket.service}/{userId}")
     * 静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
     */
    private static int onlineCount = 0;
    /**
     * concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。
     */
    private static ConcurrentHashMap<String, WebSocketServer> webSocketMap = new ConcurrentHashMap<>();
    /**
     * 与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    private Session session;
    /**
     * 接收userId
     */
    private String userId = "";

    /**
     * 连接建立成功调用的方法
     */
    @OnOpen
    public void onOpen(Session session, @PathParam("userId") String userId) {
        logger.info("生产环境的websocket配置 " + this.toString());
        this.session = session;
        this.userId = userId;
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            webSocketMap.put(userId, this);
            //加入set中
        } else {
            //加入set中
            webSocketMap.put(userId, this);
            //在线数加1
            addOnlineCount();
        }

        logger.info("连接用户的id:" + userId + ",当前在线人数为:" + getOnlineCount());

        try {
//            sendMessage("连接成功");
            sendInfo(1, MessageUtils.getMessage("notice.connectSuccess"), userId);
        } catch (Exception e) {
            logger.error("用户:" + userId + ",网络异常!!!!!!");
        }
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        if (webSocketMap.containsKey(userId)) {
            webSocketMap.remove(userId);
            //从set中删除
            subOnlineCount();
        }
        logger.info("用户退出:" + userId + ",当前在线人数为:" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     *
     * @param message 客户端发送过来的消息
     */
    @OnMessage
    public void onMessage(String message, Session session) {
//        logger.info("用户消息:" + userId + ",报文:" + message);
        //消息保存到数据库、redis

    }

    /**
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        logger.error("用户错误:" + this.userId + ",原因:" + error.getMessage());
    }

    /**
     * 实现服务器主动推送
     */
    public void sendMessage(String message) throws IOException {
        logger.info("websocket session = " + session.toString());
        synchronized (session) {
            this.session.getBasicRemote().sendText(message);
        }
    }

    /**
     * 发送自定义消息
     * 1 发送成功
     * 2 全部阅读
     * 3 付款超时报警
     * 4 闭环超时报警
     */
    public static void sendInfo(int type, String message, String userId)  {
        logger.info("发送消息到:" + userId + "，报文:" + message);
        JSONObject jsonObject = new JSONObject();
        jsonObject.put("type", type);
        jsonObject.put("data", message);
        if (StringUtils.isNotBlank(userId) && webSocketMap.containsKey(userId)) {
            try {
                webSocketMap.get(userId).sendMessage(jsonObject.toJSONString());
            } catch (IOException e) {
                throw new FebsException("websocket发送消息给前端失败，原因是：" + e.getMessage());
            }
        } else {
            logger.error("用户" + userId + ",不在线！");
        }
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        WebSocketServer.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        WebSocketServer.onlineCount--;
    }

}
