package io.knightzz.rpc.provider.common.handler;

import com.alibaba.fastjson.JSONObject;
import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.enumeration.RpcType;
import io.knightzz.rpc.protocol.header.RpcHeader;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.protocol.response.RpcResponse;
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
public class RpcProviderHandler extends SimpleChannelInboundHandler<RpcProtocol<RpcRequest>> {

    private final Logger logger = LoggerFactory.getLogger(RpcProviderHandler.class);

    private final Map<String, Object> handlerMap;

    public RpcProviderHandler(Map<String, Object> handlerMap) {
        this.handlerMap = handlerMap;
    }

    /**
     * 读取客户端发送的数据
     *
     * @param ctx      ChannelHandlerContext 上下文
     * @param protocol 请求消息
     * @throws Exception 执行异常
     */
    @Override
    protected void channelRead0(ChannelHandlerContext ctx,
                                RpcProtocol<RpcRequest> protocol) throws Exception {

        logger.info("RPC提供者接收到的消息为 ==> : {}", JSONObject.toJSON(protocol));

        Set<String> keySet = handlerMap.keySet();
        logger.info("handlerMap中存放的数据如下所示：");
        for (String key : keySet) {
            logger.info("Key => {} , Value => {}", key, handlerMap.get(key));
        }

        // 获取请求头和请求体
        RpcHeader header = protocol.getHeader();
        RpcRequest requestBody = protocol.getBody();

        // 重新封装相应请求
        header.setMsgType((byte) RpcType.RESPONSE.getType());

        RpcProtocol<RpcResponse> responseProtocol = new RpcProtocol<>();
        RpcResponse responseBody = new RpcResponse();
        responseBody.setResult("数据交互成功!");
        responseBody.setAsync(requestBody.isAsync());
        responseBody.setOneway(requestBody.isOneway());

        responseProtocol.setHeader(header);
        responseProtocol.setBody(responseBody);
        ctx.writeAndFlush(responseProtocol);
    }
}
