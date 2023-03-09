package io.knightzz.rpc.provider.common.server.base;

import io.knightzz.rpc.codec.RpcDecoder;
import io.knightzz.rpc.codec.RpcEncoder;
import io.knightzz.rpc.provider.common.handler.RpcProviderHandler;
import io.knightzz.rpc.provider.common.server.api.Server;
import io.knightzz.rpc.registry.api.RegistryService;
import io.knightzz.rpc.registry.api.config.RegistryConfig;
import io.knightzz.rpc.registry.zookeeper.ZookeeperRegistryService;
import io.netty.bootstrap.ServerBootstrap;
import io.netty.channel.*;
import io.netty.channel.nio.NioEventLoopGroup;
import io.netty.channel.socket.SocketChannel;
import io.netty.channel.socket.nio.NioServerSocketChannel;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;


/**
 * @author 王天赐
 * @title: BaseServer
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-08 17:26
 */
public class BaseServer implements Server {

    protected final Logger logger = LoggerFactory.getLogger(BaseServer.class);

    /**
     * 注册中心服务实例
     */
    protected RegistryService registryService;

    /**
     * 存储服务执行类的实例, key由 服务名称#版本号#服务分组 组成
     */
    protected Map<String, Object> handlerMap = new HashMap<>();

    /**
     * 服务提供者调用真实方法的类型 : JDK / cglib
     */
    private String reflectType;

    /**
     * IP
     */
    protected String host = "127.0.0.1";
    /**
     * 端口
     */
    protected int port = 27110;


    public BaseServer(String serverAddress,
                      String registryAddress,
                      String registryType,
                      String reflectType) {

        if (!StringUtils.isEmpty(serverAddress)) {
            // 获取id和端口
            String[] serverArray = serverAddress.split(":");
            this.host = serverArray[0];
            this.port = Integer.parseInt(serverArray[1]);
        }
        this.reflectType = reflectType;
        this.registryService = getRegistryService(registryAddress, registryType);
    }

    /**
     * 实例化 ZookeeperRegistryService
     * @param serverAddress 地址
     * @param registryType 注册类型
     * @return RegistryService 实例对象
     */
    public RegistryService getRegistryService(String registryAddress, String registryType) {
        // TODO 扩展SPI
        RegistryService registryService = null;

        RegistryConfig registryConfig = new RegistryConfig(registryAddress, registryType);

        try {
            registryService = new ZookeeperRegistryService();
            // 初始化
            registryService.init(registryConfig);
        } catch (Exception e) {
            logger.error("Zookeeper Registry Service init error ", e);
        }

        return registryService;
    }

    @Override
    public void startNettyServer() {

        // 创建 BossGroup
        EventLoopGroup bossGroup = new NioEventLoopGroup(1);
        EventLoopGroup workGroup = new NioEventLoopGroup(1);

        ServerBootstrap serverBootstrap = new ServerBootstrap();

        try {
            serverBootstrap.group(bossGroup, workGroup)
                    .channel(NioServerSocketChannel.class)
                    .childHandler(new ChannelInitializer<SocketChannel>() {
                        @Override
                        protected void initChannel(SocketChannel socketChannel) throws Exception {
                            ChannelPipeline pipeline = socketChannel.pipeline();
                            // 添加 handler
                            pipeline.addLast(new RpcEncoder());
                            pipeline.addLast(new RpcDecoder());
                            pipeline.addLast(new RpcProviderHandler(reflectType, handlerMap));
                        }
                    })
                    .childOption(ChannelOption.SO_KEEPALIVE, true)
                    .option(ChannelOption.SO_BACKLOG, 128);

            // 绑定端口
            ChannelFuture channelFuture = serverBootstrap.bind(host, port).sync();
            logger.info("Server started on host {} port {}", host, port);
            // 异步监听关闭
            channelFuture.channel().closeFuture().sync();
        } catch (InterruptedException e) {
            logger.error("Rpc Server start error", e);
        } finally {
            workGroup.shutdownGracefully();
            bossGroup.shutdownGracefully();
        }
    }
}
