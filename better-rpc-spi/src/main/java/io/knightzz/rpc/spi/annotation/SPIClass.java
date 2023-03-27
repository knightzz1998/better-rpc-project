package io.knightzz.rpc.spi.annotation;

import java.lang.annotation.*;

/**
 * @author 王天赐
 * @title: SPIClass
 * @projectName better-rpc-project
 * @description: 标注加入SPI接口的实现类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-10 18:57
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface SPIClass {
}
