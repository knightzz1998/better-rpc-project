package io.knightzz.rpc.provider;

import io.knightzz.rpc.provider.common.scanner.RpcServiceScanner;
import io.knightzz.rpc.provider.common.server.base.BaseServer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author 王天赐
 * @title: RpcSingleServer
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-09 15:27
 */
public class RpcSingleServer extends BaseServer {

    private final Logger logger = LoggerFactory.getLogger(RpcSingleServer.class);

    public RpcSingleServer(String serverAddress, String registryAddress,String registryType,
                           String scanPackage,
                           String reflectType) {
        // 调用父类的构造函数
        super(serverAddress, registryAddress, registryType, reflectType);
        try {
            this.handlerMap = RpcServiceScanner.
                    doScannerWithRpcServiceAnnotationFilterAndRegistryService(this.host,this.port, scanPackage,this.registryService);
        } catch (Exception e) {
            logger.debug("Rpc Server init error", e);
        }
    }
}
