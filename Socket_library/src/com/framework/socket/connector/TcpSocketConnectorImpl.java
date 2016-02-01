/***********************
 * socket 连接器
 * 
 * 
 * 
 **************/

package com.framework.socket.connector;


import com.framework.socket.TcpSocketServer;
import com.framework.socket.TcpSocketServerImpl;
import com.framework.socket.heartbeat.HeartbeatComponent;
import com.framework.socket.model.TcpSocketConnectorConfig;
import com.framework.socket.model.SocketConfig;
import com.framework.socket.out.TcpSocketCallback;
import com.framework.socket.out.TcpSocketConnectorCallback;
import com.framework.socket.out.Selector;
import com.framework.socket.protocol.Protocol;

import java.util.Timer;
import java.util.TimerTask;


/**
 * 连接服务器 处理
 */
public class TcpSocketConnectorImpl implements TcpSocketConnector{
    protected int mConnectTime = 0;//连接次数
    protected TcpSocketConnectorConfig mTcpSocketConnectorConfig;;
    protected volatile boolean isRun = false;//防止多个线程调用时候出现多次调用连接方法，多线程连接时候,先到先得
    protected Selector mSelector;
    protected TcpSocketConnectorCallback mTcpSocketConnectorCallback;
    protected TcpSocketCallback mTcpSocketCallback;
    
    protected TcpSocketServer mTcpSocketServer;
   
    private Timer mTimer;
    private TimerTask mTimerTask;

    public TcpSocketConnectorImpl(TcpSocketConnectorConfig tcpSocketConnectorConfig,Selector selector,TcpSocketConnectorCallback tcpSocketConnectorCallback,TcpSocketCallback tcpSocketCallback){
    	if(selector == null){
    		throw new RuntimeException("selector null point exception ");
    	}
    	if(tcpSocketConnectorCallback == null){
    		throw new RuntimeException("tcpsocketConnectorcallback null pont exception ");
    	}
    	if(tcpSocketConnectorConfig == null){
    		throw new RuntimeException("tcpSocketConnectorConfig null pont exception ");
    	}
    	this.mTcpSocketConnectorConfig = tcpSocketConnectorConfig; 
    	this.mSelector = selector;
    	this.mTcpSocketConnectorCallback = tcpSocketConnectorCallback;
    	this.mTcpSocketCallback = tcpSocketCallback;
    	
    }

    public void connect(){
        SocketConfig socketConfig = mSelector.getSocketInfo();
        Protocol protocol = mSelector.getProtocol();
        if (this.isRun) {
            return;
        }
        mTcpSocketServer = new TcpSocketServerImpl(socketConfig,protocol,mTcpSocketCallback);
        isRun = true;
        closeTimerTask();
        this.mTimer = new Timer();
        mTimerTask = new TimerTask() {
            public void run() {
                if (mConnectTime++ < mTcpSocketConnectorConfig.getMaxRetrayTime()) {
                    if (mTcpSocketServer.connect()) {
                        closeTimerTask();
                        restore();
                        mTcpSocketServer.recv();
                        mTcpSocketConnectorCallback.connectSuc(mConnectTime);
                    } else {
                        mTcpSocketConnectorCallback.connectFaild(mConnectTime);
                    }

                } else {
                    closeTimerTask();
                    restore();
                    mTcpSocketConnectorCallback.retryOverlimit(mConnectTime);
                }
            }
        };
        this.mTimer.schedule(this.mTimerTask,mTcpSocketConnectorConfig.getLaucherDelay(), mTcpSocketConnectorConfig.getPeriod());
    }


    public void connectWithHeartbeat(final HeartbeatComponent heartbeatComponent){
        if (this.isRun) {
            return;
        }
        if(mSelector == null){
        	return;
        }
        SocketConfig socketConfig = mSelector.getSocketInfo();
        Protocol protocol = mSelector.getProtocol();
        if (this.isRun) {
            return;
        }
        mTcpSocketServer = new TcpSocketServerImpl(socketConfig,protocol,mTcpSocketCallback);
        isRun = true;
        closeTimerTask();
        this.mTimer = new Timer();
        mTimerTask = new TimerTask() {
            public void run() {
                if (mConnectTime++ < mTcpSocketConnectorConfig.getMaxRetrayTime()) {
                    if (mTcpSocketServer.connect()) {
                    	mTcpSocketServer.recv();
                    	heartbeatComponent.doHeartbeat();
                    	mTcpSocketServer.takeCareHeartbeat(heartbeatComponent);
                        closeTimerTask();
                        restore();
                        mTcpSocketConnectorCallback.connectSuc(mConnectTime);
                    } else {
                    	mTcpSocketConnectorCallback.connectFaild(mConnectTime);
                    }

                } else {
                    closeTimerTask();
                    restore();
                    mTcpSocketConnectorCallback.retryOverlimit(mConnectTime);
                }
            }
        };
        this.mTimer.schedule(this.mTimerTask, mTcpSocketConnectorConfig.getLaucherDelay(), mTcpSocketConnectorConfig.getPeriod());
    }

    public void disconnect(){
        closeTimerTask();
        restore();
        if(mTcpSocketServer != null){
        	mTcpSocketServer.disconnect();
        }
    }

    private void closeTimerTask(){
        if (mTimer != null) {
            mTimer.cancel();
            mTimer = null;
        }
        if(mTimerTask != null){
            mTimerTask.cancel();
            mTimerTask = null;
        }
    }

    private void restore(){
        mTimer = null;
        mConnectTime = 0;
        isRun = false;
    }
    
    @Override
    public TcpSocketServer obtainTcpSocketServer(){
    	return mTcpSocketServer;
    }

}
