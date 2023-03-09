package io.knightzz.rpc.registry.zookeeper;

import io.knightzz.rpc.common.helper.RpcServiceHelper;
import io.knightzz.rpc.loadbalancer.api.ServiceLoadBalancer;
import io.knightzz.rpc.loadbalancer.random.RandomServiceLoadBalancer;
import io.knightzz.rpc.protocol.meta.ServiceMeta;
import io.knightzz.rpc.registry.api.RegistryService;
import io.knightzz.rpc.registry.api.config.RegistryConfig;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.x.discovery.ServiceDiscovery;
import org.apache.curator.x.discovery.ServiceDiscoveryBuilder;
import org.apache.curator.x.discovery.ServiceInstance;
import org.apache.curator.x.discovery.details.JsonInstanceSerializer;

import java.util.Collection;
import java.util.List;
import java.util.Random;

/**
 * @author 王天赐
 * @title: ZookeeperRegistryService
 * @projectName better-rpc-project
 * @description: 服务注册与发现类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-08 15:15
 */
public class ZookeeperRegistryService implements RegistryService {

    /**
     * 初始化CuratorFrame客户端时,进行连接重试的间隔时间
     */
    public static final int BASE_SLEEP_TIME_MS = 1000;

    /**
     * 初始化CuratorFrame客户端时,进行连接重试的最大重试次数
     */
    public static final int MAX_RETRIES = 3;

    /**
     * 服务注册Zookeeper根路径
     */
    public static final String ZK_BASE_PATH = "/better_rpc";

    /**
     * 服务发现接口
     */
    private ServiceDiscovery<ServiceMeta> serviceDiscovery;

    /**
     * 负载均衡接口
     */
    private ServiceLoadBalancer<ServiceInstance<ServiceMeta>> serviceLoadBalancer;
    @Override
    public void register(ServiceMeta serviceMeta) throws Exception {

        // 表示服务的 key
        String serviceKey = RpcServiceHelper.buildServiceKey(
                serviceMeta.getServiceName(),
                serviceMeta.getServiceVersion(),
                serviceMeta.getServiceGroup());
        // 封装服务实例
        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(serviceKey)
                .address(serviceMeta.getServiceAddress())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();

        // 注册服务
        serviceDiscovery.registerService(serviceInstance);

    }

    @Override
    public void unRegister(ServiceMeta serviceMeta) throws Exception {

        ServiceInstance<ServiceMeta> serviceInstance = ServiceInstance.<ServiceMeta>builder()
                .name(serviceMeta.getServiceName())
                .address(serviceMeta.getServiceAddress())
                .port(serviceMeta.getServicePort())
                .payload(serviceMeta)
                .build();

        // 注册服务
        serviceDiscovery.unregisterService(serviceInstance);

    }

    @Override
    public ServiceMeta discovery(String serviceName, int invokerHashCode) throws Exception {

        // 根据服务名获取所有的实例列表
        Collection<ServiceInstance<ServiceMeta>>
                serviceInstances = serviceDiscovery.queryForInstances(serviceName);

        // 使用自定义负载均衡算法
        ServiceInstance<ServiceMeta> instance = this.serviceLoadBalancer.select(
                (List<ServiceInstance<ServiceMeta>>) serviceInstances, invokerHashCode
        );

        if (instance != null) {
            return instance.getPayload();
        }
        return null;
    }

    @Override
    public void destroy() throws Exception {
        serviceDiscovery.close();
    }

    @Override
    public void init(RegistryConfig registryConfig) throws Exception {

        // 实例化负载均衡类
        this.serviceLoadBalancer = new RandomServiceLoadBalancer<ServiceInstance<ServiceMeta>>();

        // 失败重试3次. 每次重试时间间隔呈指数增长
        ExponentialBackoffRetry backoffRetry = new ExponentialBackoffRetry(BASE_SLEEP_TIME_MS, MAX_RETRIES);
        CuratorFramework client = CuratorFrameworkFactory.newClient(registryConfig.getRegistryAddress(), backoffRetry);

        client.start();

        JsonInstanceSerializer<ServiceMeta> serializer = new JsonInstanceSerializer<>(ServiceMeta.class);

        this.serviceDiscovery = ServiceDiscoveryBuilder
                .builder(ServiceMeta.class)
                .client(client)
                .serializer(serializer)
                .basePath(ZK_BASE_PATH)
                .build();

        this.serviceDiscovery.start();
    }
}
