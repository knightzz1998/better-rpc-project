package io.knightzz.rpc.common.utils;

import java.util.stream.IntStream;

/**
 * @author 王天赐
 * @title: SerializationUtils
 * @projectName better-rpc-project
 * @description: 序列化工具类, 对消息头中的序列化类型进行处理, 约定最大序列化类型长度为 16
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-13 11:17
 */
public class SerializationUtils {

    /**
     * 不足 16 位的补0
     */
    public final static String PADDING_STRING = "0";

    /**
     * 约定序列化类型最大长度为 16
     */
    public final static int MAX_SERIALIZATION_TYPE_COUNT = 16;

    /**
     * 为不足16位的序列化类型补0
     * @param str 原始字符串
     * @return 处理后的字符串
     */
    public static String paddingString(String str) {

        // 如果字符串是null => ""
        str = transNullToEmpty(str);

        if(str.length() >= MAX_SERIALIZATION_TYPE_COUNT) {
            return str;
        }
        // 计算补0的长度
        int paddingCount = MAX_SERIALIZATION_TYPE_COUNT - str.length();
        StringBuilder paddingString = new StringBuilder(str);

        // 补0
        IntStream.range(0, paddingCount).forEach((i) -> {
            paddingString.append(PADDING_STRING);
        });

        return paddingString.toString();
    }

    /**
     * 去除字符中的0字符
     * @param str 处理前的字符串
     * @return 处理后的字符串
     */
    public static String subString(String str){
        str = transNullToEmpty(str);
        return str.replace(PADDING_STRING, "");
    }

    /**
     * 如果 str == null , 就直接返回空串
     * @param str 原始字符串
     * @return 处理后的字符串
     */
    public static String transNullToEmpty(String str) {
        return str == null ? "" : str;
    }
}
