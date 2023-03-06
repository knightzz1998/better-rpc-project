package io.knightzz.rpc.proxy.api.future;

import io.knightzz.rpc.common.threadpool.ClientThreadPool;
import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.protocol.response.RpcResponse;
import io.knightzz.rpc.proxy.api.callback.AsyncRpcCallback;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author 王天赐
 * @title: RpcFuture
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-27 09:51
 */
public class RpcFuture extends CompletableFuture<Object> {

    private Logger logger = LoggerFactory.getLogger(RpcFuture.class);

    private Sync sync;
    private RpcProtocol<RpcRequest> requestRpcProtocol;
    private RpcProtocol<RpcResponse> responseRpcProtocol;

    private long startTime;

    private long responseTimeThread = 5000;

    /**
     * 存储 回调接口 AsyncRpcCallback
     */
    private List<AsyncRpcCallback> pendingCallbacks = new ArrayList<>();

    /**
     * 可重入锁. 在添加和执行回调方法的时候加锁和解锁
     */
    private ReentrantLock lock = new ReentrantLock();

    public RpcFuture(RpcProtocol<RpcRequest> rpcRequestRpcProtocol) {

        this.sync = new Sync();
        this.requestRpcProtocol = rpcRequestRpcProtocol;
        this.startTime = System.currentTimeMillis();
    }

    /**
     * 判断当前是否是已经接收到相应结果
     *
     * @return true 表示已经接收到了响应结果 , false 表示当前尚未接收到响应结果, 等待服务提供者响应结果
     */
    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
        // TODO ?
        sync.acquire(-1);
        if (this.responseRpcProtocol != null) {
            return this.responseRpcProtocol.getBody().getResult();
        } else {
            return null;
        }
    }

    @Override
    public Object get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
        boolean success = sync.tryAcquireNanos(-1, unit.toNanos(timeout));
        if (success) {
            if (this.responseRpcProtocol != null) {
                return this.responseRpcProtocol.getBody().getResult();
            } else {
                return null;
            }
        } else {
            long requestId = this.requestRpcProtocol.getHeader().getRequestId();
            String className = this.requestRpcProtocol.getBody().getClassName();
            String methodName = this.requestRpcProtocol.getBody().getMethodName();
            throw new RuntimeException("Request Timeout Exception ! Request Id : " + requestId + " Request Class Name : " + className + " Rpc Method Name : " + methodName);

        }
    }

    @Override
    public boolean isCancelled() {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean cancel(boolean mayInterruptIfRunning) {
        throw new UnsupportedOperationException();
    }

    public void done(RpcProtocol<RpcResponse> responseRpcProtocol) {

        this.responseRpcProtocol = responseRpcProtocol;

        // 释放锁
        sync.release(1);

        // 执行回调方法
        invokeCallbacks();

        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThread) {
            logger.warn("Serivice Response time is too slow. Request Id = " + responseRpcProtocol.getHeader().getRequestId() + ". Response Time = " + responseTime + "ms");
        }

    }

    static class Sync extends AbstractQueuedSynchronizer {

        private static final long serialVersionUID = 1L;

        // future status
        // 已完成
        private final int done = 1;
        // 待办
        private final int pending = 0;


        protected boolean tryAcquire(int acquires) {
            return getState() == done;
        }

        protected boolean tryRelease(int releases) {
            if (getState() == pending) {
                if (compareAndSetState(pending, done)) {
                    return true;
                }
            }
            return false;
        }

        public boolean isDone() {
            return getState() == done;
        }
    }


    /**
     * 异步执行回调方法
     *
     * @param callback
     */
    private void runCallback(final AsyncRpcCallback callback) {

        // 获取响应消息体
        final RpcResponse response = this.responseRpcProtocol.getBody();

        // 开启线程池提交任务
        ClientThreadPool.submit(() -> {
            if (!response.isError()) {
                callback.onSuccess(response.getResult());
            } else {
                callback.onException(new RuntimeException("Rpc Response Error ", new Throwable(response.getError())));
            }
        });
    }

    /**
     * 添加回调函数, 回调函数是在接收到响应后执行的
     *
     * @param callback 实现了 AsyncRpcCallback 接口的类的实例对象
     * @return RpcFuture 对象
     */
    public RpcFuture addCallback(AsyncRpcCallback callback) {
        lock.lock();
        try {
            if (isDone()) {
                addCallback(callback);
            } else {
                // 暂时添加到集合中
                pendingCallbacks.add(callback);
            }
        } finally {
            lock.unlock();
        }
        return this;
    }

    /**
     * 依次执行pendingCallbacks集合中的回调接口的方法
     */
    public void invokeCallbacks() {
        lock.lock();
        try {
            // TODO 这里为什么要加 final ?
            // 它在循环的每次迭代中都是不可变的。如果在循环体中尝试修改 callback 的值，代码将无法通过编译
            for (final AsyncRpcCallback callback : pendingCallbacks) {
                // 这里如果令 callback = null , 就无法通过编译
                runCallback(callback);
            }
        } finally {
            lock.unlock();
        }

    }
}

