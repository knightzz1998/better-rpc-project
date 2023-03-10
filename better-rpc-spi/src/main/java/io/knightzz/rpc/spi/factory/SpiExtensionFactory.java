package io.knightzz.rpc.spi.factory;

import io.knightzz.rpc.spi.annotation.SPI;
import io.knightzz.rpc.spi.annotation.SPIClass;

import java.util.Optional;

/**
 * @author 王天赐
 * @title: SpiExtensionFactory
 * @projectName better-rpc-project
 * @description: 基于SPI实现的扩展类加载器工厂类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-10 20:37
 */
@SPIClass
public class SpiExtensionFactory implements ExtensionFactory{


    @Override
    public <T> T getExtension(final String key, final Class<T> clazz) {

        Optional<Class<T>> classOptional = Optional.ofNullable(clazz);

        // 把不是接口的 Class 对象过滤掉
        classOptional = classOptional.filter((cls) -> {
            return cls.isInterface();
        });

        // 校验是否有 SPI 注解
        classOptional = classOptional.filter((cls) -> {
            return cls.isAnnotationPresent(SPI.class);
        });


        return null;
    }
}
