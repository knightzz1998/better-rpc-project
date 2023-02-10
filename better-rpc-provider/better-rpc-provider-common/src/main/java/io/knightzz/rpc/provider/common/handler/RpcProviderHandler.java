package io.knightzz.rpc.provider.common.handler;

import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Set;

/**
 * @author 王天赐
 * @title: RpcProviderHandler
 * @projectName better-rpc-project
 * @description: 处理底层消息的收发
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-08 17:12
 */
public class RpcProviderHandler extends SimpleChannelInboundHandler {

    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * 读取客户端发送的数据
     *
     * @param ctx ChannelHandlerContext 上下文
     * @param o   消息
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                Object o) throws Exception {

        logger.info("RPC提供者接收到的消息 : {}", o.toString());

        Set<String> keySet = handlerMap.keySet();
        logger.info("handlerMap中存放的数据如下所示：");
        for (String key : keySet) {
            logger.info("Key => {} , Value => {}", key, handlerMap.get(key));
        }

        ctx.writeAndFlush("已收到消息..");
    }
}
