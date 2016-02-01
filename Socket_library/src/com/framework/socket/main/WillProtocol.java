package com.framework.socket.main;




import java.util.HashMap;
import java.util.Map;

import com.framework.socket.protocol.Protocol;
import com.framework.socket.util.ByteUtil;


/**
 * Created by Administrator on 2015/8/21 0021.
 * 协议 拼接包
 */
public class WillProtocol extends Protocol {
    public static final String TAG = "WillProtocol";
    public static final String KEY_PROTOCOL_LEN = TAG + "_len";
    public static final String KEY_PROTOCOL_TYPE = TAG + "_type";
    public static final String KEY_PROTOCOL_DATA = TAG + "_data";

    public static final int PACK_LEN = 4;
    public static final int PACK_TYPE_LEN = 4;

    //
    public static final int ENTER_VOICEROOM_TYPE_VALUE = 10001;

    public static final int INOUT_NOTICE_TYPE_VALUE = 10007;//进出bar通知
    public static final int PUBLIC_NOTICE_TYPE_VALUE = 10019;//全局喇叭


    public static final int SEARCH_NOTICE_TYPE_VALUE = 10026;//查询房间公告
    public static final int NOTICE_TYPE_VALUE = 10027;///发送语音消息/设置公告
    public static final int DELETE_MSG_TYPE_VALUE = 10028;//删除消息
    public static final int QUIT_VOICEROOM_TYPE_VALUE = 10002;//推出房间

    public static final int HANG_UP = -100;
    public static final int HAVE_NEW_DATAS = -200;


    /**
     * 解包 返回包解析
     * @param pack
     * @return
     */
    public Map<String, byte[]> parseWillPackage(byte[] pack) {
        int pack_data_len;
        //四字节长度+四字节操作码
        if (pack == null || pack.length < PACK_LEN + PACK_TYPE_LEN) {
            return null;
        }
        Map<String, byte[]> protocolMap = new HashMap<String, byte[]>();
        byte[] lenBuffer = new byte[PACK_LEN];
        System.arraycopy(pack, 0, lenBuffer, 0, PACK_LEN);
        int dataLen = ByteUtil.bytes2Int(lenBuffer, 0, ByteUtil.BIG_ENDIAN);
        protocolMap.put(KEY_PROTOCOL_LEN, lenBuffer);

        byte[] typeBuffer = new byte[PACK_TYPE_LEN];
        System.arraycopy(pack, PACK_LEN, typeBuffer, 0, PACK_TYPE_LEN);
        protocolMap.put(KEY_PROTOCOL_TYPE, typeBuffer);

        byte[] dataBuffer = new byte[dataLen - PACK_LEN - PACK_TYPE_LEN];
        System.arraycopy(pack, PACK_LEN + PACK_TYPE_LEN, dataBuffer, 0, dataLen - PACK_LEN -PACK_TYPE_LEN);
        protocolMap.put(KEY_PROTOCOL_DATA, dataBuffer);
        return protocolMap;
    }

    /**
     * 获取数据包长
     * @param pack
     * @return
     */
    @Override
    public int getDataLen(byte[] pack) {
        //四字节长度+四字节操作码
        int packLen = PACK_LEN;
        int packTypeLen = PACK_TYPE_LEN;
        if (pack == null || pack.length < packLen + packTypeLen) {
            return -1;
        }
        byte[] lenBuffer = new byte[packLen];
        System.arraycopy(pack, 0, lenBuffer, 0, packLen);
        int dataLen = ByteUtil.bytes2Int(lenBuffer, 0, ByteUtil.BIG_ENDIAN);
        return dataLen;
    }

    /**
     * 获得数据源
     * @param pack
     * @return
     */
    @Override
    public byte[] getData(byte[] pack) {
        Map<String, byte[]> packMap = parseWillPackage(pack);
        return packMap.get(KEY_PROTOCOL_DATA);
    }

    public byte[] getData(byte[] pack,int dataLen) {
        byte[] dataBuffer = new byte[dataLen];
        System.arraycopy(pack, PACK_LEN + PACK_TYPE_LEN, dataBuffer, 0, dataLen - PACK_LEN -PACK_TYPE_LEN);
        return dataBuffer;
    }

    /**
     * 操作码 状态码
     * @param pack
     * @return
     */
    @Override
    public int getType(byte[] pack) {
        Map<String, byte[]> packMap = parseWillPackage(pack);
        byte[] typeAry = packMap.get(KEY_PROTOCOL_TYPE);
        return ByteUtil.bytes2Int(typeAry, 0, ByteUtil.BIG_ENDIAN);
    }

    /**
     * 包头长度
     * @return
     */
    @Override
    public int getHeadLen() {
        return PACK_LEN+PACK_TYPE_LEN;
    }

    //login parcel
    public static byte[] loginParcel(String jsonStr) {
        int typeValue = 10001;
        int dataLen = PACK_LEN+PACK_TYPE_LEN+jsonStr.getBytes().length;
        byte[] pack = parcel(dataLen, typeValue, jsonStr);
        return pack;
    }

    /**
     * 心跳
     * @return
     */
    @Override
    public byte[] heartbeatParcel() {
        int typeValue = 100;
        byte[] pack = parcel(PACK_LEN + PACK_TYPE_LEN, typeValue, null);
        return pack;
    }

    /**
     * 拼包方法
     * @param packLen   包长
     * @param typeValue 操作码
     * @param jsonStr   json数据
     * @return
     */
    public static byte[] parcel(int packLen, int typeValue, String jsonStr) {
        byte[] parcelAry = new byte[packLen];
        byte[] packLenAry = ByteUtil.INT32_2_INT8(packLen, ByteUtil.BIG_ENDIAN);
        byte[] typeAry = ByteUtil.INT32_2_INT8(typeValue, ByteUtil.BIG_ENDIAN);
        System.arraycopy(packLenAry, 0, parcelAry, 0, PACK_LEN);
        System.arraycopy(typeAry, 0, parcelAry, PACK_LEN, PACK_TYPE_LEN);
        if (jsonStr != null) {
            System.arraycopy(jsonStr.getBytes(), 0, parcelAry, PACK_LEN+PACK_TYPE_LEN, jsonStr.getBytes().length);
        }
        return parcelAry;
    }



    /***-------------------------房间-----------------------------**/
    //房间内部公用的发送拼包
    public static byte[] sendMessage(int typeValue,String jsonStr){
        if (jsonStr == null){
            byte[] pack = parcel( PACK_LEN+PACK_TYPE_LEN , typeValue , null);
            return pack;
        }else{
            byte[] pack = parcel( PACK_LEN+PACK_TYPE_LEN+jsonStr.getBytes().length , typeValue , jsonStr);
            return pack;
        }
    }
}