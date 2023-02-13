package io.knightzz.rpc.common.id;

import java.util.concurrent.atomic.AtomicLong;

/**
 * @author 王天赐
 * @title: IdFactory
 * @projectName better-rpc-project
 * @description: 简易ID工厂类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-10 16:36
 */
public class IdFactory {

    private final static AtomicLong REQUEST_ID_GEN = new AtomicLong(0);

    public static Long getId() {
        return REQUEST_ID_GEN.incrementAndGet();
    }
}
