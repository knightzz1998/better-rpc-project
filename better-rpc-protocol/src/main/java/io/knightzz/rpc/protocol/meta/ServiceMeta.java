package io.knightzz.rpc.protocol.meta;

import java.io.Serializable;

/**
 * @author 王天赐
 * @title: ServiceMeta
 * @projectName better-rpc-project
 * @description: 服务元数据类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-07 20:36
 */
public class ServiceMeta implements Serializable {

    private static final long serialVersionUID = -104852284433320820L;

    /**
     * 服务名称
     */
    private String serviceName;
    /**
     * 服务版本
     */
    private String serviceVersion;
    /**
     * 服务分组
     */
    private String serviceGroup;
    /**
     * 服务地址
     */
    private String serviceAddress;

    /**
     * 服务端口
     */
    private int servicePort;


    public ServiceMeta(String serviceName, String serviceVersion, String serviceGroup, String serviceAddress, int servicePort) {
        this.serviceName = serviceName;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.serviceAddress = serviceAddress;
        this.servicePort = servicePort;
    }

    public String getServiceName() {
        return serviceName;
    }

    public void setServiceName(String serviceName) {
        this.serviceName = serviceName;
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

    public String getServiceAddress() {
        return serviceAddress;
    }

    public void setServiceAddress(String serviceAddress) {
        this.serviceAddress = serviceAddress;
    }

    public int getServicePort() {
        return servicePort;
    }

    public void setServicePort(int servicePort) {
        this.servicePort = servicePort;
    }
}

