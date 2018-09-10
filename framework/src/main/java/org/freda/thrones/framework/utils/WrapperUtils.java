package org.freda.thrones.framework.utils;

import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Create on 2018/9/7 18:52
 */
public class WrapperUtils {

    // class cache
    private static final Map<Class<?>, Object> CLASS_MAP = Maps.newConcurrentMap();

    /**
     * create instance by given interface
     * use decorator patten to new instance
     * <p>
     * with order
     */
    @SuppressWarnings("unchecked")
    public static <T> T createInstance(Class<?> clazz, List<String> packagePaths) throws Throwable {
        Class<?> interfaceClazz = clazz.getInterfaces()[0];
        if (interfaceClazz == null) {
            throw new RuntimeException(clazz.getSimpleName() + "has no interface");
        }

        T cacheInstance = (T) CLASS_MAP.get(clazz);
        if (Objects.isNull(cacheInstance)) {

            cacheInstance = (T) clazz.newInstance();

            Set<Class<?>> wrapperClasses = laodClass(interfaceClazz, packagePaths);

            if (wrapperClasses != null && !wrapperClasses.isEmpty()) {

                wrapperClasses = wrapperClasses.stream().sorted(
                        Comparator.comparing(index ->
                                index.getAnnotation(Order.class) == null ?
                                        Integer.MIN_VALUE : index.getAnnotation(Order.class).value())
                ).collect(Collectors.toSet());

                for (Class<?> wrapperClass : wrapperClasses) {
                    cacheInstance = (T) wrapperClass.getConstructor(interfaceClazz).newInstance(cacheInstance);
                }
            }

            CLASS_MAP.put(clazz, cacheInstance);

        }

        return cacheInstance;
    }


    private static Set<Class<?>> laodClass(Class<?> interfaceClazz, List<String> packagePaths) {
        List<Class<?>> classes = ClassUtils.getClassesByInterface(interfaceClazz, packagePaths);
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
