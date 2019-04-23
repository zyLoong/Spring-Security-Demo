package com.zyloong.springsecurity.demo.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JavaType;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.Map;

/**
 * @author Zhu YiLong
 * @date 2019/04/23
 */
public class JsonUtil {
    private static final Logger logger = LoggerFactory.getLogger(JsonUtil.class);

    private static final ObjectWriter OBJECT_PRETTY_WRITER = new ObjectMapper().writerWithDefaultPrettyPrinter();
    private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

    /**
     * 将对象转为JSON字符串，不带格式
     */
    public static String toJson(Object obj) {
        try {
            return OBJECT_MAPPER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error("From obj to json error!", e);
            return null;
        }
    }

    /**
     * 将对象转为JSON字符串，带格式，即空格与换行。
     */
    public static String toPrettyJson(Object obj) {
        try {
            return OBJECT_PRETTY_WRITER.writeValueAsString(obj);
        } catch (JsonProcessingException e) {
            logger.error(e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 将不带格式的JSON字符串转为带格式的JSON字符串
     */
    public static String prettifyJson(String json) {
        if (StringUtils.isBlank(json)) {
            return null;
        }
        Object obj = fromJson(json, Object.class);
        return toPrettyJson(obj);
    }

    public static JavaType getCollectionType(Class<?> collectionClass, Class<?>... elementClasses) {
        return OBJECT_MAPPER.getTypeFactory().constructParametricType(collectionClass, elementClasses);
    }

    public static JavaType getMapType(Class<?> keyClass, Class<?> valueClass) {
        return OBJECT_MAPPER.getTypeFactory().constructMapType(Map.class, keyClass, valueClass);
    }

    public static <T> T fromJson(String str, Class<T> clazz) {
        try {
            return OBJECT_MAPPER.readValue(str, clazz);
        } catch (IOException e) {
//            logger.error("From json to obj error!", e);
            return null;
        }
    }

    public static <T> T fromJson(String str, JavaType javaType) {
        try {
            return OBJECT_MAPPER.readValue(str, javaType);
        } catch (IOException e) {
            logger.error("From json to obj error!", e);
            return null;
        }
    }
}
