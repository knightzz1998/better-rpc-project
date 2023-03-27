package io.knightzz.rpc.codec;

import io.knightzz.rpc.serialization.api.Serialization;
import io.knightzz.rpc.spi.loader.ExtensionLoader;

/**
 * @author 王天赐
 * @title: RpcCodec
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-13 15:27
 */
public interface RpcCodec {

    /**
     * 通过SPI机制实现序列化
     * @param serializationType 序列化类型
     * @return 序列化的对象
     */
    default Serialization getSerialization(String serializationType) {
        return ExtensionLoader.getExtension(Serialization.class, serializationType);
    }
}
