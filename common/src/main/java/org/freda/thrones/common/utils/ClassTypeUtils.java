package org.freda.thrones.common.utils;


/**
 * <p>类型转换工具类</p>
 * <p>调用端时将类描述转换为字符串传输。服务端将字符串转换为具体的类</p>
 * <pre>
 *     保证传递的时候值为可阅读格式，而不是jvm格式（[Lxxx;）：
 *     普通：java.lang.String、java.lang.String[]
 *     基本类型：int、int[]
 *     内部类：com.alipay.example.Inner、com.alipay.example.Inner[]
 *     匿名类：com.alipay.example.Xxx$1、com.alipay.example.Xxx$1[]
 *     本地类：com.alipay.example.Xxx$1Local、com.alipay.example.Xxx$1Local[]
 *     成员类：com.alipay.example.Xxx$Member、com.alipay.example.Xxx$Member[]
 * 同时Class.forName的时候又会解析出Class。
 * </pre>
 * <p>
 */
public class ClassTypeUtils {

    /**
     * Class[]转String[]
     *
     * @param typeStrs 对象描述[]
     * @return Class[]
     */
    public static Class[] getClasses(String[] typeStrs) throws RuntimeException {
        if (typeStrs.length == 0) {
            return new Class[0];
        } else {
            Class[] classes = new Class[typeStrs.length];
            for (int i = 0; i < typeStrs.length; i++) {
                classes[i] = getClass(typeStrs[i]);
            }
            return classes;
        }
    }

    /**
     * String转Class
     *
     * @param typeStr 对象描述
     * @return Class[]
     */
    public static Class getClass(String typeStr) {
        Class clazz;
        if ("void".equals(typeStr)) {
            clazz = void.class;
        } else if ("boolean".equals(typeStr)) {
            clazz = boolean.class;
        } else if ("byte".equals(typeStr)) {
            clazz = byte.class;
        } else if ("char".equals(typeStr)) {
            clazz = char.class;
        } else if ("double".equals(typeStr)) {
            clazz = double.class;
        } else if ("float".equals(typeStr)) {
            clazz = float.class;
        } else if ("int".equals(typeStr)) {
            clazz = int.class;
        } else if ("long".equals(typeStr)) {
            clazz = long.class;
        } else if ("short".equals(typeStr)) {
            clazz = short.class;
        } else {
            String jvmName = canonicalNameToJvmName(typeStr);
            clazz = ClassUtils.forName(jvmName);
        }
        return clazz;
    }

    /**
     * 通用描述转JVM描述
     *
     * @param canonicalName 例如 int[]
     * @return JVM描述 例如 [I;
     */
    public static String canonicalNameToJvmName(String canonicalName) {
        boolean isArray = canonicalName.endsWith("[]");
        if (isArray) {
            String t = ""; // 计数，看上几维数组
            while (isArray) {
                canonicalName = canonicalName.substring(0, canonicalName.length() - 2);
                t += "[";
                isArray = canonicalName.endsWith("[]");
            }
            if ("boolean".equals(canonicalName)) {
                canonicalName = t + "Z";
            } else if ("byte".equals(canonicalName)) {
                canonicalName = t + "B";
            } else if ("char".equals(canonicalName)) {
                canonicalName = t + "C";
            } else if ("double".equals(canonicalName)) {
                canonicalName = t + "D";
            } else if ("float".equals(canonicalName)) {
                canonicalName = t + "F";
            } else if ("int".equals(canonicalName)) {
                canonicalName = t + "I";
            } else if ("long".equals(canonicalName)) {
                canonicalName = t + "J";
            } else if ("short".equals(canonicalName)) {
                canonicalName = t + "S";
            } else {
                canonicalName = t + "L" + canonicalName + ";";
            }
        }
        return canonicalName;
    }


}
