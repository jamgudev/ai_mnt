package com.hc.websk;

import com.hc.utils.RunnableUtil;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;
import net.sf.json.JsonConfig;
import net.sf.json.util.CycleDetectionStrategy;

import org.apache.commons.lang.StringUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
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

    //concurrent包的线程安全List，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
    // 用来存放所有的用户
    private static CopyOnWriteArrayList<Server> servers = new CopyOnWriteArrayList<>();
    private static CopyOnWriteArrayList<DataRelay> listeners = new CopyOnWriteArrayList<>();
    private boolean hasEverPerformed = true;
    // 用于定时向Listener发送提醒
    private RunnableUtil runnableUtil;

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
        addOnlineCount();           //在线数加1
        System.out.println("有新连接加入！当前在线人数为" + getOnlineCount());
    }

    /**
     * 连接关闭调用的方法
     */
    @OnClose
    public void onClose() {
        try {
            subOnlineCount();           //在线数减1
            System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
            if (this.session.isOpen()) {
                this.session.close();
            }
            // 关闭线程，释放资源
            if (this.runnableUtil != null) {
                this.runnableUtil.cancel();
            }
            if (listeners.contains(this)) {
                listeners.remove(this);
                stopThread(false, null);
            } else {
                for (Server server :
                        servers) {
                    // 是服务
                    if (server.dataRelay.equals(this)) {

                        // 当服务端关闭，强制关闭所有已连接的用户
                        for (DataRelay dr :
                                server.clients) {
                            dr.onClose();
                            subOnlineCount();
                            System.out.println("有一连接关闭！当前在线人数为" + getOnlineCount());
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
            }
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

    @SuppressWarnings({"WrapperTypeMayBePrimitive", "Duplicates"})
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
                    for (final Server s:
                         servers) {
                        if (jso.get("place_id").equals(s.placeId)) {

                            List<List<Long>> list = new ArrayList<>();
                            List<Long> tmp = new ArrayList<>();
                            JSONObject msg = (JSONObject) jso.get("msg");
                            System.out.println(msg);

                            if (msg != null) {
                                Long curentNumber = Long.parseLong(msg.get("crt_number").toString());
                                Integer dtId = Integer.parseInt(msg.get("dt_id").toString());
                                Long timeMills = Long.parseLong(msg.get("time_mills").toString());
                                Long maxNumber = Long.parseLong(msg.get("max_number").toString());
                                Long setNumber = Long.parseLong(msg.get("set_number").toString());
                                Integer pullPort = Integer.parseInt(msg.get("video_port").toString());      // 浏览器实时视频监听端口

                                JSONObject toClient = new JSONObject();
                                toClient.put("crt_number", curentNumber);
                                toClient.put("max_number", maxNumber);
                                toClient.put("set_number", setNumber);

                                tmp.add(timeMills);
                                tmp.add(curentNumber);
                                list.add(tmp);
                                toClient.put("crt_point", JSONArray.fromObject(list));

                                try {
                                    // 若无client监听，先通知listener, 让listener跳转至client
                                    final JSONObject toListener = new JSONObject();
                                    toListener.put("place_id", jso.get("place_id"));
                                    toListener.put("video_port", pullPort);
                                    toListener.put("dt_id", dtId);
                                    // 有clients则说明listeners已被通知到
                                    if (!s.clients.isEmpty()) {
                                        // 向其clients发送
                                        for (DataRelay w :
                                                s.clients) {
                                            if (w.session.isOpen())
                                                w.session.getBasicRemote().sendText(toClient.toString());
                                        }
                                        // 停止这个地点Server的通知线程
                                        stopThread(true, s);
                                    } else if (listeners.size() != 0) {
                                        if (s.dataRelay.runnableUtil == null || s.dataRelay.runnableUtil.cancelled) {
                                            // 开启通知线程
                                            s.dataRelay.runnableUtil = new RunnableUtil() {
                                                @Override
                                                public void run() {
                                                    while (! cancelled) {
                                                        toListener.put("has_performed", s.dataRelay.hasEverPerformed);
                                                        for (final DataRelay dr :
                                                                listeners) {
                                                            try {
                                                                if (dr.session.isOpen())
                                                                    dr.session.getBasicRemote().sendText(toListener.toString());
                                                            } catch (IOException e) {
                                                                e.printStackTrace();
                                                            }
                                                        }
                                                        s.dataRelay.hasEverPerformed = false;
                                                        try {
                                                            Thread.sleep(8 * 1000);
                                                        } catch (InterruptedException e) {
                                                            e.printStackTrace();
                                                        }
                                                    }
                                                }
                                            };
                                            Thread thread = new Thread(runnableUtil);
                                            thread.start();
                                        }
                                    }
                                } catch (IOException e) {
                                    System.out.println("消息分发失败");
                                }
                                return;
                            } else if (jso.get("info") != null) {
                                JSONObject info = (JSONObject) jso.get("info");
                                Integer status = (Integer) info.get("status");
                                String clientAddress = (String) info.get("from_client");
                                String listenerAddress = (String) info.get("from_listener");
                                System.out.println("info ---> :" + info.toString());
                                if (!StringUtils.isEmpty(clientAddress) && !s.clients.isEmpty()) {
                                    if (status.equals(200)) {
                                        info.put("msg", "地点 -" + s.placeId + "- 阈值修改成功");
                                    } else {
                                        info.put("msg", "地点 -" + s.placeId + "- 阈值修改失败，请重试");
                                    }
                                    // 发送到指定的client
                                    for (DataRelay client :
                                            s.clients) {
                                        if (client.toString().equals(clientAddress) && client.session.isOpen()) {
                                            try {
                                                info.remove("from_client");
                                                info.remove("from_listener");
                                                client.session.getBasicRemote().sendText(info.toString());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                } else if (!StringUtils.isEmpty(listenerAddress) && !listeners.isEmpty()) {
                                    for (DataRelay listener :
                                            listeners) {
                                        if (listener.toString().equals(listenerAddress) && listener.session.isOpen()) {
                                            try {
                                                info.remove("from_client");
                                                info.remove("from_listener");
                                                listener.session.getBasicRemote().sendText(info.toString());
                                            } catch (IOException e) {
                                                e.printStackTrace();
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    // 如果没从上面结束，则这个server是第一次访问的
                    servers.add(new Server(this, jso.get("place_id").toString()));
                    onMessage(message, session);
                    break;
                case "client":
                    if (servers.size() == 0) {
                        try {
                            this.session.getBasicRemote().sendText("当前无任何Server提供数据，无法加入连接");
                            this.session.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        return;
                    }
                    for (Server s :
                            servers) {
                        if (s.placeId.equals(jso.get("place_id"))) {

                            if (s.clients.add(this)) {
                                onMessage(message, session);
                            } else {
                                // 获取msg对象
                                JSONObject msg = (JSONObject) jso.get("msg");
                                if (msg != null) {
                                    Integer resetNumber = Integer.parseInt(msg.get("reset_number").toString());
                                    JSONObject rt = new JSONObject();
                                    rt.put("reset_number", resetNumber);
                                    rt.put("from_client", this.toString());
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
                case "listener":
                    if (!listeners.contains(this)){
                        listeners.add(this);
                        stopThread(false, null);
                    } else {
                        // 从主页修改阈值
                        JSONObject toServer = (JSONObject) jso.get("toServer");
                        if (toServer != null) {
                            String placeName = (String) toServer.get("place_id");
                            for (Server s :
                                    servers) {
                                if (s.placeId.equals(placeName)) {
                                    toServer.remove("place_id");
                                    toServer.put("from_listener", this.toString());
                                    try {
                                        s.dataRelay.session.getBasicRemote().sendText(toServer.toString());
                                    } catch (IOException e) {
                                        e.printStackTrace();
                                    }
                                }
                            }
                        }
                    }
                    // listeners的变化需同步到线程中，以保证新进的listeners能被通知到
                    // 通过设置状态位，关闭每个地点Server开启的线程
                    break;
            }
        }
    }

    private void stopThread(boolean closeSingle, Server server){
        if (closeSingle && server.dataRelay.runnableUtil != null) {
            server.dataRelay.runnableUtil.cancel();
            server.dataRelay.runnableUtil = null;
        } else {
            for (Server s:
                    servers) {
                if (s.dataRelay.runnableUtil != null) {
                    s.dataRelay.runnableUtil.cancel();
                    s.dataRelay.runnableUtil = null;
                }
            }
        }
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
    private class Server {
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