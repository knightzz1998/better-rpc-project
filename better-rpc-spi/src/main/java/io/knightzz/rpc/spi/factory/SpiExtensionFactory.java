package io.knightzz.rpc.spi.factory;

import io.knightzz.rpc.spi.annotation.SPI;
import io.knightzz.rpc.spi.annotation.SPIClass;
import io.knightzz.rpc.spi.loader.ExtensionLoader;

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

        // 1. 校验 clazz 是否为null

        return Optional.ofNullable(clazz)
                .filter((cls) -> cls.isInterface())
                .filter(cls -> cls.isAnnotationPresent(SPI.class))
                .map(ExtensionLoader::getExtensionLoader)
                //.map((cls) -> ExtensionLoader.getExtensionLoader(cls))
                .map(ExtensionLoader::getDefaultSpiClassInstance)
                // .map((cls) -> cls.getDefaultSpiClassInstance())
                .orElse(null);
    }
}
