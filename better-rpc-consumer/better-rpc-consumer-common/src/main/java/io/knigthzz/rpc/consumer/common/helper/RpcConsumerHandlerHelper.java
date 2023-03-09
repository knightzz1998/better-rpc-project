package io.knigthzz.rpc.consumer.common.helper;

import io.knightzz.rpc.protocol.meta.ServiceMeta;
import io.knigthzz.rpc.consumer.common.handler.RpcConsumerHandler;

import java.util.Collection;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王天赐
 * @title: RpcConsumerHandlerHelper
 * @projectName better-rpc-project
 * @description: 服务消费者处理器, 用于缓存RpcConsumerHandler
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-08 20:05
 */
public class RpcConsumerHandlerHelper {

    /**
     * 缓存RpcConsumerHandler实例
     */
    private static final Map<String, RpcConsumerHandler> RPC_CONSUMER_HANDLER_MAP;

    static {
        RPC_CONSUMER_HANDLER_MAP = new ConcurrentHashMap<>();
    }

    public static String getKey(ServiceMeta serviceMeta) {
        // key ==> address:port
        return serviceMeta.getServiceAddress().concat("_").concat(String.valueOf(serviceMeta.getServicePort()));
    }

    /**
     * 将RpcConsumerHandler实例添加到缓存
     *
     * @param serviceMeta 元数据
     * @param handler     RpcConsumerHandler实例
     */
    public static void put(ServiceMeta serviceMeta, RpcConsumerHandler handler) {
        String key = getKey(serviceMeta);
        RPC_CONSUMER_HANDLER_MAP.put(key, handler);
    }

    /**
     * 获取RpcConsumerHandler实例
     *
     * @param serviceMeta 元数据
     * @return RpcConsumerHandler 实例
     */
    public static RpcConsumerHandler get(ServiceMeta serviceMeta) {
        String key = getKey(serviceMeta);
        return RPC_CONSUMER_HANDLER_MAP.get(key);
    }

    /**
     * 关闭并清空所有缓存的RpcConsumerHandler
     */
    public static void closeRpcClientHandler() {

        Collection<RpcConsumerHandler> handlers = RPC_CONSUMER_HANDLER_MAP.values();
        if (handlers != null) {
            handlers.stream().forEach((handler) -> {
                handler.close();
            });
        }
        handlers.clear();
    }

}
