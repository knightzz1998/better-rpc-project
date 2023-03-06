package io.knightzz.rpc.proxy.api.consumer;

import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.proxy.api.future.RpcFuture;

/**
 * @author 王天赐
 * @title: Consumer
 * @projectName better-rpc-project
 * @description: 消费者接口
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-06 14:51
 */
public interface Consumer {
    /**
     * 消费者发送Request请求,
     * @param protocol 封装 RpcProtocol 协议对象
     * @return 响应
     */
    RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol);
}
