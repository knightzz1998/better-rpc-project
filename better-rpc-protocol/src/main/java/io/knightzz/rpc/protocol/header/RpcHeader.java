package io.knightzz.rpc.protocol.header;

import java.io.Serializable;

/**
 * @author 王天赐
 * @title: RpcHeader
 * @projectName better-rpc-project
 * @description: 消息头
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 16:10
 */
public class RpcHeader implements Serializable {

    /**
     * serialversionUID 的作用是验证版本一致性
     */
    private static final long serialVersionUID = 6011436680686290298L;

    /*
        +---------------------------------------------------------------+
        | 魔数 2byte | 报文类型 1byte | 状态 1byte |     消息 ID 8byte      |
        +---------------------------------------------------------------+
        |           序列化类型 16byte      |        数据长度 4byte          |
        +---------------------------------------------------------------+
    */


    /**
     * 魔数 : 2字节
     */
    private short magic;

    /**
     * 报文类型 : 1字节
     */
    private byte msgType;

    /**
     * 状态 : 1字节
     */
    private byte status;

    /**
     * 消息 ID 8字节
     */
    private long requestId;

    /**
     * 序列化类型: 16字节, 不足16字节, 后面补0, 约定序列化类型长度最多不能超过16
     */
    private String serializationType;

    /**
     * 消息长度 4字节
     */
    private int msgLen;

    public short getMagic() {
        return magic;
    }

    public void setMagic(short magic) {
        this.magic = magic;
    }

    public byte getMsgType() {
        return msgType;
    }

    public void setMsgType(byte msgType) {
        this.msgType = msgType;
    }

    public byte getStatus() {
        return status;
    }

    public void setStatus(byte status) {
        this.status = status;
    }

    public long getRequestId() {
        return requestId;
    }

    public void setRequestId(long requestId) {
        this.requestId = requestId;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public int getMsgLen() {
        return msgLen;
    }

    public void setMsgLen(int msgLen) {
        this.msgLen = msgLen;
    }
}
