package io.knightzz.rpc.protocol;

import io.knightzz.rpc.protocol.header.RpcHeader;
import io.knightzz.rpc.protocol.header.RpcHeaderFactory;
import io.knightzz.rpc.protocol.request.RpcRequest;

/**
 * @author 王天赐
 * @title: RpcProtocolTest
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 17:27
 */
class RpcProtocolTest {

    public static void main(String[] args) {

        RpcHeader header = RpcHeaderFactory.getRequestHeader("jdk");
        RpcRequest body = new RpcRequest();
        body.setOneway(false);
        body.setAsync(false);
        body.setClassName("io.knightzz.rpc.protocol.RpcProtocol");
        body.setMethodName("hello");
        body.setGroup("knightzz");
        body.setParameters(new Object[]{"knightzz"});
        body.setParameterTypes(new Class[]{String.class});
        body.setVersion("1.0.0");
        RpcProtocol<RpcRequest> protocol = new RpcProtocol<>();
        protocol.setBody(body);
        protocol.setHeader(header);

        System.out.println(protocol);
    }
}