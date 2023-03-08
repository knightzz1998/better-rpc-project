package io.knightzz.rpc.registry.api.config;

import java.io.Serializable;

/**
 * @author 王天赐
 * @title: RegistryConfig
 * @projectName better-rpc-project
 * @description: 注册配置类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-07 21:20
 */
public class RegistryConfig implements Serializable {


    private static final long serialVersionUID = 8065468161909097239L;

    /**
     * 注册地址
     */
    private String registryAddress;

    /**
     * 注册类型
     */
    private String registryType;

    public RegistryConfig(String registryAddress, String registryType) {
        this.registryAddress = registryAddress;
        this.registryType = registryType;
    }

    public String getRegistryAddress() {
        return registryAddress;
    }

    public void setRegistryAddress(String registryAddress) {
        this.registryAddress = registryAddress;
    }

    public String getRegistryType() {
        return registryType;
    }

    public void setRegistryType(String registryType) {
        this.registryType = registryType;
    }
}
