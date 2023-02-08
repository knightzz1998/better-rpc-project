package io.knightzz.rpc.common.scanner.server;

import io.knightzz.rpc.annotation.RpcService;
import io.knightzz.rpc.common.scanner.ClassScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author 王天赐
 * @title: RpcServiceScanner
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-04 20:18
 */
public class RpcServiceScanner extends ClassScanner {

    private static final Logger LOGGER = LoggerFactory.getLogger(RpcServiceScanner.class);

    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(String packageName) throws Exception {

        // 逻辑 :
        // 1. 通过 ClassScanner 中的getNameList方法获取包下的所有类信息
        // 2. 遍历所有得到的类, 将@RpcService注解的类过滤出来

        Map<String, Object> handlerMap = new HashMap<>();

        List<String> classNameList = getClassNameList(packageName);

        if (classNameList == null || classNameList.isEmpty()) {
            return handlerMap;
        }

        classNameList.stream().forEach((className) -> {

            // className : cn.knightzz.Hello
            try {
                Class<?> classObj = Class.forName(className);
                // 判断 classObj 是否有 RpcService注解
                RpcService rpcServiceAnnotation = classObj.getAnnotation(RpcService.class);
                if (rpcServiceAnnotation != null) {

                    // TODO : 后续逻辑需要向注册中心注册服务元数据. 以及向handlerMap中记录了标注@RpcService注解的类的实例对象
                    LOGGER.info("当前标注了@RpcService注解的类实例名称===>>> " + classObj.getName());
                    LOGGER.info("@RpcService注解上标注的属性信息如下：");
                    LOGGER.info("interfaceClass===>>> " + rpcServiceAnnotation.interfaceClass().getName());
                    LOGGER.info("interfaceClassName===>>> " + rpcServiceAnnotation.interfaceClassName());
                    LOGGER.info("version===>>> " + rpcServiceAnnotation.version());
                    LOGGER.info("group===>>> " + rpcServiceAnnotation.group());

                }

            } catch (ClassNotFoundException e) {
                LOGGER.error("scanner classes throws exception: {}", e);
            }

        });
        return handlerMap;
    }

}
