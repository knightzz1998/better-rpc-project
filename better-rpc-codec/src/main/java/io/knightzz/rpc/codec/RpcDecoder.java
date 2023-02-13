package io.knightzz.rpc.codec;

import io.knightzz.rpc.common.utils.SerializationUtils;
import io.knightzz.rpc.constants.RpcConstants;
import io.knightzz.rpc.protocol.enumeration.RpcType;
import io.knightzz.rpc.protocol.header.RpcHeader;
import io.netty.buffer.ByteBuf;
import io.netty.channel.ChannelHandlerContext;
import io.netty.handler.codec.ByteToMessageDecoder;

import java.util.List;

import static java.nio.charset.StandardCharsets.UTF_8;

/**
 * @author 王天赐
 * @title: RpcDecoder
 * @projectName better-rpc-project
 * @description: RPC解码器
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-13 16:21
 */
public class RpcDecoder extends ByteToMessageDecoder implements RpcCodec {
    @Override
    protected void decode(ChannelHandlerContext channelHandlerContext,
                          ByteBuf in, List<Object> out) throws Exception {

        // 可读消息 < RpcConstants.HEAD_TOTAL_LEN 消息头, 说明不是完整消息
        if(in.readableBytes() < RpcConstants.HEAD_TOTAL_LEN) {
            return;
        }

        // https://blog.csdn.net/qq_35930102/article/details/124907282
        // mark 操作会将当前指针备份到 positionIndex 中
        in.markReaderIndex();

        // [magic, msgType, status, request, body]
        // 这个读取顺序要和 RpcEncoder的顺序一致
        short magic = in.readShort();

        if(magic != RpcConstants.MAGIC) {
            throw new IllegalArgumentException("the magic number is illegal, " + magic);
        }

        // 消息类型
        byte msgType = in.readByte();
        // 消息状态
        byte status = in.readByte();
        // 消息id
        long requestId = in.readLong();

        // 序列化类型
        ByteBuf serializationTypeBuf =  in.readBytes(SerializationUtils.MAX_SERIALIZATION_TYPE_COUNT);
        // 对序列化后的数据移除0
        String serializationType = SerializationUtils
                .subString(serializationTypeBuf.toString(UTF_8));


        // 获取消息体的长度
        int dataLength = in.readInt();
        // 如果剩下可读的字节数小于消息体的长度, 说明消息体数据丢失
        if(in.readableBytes() < dataLength) {
            // 重置读指针位置
            in.resetReaderIndex();
            return;
        }

        // 将消息类型转换成对应的注解
        RpcType msgTypeEnum = RpcType.findByType(msgType);
        // 判断消息类型是否为空
        if(msgTypeEnum == null) {
            return;
        }

        // 将获取到的信息重新封装成Rpc对象
        RpcHeader header = new RpcHeader();

        header.setMagic(magic);
        header.setMsgType(msgType);
        header.setStatus(status);
        header.setRequestId(requestId);
    }
}
