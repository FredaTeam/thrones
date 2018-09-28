package org.freda.thrones.framework.utils;

import com.google.common.collect.Lists;
import org.freda.thrones.common.utils.WrapperUtils;
import org.junit.Assert;
import org.junit.Test;

/**
 * Create on 2018/9/7 18:55
 */
public class WrapperUtilsTest {

    @Test
    public void test_wrapperInstance() throws Throwable {
        DemoService demoService = WrapperUtils.createInstance(DemoServiceImpl.class, Lists.newArrayList("org"));
        Assert.assertEquals("hello world", demoService.echo("hello world"));

    }
}