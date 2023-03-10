package io.knightzz.rpc.spi.loader;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author 王天赐
 * @title: ExtensionLoader
 * @projectName better-rpc-project
 * @description:
 * @website <a href="https://knightzz.cn/">https://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-03-10 20:53
 */
public final class ExtensionLoader<T> {
    // TODO ? 这里为什么要加 final

    private static final Logger LOGGER = LoggerFactory.getLogger(ExtensionLoader.class);

    private static final String SERVICES_DIRECTORY = "META-INF/services/";
    private static final String BETTER_RPC_DIRECTORY = "META-INF/better-rpc/";

    /**
     * 存储第三方提供的文件, 可以被其他模块或应用程序使用
     * 我们可以在 external 目录中创建一个以服务接口全限定名为名称的文件，文件中包含服务提供者的全限定名
     * 这样其他模块或应用程序就可以使用这个服务提供者
     */
    private static final String BETTER_RPC_DIRECTORY_EXTERNAL = "META-INF/better-rpc/external/";

    /**
     * internal 目录：这个目录中包含的文件是由模块或应用程序自己提供的，只能被模块或应用程序内部使用。
     * 比如，我们可以在 internal 目录中创建一个以服务接口全限定名为名称的文件，文件中包含模块或应用程序自己提供的服务提供者的全限定名，这样只有模块或应用程序内部的代码才能使用这个服务提供者。
     */
    private static final String BETTER_RPC_DIRECTORY_INTERNAL = "META-INF/better-rpc/internal/";

    private static final String[] SPI_DIRECTORY = new String[]{
            SERVICES_DIRECTORY,
            BETTER_RPC_DIRECTORY,
            BETTER_RPC_DIRECTORY_EXTERNAL,
            BETTER_RPC_DIRECTORY_INTERNAL
    };


    /**
     * 与 Java底层类似, 每个类对应一个类加载器, 同一个Class 类加载器不同, 也被认为是不同的Class
     */
    private static final Map<Class<?>, ExtensionLoader<?>> LOADERS = new ConcurrentHashMap<>();

    private final Class<T> clazz;

    private final ClassLoader classLoader;

    /**
     * 存储 Class 类对象
     */
    private final Holder<Map<String, Class<?>>> cachedClasses = new Holder<>();

    /**
     * 缓存对象
     */
    private final Map<String, Holder<Object>> cachedInstances = new ConcurrentHashMap<>();

    /**
     * 缓存SPI实现类的对象
     */
    private final Map<Class<?>, Object> spiClassInstances = new ConcurrentHashMap<>();

    private String cachedDefaultName;


    public ExtensionLoader(final Class<T> clazz, final ClassLoader classLoader) {
        this.clazz = clazz;
        this.classLoader = classLoader;

        // TODO ...


    }

    /**
     * 获取指定 Class 对象的扩展类加载器
     * @param clazz 被加载的 Clazz 类
     * @param cl 类加载器实例
     * @return 类加载器对象
     * @param <T> 泛型
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz, final ClassLoader cl) {

        // 校验 clazz 是否为 null
        Objects.requireNonNull(clazz, "extension class is null ");

        // 校验 clazz 是否是接口
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") is not interface!");
        }

        // 根据 Class 对象, 从LOADERS总获取对应的类加载器
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) LOADERS.get(clazz);
        if(Objects.nonNull(extensionLoader)) {
            return extensionLoader;
        }

        // 如果不存在, 就添加
        LOADERS.putIfAbsent(clazz, new ExtensionLoader<>(clazz, cl));
        return (ExtensionLoader<T>) LOADERS.get(clazz);
    }

    private static class Holder<T>{

        /**
         * volatile 保证在多线程的环境下, value的值被修改可以在其他线程立马生效
         */
        private volatile T value;

        public T getValue() {
            return value;
        }

        public void setValue(T value) {
            this.value = value;
        }
    }

}
