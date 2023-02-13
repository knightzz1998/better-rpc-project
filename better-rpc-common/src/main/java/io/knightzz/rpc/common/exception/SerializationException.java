package io.knightzz.rpc.common.exception;

/**
 * @author 王天赐
 * @title: SerializationException
 * @projectName better-rpc-project
 * @description: 序列化异常类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-13 11:04
 */
public class SerializationException extends RuntimeException{

    private static final long serialVersionUID = -6783134254669118520L;

    public SerializationException(final Throwable e) {
        super(e);
    }

    public SerializationException(String message) {
        super(message);
    }

    public SerializationException(String message, Throwable cause) {
        super(message, cause);
    }
}
