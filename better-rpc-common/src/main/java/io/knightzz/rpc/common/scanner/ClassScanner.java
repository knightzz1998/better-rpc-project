package io.knightzz.rpc.common.scanner;

import org.apache.tools.ant.taskdefs.optional.clearcase.CCMkdir;

import java.io.File;
import java.io.FileFilter;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;


/**
 * @author 王天赐
 * @title: ClassScanner
 * @projectName better-rpc-project
 * @description: 通用扫描器类
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-02 13:05
 */
public class ClassScanner {

    /**
     * 路径协议 , file:// 开头的
     */
    private static final String PROTOCOL_FILE = "file";
    private static final String PROTOCOL_JAR = "jar";
    private static final String CLASS_FILE_SUFFIX = ".class";

    /**
     * 获取指定包下所有的class类
     *
     * @param packageName 包名
     * @return 返回该包下所有的class类
     */
    public static List<String> getClassNameList(String packageName) throws IOException {

        List<String> classNameList = new ArrayList<>();
        // 是否循环迭代子目录
        boolean recursive = true;
        // 1. 将 packageName 转换为 实际的路径
        String packageDirName = packageName.replace(".", "/");

        // 去 classPath 下面找到真实的路径
        Enumeration<URL> urls = Thread.currentThread().getContextClassLoader().getResources(packageDirName);
        // 2. 迭代这个路径下的所有文件(包括目录)
        while (urls.hasMoreElements()) {

            URL url = urls.nextElement();

            String protocol = url.getProtocol();

            // 如果是以文件的形式保存在服务器上
            if (PROTOCOL_FILE.equals(protocol)) {
                // 获取包的物理路径
                String filePath = URLDecoder.decode(url.getFile(), "UTF-8");
                findAndAddClassesInPackageByFile(packageName, filePath, recursive, classNameList);
            } else if (PROTOCOL_JAR.equals(protocol)) {
                // 如果是以jar包的形式存在
                packageName = findAndAddClassesInPackageByJar(packageName, classNameList,
                        recursive, packageDirName, url);
            }
        }
        return classNameList;
    }


    /**
     * 查找指定目录下的class文件
     *
     * @param packageName   包名
     * @param packagePath   包路径
     * @param recursive     是否递归调用(子目录)
     * @param classNameList 存储类名的List
     */
    private static void findAndAddClassesInPackageByFile(String packageName, String packagePath,
                                                         final boolean recursive,
                                                         List<String> classNameList
    ) {
        // 获取这个包的目录, 创建File文件
        // 正产情况 : packagePath 是一个目录
        File dir = new File(packagePath);

        // 如果文件不存在,  或者也不是目录, 就直接返回
        if (!dir.exists() || !dir.isDirectory()) {
            return;
        }

        // 如果存在, 就获取包下面所有的文件
        File[] listFiles = dir.listFiles(new FileFilter() {
            // 自定义过滤规则 :
            // 保留 以 .class结尾的 文件 或者 目录(后面处理)
            @Override
            public boolean accept(File file) {

                return (file.getName().endsWith(CLASS_FILE_SUFFIX))
                        || (recursive && file.isDirectory());
            }
        });

        // 将类名加入到ClassList中
        for (File file : listFiles) {

            // 判断下是以class结尾的文件还是目录
            if (file.isDirectory()) {
                // 如果是目录, 就递归调用当前方法区处理子目录

                String newPackageName = packageName + "." + file.getName();
                String newPath = file.getAbsolutePath();

                findAndAddClassesInPackageByFile(newPackageName,
                        newPath, recursive, classNameList);
            } else {
                // 不是目录, 是clas文件
                String fileName = file.getName();
                // fileName 是 Hello.class => Hello
                // className = cn.knightzz.xxx + Hello
                String className = packageName + "." + fileName.substring(0, fileName.length() - 6);
                classNameList.add(className);
            }
        }
    }

    /**
     * 扫描jar文件指定包下的所以类文件信息
     *
     * @param packageName    - 扫描的包名
     * @param classNameList  - 扫描完成, 存放类名信息的集合
     * @param recursive      - 是否递归调用(扫描子文件夹)
     * @param packageDirName - 当前包名前面部分的名称
     * @param url            - 包的URL地址
     * @return 处理后的包名, 以方便下次调用
     */
    private static String findAndAddClassesInPackageByJar(String packageName,
                                                          List<String> classNameList,
                                                          boolean recursive,
                                                          String packageDirName,
                                                          URL url
    ) throws IOException {

        // 获取Jar包的URL
        JarURLConnection connection = (JarURLConnection) url.openConnection();
        JarFile jar = connection.getJarFile();

        // 从这个jar包得到一个枚举类
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {

            // 获取jar里的一个实体 JarEntry 可以是目录 和一些jar包里的其他文件 如META-INF等文件
            JarEntry entry = entries.nextElement();

            // 获取实体名字
            String name = entry.getName();
            // 如果是以 / 开头的
            if (name.charAt(0) == '/') {
                // 获取 / 后面的字符串
                name = name.substring(1);
            }

            // 如果前半部分的定义和包名相同
            if (name.startsWith(packageDirName)) {

                // 返回 "/" 在 name 中出现的最后一个下标位置
                int idx = name.lastIndexOf("/");
                // 如果以 / 结尾, 说明是一个包
                if (idx != -1) {
                    packageName = name.substring(0, idx).replace("/", ".");
                }

                // 如果可以迭代下去, 并且是一个包
                if (idx != -1 && recursive) {

                    // 如果是一个class文件, 而不是目录
                    if (name.endsWith(CLASS_FILE_SUFFIX) && !entry.isDirectory()) {
                        // 去掉后面的 .class , 获取真正的类名
                        // io.knightzz.rpc.annotation.RpcReference.class => RpcReference
                        String className = name.substring(packageName.length() + 1, name.length() - 6);
                        classNameList.add(className);
                    }

                }
            }
        }
        return packageName;
    }

}
