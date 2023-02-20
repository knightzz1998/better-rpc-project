package io.knightzz.rpc.common.threadpool;

import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author 王天赐
 * @title: ServerThreadPool
 * @projectName better-rpc-project
 * @description: 线程池工具类, ServerThread主要的作用是在服务提供者一端异步的执行消费者端调用的方法1
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-20 15:30
 */
public class ServerThreadPool {

    private static ThreadPoolExecutor threadPoolExecutor;

    static {
        // 初始化线程池
        threadPoolExecutor = new ThreadPoolExecutor(16, 16, 600L, TimeUnit.SECONDS,
                new ArrayBlockingQueue<Runnable>(65536));
    }

    public static void submit(Runnable task) {
        threadPoolExecutor.submit(task);
    }

    public static void shutdown() {
        threadPoolExecutor.shutdown();
    }

}
