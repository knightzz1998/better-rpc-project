package io.knigthzz.rpc.consumer.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.header.RpcHeader;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.protocol.response.RpcResponse;
import io.knigthzz.rpc.consumer.common.context.RpcContext;
import io.knightzz.rpc.proxy.api.future.RpcFuture;
import io.netty.buffer.Unpooled;
import io.netty.channel.Channel;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.net.SocketAddress;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

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

    /**
     * 用于存储请求ID 与对应相响应结果的映射
     * key = requestId, value = RpcFuture
     */
    private Map<Long, RpcFuture> pendingRpc = new ConcurrentHashMap<>();

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

    /**
     * 接收响应数据
     *
     * @param channelHandlerContext
     * @param protocol
     * @throws Exception
     */
    @Override
    protected void channelRead0(ChannelHandlerContext channelHandlerContext, RpcProtocol<RpcResponse> protocol) throws Exception {

        if (protocol == null) {
            return;
        }
        logger.info("服务消费者接收到的数据 ===> : {}", JSONObject.toJSONString(protocol));

        // 获取 requestId
        RpcHeader header = protocol.getHeader();
        long requestId = header.getRequestId();
        RpcFuture rpcFuture = pendingRpc.remove(requestId);
        if (rpcFuture != null) {
            rpcFuture.done(protocol);
        }
    }

    /**
     * 处理请求调用
     *
     * @param rpcRequestRpcProtocol Request对象
     * @param isAsync               是否异步
     * @param isOneway              是否单向调用(不需要返回值)
     * @return
     */
    public RpcFuture sendRequest(RpcProtocol<RpcRequest> rpcRequestRpcProtocol, boolean isAsync, boolean isOneway) {


        logger.info("服务消费者接发送的数据 ===> : {}", JSONObject.toJSONString(rpcRequestRpcProtocol));

        if (isOneway) {
            return this.sendRequestOneway(rpcRequestRpcProtocol);
        }

        return isAsync ? this.sendRequestAsync(rpcRequestRpcProtocol)
                : this.sendRequestSync(rpcRequestRpcProtocol);
    }

    /**
     * 同步发送请求
     *
     * @param rpcRequestRpcProtocol Request请求对象
     * @return RpcFuture 对象
     */
    public RpcFuture sendRequestSync(RpcProtocol<RpcRequest> rpcRequestRpcProtocol) {
        RpcFuture rpcFuture = this.getRpcFuture(rpcRequestRpcProtocol);
        channel.writeAndFlush(rpcRequestRpcProtocol);
        return rpcFuture;
    }

    /**
     * 异步发送请求
     *
     * @param rpcRequestRpcProtocol Request请求对象
     * @return RpcFuture 对象
     */
    public RpcFuture sendRequestAsync(RpcProtocol<RpcRequest> rpcRequestRpcProtocol) {
        RpcFuture rpcFuture = this.getRpcFuture(rpcRequestRpcProtocol);
        // 将RpcFuture对象添加到 RpcContext
        RpcContext context = RpcContext.getContext();
        context.setRpcFuture(rpcFuture);
        channel.writeAndFlush(rpcRequestRpcProtocol);
        return rpcFuture;
    }

    /**
     * 获取RpcFuture对象
     *
     * @param rpcRequestRpcProtocol RpcRequest 对象
     * @return RpcFuture
     */
    private RpcFuture getRpcFuture(RpcProtocol<RpcRequest> rpcRequestRpcProtocol) {

        RpcFuture rpcFuture = new RpcFuture(rpcRequestRpcProtocol);
        long requestId = rpcRequestRpcProtocol.getHeader().getRequestId();
        pendingRpc.put(requestId, rpcFuture);
        return rpcFuture;
    }

    /**
     * 单向调用, 不需要返回值
     *
     * @param rpcRequestRpcProtocol RpcRequest 对象
     * @return null
     */
    private RpcFuture sendRequestOneway(RpcProtocol<RpcRequest> rpcRequestRpcProtocol) {
        channel.writeAndFlush(rpcRequestRpcProtocol);
        return null;
    }


    public void close() {
        channel.writeAndFlush(Unpooled.EMPTY_BUFFER).addListener(ChannelFutureListener.CLOSE);
    }


    public SocketAddress getRemotePeer() {
        return remotePeer;
    }

    public void setRemotePeer(SocketAddress remotePeer) {
        this.remotePeer = remotePeer;
    }
}
