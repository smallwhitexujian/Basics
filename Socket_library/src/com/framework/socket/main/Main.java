package com.framework.socket.main;

import java.util.concurrent.TimeUnit;

import com.framework.socket.factory.SocketModuleManager;
import com.framework.socket.factory.SocketModuleManagerImpl;
import com.framework.socket.model.SocketConfig;
import com.framework.socket.model.TcpSocketConnectorConfig;
import com.framework.socket.out.Selector;
import com.framework.socket.out.TcpSocketCallback;
import com.framework.socket.out.TcpSocketConnectorCallback;
import com.framework.socket.protocol.Protocol;
import com.framework.socket.thread.ThreadPool;
import com.framework.socket.util.ByteUtil;

public class Main {

	
	
	private static SocketModuleManager socketModuleManager;
	private static boolean isFinish = false;
	
	public static void main(String[] args) {
		Selector mSelector = new Selector() {
			
			@Override
			public SocketConfig getSocketInfo() {
				SocketConfig socketConfig = new SocketConfig();
				socketConfig.setHost("192.168.199.190");
				socketConfig.setPort(60000);
				return socketConfig;
			}
			
			@Override
			public Protocol getProtocol() {
				Protocol protocol = new WillProtocol();
				return protocol;
			}
		};
		TcpSocketConnectorCallback mTcpSocketConnectorCallback = new TcpSocketConnectorCallback() {
			
			@Override
			public void retryOverlimit(int connectTime) {
				
			}
			
			@Override
			public void connectSuc(int connectTime) {
				
			}
			
			@Override
			public void connectFaild(int connectTime) {
				
			}
		};
		
		TcpSocketCallback tcpSocketCallback = new TcpSocketCallback() {
			
			@Override
			public void onReceiveParcel(byte[] receive) {
				System.out.println("----"+ByteUtil.bytes2Hex(receive));
			}
			
			@Override
			public void onReadTaskFinish() {
				
			}
			
			@Override
			public void onLostConnect() {
				System.out.println("jjfly onLost");
				if(socketModuleManager != null){
					socketModuleManager.stopSocket();
					socketModuleManager = null;
					isFinish = true;
				}
			}
		};
		
		TcpSocketConnectorConfig config = new TcpSocketConnectorConfig();
		socketModuleManager = new SocketModuleManagerImpl(config, mSelector,mTcpSocketConnectorCallback, tcpSocketCallback);
		socketModuleManager.startSocket();
//		try {
//			// awaitTermination返回false即超时会继续循环，返回true即线程池中的线程执行完成主线程跳出循环往下执行，每隔10秒循环一次
//			while (!ThreadPool.getInstance().getThreadPool().awaitTermination(10, TimeUnit.SECONDS));
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}
		while(!isFinish){
			try {
				Thread.sleep(100);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
		System.out.println("finish----------------------------------");
		
		
	}
	
	
}
