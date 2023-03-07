package io.knightzz.rpc.proxy.api.object;

import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.header.RpcHeaderFactory;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.proxy.api.async.IAsyncObjectProxy;
import io.knightzz.rpc.proxy.api.consumer.Consumer;
import io.knightzz.rpc.proxy.api.future.RpcFuture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.util.concurrent.TimeUnit;

/**
 * @author 王天赐
 * @title: ObjectProxy
 * @projectName better-rpc-project
 * @description: 动态代理的执行类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-06 15:09
 */
public class ObjectProxy<T> implements IAsyncObjectProxy, InvocationHandler {

    private final Logger logger = LoggerFactory.getLogger(ObjectProxy.class);

    private Class<?> clazz;
    private String serviceVersion;
    private String serviceGroup;

    private long timeout = 15000;

    private Consumer consumer;

    private String serializationType;

    private boolean async;

    private boolean oneway;

    public ObjectProxy(Class<?> clazz) {
        this.clazz = clazz;
    }

    public ObjectProxy(Class<?> clazz, String serviceVersion,
                       String serviceGroup, Consumer consumer,
                       String serializationType, long timeout,
                       boolean async, boolean oneway) {
        this.clazz = clazz;
        this.serviceVersion = serviceVersion;
        this.serviceGroup = serviceGroup;
        this.consumer = consumer;
        this.serializationType = serializationType;
        this.timeout = timeout;
        this.async = async;
        this.oneway = oneway;
    }

    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {

        // TODO : 这段代码是什么意思 ?
        if (Object.class == method.getDeclaringClass()) {
            String name = method.getName();
            if ("equals".equals(name)) {
                return proxy == args[0];
            } else if ("hashCode".equals(name)) {
                // System.identityHashCode(Object x)方法返回指定对象的哈希码，哈希码是一个int值，由对象的内存地址计算而来。
                // 与Object类的hashCode()方法不同的是，identityHashCode()方法不会计算对象的属性值或状态，而是直接返回对象的内存地址的哈希码
                return System.identityHashCode(proxy);
            } else if ("toString".equals(name)) {
                return proxy.getClass().getName() + "@" + Integer.toHexString(System.identityHashCode(proxy)) + ", with InvocationHandler" + this;
            } else {
                throw new IllegalStateException(String.valueOf(method));
            }
        }

        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();
        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType));
        RpcRequest request = new RpcRequest();
        request.setVersion(this.serviceVersion);
        request.setClassName(method.getDeclaringClass().getName());
        request.setMethodName(method.getName());
        request.setParameterTypes(method.getParameterTypes());
        request.setGroup(this.serviceGroup);
        request.setParameters(args);
        request.setAsync(async);
        request.setOneway(oneway);
        requestRpcProtocol.setBody(request);

        // Debug
        logger.debug(method.getDeclaringClass().getName());
        logger.debug(method.getName());

        // 打印参数类型
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (parameterTypes != null && parameterTypes.length > 0) {
            for (int i = 0; i < parameterTypes.length; i++) {
                logger.debug(parameterTypes[i].getName());
            }
        }

        // 调用当前消费者类, 发送请求
        RpcFuture rpcFuture = this.consumer.sendRequest(requestRpcProtocol);

        // 单向请求
        if (rpcFuture == null) {
            return null;
        }
        return timeout > 0 ? rpcFuture.get(timeout, TimeUnit.MILLISECONDS) : rpcFuture.get();
    }

    @Override
    public RpcFuture call(String methodName, Object... args) {

        RpcProtocol<RpcRequest> requestRpcProtocol = createRequest(this.clazz.getName(), methodName, args);

        RpcFuture rpcFuture = null;
        try {
            rpcFuture = this.consumer.sendRequest(requestRpcProtocol);
        } catch (Exception e) {
            logger.error("async all throws exception ", e);
        }

        return rpcFuture;
    }

    public RpcProtocol<RpcRequest> createRequest(String className, String methodName, Object[] args) {

        RpcProtocol<RpcRequest> requestRpcProtocol = new RpcProtocol<>();

        requestRpcProtocol.setHeader(RpcHeaderFactory.getRequestHeader(serializationType));

        RpcRequest request = new RpcRequest();

        request.setClassName(className);
        request.setMethodName(methodName);
        request.setVersion(this.serviceVersion);
        request.setParameters(args);
        request.setGroup(this.serviceGroup);
        request.setAsync(this.async);
        request.setOneway(this.oneway);

        Class[] parameterTypes = new Class[args.length];
        for (int i = 0; i < args.length; i++) {
            parameterTypes[i] = getClassType(args[i]);
        }

        request.setParameterTypes(parameterTypes);
        requestRpcProtocol.setBody(request);

        logger.debug(className);
        logger.debug(methodName);
        for (int i = 0; i < parameterTypes.length; ++i) {
            logger.debug(parameterTypes[i].getName());
        }
        for (int i = 0; i < args.length; ++i) {
            logger.debug(args[i].toString());
        }

        return requestRpcProtocol;
    }

    private Class getClassType(Object obj) {

        Class<?> classType = obj.getClass();

        String typeName = classType.getName();

        switch (typeName) {
            case "java.lang.Integer":
                return Integer.TYPE;
            case "java.lang.Long":
                return Long.TYPE;
            case "java.lang.Float":
                return Float.TYPE;
            case "java.lang.Double":
                return Double.TYPE;
            case "java.lang.Character":
                return Character.TYPE;
            case "java.lang.Boolean":
                return Boolean.TYPE;
            case "java.lang.Short":
                return Short.TYPE;
            case "java.lang.Byte":
                return Byte.TYPE;
        }
        return classType;
    }
}
