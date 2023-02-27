package io.knightzz.rpc.test.provider.single;

import io.knightzz.rpc.provider.RpcSingleServer;
import org.junit.Test;

/**
 * @author 王天赐
 * @title: RpcSingleServerTest
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 11:13
 */
public class RpcSingleServerTest {

    @Test
    public void startSingleServer() {
        RpcSingleServer rpcSingleServer =
                new RpcSingleServer("127.0.0.1:27880",
                        "io.knightzz.rpc.test", "cglib");
        rpcSingleServer.startNettyServer();
    }
}
