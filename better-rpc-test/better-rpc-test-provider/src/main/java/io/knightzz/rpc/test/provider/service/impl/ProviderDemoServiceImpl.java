package io.knightzz.rpc.test.provider.service.impl;

import io.knightzz.rpc.annotation.RpcService;
import io.knightzz.rpc.test.provider.service.DemoService;

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
        interfaceClassName = "io.knightzz.rpc.test.provider.service.DemoService",
        version = "1.0.0",
        group = "demoGroup"
)
public class ProviderDemoServiceImpl implements DemoService {

}
