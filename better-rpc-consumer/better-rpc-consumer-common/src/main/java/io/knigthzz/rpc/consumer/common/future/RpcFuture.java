package io.knigthzz.rpc.consumer.common.future;

import io.knightzz.rpc.protocol.RpcProtocol;
import io.knightzz.rpc.protocol.request.RpcRequest;
import io.knightzz.rpc.protocol.response.RpcResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.concurrent.locks.AbstractQueuedSynchronizer;

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

    public RpcFuture(RpcProtocol<RpcRequest> rpcRequestRpcProtocol) {

        this.sync = new Sync();
        this.requestRpcProtocol = rpcRequestRpcProtocol;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public boolean isDone() {
        return sync.isDone();
    }

    @Override
    public Object get() throws InterruptedException, ExecutionException {
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
            throw new RuntimeException("Request Timeout Exception ! Request Id : "
                    + requestId + " Request Class Name : " +
                    className + " Rpc Method Name : " + methodName
            );

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

        long responseTime = System.currentTimeMillis() - startTime;
        if (responseTime > this.responseTimeThread) {
            logger.warn("Serivice Response time is too slow. Request Id = " + responseRpcProtocol.getHeader().getRequestId()
                    + ". Response Time = " + responseTime + "ms"
            );
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
}

