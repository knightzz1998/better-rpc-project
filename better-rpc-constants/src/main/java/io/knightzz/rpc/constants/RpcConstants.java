package io.knightzz.rpc.constants;

/**
 * @author 王天赐
 * @title: RpcConstants
 * @projectName better-rpc-project
 * @description: 存放框架使用的常量类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 16:41
 */
public class RpcConstants {

    /**
     * 消息头固定长度
     */
    public static final int HEAD_TOTAL_LEN = 32;

    /**
     * 魔数
     */
    public static final short MAGIC = 0x10;

    /**
     * 版本号
     */
    public static final byte VERSION = 0x1;

    /**
     * 反射类型 : 基于JDK的反射
     */
    public static final String REFLECT_TYPE_JDK = "jdk";

    /**
     * 反射类型 : 基于 cglib 反射
     */
    public static final String REFLECT_TYPE_CGLIB = "cglib";

    /**
     * JDK动态代理
     */
    public static final String PROXY_JDK = "jdk";

    /**
     * javassist动态代理
     */
    public static final String PROXY_JAVASSIST = "javassist";

    /**
     * cglib动态代理
     */
    public static final String PROXY_CGLIB = "cglib";

}
