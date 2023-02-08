package io.knightzz.rpc.test.other.jar;

import org.junit.Test;

import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

/**
 * @author 王天赐
 * @title: JarFileReadTest
 * @projectName better-rpc-project
 * @description: 测试Jar包的读取
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-08 15:51
 */
public class JarFileReadTest {

    @Test
    public void testClassPath() {

        URL url = Thread.currentThread().getContextClassLoader().getResource("");

        // file:/K:/CodeSpace/bh/better-rpc-project/better-rpc-test/better-rpc-test-other/target/classes/
        System.out.println(url);
        // 读取jar包
    }

    @Test
    public void testJarPath() throws IOException {

        String filePath = "io/knightzz/rpc/test/other/jar";

        URL url = Thread.currentThread().
                getContextClassLoader().getResource("better-rpc-test-scanner-1.0-SNAPSHOT.jar");
    }
}
