package io.knightzz.rpc.serialization.hessian2;

import com.caucho.hessian.io.Hessian2Input;
import com.caucho.hessian.io.Hessian2Output;
import io.knightzz.rpc.common.exception.SerializationException;
import io.knightzz.rpc.serialization.api.Serialization;
import io.knightzz.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author 王天赐
 * @title: Hessian2Serialization
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-28 14:38
 */
@SPIClass
public class Hessian2Serialization implements Serialization {

    private final Logger logger = LoggerFactory.getLogger(Hessian2Serialization.class);

    @Override
    public <T> byte[] serialization(T obj) {

        logger.info("execute hessian2 serialize...");
        if (obj == null) {
            throw new SerializationException("serialize object is null");
        }
        byte[] result = new byte[0];
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        Hessian2Output hessian2Output = new Hessian2Output(byteArrayOutputStream);
        try {
            hessian2Output.startMessage();
            hessian2Output.writeObject(obj);
            hessian2Output.flush();
            hessian2Output.completeMessage();
            result = byteArrayOutputStream.toByteArray();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        } finally {
            try {
                if (hessian2Output != null) {
                    hessian2Output.close();
                    byteArrayOutputStream.close();
                }
            } catch (IOException e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }

        return result;
    }

    @Override
    public <T> T deserialization(byte[] data, Class<T> cls) {
        logger.info("execute hessian2 deserialize...");
        if (data == null) {
            throw new SerializationException("deserialize data is null");
        }
        ByteArrayInputStream byteInputStream = new ByteArrayInputStream(data);
        Hessian2Input hessian2Input = new Hessian2Input(byteInputStream);
        T object = null;
        try {
            hessian2Input.startMessage();
            object = (T) hessian2Input.readObject();
            hessian2Input.completeMessage();
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        } finally {
            try {
                if (null != hessian2Input) {
                    hessian2Input.close();
                    byteInputStream.close();
                }
            } catch (IOException e) {
                throw new SerializationException(e.getMessage(), e);
            }
        }
        return object;
    }
}
