package org.freda.thrones.framework.utils;

import com.google.common.collect.Lists;
import org.junit.Assert;

/**
 * Create on 2018/9/7 18:55
 */
public class WrapperUtilsTest {
    public static void main(String[] args) throws Throwable {

        DemoService demoService = WrapperUtils.createInstance(DemoServiceImpl.class, Lists.newArrayList("org"));
        Assert.assertEquals("hello world", demoService.echo("hello world"));

//        DemoService demoService1 = WrapperUtils.createInstance(DemoServiceImpl.class);
//        demoService1.echo("hello world1");
//
//        DemoService demoService2 = WrapperUtils.createInstance(DemoServiceImpl.class);
//        demoService2.echo("hello world2");
    }

}