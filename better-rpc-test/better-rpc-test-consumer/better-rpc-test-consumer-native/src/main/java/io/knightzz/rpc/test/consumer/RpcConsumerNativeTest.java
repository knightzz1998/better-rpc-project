package io.knightzz.rpc.test.consumer;

import io.knightzz.rpc.consumer.RpcClient;
import io.knightzz.rpc.proxy.api.async.IAsyncObjectProxy;
import io.knightzz.rpc.proxy.api.future.RpcFuture;
import io.knightzz.rpc.test.api.DemoService;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.ExecutionException;

/**
 * @author 王天赐
 * @title: RpcConsumerNativeTest
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-06 18:34
 */
public class RpcConsumerNativeTest {

    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerNativeTest.class);

    private RpcClient rpcClient;

    @Before
    public void initRpcClient() {

        rpcClient = new RpcClient(
                "127.0.0.1:2181", "zookeeper",
                "1.0.0", "knightzz", 0, "hessian2"
                , false, false);
    }

    @Test
    public void testInterfaceInfo() {
        DemoService demoService = rpcClient.create(DemoService.class);
        String result = demoService.hello("knightzz");
        logger.info("返回的结果数据 ==> " + result);
    }

    @Test
    public void testAsyncInterfaceRpc() throws ExecutionException, InterruptedException {

        IAsyncObjectProxy demoService = rpcClient.createAsync(DemoService.class);
        RpcFuture rpcFuture = demoService.call("hello", "knightzz");
        logger.info("异步消费者接收到的数据 ====> {}", rpcFuture.get());
        rpcClient.shutdown();
    }

}
