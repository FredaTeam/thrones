package org.freda.thrones.framework.utils;

import static org.junit.Assert.*;

/**
 * Create on 2018/9/7 18:55
 */
public class WrapperUtilsTest {
    public static void main(String[] args) throws Exception {



        WrapperUtils utils = new WrapperUtils();

        DemoService demoService = WrapperUtils.createInstance(DemoServiceImpl.class);
        demoService.echo("hello world");

        DemoService demoService1 = WrapperUtils.createInstance(DemoServiceImpl.class);


        DemoService demoService2 = WrapperUtils.createInstance(DemoServiceImpl.class);

    }
}