package com.tuituidan.oss.util;

import com.tuituidan.oss.exception.ImageHostException;

import java.lang.reflect.Field;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;

/**
 * BeanExtUtils.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@UtilityClass
public class BeanExtUtils {

    /**
     * 给对象属性赋值.
     *
     * @param bean  bean
     * @param name  name
     * @param value value
     */
    public static void setProperty(Object bean, String name, Object value) {
        try {
            Field field = bean.getClass().getDeclaredField(name);
            field.setAccessible(true);
            field.set(bean, value);
        } catch (Exception ex) {
            throw new IllegalArgumentException(StringExtUtils.format("对象属性设置失败，class：{}，name：{}，value：{}",
                    bean.getClass().getName(), name, value), ex);
        }
    }

    /**
     * bean 转换.
     *
     * @param source source
     * @param cls    cls
     * @param <T>    T
     * @return T
     */
    public static <T> T convert(Object source, Class<T> cls) {
        if (source == null) {
            return null;
        }
        T target = newInstanceByCls(cls);
        BeanUtils.copyProperties(source, target);
        return target;
    }

    private static <T> T newInstanceByCls(Class<T> cls) {
        try {
            return cls.getDeclaredConstructor().newInstance();
        } catch (Exception ex) {
            throw ImageHostException.builder().error("反射创建对象失败，className：【{}】", cls.getName(), ex).build();
        }
    }

}
