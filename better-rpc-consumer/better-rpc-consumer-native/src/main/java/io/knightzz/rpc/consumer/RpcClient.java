package io.knightzz.rpc.consumer;

import io.knightzz.rpc.common.exception.RegistryException;
import io.knightzz.rpc.proxy.api.ProxyFactory;
import io.knightzz.rpc.proxy.api.async.IAsyncObjectProxy;
import io.knightzz.rpc.proxy.api.config.ProxyConfig;
import io.knightzz.rpc.proxy.api.object.ObjectProxy;
import io.knightzz.rpc.proxy.jdk.JdkProxyFactory;
import io.knightzz.rpc.registry.api.RegistryService;
import io.knightzz.rpc.registry.api.config.RegistryConfig;
import io.knightzz.rpc.registry.zookeeper.ZookeeperRegistryService;
import io.knightzz.rpc.spi.loader.ExtensionLoader;
import io.knigthzz.rpc.consumer.common.RpcConsumer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.util.StringUtils;

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
     * 动态代理的方式
     */
    private String proxy;

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

    private RegistryService registryService;

    public RpcClient(String registryAddress, String registryType, String proxy, String serviceVersion, String serviceGroup, long timeout,
                     String serializationType,
                     boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.proxy = proxy;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
        this.registryService = this.getRegistryService(registryAddress, registryType);
    }

    private RegistryService getRegistryService(String registryAddress, String registryType) {

        if (StringUtils.isEmpty(registryType)) {
            throw new RuntimeException("registry Type is null ");
        }

        RegistryService registryService = new ZookeeperRegistryService();

        RegistryConfig registryConfig = new RegistryConfig(registryAddress, registryType);

        try {
            registryService.init(registryConfig);
        } catch (Exception e) {
            logger.error("Rpc registry service init throw exception ", e);
            throw new RegistryException(e.getMessage(), e);
        }

        return registryService;
    }

    /**
     * 创建代理对象
     *
     * @param interfaceClass Class 类对象
     * @param <T>            类泛型
     * @return 代理对象
     */
    public <T> T create(Class<T> interfaceClass) {

        ProxyFactory proxyFactory = ExtensionLoader.getExtension(ProxyFactory.class, proxy);

        ProxyConfig<T> proxyConfig = new ProxyConfig<>(interfaceClass, serviceVersion, serviceGroup,
                RpcConsumer.getInstance(), timeout, serializationType, async, oneway, registryService);

        // 初始化
        proxyFactory.init(proxyConfig);
        return proxyFactory.getProxy(interfaceClass);
    }

    public void shutdown() {
        RpcConsumer.getInstance().close();
    }

    public <T> IAsyncObjectProxy createAsync(Class<T> interfaceClass) {

        return new ObjectProxy<T>(interfaceClass, serviceVersion,
                serviceGroup, RpcConsumer.getInstance(),
                serializationType, timeout, async, oneway, registryService);
    }
}
