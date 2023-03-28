package io.knightzz.rpc.proxy.cglib;

import io.knightzz.rpc.proxy.api.BaseProxyFactory;
import io.knightzz.rpc.proxy.api.ProxyFactory;
import io.knightzz.rpc.spi.annotation.SPIClass;
import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.InvocationHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;

/**
 * @author 王天赐
 * @title: CglibProxyFactory
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-28 15:45
 */
@SPIClass
public class CglibProxyFactory<T> extends BaseProxyFactory<T> implements ProxyFactory {

    private final Logger logger = LoggerFactory.getLogger(CglibProxyFactory.class);

    private final Enhancer enhancer = new Enhancer();

    @Override
    public <T> T getProxy(Class<T> clazz) {

        logger.info("基于 cglib的动态代理 ... ");

        enhancer.setInterfaces(new Class[]{clazz});
        enhancer.setCallback(new InvocationHandler() {
            @Override
            public Object invoke(Object o, Method method, Object[] objects) throws Throwable {
                return objectProxy.invoke(o, method, objects);
            }
        });

        return (T) enhancer.create();
    }
}
