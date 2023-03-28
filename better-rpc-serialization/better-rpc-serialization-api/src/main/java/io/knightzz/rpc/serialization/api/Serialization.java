package io.knightzz.rpc.serialization.api;

import com.fasterxml.jackson.core.JsonProcessingException;
import io.knightzz.rpc.constants.RpcConstants;
import io.knightzz.rpc.spi.annotation.SPI;

/**
 * @author 王天赐
 * @title: Serialization
 * @projectName better-rpc-project
 * @description: 序列化接口
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-13 14:26
 */
@SPI(RpcConstants.SERIALIZATION_JDK)
public interface Serialization {

    /**
     * 序列化
     * @param obj 需要序列化的类
     * @return 序列化的结果, 以byte数组存储
     * @param <T> 参数类型
     */
    <T> byte[] serialization(T obj);

    /**
     * 反序列化, 将byte[]数组转换成对象
     * @param data 待序列化的数据
     * @param cls 反序列化后的类型
     * @return 反序列化对象
     * @param <T> 反序列化对象类型
     */
    <T> T deserialization(byte[] data , Class<T> cls);
}
