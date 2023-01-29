package io.knightzz.rpc.annotation;

import org.springframework.stereotype.Component;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author 王天赐
 * @title: RpcService
 * @projectName better-rpc-project
 * @description: 服务提供者注解接口
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-01-29 21:07
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Component
public @interface RpcService {


    /**
     * 接口的 Class 对象
     * @return void.class
     */
    Class<?> interfaceClass() default void.class;


    /**
     * 接口的全类名
     * @return 默认返回 ""
     */
    String interfaceClassName() default "";

    /**
     * 默认版本号
     * @return 默认返回 1.0.0
     */
    String version() default "1.0.0";

    /**
     * 服务分组, 默认为空
     * @return 默认是 ""
     */
    String group() default "";
}
