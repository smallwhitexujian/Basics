package com.framework.socket.factory;

import com.framework.socket.TcpSocket;
import com.framework.socket.TcpSocketServer;
import com.framework.socket.connector.TcpSocketConnector;
import com.framework.socket.connector.TcpSocketConnectorImpl;
import com.framework.socket.model.TcpSocketConnectorConfig;
import com.framework.socket.out.Selector;
import com.framework.socket.out.TcpSocketCallback;
import com.framework.socket.out.TcpSocketConnectorCallback;



public class SocketModuleManagerImpl implements SocketModuleManager{
	
	private TcpSocketConnector mTcpSocketConnector;
	
	private Selector mSelector;
	private TcpSocketConnectorCallback mTcpSocketConnectorCallback;
	private TcpSocketConnectorConfig mTcpSocketConnectorConfig;
	private TcpSocketCallback mTcpSocketCallback;
	
	public SocketModuleManagerImpl(TcpSocketConnectorConfig config,Selector selector,TcpSocketConnectorCallback tcpSocketConnectorCallback,TcpSocketCallback tcpSocketCallback) {
		this.mTcpSocketConnectorConfig = config;
		this.mSelector = selector;
		this.mTcpSocketConnectorCallback = tcpSocketConnectorCallback;
		this.mTcpSocketCallback = tcpSocketCallback;
	}
	
	@Override
	public void startSocket() {
		SocketModuleManagerImpl.Builder builder = new SocketModuleManagerImpl.Builder();
		mTcpSocketConnector = builder
				.setTcpConnectorConfig(mTcpSocketConnectorConfig)
				.setSelector(mSelector)
				.setTcpConnectorCallback(mTcpSocketConnectorCallback)
				.setTcpSocketCallback(mTcpSocketCallback)
				.obtainTcpSocketConnector();
		mTcpSocketConnector.connect();
	}

	@Override
	public boolean send(byte[] data) {
		if(mTcpSocketConnector == null){
			return false;
		}
		TcpSocketServer tcpSocketServer = mTcpSocketConnector.obtainTcpSocketServer();
		if(tcpSocketServer == null){
			return false;
		}
		return tcpSocketServer.send(data, 0, data.length);
	}

	@Override
	public void stopSocket() {
		if(mTcpSocketConnector != null){
			mTcpSocketConnector.disconnect();
		}
	}

	@Override
	public int getRunStatus() {
		if(mTcpSocketConnector != null){
			TcpSocketServer tcpSocketServer = mTcpSocketConnector.obtainTcpSocketServer();
			if(tcpSocketServer != null){
				tcpSocketServer.getRunStatus();
			}
		}
		return TcpSocket.CONNECTNULL;
	}
	
	public static class Builder {
		private Selector mSelector;
		private TcpSocketConnectorCallback mTcpSocketConnectorCallback;
		private TcpSocketConnectorConfig mTcpSocketConnectorConfig;
		private TcpSocketCallback mTcpSocketCallback;//业务接口
		
		public Builder() {
			
		}
		
		public Builder setSelector(Selector selector){
			this.mSelector = selector;
			return this;
		}
		
		public Builder setTcpConnectorCallback(TcpSocketConnectorCallback tcpSocketConnectorCallback){
			this.mTcpSocketConnectorCallback = tcpSocketConnectorCallback;
			return this;
		}
		
		public Builder setTcpConnectorConfig(TcpSocketConnectorConfig tcpSocketConnectorConfig){
			this.mTcpSocketConnectorConfig = tcpSocketConnectorConfig;
			return this;
		}
		
		public Builder setTcpSocketCallback(TcpSocketCallback tcpSocketCallback){
			this.mTcpSocketCallback = tcpSocketCallback;
			return this;
		}
		
		public TcpSocketConnector obtainTcpSocketConnector(){
			if(mSelector == null || mTcpSocketConnectorCallback == null || mTcpSocketConnectorConfig == null){
				throw new RuntimeException("selector or tcpsocketconnectorcallback null point exception ");
			}
			TcpSocketConnector tcpSocketConnector = new TcpSocketConnectorImpl(mTcpSocketConnectorConfig,mSelector, mTcpSocketConnectorCallback,mTcpSocketCallback);
			return tcpSocketConnector;
		}
		
	}

}
