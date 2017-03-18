package com.baturu.util.webSocket;

import com.baturu.util.kafka.bean.Message;
import com.baturu.util.kafka.constants.TopicType;
import com.baturu.util.kafka.producer.Producer;
import com.google.common.collect.Maps;

import javax.servlet.http.HttpSession;
import javax.websocket.*;
import javax.websocket.server.ServerEndpoint;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

//该注解用来指定一个URI，客户端可以通过这个URI来连接到WebSocket。类似Servlet的注解mapping。无需在web.xml中配置。
@ServerEndpoint(value="/websocket/chat", configurator = GetHttpSessionConfigurator.class)
public class MyWebSocket {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    private static CopyOnWriteArraySet<MyWebSocket> webSocketSet = new CopyOnWriteArraySet<MyWebSocket>();
    private static Map<String, MyWebSocket> sessionIdWebSocketMap = Maps.newHashMap();
    private static Map<Session, String> sessionSessionIdMap = Maps.newHashMap();

    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session, EndpointConfig config){
        this.session = session;
        HttpSession httpSession = (HttpSession) config.getUserProperties().get(HttpSession.class.getName());
        webSocketSet.add(this);     //加入set中
        sessionIdWebSocketMap.put(httpSession.getId(), this);
        sessionSessionIdMap.put(session, httpSession.getId());
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose(){
        webSocketSet.remove(this);  //从set中删除
        subOnlineCount();           //在线数减1
        System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
    }

    /**
     * 收到客户端消息后调用的方法
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */
    @OnMessage
    public void onMessage(Session session, String message) {
        System.out.println("来自客户端的消息:" + message);
        String sessionId = sessionSessionIdMap.get(session);
        new Producer().send(Message.builder()
                .createTime(System.currentTimeMillis())
                .id(new Date().toString())
                .senderSessionId(sessionId)
                .type(TopicType.TEST)
                .content(message).build());
/*        //群发消息
        for(MyWebSocket item: webSocketSet){
            try {
                item.sendMessage(message + new Date());
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }*/
    }

    public static final void sendMsg(Message message) {
        //群发消息
        DateTimeFormatter f = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
        String time = LocalDateTime.now().format(f).toString();
        for(String sessionId : sessionIdWebSocketMap.keySet()){
            try {
                if (message.getSenderSessionId().equals(sessionId)) {
                    sessionIdWebSocketMap.get(sessionId).sendMessage(message.getSenderSessionId() + "说:" + message.getContent() + "(" + time + ")");
                } else {
                    sessionIdWebSocketMap.get(sessionId).sendMessage(message.getSenderSessionId() + "说:" + message.getContent() + "(" + time + ")");
                }
            } catch (IOException e) {
                e.printStackTrace();
                continue;
            }
        }
    }

    /**
     * 发生错误时调用
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error){
        System.out.println("发生错误");
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     * @param message
     * @throws IOException
     */
    public void sendMessage(String message) throws IOException{
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    public static synchronized int getOnlineCount() {
        return onlineCount;
    }

    public static synchronized void addOnlineCount() {
        MyWebSocket.onlineCount++;
    }

    public static synchronized void subOnlineCount() {
        MyWebSocket.onlineCount--;
    }
}
