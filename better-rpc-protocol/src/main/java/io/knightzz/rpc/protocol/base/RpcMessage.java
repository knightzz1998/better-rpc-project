package io.knightzz.rpc.protocol.base;

import java.io.Serializable;

/**
 * @author 王天赐
 * @title: RpcMessage
 * @projectName better-rpc-project
 * @description: 基础消息类型
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 15:33
 */
public class RpcMessage implements Serializable {


    /**
     * 是否单向发送
     */
    private boolean oneway;

    /**
     * 是否异步发送
     */
    private boolean async;

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }
}
