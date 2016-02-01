/****
 *  用于切换选择socket提供接口 
 *  并且提供获取协议的接口
 */

package com.framework.socket.out;

import com.framework.socket.model.SocketConfig;
import com.framework.socket.protocol.Protocol;

public interface Selector {
	public SocketConfig getSocketInfo();//提供可以动态替换socket地址的选择
	public Protocol getProtocol();//提供协议接口
}
