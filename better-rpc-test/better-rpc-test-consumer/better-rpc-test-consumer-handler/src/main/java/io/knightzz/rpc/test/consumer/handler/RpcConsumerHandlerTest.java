package io.knightzz.rpc.test.consumer.handler;

import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.header.RpcHeaderFactory;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knigthzz.rpc.consumer.common.RpcConsumer;
import io.knigthzz.rpc.consumer.common.callback.AsyncRpcCallback;
import io.knigthzz.rpc.consumer.common.context.RpcContext;
import io.knigthzz.rpc.consumer.common.future.RpcFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


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

    private static final Logger logger = LoggerFactory.getLogger(RpcConsumerHandlerTest.class);

    public static void testSync() throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        RpcFuture rpcFuture = consumer.sendRequest(getRpcRequestProtocol());
        // 添加回调函数
        rpcFuture.addCallback(new AsyncRpcCallback() {
            @Override
            public void onSuccess(Object result) {
                logger.info("从服务消费者获到的数据 ==> {}" , result);
            }

            @Override
            public void onException(Exception e) {
                logger.error("抛出异常 ==> ", e);
            }
        });
        logger.info("从消费者获取到的数据 ==> {}", rpcFuture.get());
        // 阻塞, 不然回调方法还没执行, 线程池就关闭了
        Thread.sleep(200);
        consumer.close();
    }

    public static void testAsync() throws Exception {
        RpcConsumer consumer = RpcConsumer.getInstance();
        RpcProtocol<RpcRequest> protocol = getRpcRequestProtocol();
        protocol.getBody().setAsync(true);

        consumer.sendRequest(protocol);


        RpcFuture future = RpcContext.getContext().getRpcFuture();
        logger.info("从消费者获取到的数据 ==> {}", future.get());
        consumer.close();
    }

    public static void testOneway() throws Exception {

        RpcConsumer consumer = RpcConsumer.getInstance();
        RpcProtocol<RpcRequest> protocol = getRpcRequestProtocol();
        protocol.getBody().setAsync(true);
        protocol.getBody().setOneway(true);
        consumer.sendRequest(protocol);
        logger.info("无序获取返回的数据!");
    }

    public static void main(String[] args) throws Exception {
        testSync();
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
