package com.lagou.edu.common.util;

import net.sf.cglib.beans.BeanCopier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @Description:(对象拷贝 - 性能比较好)   
 * @author: ma wei long
 * @date:   2020年6月17日 下午5:07:58
 */
public class ConvertUtils {

    private static final Logger LOGGER = LoggerFactory.getLogger(ConvertUtils.class);

    public static <S, T> T convert(S source, Class<T> dest, Function<T, T> function) {
        if (source == null) {
            return null;
        }
        try {
            T result = dest.newInstance();
            final BeanCopier copier = BeanCopier.create(source.getClass(), dest, false);
            copier.copy(source, result, null);
            if (function != null) {
                function.apply(result);
            }
            return result;
        } catch (Exception e) {
            LOGGER.error("convert error", e);
        }
        return null;
    }

    public static <S, T> T convert(S source, Class<T> dest) {
        return convert(source, dest, null);
    }

    public static <S, T> T convert(S source, T dest) {
        if (source == null || dest == null) {
            return null;
        }
        T result = dest;
        final BeanCopier copier = BeanCopier.create(source.getClass(), dest.getClass(), false);
        copier.copy(source, result, null);
        return result;
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> dest) {
        return convertList(source, dest, null);
    }

    public static <S, T> List<T> convertList(List<S> source, Class<T> dest, ConvertCallback<S, T> callback) {
        if (source == null) {
            return null;
        }
        return source.stream().map(s -> {
            T result = null;
            try {
                result = dest.newInstance();
                convert(s, result);
                if (callback != null) {
                    callback.callback(s, result);
                }
            } catch (InstantiationException | IllegalAccessException e) {
                LOGGER.error("convert error", e);
            }
            return result;
        }).collect(Collectors.toList());
    }

    public interface ConvertCallback<S, D> {
        void callback(S source, D dest);
    }
}
