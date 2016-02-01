/***********************
 * 提供Tcp 的服务
 * 
 * 服务包括
 * 		1、连接tcp socket
 * 		2、管理心跳
 * 
 * 
 ***********************/


package com.framework.socket;

import com.framework.socket.heartbeat.Heartbeat;


public interface TcpSocketServer {
	public boolean connect();//socket连接	
	public void recv();//
	public boolean send(final byte[] src,final int start,final int len);
	public boolean send(final byte[] parcel);
	public void takeCareHeartbeat(Heartbeat heartbeat);
	public int getRunStatus();
	public void disconnect();
	
}
