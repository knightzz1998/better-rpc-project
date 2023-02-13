package io.knightzz.rpc.codec;

import io.knightzz.rpc.serialization.api.Serialization;
import io.knightzz.rpc.serialization.jdk.JdkSerialization;

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
     * 获取基础JDK实现的序列化对象
     * @return JdkSerialization 的对象
     */
    default Serialization getJdkSerialization() {
        return new JdkSerialization();
    }
}
