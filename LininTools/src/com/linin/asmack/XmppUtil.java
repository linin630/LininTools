package com.linin.asmack;

import org.jivesoftware.smack.Chat;
import org.jivesoftware.smack.ChatManager;
import org.jivesoftware.smack.ChatManagerListener;
import org.jivesoftware.smack.ConnectionConfiguration;
import org.jivesoftware.smack.XMPPConnection;
import org.jivesoftware.smack.XMPPException;

/**
 * xmpp������
 * 
 * @author linin
 * 
 */
public class XmppUtil {

	private static XmppUtil me;

	private String IP = "192.168.1.1";
	private int PORT = 8080;

	private XMPPConnection con = null;
	private ChatManager cm = null;

	private Chat chat;

	private XmppUtil(String ip, int port) {
		// ���õ���ģʽ
		IP = ip;
		PORT = port;
		openConnection();
	}

	/**
	 * ��ʼ��IP�Ͷ˿�
	 * 
	 * @param ip
	 * @param port
	 */
	public static XmppUtil init(String ip, int port) {
		if (me == null) {
			me = new XmppUtil(ip, port);
		}
		return me;
	}

	private void openConnection() {
		try {
			ConnectionConfiguration connConfig = new ConnectionConfiguration(
					IP, PORT);
			con = new XMPPConnection(connConfig);
			con.connect();
			cm = con.getChatManager();
		} catch (XMPPException xe) {
			xe.printStackTrace();
		}
	}

	public XMPPConnection getConnection() {
		if (con == null) {
			openConnection();
		}
		return con;
	}

	public void closeConnection() {
		con.disconnect();
		con = null;
	}

	/**
	 * ��¼xmpp
	 * 
	 * @param USERID
	 * @param PWD
	 */
	public void login(String USERID, String PWD) {
		try {
			con.login(USERID, PWD);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

	/**
	 * ������Ϣ
	 * 
	 * @param url
	 *            test@linin
	 * @param message
	 *            ��������
	 */
	public void send(String url, String message) {
		chat = cm.createChat(url, null);
		cm.addChatListener(new ChatManagerListener() {
			@Override
			public void chatCreated(Chat chat, boolean able) {
				// TODO ���������Ҫ���´���
			}
		});
		try {
			chat.sendMessage(message);
		} catch (XMPPException e) {
			e.printStackTrace();
		}
	}

}
