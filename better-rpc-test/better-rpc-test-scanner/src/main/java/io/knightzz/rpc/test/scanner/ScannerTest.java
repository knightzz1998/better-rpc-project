package io.knightzz.rpc.test.scanner;

import io.knightzz.rpc.common.scanner.ClassScanner;
import io.knightzz.rpc.common.scanner.server.reference.RpcReferenceScanner;
import io.knightzz.rpc.provider.common.scanner.RpcServiceScanner;
import org.junit.Test;

import java.io.IOException;
import java.util.List;

/**
 * @author 王天赐
 * @title: ScannerTest
 * @projectName better-rpc-project
 * @description: 测试扫描器逻辑
 * @website <a href="http://knightzz.cn/">http://knightzz.cn/</a>
 * @github <a href="https://github.com/knightzz1998">https://github.com/knightzz1998</a>
 * @create: 2023-02-07 20:10
 */
public class ScannerTest {

    @Test
    public void testScannerClassNameList() throws IOException {

        List<String> classNameList = ClassScanner.
                getClassNameList("io.knightzz.rpc.test.scanner");

        classNameList.forEach(System.out::println);
    }


    @Test
    public void testScannerClassNameListByRpcService() throws Exception {

        // RpcServiceScanner.
        //        doScannerWithRpcServiceAnnotationFilterAndRegistryService(
        //                "127.0.0.1", 27880,
        //                "io.knightzz.rpc.test.scanner");

    }

    @Test
    public void testScannerClassNameListByRpcReference() throws IOException {

        RpcReferenceScanner.doScannerWithRpcReferenceFilter("io.knightzz.rpc.test.scanner");

    }
}
