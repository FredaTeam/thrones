package org.freda.thrones.framework.manager.support;

import org.freda.thrones.framework.common.URL;
import org.freda.thrones.framework.manager.invoke.Invoker;
import org.freda.thrones.framework.manager.proxy.JdkProxyFactory;
import org.freda.thrones.framework.manager.proxy.ProxyFactory;
import org.freda.thrones.framework.utils.DemoService;
import org.freda.thrones.framework.utils.DemoServiceImpl;
import org.junit.Assert;
import org.junit.Test;

/**
 * Create on 2018/9/19 21:42
 */
public class ThronesKernelTest {

    @Test
    public void testExport() {
        DemoService demoService = new DemoServiceImpl();

        ThronesKernel thronesKernel = new ThronesKernel();

        ProxyFactory jdkProxyFactory = new JdkProxyFactory();

        URL url = URL.valueOf("thrones://127.0.0.1:9091/" + DemoService.class.getName());
        thronesKernel.export(jdkProxyFactory.getInvoker(demoService, DemoService.class, url));

        Invoker<DemoService> invoker = thronesKernel.refer(DemoService.class, url);
        DemoService proxyService = jdkProxyFactory.getProxy(invoker);

//        proxyService.echo("mike");
//        Assert.assertEquals("mike", proxyService.echo("mike"));
    }
}
