package io.knightzz.rpc.proxy.api.config;

import io.knightzz.rpc.proxy.api.consumer.Consumer;

import java.io.Serializable;

/**
 * @author 王天赐
 * @title: ProxyConfig
 * @projectName better-rpc-project
 * @description: 代理配置类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-07 18:55
 */
public class ProxyConfig<T> implements Serializable {

    private static final long serialVersionUID = 6648940252795742398L;

    /**
     * Class 接口实例
     */
    private Class<T> clazz;

    /**
     * 服务版本
     */
    private String serviceVersion;

    /**
     * 服务分组
     */
    private String serviceGroup;

    /**
     * 消费者接口
     */
    private Consumer consumer;

    /**
     * 超时时间
     */
    private long timeout = 15000;

    /**
     * 序列化类型
     */
    private String serializationType;

    /**
     * 是否异步
     */
    private boolean async;

    /**
     * 是否单向请求(无返回值)
     */
    private boolean oneway;

    public ProxyConfig(Class<T> clazz, String serviceVersion,
                       String serviceGroup, Consumer consumer,
                       long timeout, String serializationType,
                       boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.consumer = consumer;
        this.timeout = timeout;
        this.serializationType = serializationType;
        this.async = async;
        this.oneway = oneway;
    }

    public Class<T> getClazz() {
        return clazz;
    }

    public void setClazz(Class<T> clazz) {
        this.clazz = clazz;
    }

    public String getServiceVersion() {
        return serviceVersion;
    }

    public void setServiceVersion(String serviceVersion) {
        this.serviceVersion = serviceVersion;
    }

    public String getServiceGroup() {
        return serviceGroup;
    }

    public void setServiceGroup(String serviceGroup) {
        this.serviceGroup = serviceGroup;
    }

    public Consumer getConsumer() {
        return consumer;
    }

    public void setConsumer(Consumer consumer) {
        this.consumer = consumer;
    }

    public long getTimeout() {
        return timeout;
    }

    public void setTimeout(long timeout) {
        this.timeout = timeout;
    }

    public String getSerializationType() {
        return serializationType;
    }

    public void setSerializationType(String serializationType) {
        this.serializationType = serializationType;
    }

    public boolean isAsync() {
        return async;
    }

    public void setAsync(boolean async) {
        this.async = async;
    }

    public boolean isOneway() {
        return oneway;
    }

    public void setOneway(boolean oneway) {
        this.oneway = oneway;
    }
}
