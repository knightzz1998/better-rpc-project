package io.knightzz.rpc.test.provider.service.impl;

import io.knightzz.rpc.annotation.RpcService;
import io.knightzz.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王天赐
 * @title: ProviderDemoServiceImpl
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 10:32
 */
@RpcService(
        interfaceClass = DemoService.class,
        interfaceClassName = "io.knightzz.rpc.test.api.DemoService",
        version = "1.0.0",
        group = "knightzz"
)
public class ProviderDemoServiceImpl implements DemoService {

    private final Logger logger = LoggerFactory.getLogger(ProviderDemoServiceImpl.class);

    @Override
    public String hello(String name) {
        logger.info("调用 hello 方法, 传入参数为 name = {} ", name);
        return "hello : " + name;
    }
}
