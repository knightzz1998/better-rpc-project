package io.knightzz.rpc.reflect.cglib;

import io.knightzz.rpc.reflect.api.ReflectInvoker;
import io.knightzz.rpc.spi.annotation.SPIClass;
import net.sf.cglib.reflect.FastClass;
import net.sf.cglib.reflect.FastMethod;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王天赐
 * @title: CglibReflectInvoker
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-28 16:52
 */
@SPIClass
public class CglibReflectInvoker implements ReflectInvoker {

    private final Logger logger = LoggerFactory.getLogger(CglibReflectInvoker.class);

    @Override
    public Object invokeMethod(Object serviceBean, Class<?> serviceClass, String methodName, Class<?>[] parameterTypes, Object[] parameters) throws Throwable {

        logger.info("使用 cglib 反射调用真实方法");
        FastClass serviceFastClass = FastClass.create(serviceClass);
        FastMethod serviceFastMethod = serviceFastClass.getMethod(methodName, parameterTypes);
        return serviceFastMethod.invoke(serviceBean, parameters);
    }
}