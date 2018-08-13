package org.freda.thrones.consumer.annotations;

import java.lang.annotation.*;

/**
 * @author lixiaoyu15@meituan.com
 * @time 下午4:17
 * @Package org.freda.thrones.consumer.annotations
 * @Describtion
 * Scan the classes with this annotation and construct bean into the ThronesContext
 *
 * @file ThronesClient.java
 */
@Retention(RetentionPolicy.RUNTIME)
@Documented
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface ThronesClient {

    public String name();

    public String value();

    public long maxAttempts();
}
