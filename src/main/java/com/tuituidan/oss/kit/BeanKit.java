package com.tuituidan.oss.kit;

import com.tuituidan.oss.exception.ImageHostException;

import lombok.experimental.UtilityClass;
import org.springframework.beans.BeanUtils;

/**
 * BeanKit.
 *
 * @author zhujunhan
 * @version 1.0
 * @date 2020/10/14
 */
@UtilityClass
public class BeanKit {

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
