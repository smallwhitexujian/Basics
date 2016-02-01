package com.framework.socket.out;


public interface TcpSocketConnectorCallback {
	public void retryOverlimit(int connectTime);// 连接超过次数用于提示统计
	public void connectFaild(int connectTime);// 连接失败
	public void connectSuc(int connectTime);// 连接成功
}