package io.knightzz.rpc.provider;

import io.knightzz.rpc.common.scanner.server.RpcServiceScanner;
import io.knightzz.rpc.provider.common.server.base.BaseServer;
import org.apache.log4j.lf5.LF5Appender;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;

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

    public RpcSingleServer(String serverAddress, String packageName, String reflectType) {
        super(serverAddress, reflectType);
        try {
            this.handlerMap = RpcServiceScanner.
                    doScannerWithRpcServiceAnnotationFilterAndRegistryService(packageName);
        } catch (Exception e) {
            logger.debug("Rpc Server init error", e);
        }
    }
}
