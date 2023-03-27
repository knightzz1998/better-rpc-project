package io.knightzz.rpc.spi.loader;

import io.knightzz.rpc.spi.annotation.SPI;
import io.knightzz.rpc.spi.annotation.SPIClass;
import io.knightzz.rpc.spi.factory.ExtensionFactory;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.*;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

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

        if (!Objects.equals(clazz, ExtensionFactory.class)) {
            ExtensionLoader.getExtensionLoader(ExtensionFactory.class).getExtensionClasses();
        }
    }

    /**
     * 获取指定 Class 对象的扩展类加载器
     *
     * @param clazz 被加载的 Clazz 类
     * @param cl    类加载器实例
     * @param <T>   泛型
     * @return 类加载器对象
     */
    @SuppressWarnings("unchecked")
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz, final ClassLoader cl) {

        // 校验 clazz 是否为 null
        Objects.requireNonNull(clazz, "extension class is null ");
        // 校验 clazz 是否是接口
        if (!clazz.isInterface()) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") is not interface!");
        }
        if (!clazz.isAnnotationPresent(SPI.class)) {
            throw new IllegalArgumentException("extension clazz (" + clazz + ") without @" + SPI.class + " Annotation");
        }

        // 根据 Class 对象, 从LOADERS总获取对应的类加载器
        ExtensionLoader<T> extensionLoader = (ExtensionLoader<T>) LOADERS.get(clazz);
        if (Objects.nonNull(extensionLoader)) {
            return extensionLoader;
        }

        // 如果不存在, 就添加
        LOADERS.putIfAbsent(clazz, new ExtensionLoader<>(clazz, cl));

        return (ExtensionLoader<T>) LOADERS.get(clazz);
    }

    public static <T> T getExtension(final Class<T> clazz, String name){
        return StringUtils.isEmpty(name) ? getExtensionLoader(clazz).getDefaultSpiClassInstance()
                : getExtensionLoader(clazz).getSpiClassInstance(name);
    }

    /**
     * 根据Class对象, 获取对应的扩展类实例, 默认使用 ExtensionLoader 的类加载器
     *
     * @param clazz
     * @param <T>
     * @return
     */
    public static <T> ExtensionLoader<T> getExtensionLoader(final Class<T> clazz) {
        // clazz : SPIService.class 接口类
        return getExtensionLoader(clazz, ExtensionLoader.class.getClassLoader());
    }


    /**
     * 获取默认的SPI类实例
     *
     * @return
     */
    public T getDefaultSpiClassInstance() {
        getExtensionClasses();
        if (StringUtils.isBlank(cachedDefaultName)) {
            return null;
        }
        return getSpiClassInstance(cachedDefaultName);
    }

    public T getSpiClassInstance(final String name) {

        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("get spi class name is null");
        }
        // 查看缓存中是否已经存在
        Holder<Object> objectHolder = cachedInstances.get(name);
        if(Objects.isNull(objectHolder)) {
            cachedInstances.putIfAbsent(name, new Holder<>());
            objectHolder = cachedInstances.get(name);
        }
        Object value = objectHolder.getValue();
        if (Objects.isNull(value)) {
            synchronized (cachedInstances) {
                value = objectHolder.getValue();
                if (Objects.isNull(value)) {
                    value = createExtension(name);
                    objectHolder.setValue(value);
                }
            }
        }
        return (T) value;
    }

    public List<T> getSpiClassInstances() {

        Map<String, Class<?>> extensionClasses = this.getExtensionClasses();

        if (extensionClasses.isEmpty()) {
            return Collections.emptyList();
        }

        if (Objects.equals(extensionClasses.size(), cachedInstances.size())) {
            // cachedInstances : Map<String, Holder<Object>>
            return (List<T>) this.cachedInstances.values().stream().map(e -> {
               return e.getValue();
            }).collect(Collectors.toList());
        }

        List<T> instances = new ArrayList<>();
        // 通过name获取具体的实例
        extensionClasses.forEach((name,v) -> {
            T instance = this.getSpiClassInstance(name);
            instances.add(instance);
        });

        return instances;
    }

    @SuppressWarnings("unchecked")
    private T createExtension(final String name) {

        Class<?> aClass = getExtensionClasses().get(name);

        if (Objects.isNull(aClass)) {
            throw new IllegalArgumentException("name is error");
        }

        Object o = spiClassInstances.get(aClass);

        if (Objects.isNull(o)) {
            try {
                // 如果 aClass, aClass.newInstance() 不存在, 就添加
                spiClassInstances.putIfAbsent(aClass, aClass.newInstance());
                o = spiClassInstances.get(aClass);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new IllegalStateException("Extension instance(name: " + name + ", class: "
                        + aClass + ")  could not be instantiated: " + e.getMessage(), e);
            }
        }


        return (T) o;
    }

    public Map<String, Class<?>> getExtensionClasses() {
        // 获取所有缓存的 Classes
        Map<String, Class<?>> classes = cachedClasses.getValue();

        // 如果是 null (首次加载)
        if (Objects.isNull(classes)) {
            synchronized (cachedClasses) {
                Map<String, Class<?>> value = cachedClasses.getValue();
                if (Objects.isNull(classes)) {
                    classes = loadExtensionClass();
                    cachedClasses.setValue(classes);
                }
            }
        }

        return classes;
    }

    private Map<String, Class<?>> loadExtensionClass() {

        SPI annotation = clazz.getAnnotation(SPI.class);
        if (Objects.nonNull(annotation)) {
            String value = annotation.value();
            if (StringUtils.isNotBlank(value)) {
                cachedDefaultName = value;
            }
        }
        // 加载目录下的 Classes
        Map<String, Class<?>> classes = new HashMap<>(16);
        loadDirectory(classes);
        return classes;
    }


    private void loadDirectory(final Map<String, Class<?>> classes) {
        // 遍历所有的SPI目录
        for (String directory : SPI_DIRECTORY) {

            // 分别拼接 :
            // META-INF/better-rpc/external/io.knightzz.rpc.test.spi.service.SPIService
            String fileName = directory + clazz.getName();
            // 拼接目录
            try {
                // 如果 当前类加载不为null, 就是用classLoader加载, 否则使用
                Enumeration<URL> urls = Objects.nonNull(this.classLoader) ? classLoader.getResources(fileName) :
                        ClassLoader.getSystemResources(fileName);

                if (Objects.nonNull(urls)) {
                    while (urls.hasMoreElements()) {
                        URL url = urls.nextElement();
                        // TODO 通过URL 加载
                        loadResources(classes, url);
                    }
                }

            } catch (IOException e) {
                LOGGER.error("load extension class error {}", fileName, e);
            }

        }
    }

    /**
     * 加载资源
     *
     * @param classes
     * @param url
     * @throws IOException
     */
    private void loadResources(final Map<String, Class<?>> classes, final URL url) throws IOException {

        try (InputStream inputStream = url.openStream()) {

            // 读取 META-INF 目录下的 SPI 配置文件
            Properties properties = new Properties();
            properties.load(inputStream);
            properties.forEach((k, v) -> {
                String name = (String) k;
                String classPath = (String) v;
                // 校验
                if (StringUtils.isNotBlank(name) && StringUtils.isNotBlank(classPath)) {
                    try {
                        loadClass(classes, name, classPath);
                    } catch (ClassNotFoundException e) {
                        throw new IllegalStateException("load extension resources error", e);
                    }
                }
            });


        }

    }

    /**
     * 加载 class 类
     *
     * @param classes
     * @param name
     * @param classPath
     * @throws ClassNotFoundException
     */
    private void loadClass(final Map<String, Class<?>> classes,
                           final String name, final String classPath) throws ClassNotFoundException {
        Class<?> subClass = Objects.nonNull(this.classLoader)
                ? Class.forName(classPath, true, this.classLoader)
                : Class.forName(classPath);

        // 判断 subClass 是否是 clazz 的子类
        if (!clazz.isAssignableFrom(subClass)) {
            throw new IllegalStateException("load extension resources error," + subClass + " subtype is not of " + clazz);
        }
        // 判断 subClass 是否包含 SPIClass 注解
        if (!subClass.isAnnotationPresent(SPIClass.class)) {
            throw new IllegalStateException("load extension resources error," + subClass + " without @" + SPIClass.class + " annotation");
        }

        Class<?> oldClass = classes.get(name);
        if (Objects.isNull(oldClass)) {
            // 首次加载, oldClass 肯定是 null
            classes.put(name, subClass);
        } else if (!Objects.equals(oldClass, subClass)) {

            // 假如再次加载同一个类, 就要比较缓存中和当前类是否相同,如果不同 ==> 报错

            throw new IllegalStateException("load extension resources error,Duplicate class "
                    + clazz.getName() + " name " + name + " on "
                    + oldClass.getName() + " or " + subClass.getName());
        }

    }


    public static class Holder<T> {

        /**
         * volatile 保证在多线程的环境下, value的值被修改可以在其他线程立马生效
         */
        private volatile T value;

        public T getValue() {
            return value;
        }

        public void setValue(final T value) {
            this.value = value;
        }
    }

}
