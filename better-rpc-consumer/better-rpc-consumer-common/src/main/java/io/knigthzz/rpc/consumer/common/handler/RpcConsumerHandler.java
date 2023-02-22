package io.knigthzz.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.protocol.response.RpcResponse;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;

/**
 * @author 王天赐
 * @title: RpcConsumerHandler
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-21 18:28
 */
public class RpcConsumerHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcResponse>> {

    private final Logger logger = LoggerFactory.getLogger(RpcConsumerHandler.class);

    private volatile Channel channel;

    private SocketAddress remotePeer;

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    @Override
    public void channelActive(ChannelHandlerContext ctx) throws Exception {
        super.channelActive(ctx);
        // 首次连通时, 获取远程地址
        this.remotePeer = ctx.channel().remoteAddress();
    }

    @Override
    public void channelRegistered(ChannelHandlerContext ctx) throws Exception {
        super.channelRegistered(ctx);
        this.channel = ctx.channel();
    }

    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext,
                                RpcProtocol<RpcResponse> protocol)
            throws Exception {

        logger.info("服务消费者接收到的数据 ===> : {}", JSONObject.toJSONString(protocol));
    }

    public void sendRequest(RpcProtocol<RpcRequest> protocol) {
        logger.info("服务消费者接发送的数据 ===> : {}", JSONObject.toJSONString(protocol));
        channel.writeAndFlush(protocol);
    }

    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }

}
