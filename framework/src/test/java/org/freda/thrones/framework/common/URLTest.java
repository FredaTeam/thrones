package org.freda.thrones.framework.common;

import org.junit.Test;

import static org.junit.Assert.*;

/**
 * Create on 2018/9/3 18:16
 */
public class URLTest {


    @Test
    public void test_valueOf(){
        String urlStr = "thrones://secret@127.0.0.1:9010/com.test.service.DemoService?param1=value1&param2=value2";
        URL url = URL.valueOf(urlStr);

        assertEquals(url.getHost(),"127.0.0.1");
        assertEquals(url.getPort(),9010);
        assertEquals(url.getParam("param1"),"value1");
    }
}