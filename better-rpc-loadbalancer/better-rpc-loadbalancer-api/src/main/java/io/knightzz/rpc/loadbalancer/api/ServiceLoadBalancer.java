package io.knightzz.rpc.loadbalancer.api;

import io.knightzz.rpc.constants.RpcConstants;
import io.knightzz.rpc.spi.annotation.SPI;

import java.util.List;

/**
 * @author 王天赐
 * @title: ServiceBalance
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-09 19:25
 */
@SPI(value = RpcConstants.SERVICE_LOAD_BALANCER_RANDOM)
public interface ServiceLoadBalancer<T> {

    /**
     * 以负载均衡的方式选择一个服务节点
     * @param servers 服务列表
     * @param hashCode Hash值
     * @return 可用的服务节点
     */
    T select(List<T> servers, int hashCode);
}
