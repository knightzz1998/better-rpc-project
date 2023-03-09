package io.knightzz.rpc.provider.common.scanner;

import io.knightzz.rpc.annotation.RpcService;
import io.knightzz.rpc.common.helper.RpcServiceHelper;
import io.knightzz.rpc.common.scanner.ClassScanner;
import io.knightzz.rpc.protocol.meta.ServiceMeta;
import io.knightzz.rpc.registry.api.RegistryService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

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


    public static Map<String, Object> doScannerWithRpcServiceAnnotationFilterAndRegistryService(
            String host, int port, String scanPackage,
            RegistryService registryService) throws Exception {

        // 逻辑 :
        // 1. 通过 ClassScanner 中的getNameList方法获取包下的所有类信息
        // 2. 遍历所有得到的类, 将@RpcService注解的类过滤出来

        Map<String, Object> handlerMap = new HashMap<>();

        List<String> classNameList = getClassNameList(scanPackage);

        if (classNameList == null || classNameList.isEmpty()) {
            return handlerMap;
        }

        classNameList.stream().forEach((className) -> {

            // className : cn.knightzz.Hello
            try {
                Class<?> classObj = Class.forName(className);
                // 判断 classObj 是否有 RpcService注解
                RpcService rpcService = classObj.getAnnotation(RpcService.class);
                if (rpcService != null) {


                    String serviceName = getServiceName(rpcService);
                    // key = 服务名称#版本名称#分组名称
                    String serviceBeanKey = RpcServiceHelper.
                            buildServiceKey(serviceName, rpcService.version(), rpcService.group());

                    // 构建元数据
                    ServiceMeta serviceMeta = new ServiceMeta(
                            serviceName,
                            rpcService.version(),
                            rpcService.group(),
                            host,
                            port
                    );
                    // 将元数据注册到服务中心
                    registryService.register(serviceMeta);

                    // TODO : 后续逻辑需要向注册中心注册服务元数据. 以及向handlerMap中记录了标注@RpcService注解的类的实例对象
                    // 服务启动后, 会自动扫描所有带 @RpcService注解的类, 然后去实例化这些类, 并把这些类存入HashMap
                    // 接口实现类的全类名 + 版本号 + 分组 去作为唯一的hashKey
                    handlerMap.put(serviceBeanKey, classObj.newInstance());
                }
            } catch (Exception e) {
                LOGGER.error("scanner classes throws exception", e);
            }
        });
        return handlerMap;
    }

    private static String getServiceName(RpcService rpcService) {

        // RpcService 标注的接口的 Class 类
        Class<?> interfaceClass = rpcService.interfaceClass();

        if (interfaceClass == null || interfaceClass == void.class) {
            // 返回接口的全类名
            return rpcService.interfaceClassName();
        }
        // interfaceClassName 是 @RpcService 注解的字段
        return interfaceClass.getName();
    }

}
