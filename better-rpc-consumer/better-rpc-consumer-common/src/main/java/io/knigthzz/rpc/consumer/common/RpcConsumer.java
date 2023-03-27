package io.knigthzz.rpc.consumer.common;

import io.knightzz.rpc.common.helper.RpcServiceHelper;
import io.knightzz.rpc.common.threadpool.ClientThreadPool;
import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.meta.ServiceMeta;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.proxy.api.consumer.Consumer;
import io.knightzz.rpc.proxy.api.future.RpcFuture;
import io.knightzz.rpc.registry.api.RegistryService;
import io.knigthzz.rpc.consumer.common.handler.RpcConsumerHandler;
import io.knigthzz.rpc.consumer.common.helper.RpcConsumerHandlerHelper;
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
public class RpcConsumer implements Consumer {

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
        RpcConsumerHandlerHelper.closeRpcClientHandler();
        workGroup.shutdownGracefully();
        // 关闭线程池
        ClientThreadPool.shutdown();
    }

    @Override
    public RpcFuture sendRequest(RpcProtocol<RpcRequest> protocol, RegistryService registryService) throws Exception {

        // 获取 Request
        RpcRequest request = protocol.getBody();
        String serviceKey = RpcServiceHelper.buildServiceKey(request.getClassName(),
                request.getVersion(), request.getGroup());

        Object[] parameters = request.getParameters();
        // 获取 hashCode
        int invokerHashCode = (parameters == null || parameters.length == 0) ? serviceKey.hashCode() : parameters[0].hashCode();

        // 服务发现, 获取服务实例
        ServiceMeta serviceMeta = registryService.discovery(serviceKey, invokerHashCode);

        if (serviceMeta != null) {

            // 从缓存中获取 RpcConsumerHandler
            RpcConsumerHandler rpcConsumerHandler = RpcConsumerHandlerHelper.get(serviceMeta);

            // 缓存中无 Handler
            if (rpcConsumerHandler == null) {
                rpcConsumerHandler = getRpcConsumerHandler(serviceMeta.getServiceAddress(), serviceMeta.getServicePort());
                RpcConsumerHandlerHelper.put(serviceMeta, rpcConsumerHandler);
            } else if (!rpcConsumerHandler.getChannel().isActive()) {
                // 缓存中存在, 但是不活跃
                rpcConsumerHandler.close();

                // 重新获取
                rpcConsumerHandler = getRpcConsumerHandler(serviceMeta.getServiceAddress(), serviceMeta.getServicePort());
                RpcConsumerHandlerHelper.put(serviceMeta, rpcConsumerHandler);
            }

            return rpcConsumerHandler.sendRequest(protocol, request.isAsync(), request.isAsync());
        }
        return null;
    }


    private RpcConsumerHandler getRpcConsumerHandler(String serviceAddress, int port) throws Exception {

        ChannelFuture channelFuture = bootstrap.connect(serviceAddress, port).sync();

        channelFuture.addListener(listener -> {

            if (channelFuture.isSuccess()) {
                logger.info("connect rpc server address {} on port {} success", serviceAddress, port);
            } else {
                logger.error("connect rpc server address {} on port {} failed", serviceAddress, port);
                channelFuture.cause().printStackTrace();
                //关闭当前work组
                workGroup.shutdownGracefully();
            }
        });

        // 获取当前管道对应的handler的实例对象
        return channelFuture.channel().pipeline().get(RpcConsumerHandler.class);
    }
}
