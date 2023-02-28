package io.knigthzz.rpc.consumer.common.context;

import io.knigthzz.rpc.consumer.common.future.RpcFuture;

/**
 * @author 王天赐
 * @title: RpcContext
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-28 15:02
 */
public class RpcContext {

    private RpcContext() {
    }

    /**
     * RpcContext实例, 全局唯一
     */
    private static final RpcContext AGENT = new RpcContext();

    /**
     * 存放RpcFuture实例的InheritableThreadLocal, 每个线程唯一
     */
    public static final InheritableThreadLocal<RpcFuture> RPC_FUTURE_INHERITABLE_THREAD_LOCAL = new InheritableThreadLocal<>();

    /**
     * 获取RpcContext实例
     *
     * @return RpcContext对象
     */
    public static RpcContext getContext() {
        return AGENT;
    }

    /**
     * 将RpcFuture添加到线程的上下文
     *
     * @param future RpcFuture 对象
     */
    public void setRpcFuture(RpcFuture future) {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.set(future);
    }

    /**
     * 获取当前线程保存的 RpcFuture 对象
     *
     * @return RpcFuture 对象
     */
    public RpcFuture getRpcFuture() {
        return RPC_FUTURE_INHERITABLE_THREAD_LOCAL.get();
    }

    /**
     * 移除存储在当前线程的 RpcFuture 对象
     */
    public void removeRpcFuture() {
        RPC_FUTURE_INHERITABLE_THREAD_LOCAL.remove();
    }

}
