package io.knightzz.rpc.annotation;

/**
 * @author 王天赐
 * @title: RpcReference
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-01-29 22:12
 */
public @interface RpcReference {

    /**
     * 版本号
     * @return 默认是 1.0.0
     */
    String version() default "1.0.0";

    /**
     * 注册中心类型, 目前支持的类型包含 : zookeeper, nacos、etcd、consul
     * @return 默认注册中心类型的 zookeeper
     */
    String registryType() default "zookeeper";


    /**
     * 注册地址
     * @return 默认地址 "127.0.0.1:2181"
     */
    String registryAddress() default "127.0.0.1:2181";

    /**
     * 负载均衡算法类型
     * @return 默认是基于 zookeeper 的一致性hash
     */
    String loadBalanceType() default "zkconsistenthash";

    /**
     * 序列化类型, 支持类型有 protostuff, kryo、json、jdk、hessian2、fst
     * @return
     */
    String serializationType() default "protostuff";

    /**
     * 超时时间
     * @return 默认是 5 s
     */
    long timeout() default 5000;

    /**
     * 是否异步执行
     * @return 默认是 false
     */
    boolean async() default false;

    /**
     * 是否单向调用
     * @return 默认 false
     */
    boolean oneway() default false;

    /**
     * 代理的类型 支持 jdk：jdk代理， javassist: javassist代理, cglib: cglib代理
     * @return 默认是 jdk
     */
    String proxy() default "jdk";

    /**
     * 服务分组
     * @return 返回服务分组名称, 默认是 ""
     */
    String group() default "";

}
