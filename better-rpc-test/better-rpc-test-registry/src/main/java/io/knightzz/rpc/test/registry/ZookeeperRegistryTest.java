package io.knightzz.rpc.test.registry;

import io.knightzz.rpc.protocol.meta.ServiceMeta;
import io.knightzz.rpc.registry.api.RegistryService;
import io.knightzz.rpc.registry.api.config.RegistryConfig;
import io.knightzz.rpc.registry.zookeeper.ZookeeperRegistryService;
import org.junit.Before;
import org.junit.Test;

/**
 * @author 王天赐
 * @title: ZookeeperRegisryTest
 * @projectName better-rpc-project
 * @description: 测试服务注册与发现功能
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-08 16:43
 */
public class ZookeeperRegistryTest {

    private RegistryService registryService;

    private ServiceMeta serviceMeta;

    @Before
    public void init() throws Exception {

        RegistryConfig registryConfig = new RegistryConfig("127.0.0.1:2181", "zookeeper");

        this.registryService = new ZookeeperRegistryService();
        this.registryService.init(registryConfig);

        this.serviceMeta = new ServiceMeta(ZookeeperRegistryTest.class.getName(),
                "1.0.0", "knightzz", "127.0.0.0.1", 8080);
    }

    @Test
    public void testRegister() throws Exception {
        System.out.println(serviceMeta);
        this.registryService.register(serviceMeta);
    }

    @Test
    public void testUnRegister() throws Exception {
        this.registryService.unRegister(serviceMeta);
    }

    @Test
    public void testDiscovery() throws Exception {


        ServiceMeta discovery = this.registryService.discovery(RegistryService.class.getName(),
                "knightzz".hashCode());
        System.out.println(discovery);
    }

    @Test
    public void testDestroy() throws Exception {
        this.registryService.destroy();
    }

}