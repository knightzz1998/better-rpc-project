package io.knightzz.rpc.consumer;

import io.knightzz.rpc.proxy.api.async.IAsyncObjectProxy;
import io.knightzz.rpc.proxy.api.config.ProxyConfig;
import io.knightzz.rpc.proxy.api.object.ObjectProxy;
import io.knightzz.rpc.proxy.jdk.JdkProxyFactory;
import io.knigthzz.rpc.consumer.common.RpcConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王天赐
 * @title: RpcClient
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-06 17:04
 */
public class RpcClient {

    private Logger logger = LoggerFactory.getLogger(RpcClient.class);

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务分组
     */
    private String serviceGroup;

    /**
     * 超时时间
     */
    private long timeout = 15000;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步
     */
    private boolean async;

    /**
     * 是否单向请求(无返回值)
     */
    private boolean oneway;

    public RpcClient(String serviceVersion, String serviceGroup, long timeout, String serializationType, boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    /**
     * 创建代理对象
     *
     * @param interfaceClass Class 类对象
     * @param <T>            类泛型
     * @return 代理对象
     */
    public <T> T create(Class<T> interfaceClass) {

        JdkProxyFactory<Object> jdkProxyFactory = new JdkProxyFactory<>();

        ProxyConfig<T> proxyConfig = new ProxyConfig<>(interfaceClass, serviceVersion, serviceGroup,
                RpcConsumer.getInstance(), timeout, serializationType, async, oneway);

        // 初始化
        jdkProxyFactory.init(proxyConfig);
        return jdkProxyFactory.getProxy(interfaceClass);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {

        return new ObjectProxy<T>(interfaceClass, serviceVersion,
                serviceGroup, RpcConsumer.getInstance(),
                serializationType, timeout, async, oneway);
    }
}
