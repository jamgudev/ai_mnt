package com.hc.core;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArraySet;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 * Created by GOPENEDD on 2019/5/19
 */
public class ServerListeningThread extends Thread {

    private int bindPort;
    private ServerSocket serverSocket;

    public ServerListeningThread(int port) {
        this.bindPort = port;
    }

    @Override
    public void run() {
        try {
            serverSocket = new ServerSocket(bindPort);
            while (true) {
                Socket rcvSocket = serverSocket.accept();

                //单独写一个类，处理接收的Socket，类的定义在下面
                HttpRequestHandler request = new HttpRequestHandler(rcvSocket);
                request.handle();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

class HttpRequestHandler {
    private Socket socket;

    public HttpRequestHandler(Socket socket) {
        this.socket = socket;
    }

    public void handle() throws IOException {
        StringBuilder builder = new StringBuilder();
        HttpInputStream his = new HttpInputStream(socket.getInputStream());
        char[] chars = new char[1024];
        Map<String, Object> httpHeaders = his.getHttpHeaders();
//        System.out.println(httpHeaders);
        int len = -1;
        byte[] buf = new byte[2048];
        StringBuilder sb = new StringBuilder();
        while((len=his.read(buf, 0,  buf.length)) != -1)
        {
            sb.append(new String(buf, 0, len));
            new WebSocketTest().onMessage(sb.toString(), null);
        }
        if (his.read() == -1) socket.close();

//        int mark = -1;
//        while ((mark = isr.read(chars)) != -1) {
//            builder.append(chars, 0, mark);
//            if (mark < chars.length)
//                break;
//        }
//        String rt = "";
//        Map<String, String> headers = new HashMap<>();
//        Map<String, String> parameters = new HashMap<>();
//        StringBuilder respStr = new StringBuilder("头部信息\r\n");
//        if (mark == -1) respStr.append("无数据");
//        else {
//
//            String[] splits = builder.toString().split("\r\n");
//            if (splits.length > 0) {
//                int index = 1;
//
//                //处理header
//                while (index < splits.length && splits[index].length() > 0) {
//                    String[] keyVal = splits[index].split(":");
//                    headers.put(keyVal[0], keyVal[1].trim());
//                    index++;
//                }
//                String body = splits[index + 1];
//                String[] bodySplits = body.split("&");
//
//                //处理body的参数
//                for (String str : bodySplits) {
//                    String[] param = str.split("=");
//                    parameters.put(param[0], param[1]);
//                }
//
//                for (Map.Entry<String, String> entry : headers.entrySet()) {
//                    respStr.append("名称: " + entry.getKey() + ", 值: " + entry.getValue() + "<br/>");
//                }
//
//                respStr.append("\r\nbody信息\r\n");
//                for (Map.Entry<String, String> entry : parameters.entrySet()) {
//                    respStr.append("名称: " + entry.getKey() + ", 值: " + entry.getValue() + "<br/>");
//                }
//            }
//
//        }
//        socket.getOutputStream().
//                write(("HTTP/1.1 200 OK\r\n" +
//                        "Content-Type: text/html; charset=utf-8\r\n" +
//                        "\r\n" +
//                        "<h1>这是响应报文</h1>\r\n" + respStr).getBytes());
    }

//    private static int getChunkSize(InputStream is) throws IOE
//        return 0;

    @ServerEndpoint("/relay")
    public static class WebSocketTest {
        //静态变量，用来记录当前在线连接数。应该把它设计成线程安全的。
        private static int onlineCount = 0;

        //concurrent包的线程安全Set，用来存放每个客户端对应的MyWebSocket对象。若要实现服务端与单一客户端通信的话，可以使用Map来存放，其中Key可以为用户标识
        private static CopyOnWriteArraySet<WebSocketTest> webSocketSet = new CopyOnWriteArraySet<>();

        //与某个客户端的连接会话，需要通过它来给客户端发送数据
        private Session session;

        /**
         * 连接建立成功调用的方法
         * @param session  可选的参数。session为与某个客户端的连接会话，需要通过它来给客户端发送数据
         */
        @OnOpen
        public void onOpen(Session session){
            this.session = session;
            webSocketSet.add(this);     //加入set中
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
        public void onMessage(String message, Session session) {
            //群发消息
            for(WebSocketTest item: webSocketSet){
                try {
                    item.sendMessage(message);
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
            onlineCount++;
        }

        public static synchronized void subOnlineCount() {
            onlineCount--;
        }
    }

}
