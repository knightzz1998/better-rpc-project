package io.knightzz.rpc.loadbalancer.random;

import io.knightzz.rpc.loadbalancer.api.ServiceLoadBalancer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.Random;

/**
 * @author 王天赐
 * @title: RandomServiceLoaderBalancer
 * @projectName better-rpc-project
 * @description: 基于随机算法的负载均衡类
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-09 19:37
 */
public class RandomServiceLoadBalancer<T> implements ServiceLoadBalancer<T> {

    private final Logger logger = LoggerFactory.getLogger(RandomServiceLoadBalancer.class);

    @Override
    public T select(List<T> servers, int hashCode) {

        logger.info("基于随机算法的负载均衡策略");

        if (servers == null || servers.isEmpty()) {
            return null;
        }

        Random random = new Random();
        int index = random.nextInt(servers.size());
        return servers.get(index);
    }
}
