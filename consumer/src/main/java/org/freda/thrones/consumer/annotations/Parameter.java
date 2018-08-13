package org.freda.thrones.consumer.annotations;

import java.lang.annotation.*;

/**
 * @author leemaster
 * @Title: Parameter
 * @Package org.freda.thrones.consumer.annotations
 * @Description:
 * @date 8/13/1823:26
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.PARAMETER)
@Documented
public @interface Parameter {

    /**
     * parameter name
     * @return
     */
    public String name();
}
