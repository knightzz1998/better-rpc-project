package io.knightzz.rpc.proxy.api;

import io.knightzz.rpc.proxy.api.config.ProxyConfig;

/**
 * @author 王天赐
 * @title: ProxyFactory
 * @projectName better-rpc-project
 * @description: 代理工厂类接口
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-07 19:16
 */
public interface ProxyFactory {

    /**
     * 获取代理类
     * @param clazz 代理类接口
     * @return 代理类对象
     * @param <T>
     */
    <T> T getProxy(Class<T> clazz);

    default <T> void init(ProxyConfig<T> proxyConfig){};
}
