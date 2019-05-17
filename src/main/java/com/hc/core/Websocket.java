package com.hc.core;

import java.io.IOException;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

@ServerEndpoint(value="/ws/websk")
public class Websocket {
	
	private Session session;
	
	public Websocket() {
		System.out.println("WebsocketTest..");
	}

	@OnOpen
	public void onOpen(Session session) {
		System.out.println("连接成功");
		try {
			session.getBasicRemote().sendText("hello client...");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnClose
	public void onClose(Session session) {
		System.out.println("close....");

	}

	@OnMessage
	public void onSend(Session session, String msg) {
		try {
			session.getBasicRemote().sendText("client" + session.getId() + "say:" + msg);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@OnError
    public void onError(Session session, Throwable error) {
        System.out.println("发生错误");
        error.printStackTrace();
    }

    public void sendMessage(String message) throws IOException {
        this.session.getBasicRemote().sendText(message);//同步
        //this.session.getAsyncRemote().sendText(message);//异步
    }

	
}