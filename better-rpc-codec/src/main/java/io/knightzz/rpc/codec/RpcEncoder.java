package io.knightzz.rpc.codec;

import io.knightzz.rpc.common.utils.SerializationUtils;
import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.header.RpcHeader;
import io.knightzz.rpc.serialization.api.Serialization;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.MessageToByteEncoder;

import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

/**
 * @author 王天赐
 * @title: RpcEncoder
 * @projectName better-rpc-project
 * @description: Rpc编码类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-13 15:34
 */
public class RpcEncoder extends MessageToByteEncoder<RpcProtocol<Object>> implements RpcCodec {
    @Override
    protected void encode(ChannelHandlerContext channelHandlerContext,
                          RpcProtocol<Object> msg, ByteBuf byteBuf) throws Exception {

        RpcHeader header = msg.getHeader();

        // 魔数
        byteBuf.writeShort(header.getMagic());
        byteBuf.writeByte(header.getMsgType());
        byteBuf.writeByte(header.getStatus());
        byteBuf.writeLong(header.getRequestId());

        // 序列化类型 : jdk cglib ...
        String serializationType = header.getSerializationType();

        // TODO : Serialization 是扩展点
        Serialization serialization = getJdkSerialization();
        byteBuf.writeBytes(SerializationUtils.paddingString(serializationType)
                .getBytes(StandardCharsets.UTF_8));

        // 对消息体进行序列化
        byte[] data = serialization.serialization(msg.getBody());

        // 添加消息体, 消息的长度 dataLength
        byteBuf.writeInt(data.length);
        byteBuf.writeBytes(data);
    }
}
