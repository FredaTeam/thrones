package org.freda.thrones.framework.utils;

/**
 * Create on 2018/9/7 17:04
 */
public class DemoServiceImpl implements DemoService {


    @Override
    public String echo(String str) {
        System.out.println(this.getClass().getSimpleName());
        System.out.println(str);
        return str;
    }
}
