package com.framework.socket.connector;

import com.framework.socket.TcpSocketServer;
import com.framework.socket.heartbeat.HeartbeatComponent;

public interface TcpSocketConnector {
	
	 public void connect();
	 public void connectWithHeartbeat(final HeartbeatComponent heartbeatComponent);
	 public TcpSocketServer obtainTcpSocketServer();
	 public void disconnect();
	 

}
