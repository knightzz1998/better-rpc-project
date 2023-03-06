package io.knightzz.rpc.proxy.api.callback;

/**
 * @author 王天赐
 * @title: AsyncRpcCallback
 * @projectName better-rpc-project
 * @description: RPC回调接口
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-03 14:30
 */
public interface AsyncRpcCallback {


    /**
     * 成功以后的回调方法
     *
     * @param result
     */
    void onSuccess(Object result);


    /**
     * 出现异常时的回调方法
     *
     * @param e
     */
    void onException(Exception e);
}

