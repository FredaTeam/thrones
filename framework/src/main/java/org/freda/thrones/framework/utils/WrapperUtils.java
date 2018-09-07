package org.freda.thrones.framework.utils;

import com.google.common.collect.Lists;

import java.lang.reflect.InvocationTargetException;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create on 2018/9/7 18:52
 */
public class WrapperUtils {

    /**
     * create instance by given interface
     * use decorator patten to new instance
     * <p>
     * without order
     */
    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<?> clazz) throws IllegalAccessException, InstantiationException, NoSuchMethodException, InvocationTargetException {
        Class<?> interfaceClazz = clazz.getInterfaces()[0];
        if (interfaceClazz == null) {
            throw new RuntimeException(clazz.getSimpleName() + "has no interface");
        }
        T instance = (T) clazz.newInstance();
        Set<Class<?>> wrapperClasses = laodClass(interfaceClazz);
        if (wrapperClasses != null && !wrapperClasses.isEmpty()) {
            for (Class<?> wrapperClass : wrapperClasses) {
                instance = (T) wrapperClass.getConstructor(interfaceClazz).newInstance(instance);
            }
        }
        return instance;
    }


    private static Set<Class<?>> laodClass(Class<?> interfaceClazz) {
        List<Class<?>> classes = ClassUtils.getClassesByInterface(interfaceClazz, Lists.newArrayList());
        return classes.stream().filter(it -> isWrapperClass(it, interfaceClazz)).collect(Collectors.toSet());
    }

    private static boolean isWrapperClass(Class<?> wrapperClass, Class<?> interfaceClazz) {
        try {
            wrapperClass.getConstructor(interfaceClazz);
            return true;
        } catch (NoSuchMethodException e) {
            return false;
        }
    }
}
