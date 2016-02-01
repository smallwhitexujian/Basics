package com.framework.socket.out;

//socket  业务回调
public interface TcpSocketCallback {
	// 业务接口
	public void onReceiveParcel(byte[] receive);// 收到包时候回调
	public void onLostConnect();// 丢失链接时候回调
	public void onReadTaskFinish();// 读线程正常在没有丢失连接退出后的回调

}