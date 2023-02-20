package io.knightzz.rpc.provider.common.handler;

import io.knightzz.rpc.common.helper.RpcServiceHelper;
import io.knightzz.rpc.common.threadpool.ServerThreadPool;
import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.enumeration.RpcStatus;
import io.knightzz.rpc.protocol.enumeration.RpcType;
import io.knightzz.rpc.protocol.header.RpcHeader;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.protocol.response.RpcResponse;
import io.netty.channel.ChannelFuture;
import io.netty.channel.ChannelFutureListener;
import io.netty.channel.ChannelHandlerContext;
import io.netty.channel.SimpleChannelInboundHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Method;
import java.util.Map;

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


        ServerThreadPool.submit(() -> {

            // 获取请求头
            RpcHeader header = protocol.getHeader();
            // 设置请求头消息类型为 REQUEST -> RESPONSE
            header.setMsgType((byte) RpcType.RESPONSE.getType());

            // 获取请求体
            RpcRequest request = protocol.getBody();

            logger.debug("receive request id : {}", header.getRequestId());

            RpcProtocol<RpcResponse> responseRpcProtocol = new RpcProtocol<>();

            // 封装响应结果
            RpcResponse response = new RpcResponse();


            try {
                Object result = handle(request);
                response.setResult(result);
                response.setAsync(request.isAsync());
                response.setOneway(request.isOneway());
                // 设置响应头状态
                header.setStatus((byte) RpcStatus.SUCCESS.getCode());
            } catch (Throwable t) {
                // 异常情况下的处理
                response.setError(t.toString());
                header.setStatus((byte) RpcStatus.FAIL.getCode());
                logger.error("Rpc server handle request error : ", t);
            }

            responseRpcProtocol.setHeader(header);
            responseRpcProtocol.setBody(response);

            // 将数据响应给消费者
            ctx.writeAndFlush(responseRpcProtocol).addListener(new ChannelFutureListener() {
                @Override
                public void operationComplete(ChannelFuture channelFuture) throws Exception {
                    logger.debug("Send response for request {}", header.getRequestId());
                }
            });

        });
    }

    /**
     * 这个方法的主要作用通过消费者传过来的ServiceName等信息调用服务提供者真实的方法
     *
     * @param request
     * @return 调用真实方法的返回值
     */
    private Object handle(RpcRequest request) throws Throwable {

        // 获取调用类的相关信息, 组成ServiceBeanKey
        String className = request.getClassName();
        String version = request.getVersion();
        String group = request.getGroup();

        // 查询hashmap中是否已经实例化过了
        String serviceBeanKey = RpcServiceHelper.buildServiceKey(className, version, group);

        Object serviceBean = handlerMap.get(serviceBeanKey);

        if (serviceBean == null) {
            // 抛出异常
            throw new RuntimeException(
                    String.format("service not exist : %s:%s:%s", className, version, group));
        }

        Class<?> serviceClass = serviceBean.getClass();
        // 获取消费者传过来的服务调用方法, 以及相关参数类型
        String methodName = request.getMethodName();
        Object[] parameters = request.getParameters();
        Class<?>[] parameterTypes = request.getParameterTypes();

        // 打印参数列表和参数值
        logger.debug(serviceClass.getName());
        logger.debug(methodName);
        showMethodInfo(parameterTypes, parameters);

        return invokeMethod(serviceBean, serviceClass, methodName, parameterTypes, parameters);
    }

    /**
     * 打印参数列表以及参数类型列表
     * @param parameterTypes 参数类型列表
     * @param parameters 参数列表
     */
    private void showMethodInfo(Class<?>[] parameterTypes, Object[] parameters) {

        if (parameterTypes != null && parameterTypes.length > 0) {
            for (int i = 0; i < parameterTypes.length; i++) {
                logger.debug(parameterTypes[i].getName());
            }
        }

        if (parameters != null && parameters.length > 0) {
            for (int i = 0; i < parameters.length; i++) {
                logger.debug(parameters[i].toString());
            }
        }

    }

    private Object invokeMethod(Object serviceBean, Class<?> serviceClass,
                                String methodName, Class<?>[] parameterTypes, Object[] parameters)
            throws Throwable {
        Method method = serviceClass.getMethod(methodName, parameterTypes);
        method.setAccessible(true);
        return method.invoke(serviceBean, parameters);
    }
}
