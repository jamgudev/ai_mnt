package com.hc.core;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;


/**
 * @ServerEndpoint 注解是一个类层次的注解，它的功能主要是将目前的类定义成一个websocket服务器端,
 * 注解的值将被用于监听用户连接的终端访问URL地址,客户端可以通过这个URL来连接到WebSocket服务器端
 */
@SuppressWarnings({"ResultOfMethodCallIgnored", "MismatchedQueryAndUpdateOfCollection", "JavaDoc", "unused"})
@ServerEndpoint("/wb/update")
public class DataRelay {
    //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
    private static int onlineCount = 0;

    //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    // 用来存放所有的用户
    private static CopyOnWriteArraySet<Server> servers = new CopyOnWriteArraySet<>();


    //与某个客户端的连接会话，需要通过它来给客户端发送数据
    private Session session;

    /**
     * 连接建立成功调用的方法
     *
     * @param session 可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
     */
    @OnOpen
    public void onOpen(Session session) {
        this.session = session;
//        webSocketSet.add(this);     //加入set中
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
//        webSocketSet.remove(this);  //从set中删除
        try {
            if (this.session.isOpen()) {
                this.session.close();
                subOnlineCount();           //在线数减1
                System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
            }
            for (Server server :
                    servers) {
                // 是服务
                if (server.dataRelay.equals(this)) {

                    // 当服务端关闭，强制关闭已所有连接的用户
                    for (DataRelay dr :
                            server.clients) {
                        dr.onClose();
                    }

                    server.dataRelay = null;

                    server.clients.clear();
                    servers.remove(server);

                    break;
                } else if (server.clients.contains(this)) {
                    server.clients.remove(this);
                    break;
                }

            }
//            this.session.close();
//            if (isService) {
//            } else {
////            if (clients.contains(this)) clients.remove(this);
////            else {
////                for (DataRelay.Server svr :
////                        servers) {
////                    if (svr.dataRelay == this || svr.dataRelay.equals(this)) {
////                        for (DataRelay d :
////                                svr.clients) {
////                            d.session.close();
////                        }
////                        svr.clients.clear();
////
////                    }
////                }
////            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 收到客户端消息后调用的方法
     * <p>
     * _id      用户身份        String      server/clients
     *
     * @param message 客户端发送过来的消息
     * @param session 可选的参数
     */

    private static List<List<Long>> list = new ArrayList<>();
    private static List<Long> l = new ArrayList<>();
    @OnMessage
    public void onMessage(String message, Session session) {
        // 防止FromObject时发生包自闭现象
        JsonConfig jsonConfig = new JsonConfig();
        jsonConfig.setCycleDetectionStrategy(CycleDetectionStrategy.LENIENT);
        JSONObject jso = JSONObject.fromObject(message);
        String id = (String) jso.get("id");            // 连接身份
        if (!StringUtils.isEmpty(id)) {
            switch (id) {
                case "server":
                    for (Server s:
                         servers) {
                        if (jso.get("place_id").equals(s.placeId)) {
                            list.clear();
                            l.clear();

                            JSONObject msg = (JSONObject) jso.get("msg");
                            if (msg != null) {
                                Integer curentNumber = (Integer) msg.get("crt_number");
                                Long timeMills = (Long) msg.get("time_mills");
                                Integer pullPort = (Integer) msg.get("video_port");      // 浏览器实时视频监听端口

                                JSONObject send = new JSONObject();
                                send.put("crt_number", curentNumber);
                                send.put("video_port", pullPort);

                                l.add(timeMills);
                                l.add(Long.valueOf(curentNumber));
                                list.add(l);
                                send.put("crt_point", JSONArray.fromObject(list));

                                for (DataRelay w :
                                        s.clients) {
                                    try {
                                        if (w.session.isOpen())
                                            w.session.getBasicRemote().sendText(send.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                                return;
                            }

                        }
                    }
                    // 如果没从上面结束，则这个server是第一次访问的
                    servers.add(new Server(this, (String) jso.get("place_id")));
                    // 同时通知浏览器，有实时数据传输了
//                    JSONObject toWeb = new JSONObject();
//                    toWeb.put("status", 200);
//                    for(ListenWebSocket item: ListenWebSocket.clients){
//                        try {
//                            if (item.session.isOpen())
//                                item.sendMessage(toWeb.toString());
//                        } catch (IOException e) {
//                            e.printStackTrace();
//                        }
//                    }
                    onMessage(message, session);
                    break;
                case "client":
                    for (Server s :
                            servers) {
                        if (s.placeId.equals(jso.get("place_id"))) {

                            if (s.clients.add(this)) {
                                onMessage(message, session);
                            } else {
                                // 获取msg对象
                                JSONObject msg = (JSONObject) jso.get("msg");
                                if (msg != null) {
                                    Integer resetNumber = (Integer) msg.get("reset_number");
                                    JSONObject rt = new JSONObject();
                                    rt.put("reset_number", resetNumber);
                                    try {
                                        if (s.dataRelay.session.isOpen())
                                            s.dataRelay.session.getBasicRemote().sendText(rt.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }

                            }
                        }
                    }
                    break;
            }
        }

//        System.out.println("来自客户端的消息:" + message);
//        //群发消息/
//        for (DataRelay item : webSocketSet) {
//            try {
//                item.sendMessage(message);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
    }

    /**
     * 发生错误时调用
     *
     * @param session
     * @param error
     */
    @OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        try {
            if (this.session.isOpen()) {
                session.close();
                subOnlineCount();
                System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        error.printStackTrace();
    }

    /**
     * 这个方法与上面几个方法不一样。没有用注解，是根据自己需要添加的方法。
     *
     * @param message
     * @throws IOException
     */
    private void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);
        //this.session.getAsyncRemote().sendText(message);
    }

    private static synchronized int getOnlineCount() {
        return onlineCount;
    }

    private static synchronized void addOnlineCount() {
        DataRelay.onlineCount++;
    }

    private static synchronized void subOnlineCount() {
        DataRelay.onlineCount--;
    }
    static class Server {
        DataRelay dataRelay;
        String placeId;

        public Server(DataRelay dataRelay, String placeId) {
            this.dataRelay = dataRelay;
            this.placeId = placeId;
        }

        CopyOnWriteArraySet<DataRelay> clients = new CopyOnWriteArraySet<>();

        public CopyOnWriteArraySet getClients() {
            return clients;
        }
    }

}