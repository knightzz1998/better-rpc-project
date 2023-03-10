package io.knightzz.rpc.spi.factory;

import io.knightzz.rpc.spi.annotation.SPI;

/**
 * @author 王天赐
 * @title: ExtensionFactory
 * @projectName better-rpc-project
 * @description: 扩展类加载器的工厂接口
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-10 19:08
 */
@SPI("spi")
public interface ExtensionFactory {

    /**
     * 扩区SPI扩展类对象
     * @param key 传入的key值, 表示扩展类
     * @param clazz 扩展类的 Class对象
     * @return 扩展类对象
     * @param <T> 泛型
     */
    <T> T getExtension(String key, Class<T> clazz);

}
