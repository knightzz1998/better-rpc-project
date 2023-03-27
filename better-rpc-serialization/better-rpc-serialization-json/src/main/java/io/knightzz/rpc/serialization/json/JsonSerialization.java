package io.knightzz.rpc.serialization.json;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import io.knightzz.rpc.common.exception.SerializationException;
import io.knightzz.rpc.serialization.api.Serialization;
import io.knightzz.rpc.spi.annotation.SPIClass;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.text.SimpleDateFormat;

/**
 * @author 王天赐
 * @title: JsonSerialization
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-27 15:18
 */
@SPIClass
public class JsonSerialization implements Serialization {
    private final Logger logger = LoggerFactory.getLogger(JsonSerialization.class);

    /**
     * 实现Java对象的序列化与反序列化
     */
    private static ObjectMapper objectMapper = new ObjectMapper();

    // 静态初始化 ObjectMapper对象
    static {
        SimpleDateFormat dateFormat = new SimpleDateFormat(
                "yyyy-MM-dd HH:mm:ss");
        objectMapper.setDateFormat(dateFormat);

        // 序列化策略 : Java对象在序列化时忽略值为null的属性
        objectMapper.setSerializationInclusion(JsonInclude.Include.NON_NULL);
        // 开启输出的字符串缩进, 默认是都在同一行, 不便于阅读
        objectMapper.enable(SerializationFeature.INDENT_OUTPUT);

        /*
             禁用Jackson序列化器的自动关闭目标流。
             默认情况下，Jackson序列化器在完成所有写入操作后会自动关闭目标输出流，以确保所有数据都已刷新到底层存储。
             然而，有时您可能希望在写入多个JSON对象或数组时保持目标流处于打开状态。
             此时，禁用自动关闭目标流可能会有所帮助。
         */
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_TARGET, false);
        /*
             禁用Jackson序列化器的自动关闭JSON内容
             在默认情况下，Jackson序列化器会自动关闭JSON内容，在写完一个JSON对象或数组后，会自动添加一个结束符。
             这种行为通常不会导致问题，但在某些情况下可能会导致意外的行为。
             例如，如果您正在向输出流写入多个JSON对象，而其中一些对象是嵌套的（即一个对象是另一个对象的属性），
             则自动关闭JSON内容可能会导致输出流被关闭，从而导致后续对象无法写入。
             此时，禁用自动关闭JSON内容可能会解决该问题。
         */
        objectMapper.configure(JsonGenerator.Feature.AUTO_CLOSE_JSON_CONTENT, false);


        /*
            禁用在序列化时写完一个值后立即将其刷新到底层输出流的特性。
            这可以提高性能，因为在写完一个对象后，序列化器不必立即将其刷新到输出流中
         */
        objectMapper.disable(SerializationFeature.FLUSH_AFTER_WRITE_VALUE);

        /*
            禁用在序列化时自动关闭可关闭的对象（例如InputStream和OutputStream）。
            这可以防止在序列化期间关闭应用程序自己打开的流
         */
        objectMapper.disable(SerializationFeature.CLOSE_CLOSEABLE);
        /*
            禁用在序列化空对象时抛出异常的特性。这可以防止在序列化空对象时引发异常，并使输出更加干净。
         */
        objectMapper.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS);

        /*
            禁用在反序列化时检测并抛出异常，如果有一些属性在目标Java类中没有对应的setter方法时，这些属性会被忽略。
            禁用此功能可以允许忽略不需要的属性，而不必创建setter方法。
         */
        objectMapper.disable(DeserializationFeature.FAIL_ON_IGNORED_PROPERTIES);
        objectMapper.configure(JsonParser.Feature.IGNORE_UNDEFINED, true);
    }

    @Override
    public <T> byte[] serialization(T obj) {
        logger.info("exec json serialize ... ");

        if (obj == null) {
            throw new SerializationException("serialize object is null");
        }

        byte[] bytes = new byte[0];

        try {
            bytes = objectMapper.writeValueAsBytes(obj);
        } catch (JsonProcessingException e) {
            throw new SerializationException(e.getMessage(), e);
        }
        return bytes;
    }

    @Override
    public <T> T deserialization(byte[] data, Class<T> cls) {
        logger.info("exec json deserialize ... ");
        if (data == null) {
            throw new SerializationException("deserialize data is null");
        }
        T obj = null;
        try {
            obj = objectMapper.readValue(data, cls);
        } catch (IOException e) {
            throw new SerializationException(e.getMessage(), e);
        }
        return obj;
    }
}
