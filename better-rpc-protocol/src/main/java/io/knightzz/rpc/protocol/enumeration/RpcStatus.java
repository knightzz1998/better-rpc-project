package io.knightzz.rpc.protocol.enumeration;

/**
 * @author 王天赐
 * @title: RpcStatus
 * @projectName better-rpc-project
 * @description: 用于标示Rpc服务调用的状态
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-20 15:23
 */
public enum RpcStatus {

    /**
     * 执行成功的状态
     */
    SUCCESS(1),
    /**
     * 执行失败的状态
     */
    FAIL(0);

    private final int code;

    RpcStatus(int code) {
        this.code = code;
    }

    public int getCode() {
        return code;
    }
}
