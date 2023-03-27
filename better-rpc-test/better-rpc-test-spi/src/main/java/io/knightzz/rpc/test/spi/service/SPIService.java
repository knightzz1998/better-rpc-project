package io.knightzz.rpc.test.spi.service;

import io.knightzz.rpc.spi.annotation.SPI;

/**
 * @author 王天赐
 * @title: SPIService
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-13 20:48
 */
@SPI("spiService")
public interface SPIService {

    String hello(String name);

}
