package com.zyloong.springsecurity.demo.util;

import java.util.HashMap;
import java.util.Map;

/**
 * @author zyloong
 */
public class RedisUtil {
    // 为了简化测试，用 Map 模仿 Redis
    private static final Map<String, String> REDIS_DB = new HashMap<>();

    /**
     * 【测试】添加redis数据
     */
    public static void set(String key, String value) {
        REDIS_DB.put(key, value);
    }

    /**
     * 【测试】获取redis中相应Key的数据
     */
    public static String get(String key) {
        return REDIS_DB.get(key);
    }

    /**
     * 【测试】 添加redis数据，并设置过期时间
     */
    public static void setWithExpire(String key, String value, int loginExpireTime) {
        REDIS_DB.put(key, value);
    }

    /**
     * 【测试】删除一个key
     */
    public static void delete(String key) {
        REDIS_DB.remove(key);
    }
}
