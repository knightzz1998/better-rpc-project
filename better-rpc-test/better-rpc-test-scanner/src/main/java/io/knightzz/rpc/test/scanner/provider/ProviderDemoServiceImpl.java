package io.knightzz.rpc.test.scanner.provider;

import io.knightzz.rpc.annotation.RpcService;
import io.knightzz.rpc.test.scanner.service.DemoService;

/**
 * @author 王天赐
 * @title: ProviderDemoServiceImpl
 * @projectName better-rpc-project
 * @description: DemoService 接口实现类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-07 19:27
 */

@RpcService(interfaceClass = DemoService.class,
        interfaceClassName = "io.knightzz.rpc.test.scanner.service.DemoService",
        version = "1.0.0",
        group = "knightzz"
)
public class ProviderDemoServiceImpl implements DemoService {

}
