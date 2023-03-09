package io.knightzz.rpc.proxy.api;

import io.knightzz.rpc.proxy.api.config.ProxyConfig;
import io.knightzz.rpc.proxy.api.object.ObjectProxy;
import io.knightzz.rpc.registry.api.RegistryService;

/**
 * @author 王天赐
 * @title: BaseFactory
 * @projectName better-rpc-project
 * @description: 基础工厂类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-07 19:28
 */
public abstract class BaseProxyFactory<T> implements ProxyFactory {

    protected ObjectProxy<T> objectProxy;

    @Override
    public <T> void init(ProxyConfig<T> proxyConfig) {

        this.objectProxy = new ObjectProxy<>(
                proxyConfig.getClazz(),
                proxyConfig.getServiceVersion(),
                proxyConfig.getServiceGroup(),
                proxyConfig.getConsumer(),
                proxyConfig.getSerializationType(),
                proxyConfig.getTimeout(),
                proxyConfig.isAsync(),
                proxyConfig.isOneway(),
                proxyConfig.getRegistryService()
        );

    }
}
