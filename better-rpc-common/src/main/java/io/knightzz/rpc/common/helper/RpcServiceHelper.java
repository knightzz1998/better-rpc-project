package io.knightzz.rpc.common.helper;

/**
 * @author 王天赐
 * @title: RpcServiceHelper
 * @projectName better-rpc-project
 * @description:
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-20 16:24
 */
public class RpcServiceHelper {

    /**
     * 拼接字符串
     * @param serviceName 服务名称 : 全包名 + 类名
     * @param version 版本号
     * @param group 分组
     * @return 服务名称#服务版本号#服务分组
     */
    public static String buildServiceKey(String serviceName, String version, String group) {
        return String.join("#", serviceName, version, group);
    }
}
