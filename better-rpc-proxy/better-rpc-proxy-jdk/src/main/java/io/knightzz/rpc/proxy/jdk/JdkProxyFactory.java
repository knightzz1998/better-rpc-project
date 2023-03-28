package io.knightzz.rpc.proxy.jdk;

import io.knightzz.rpc.proxy.api.BaseProxyFactory;
import io.knightzz.rpc.proxy.api.ProxyFactory;
import io.knightzz.rpc.proxy.api.consumer.Consumer;
import io.knightzz.rpc.proxy.api.object.ObjectProxy;
import io.knightzz.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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
@SPIClass
public class JdkProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {

    private final Logger logger = LoggerFactory.getLogger(JdkProxyFactory.class);

    /**
     * 获取代理对象
     *
     * @param clazz 类对象
     * @param <T>   对象的类型
     * @return 生成的代理对象
     */
    public <T> T getProxy(Class<T> clazz) {

        logger.info("基于JDK的动态代理");

        return (T) Proxy.newProxyInstance(clazz.getClassLoader(),
                new Class[]{clazz}, objectProxy);
    }
}
