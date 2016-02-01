package com.framework.socket;

import com.framework.socket.heartbeat.Heartbeat;


public interface TcpSocket {

	public static final int CONNECTNULL = -1;// 原始状态
	public static final int CONNECTLOST = -2;// 丢失连接
	public static final int CONNECTFAILD = 0;// 连接失败
	public static final int CONNECTINIT = 1;// 初始化中....
	public static final int CONNECTING = 2;// 连接中
	public static final int CONNECTED = 3;// 连接成功
	public static final int CONNECTCLOSE = 4;

	
	public boolean connect();//socket连接	
	public int getRunStatus();
	public void onLostConnect();
	public void recv();//
	public boolean send(final byte[] src,final int start,final int len);
	public void takeCareHeartbeat(Heartbeat heartbeat);//接收心跳
	
	public void disconnect();
}
