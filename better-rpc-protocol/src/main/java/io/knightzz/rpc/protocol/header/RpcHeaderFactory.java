package io.knightzz.rpc.protocol.header;

import com.sun.xml.internal.bind.v2.model.core.ID;
import io.knightzz.rpc.common.id.IdFactory;
import io.knightzz.rpc.constants.RpcConstants;
import io.knightzz.rpc.protocol.enumeration.RpcType;

/**
 * @author 王天赐
 * @title: RpcHeaderFactory
 * @projectName better-rpc-project
 * @description: 请求头工厂类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 16:28
 */
public class RpcHeaderFactory {

    public static RpcHeader getRpcHeader(String serializationType){
        RpcHeader header = new RpcHeader();

        Long requestId = IdFactory.getId();
        header.setMagic(RpcConstants.MAGIC);
        header.setRequestId(requestId);
        // 设置消息类型为请求类型
        header.setMsgType((byte) RpcType.REQUEST.getType());
        // 设置状态
        header.setStatus((byte) 0x1);
        header.setSerializationType(serializationType);
        return header;
    }
}
