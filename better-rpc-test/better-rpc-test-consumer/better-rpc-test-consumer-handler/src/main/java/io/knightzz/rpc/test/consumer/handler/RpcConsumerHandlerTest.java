package io.knightzz.rpc.test.consumer.handler;

import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.header.RpcHeaderFactory;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knigthzz.rpc.consumer.common.RpcConsumer;

/**
 * @author 王天赐
 * @title: RpcConsumerHandlerTest
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-22 16:08
 */
public class RpcConsumerHandlerTest {

    public static void main(String[] args) throws Exception {

        RpcConsumer consumer = RpcConsumer.getInstance();

        RpcProtocol<RpcRequest> protocol = getRpcRequestProtocol();

        consumer.sendRequest(protocol);

        Thread.sleep(2000);

        consumer.close();
    }

    private static RpcProtocol<RpcRequest> getRpcRequestProtocol() {
        //模拟发送数据
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<RpcRequest>();
        protocol.setHeader(RpcHeaderFactory.getRequestHeader("jdk"));
        RpcRequest request = new RpcRequest();
        request.setClassName("io.knightzz.rpc.test.api.DemoService");
        request.setGroup("knightzz");
        request.setMethodName("hello");
        request.setParameters(new Object[]{"knightzz"});
        request.setParameterTypes(new Class[]{String.class});
        request.setVersion("1.0.0");
        request.setAsync(false);
        request.setOneway(false);
        protocol.setBody(request);
        return protocol;
    }
}
