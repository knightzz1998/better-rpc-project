package io.knightzz.rpc.registry.api;

import io.knightzz.rpc.protocol.meta.ServiceMeta;
import io.knightzz.rpc.registry.api.config.RegistryConfig;

/**
 * @author 王天赐
 * @title: RegistryService
 * @projectName better-rpc-project
 * @description: 服务注册与发现接口
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-07 21:22
 */
public interface RegistryService {


    /**
     * 服务注册类
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void register(ServiceMeta serviceMeta) throws Exception;

    /**
     * 服务取消注册
     * @param serviceMeta 服务元数据
     * @throws Exception 抛出异常
     */
    void unRegister(ServiceMeta serviceMeta) throws Exception;


    /**
     * 根据传入的serviceName和 invokerHashCode 从Zookeeper 中获取对应的ServiceMeta元数据信息
     * @param serviceName 服务名称
     * @param invokerHashCode HashCode值
     * @return 服务元数据
     * @throws Exception 抛出异常
     */
    ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception;

    /**
     * 销毁服务, 关闭与Zookeeper的连接
     * @throws Exception 抛出异常
     */
    void destroy() throws Exception;

    /**
     * 默认初始化方法
     * @param registryConfig RegistryConfig 配置类
     * @throws Exception 抛出异常
     */
    default void init(RegistryConfig registryConfig) throws Exception{};
}
