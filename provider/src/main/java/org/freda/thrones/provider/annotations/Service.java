package org.freda.thrones.provider.annotations;

import org.freda.thrones.framework.constants.ThronesProto;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author leemaster
 * @Title: Service
 * @Package org.freda.thrones.provider.annotations
 * @Description:
 * @date 8/12/1808:49
 */
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD,ElementType.TYPE})
public @interface Service {

    /**
     * service name will be used to register on the service endpoint
     * withe the app name
     * @return
     */
    public String name() default "";


    /**
     * define the serialize methods
     * @return
     */
    public ThronesProto protocol() default ThronesProto.ALL;

}
