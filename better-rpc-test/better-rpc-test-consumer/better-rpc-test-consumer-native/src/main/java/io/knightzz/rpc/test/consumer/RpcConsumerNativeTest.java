package io.knightzz.rpc.test.consumer;

import io.knightzz.rpc.consumer.RpcClient;
import io.knightzz.rpc.test.api.DemoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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

    public static void main(String[] args) {
        RpcClient client = new RpcClient(
                "1.0.0", "knightzz", 3000, "jdk"
                , false, false);

        DemoService demoService = client.create(DemoService.class);
        String result = demoService.hello("knightzz");
        logger.info("返回的结果数据 ==> " + result);

    }

}
