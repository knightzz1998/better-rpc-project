package io.knightzz.rpc.protocol;

import io.knightzz.rpc.protocol.header.RpcHeader;

import java.io.Serializable;

/**
 * @author 王天赐
 * @title: RpcProtocol
 * @projectName better-rpc-project
 * @description: RpcProtocol协议类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 17:13
 */
public class RpcProtocol<T> implements Serializable {

    private static final long serialVersionUID = 292789485166173277L;

    /**
     * 消息头
     */
    private RpcHeader header;

    /**
     * 消息体
     */
    private T body;

    public RpcHeader getHeader() {
        return header;
    }

    public void setHeader(RpcHeader header) {
        this.header = header;
    }

    public T getBody() {
        return body;
    }

    public void setBody(T body) {
        this.body = body;
    }
}
