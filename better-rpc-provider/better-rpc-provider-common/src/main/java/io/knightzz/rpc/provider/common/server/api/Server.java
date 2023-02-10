package io.knightzz.rpc.provider.common.server.api;

/**
 * @author 王天赐
 * @title: Server
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-08 17:19
 */
public interface Server {

    /**
     * 启动Netty服务
     */
    void startNettyServer();
}
