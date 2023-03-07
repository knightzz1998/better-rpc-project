package io.knightzz.rpc.proxy.api.async;

import io.knightzz.rpc.proxy.api.future.RpcFuture;

/**
 * @author 王天赐
 * @title: IAsyncObjectProxy
 * @projectName better-rpc-project
 * @description: 异步消费者代理接口
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-07 14:46
 */
public interface IAsyncObjectProxy {

    /**
     * 异步代理对象调用方法
     *
     * @param methodName 方法名称
     * @param args       方法参数
     * @return 封装好的 Rpc对象
     */
    RpcFuture call(String methodName, Object... args);

}
