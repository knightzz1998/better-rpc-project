package io.knightzz.rpc.proxy.jdk;

import io.knightzz.rpc.proxy.api.consumer.Consumer;
import io.knightzz.rpc.proxy.api.object.ObjectProxy;

import java.lang.reflect.Proxy;

/**
 * @author 王天赐
 * @title: JdkProxyFactory
 * @projectName better-rpc-project
 * @description: JDK动态代理工厂类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-06 15:16
 */
public class JdkProxyFactory<T> {

    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 超时时间，默认15s
     */
    private long timeout = 15000;
    /**
     * 服务消费者
     */
    private Consumer consumer;
    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步调用
     */
    private boolean async;

    /**
     * 是否单向调用
     */
    private boolean oneway;

    public JdkProxyFactory(String serviceVersion, String serviceGroup, long timeout,
                           Consumer consumer, String serializationType,
                           boolean async, boolean oneway) {
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.timeout = timeout;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    /**
     * 获取代理对象
     *
     * @param clazz 类对象
     * @param <T>   对象的类型
     * @return 生成的代理对象
     */
    public <T> T getProxy(Class<T> clazz) {

        ObjectProxy<T> objectProxy = new ObjectProxy<>(clazz, serviceVersion, serviceGroup,
                consumer, serializationType, timeout, async, oneway);
        return (T) Proxy.newProxyInstance(
                clazz.getClassLoader(), new Class<?>[]{clazz},
                objectProxy
        );

    }
}
