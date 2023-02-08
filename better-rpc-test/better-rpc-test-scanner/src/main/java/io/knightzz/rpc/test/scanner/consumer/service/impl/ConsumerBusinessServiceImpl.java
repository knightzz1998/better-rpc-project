package io.knightzz.rpc.test.scanner.consumer.service.impl;

import io.knightzz.rpc.annotation.RpcReference;
import io.knightzz.rpc.annotation.RpcService;
import io.knightzz.rpc.test.scanner.consumer.service.ConsumerBusinessService;
import io.knightzz.rpc.test.scanner.service.DemoService;

/**
 * @author 王天赐
 * @title: ConsumerBusinessServiceImpl
 * @projectName better-rpc-project
 * @description: 服务消费者业务逻辑接口实现类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-07 20:01
 */
public class ConsumerBusinessServiceImpl
        implements ConsumerBusinessService {

    @RpcReference(registryType = "zookeeper",
            registryAddress = "127.0.0.1:2181",
            version = "1.0.0",
            group = "knightzz"
    )
    private DemoService demoService;

}
