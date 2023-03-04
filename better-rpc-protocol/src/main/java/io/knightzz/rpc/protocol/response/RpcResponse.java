package io.knightzz.rpc.protocol.response;

import io.knightzz.rpc.protocol.base.RpcMessage;

/**
 * @author 王天赐
 * @title: RpcResponse
 * @projectName better-rpc-project
 * @description: 响应消息的消息体
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 15:49
 */
public class RpcResponse extends RpcMessage {

    /**
     * serialversionUID 的作用是验证版本一致性
     */
    private static final long serialVersionUID = 425335064405584525L;

    /**
     * 错误信息
     */
    private String error;

    /**
     * 响应结果
     */
    private Object result;


    public boolean isError() {
        return error != null;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public Object getResult() {
        return result;
    }

    public void setResult(Object result) {
        this.result = result;
    }
}
