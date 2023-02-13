package io.knightzz.rpc.protocol.enumeration;

/**
 * @author 王天赐
 * @title: RpcType
 * @projectName better-rpc-project
 * @description: 枚举类, 主要标示传输消息的类型, 包括请求消息, 响应消息,和心跳消息
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 15:21
 */
public enum RpcType {

    /**
     * 请求消息类型
     */
    REQUEST(1),
    /**
     * 响应消息类型
     */
    RESPONSE(2),

    /**
     * 心跳消息类型
     */
    HEARTBEAT(3);

    RpcType(int value) {
        this.type = value;
    }

    private final int type;

    public static RpcType findByType(byte msgType) {

        for (RpcType value : RpcType.values()) {
            if(value.getType() == msgType) {
                return value;
            }
        }
        return null;
    }

    public int getType() {
        return type;
    }
}
