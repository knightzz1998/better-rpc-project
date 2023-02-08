package io.knightzz.rpc.common.scanner.server.reference;

import io.knightzz.rpc.annotation.RpcReference;
import io.knightzz.rpc.common.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

/**
 * @author 王天赐
 * @title: RpcReferenceScanner
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-07 15:03
 */
public class RpcReferenceScanner extends ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcReferenceScanner.class);

    public static Map<String, Object> doScannerWithRpcReferenceFilter(String packageName) throws IOException {

        Map<String, Object> handlerMap = new HashMap<>();

        List<String> classNameList = getClassNameList(packageName);

        if(classNameList != null && classNameList.isEmpty()) {
            return handlerMap;
        }

        classNameList.stream().forEach((className) -> {

            // 获取Class
            try {
                Class<?> classObj = Class.forName(className);
                // 判断当前对象是否有 @RpcReference
                // 注意这里和 @RpcService 获取的区别
                // @ReReference 是注解在类的字段上的, @RpcService是
                //
                Field[] fields = classObj.getDeclaredFields();
                Stream.of(fields).forEach((field) -> {

                    RpcReference rpcReferenceAnnotation = field.getAnnotation(RpcReference.class);

                    if (rpcReferenceAnnotation != null) {
                        // TODO : 后续逻辑需要向注册中心注册服务元数据. 以及向handlerMap中记录了标注@RpcReference注解的类的实例对象
                        LOGGER.info("当前标注了@RpcReference注解的类实例名称===>>> " + classObj.getName());
                        LOGGER.info("@RpcReference注解上标注的属性信息如下：");
                        LOGGER.info("version===>>> " + rpcReferenceAnnotation.version());
                        LOGGER.info("group===>>> " + rpcReferenceAnnotation.group());
                        LOGGER.info("registryType===>>> " + rpcReferenceAnnotation.registryType());
                        LOGGER.info("registryAddress===>>> " + rpcReferenceAnnotation.registryAddress());
                    }

                });


            } catch (ClassNotFoundException e) {
                LOGGER.error("scanner classes throws exception : {}", e);
            }
        });
        return handlerMap;
    }

}
