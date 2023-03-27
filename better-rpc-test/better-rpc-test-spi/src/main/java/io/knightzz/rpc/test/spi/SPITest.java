package io.knightzz.rpc.test.spi;

import io.knightzz.rpc.spi.loader.ExtensionLoader;
import io.knightzz.rpc.test.spi.service.SPIService;

/**
 * @author 王天赐
 * @title: SPITest
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-13 21:00
 */
public class SPITest {

    public static void main(String[] args) {

        // name => @SPI("name")
        SPIService spiService = ExtensionLoader.getExtension(SPIService.class, "spiService");

        // 1. 先把 SPIService.class

        String result = spiService.hello("knightzz");
        System.out.println(result);
    }
}
