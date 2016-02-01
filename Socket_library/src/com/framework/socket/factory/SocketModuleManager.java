package com.framework.socket.factory;


/**
 *
 *
 */
public interface SocketModuleManager {
    public void startSocket();
    //发送
    public boolean send(byte[] data);
    public void stopSocket();
    public int getRunStatus();


}
