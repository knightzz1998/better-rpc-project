package io.knightzz.rpc.test.spi.service.impl;

import io.knightzz.rpc.spi.annotation.SPIClass;
import io.knightzz.rpc.test.spi.service.SPIService;

/**
 * @author 王天赐
 * @title: SPIServiceImpl
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-13 20:49
 */
@SPIClass
public class SPIServiceImpl implements SPIService {


    @Override
    public String hello(String name) {
        return "hello : " + name;
    }
}
