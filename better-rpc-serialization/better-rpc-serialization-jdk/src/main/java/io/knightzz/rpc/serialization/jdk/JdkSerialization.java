package io.knightzz.rpc.serialization.jdk;

import io.knightzz.rpc.common.exception.SerializationException;
import io.knightzz.rpc.serialization.api.Serialization;
import io.knightzz.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * @author 王天赐
 * @title: JdkSerialization
 * @projectName better-rpc-project
 * @description: JDK序列化实现类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-13 14:47
 */
@SPIClass
public class JdkSerialization implements Serialization {

    private final Logger logger = LoggerFactory.getLogger(JdkSerialization.class);

    @Override
    public <T> byte[] serialization(T obj) {

        logger.info("exec jdk serialize ... ");

        if (obj == null) {
            throw new SerializationException("serialization object is null !");
        }
        // 实现思路 :
        // 通过Byte流的形式将对象转换成 byte 数组
        try {
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            ObjectOutputStream out = new ObjectOutputStream(os);
            // 将指定的对象写入 ObjectOutputStream
            // 可以使用 writeObject 和 readObject 方法重写类的默认序列化
            out.writeObject(obj);
            return os.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }

    @Override
    public <T> T deserialization(byte[] data, Class<T> cls) {

        logger.info("exec jdk deserialize ... ");

        if (data == null) {
            throw new SerializationException("deserialization data is null !");
        }
        try {
            ByteArrayInputStream is = new ByteArrayInputStream(data);
            ObjectInputStream in = new ObjectInputStream(is);
            return (T) in.readObject();
        } catch (Exception e) {
            throw new SerializationException(e.getMessage(), e);
        }
    }
}
