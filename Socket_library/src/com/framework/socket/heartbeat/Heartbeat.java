/*****
 * 心跳接口（用于操作心跳）
 */
package com.framework.socket.heartbeat;


public interface Heartbeat {
	public void doHeartbeat();// 发送心跳包
	public void doneHeartbeat();// 关闭心跳包
	public int obtainPeriod();// 获取心跳周期
	public byte[] obtainHeartbeatParcel();//获取心跳包

}
