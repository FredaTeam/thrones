package org.freda.thrones.framework.utils;

import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;

/**
 * Create on 2018/9/7 17:44
 */
@Slf4j
public class ClassUtils {

    /**
     * find all subClass implements given interface under given package path
     * <p>
     * if there is no package path then use the package path of the given interface
     */
    public static List<Class<?>> getClassesByInterface(Class<?> clazz, List<String> packagePaths) {
        List<Class<?>> classes = Lists.newArrayList();
        if (clazz.isInterface()) {
            try {
                packagePaths.add(clazz.getPackage().getName());
                List<Class<?>> allClass = getClasses(packagePaths);
                for (Class allClas : allClass) {
                    if ((!allClas.isInterface())
                            && (clazz.isAssignableFrom(allClas))
                            && (!clazz.equals(allClas))) {
                        classes.add(allClas);
                    }
                }
            } catch (Throwable t) {
                log.error(t.getMessage(), t);
                throw new RuntimeException(t.getMessage());
            }
        } else {
            throw new RuntimeException(clazz.getSimpleName() + " is not an interface");
        }

        return classes;
    }

    /**
     * find all classes under the given pachage path
     */
    private static List<Class<?>> getClasses(List<String> patchkagePath)
            throws ClassNotFoundException, IOException {
        List<Class<?>> classes = Lists.newArrayList();
        ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
        for (String packageName : patchkagePath) {
            String path = packageName.replace('.', '/');
            Enumeration<URL> resources = classLoader.getResources(path);
            List<File> dirs = Lists.newArrayList();
            while (resources.hasMoreElements()) {
                URL resource = resources.nextElement();
                dirs.add(new File(resource.getFile()));
            }
            for (File directory : dirs) {
                classes.addAll(findClasses(directory, packageName));
            }
        }

        return classes;
    }

    /**
     * find all .class file by given directory and packageName
     */
    private static List<Class<?>> findClasses(File directory, String packageName)
            throws ClassNotFoundException {
        List<Class<?>> classes = Lists.newArrayList();
        if (!directory.exists()) {
            return classes;
        }
        File[] files = directory.listFiles();
        if (files == null || files.length == 0) {
            throw new RuntimeException("there are no files under the directory");
        }
        for (File file : files) {
            if (file.isDirectory()) {
                classes.addAll(findClasses(file, packageName + "." + file.getName()));
            } else if (file.getName().endsWith(".class")) {
                classes.add(Class.forName(packageName + '.' +
                        file.getName().substring(0, file.getName().length() - 6)));
            }
        }
        return classes;
    }
}