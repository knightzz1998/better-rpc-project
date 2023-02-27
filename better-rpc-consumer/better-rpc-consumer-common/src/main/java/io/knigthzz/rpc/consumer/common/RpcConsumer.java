package io.knigthzz.rpc.consumer.common;

import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.protocol.response.RpcResponse;
import io.knigthzz.rpc.consumer.common.handler.RpcConsumerHandler;
import io.knigthzz.rpc.consumer.common.initializer.RpcConsumerInitializer;
import io.netty.bootstrap.Bootstrap;
import io.netty.channel.ChannelFuture;
import io.netty.channel.EventLoopGroup;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.nio.NioSocketChannel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王天赐
 * @title: RpcConsumer
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-22 15:11
 */
public class RpcConsumer {

    private final Logger logger = LoggerFactory.getLogger(RpcConsumer.class);
    /**
     * Netty 启动类
     */
    private Bootstrap bootstrap;
    private EventLoopGroup workGroup;

    /**
     *
     */
    private static volatile RpcConsumer instance;

    private static Map<String, RpcConsumerHandler> handlerMap = new ConcurrentHashMap<>();



    private RpcConsumer() {

        bootstrap = new Bootstrap();
        workGroup = new NioEventLoopGroup(4);

        bootstrap.group(workGroup).channel(NioSocketChannel.class)
                .handler(new RpcConsumerInitializer());

    }


    /**
     * 获取RpcConsumer实例对象
     *
     * @return RpcConsumer 实例对象
     */
    public static RpcConsumer getInstance() {

        if (instance == null) {

            synchronized (RpcConsumer.class) {
                if (instance == null) {
                    instance = new RpcConsumer();
                }
            }

        }
        return instance;
    }

    public void close() {
        workGroup.shutdownGracefully();
    }

    public Object sendRequest(RpcProtocol<RpcRequest> protocol) throws Exception {

        // TODO 这里的IP地址暂时写死, 后面引入注册中心的时候从注册中心获取

        String serviceAddress = "127.0.0.1";
        int port = 27880;

        String key = serviceAddress.concat("_").concat(String.valueOf(port));

        // 从缓存的handler中拿到 key
        RpcConsumerHandler handler = handlerMap.get(key);
        if (handler == null) {

            handler = getRpcConsumerHandler(serviceAddress, port);
            // 添加到缓存
            handlerMap.put(key, handler);

        } else if (!handler.getChannel().isActive()) {
            // 缓存中存在, 但是不活跃
            // 关闭, 重新获取
            handler.close();
            handler = getRpcConsumerHandler(serviceAddress, port);
            // 添加到缓存
            handlerMap.put(key, handler);

        }

        return handler.sendRequest(protocol);
    }



    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws Exception {

        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();

        channelFuture.addListener(listener -> {

            if(channelFuture.isSuccess()) {
                logger.info("connect rpc server address {} on port {} success" , serviceAddress, port);
            }else{
                logger.error("connect rpc server address {} on port {} failed" , serviceAddress, port);
                channelFuture.cause().printStackTrace();
                //关闭当前work组
                workGroup.shutdownGracefully();
            }
        });

        // 获取当前管道对应的handler的实例对象
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }
}
