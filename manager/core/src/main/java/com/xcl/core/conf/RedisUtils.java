package com.xcl.core.conf;

import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;
import javax.annotation.Resource;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.serializer.RedisSerializer;
import org.springframework.data.redis.serializer.StringRedisSerializer;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;

/**
 * @author :xiaochanglu
 * @Description :redis缓存
 * @date :2018/12/13 20:28
 */
@Slf4j
@Component
public class RedisUtils {
    /**
     * redis存储前缀 默认为空
     */
    @Value("${redis.key-prefix: }")
    private String keyPrefix;

    public RedisUtils() {
        log.info("redis缓存启动......");
    }
    /**
     * @Description  ：redis key value 序列化
     * @author       : xcl
     * @param        : [factory]
     * @return       : org.springframework.data.redis.com.xcl.sso.core.RedisTemplate<java.lang.String,java.lang.Object>
     * @date         : 2018/12/12 14:58
     */
    @Bean
    public RedisTemplate<String, Object> redisTemplate(RedisConnectionFactory factory) {
        RedisSerializer<String> redisSerializer = new StringRedisSerializer();
        RedisTemplate<String, Object> template = new RedisTemplate<String, Object>();
        template.setConnectionFactory(factory);
        template.setKeySerializer(redisSerializer);
        template.setHashKeySerializer(redisSerializer);
        template.setValueSerializer(redisSerializer);
        template.setHashValueSerializer(redisSerializer);
        template.afterPropertiesSet();
        return template;
    }

    @Resource
    private RedisTemplate redisTemplate;

    /**
     * @param : [key, time] [键,时间(秒)]
     * @return : boolean
     * @Description ：指定缓存失效时间  到期时间
     * @author : xcl
     * @date :/12/12:46
     */
    public  boolean expire(String key, long time) {
        try {
            if (time > 0) {
                redisTemplate.expire(keyPrefix+key, time, TimeUnit.MILLISECONDS);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key] [key 键 不能为null]
     * @return : long 时间(秒) 返回0代表为永久有效
     * @Description ：根据key 获取过期时间
     * @author : xcl
     * @date :/12/12:47
     */
    public  long getExpire(String key) {
        return redisTemplate.getExpire(keyPrefix+key, TimeUnit.MILLISECONDS);
    }
    /**
     * @Description  ：判断key是否存在
     * @author       : xcl
     * @param        : [key] [键]
     * @return       : boolean [true 存在 false不存在]
     * @date         : 2018/12/28 15:34
     */
    public  boolean exists(String key) {
        try {
            return redisTemplate.hasKey(keyPrefix+key);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key]
     * @return : void
     * @Description ：删除缓存
     * 删除指定的  key
     * @author : xcl
     * @date :/12/12:50
     */
    public  void del(String... key) {
        if (key != null && key.length > 0) {
            if (key.length == 1) {
                redisTemplate.delete(keyPrefix+key[0]);
            } else {
                redisTemplate.delete(CollectionUtils.arrayToList(keyPrefix+key));
            }
        }
    }
    // ============================String=============================
    /**
     * @param : [key]
     * @return : java.lang.Object
     * @Description ：普通缓存获取
     * @author : xcl
     * @date :/12/12:50
     */
    public Object get(String key) {
        return key == null ? null : redisTemplate.opsForValue().get(keyPrefix+key);
    }
    /**
     * @param : [key, value]
     * @return : boolean true成功 false失败
     * @throws :
     * @Description ：普通缓存放入
     * @author : xcl
     * @date :/12/12:51
     */
    public  boolean set(String key, Object value) {
        try {
            redisTemplate.opsForValue().set(keyPrefix+key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, value, time]
     * @return : boolean true成功 false 失败
     * @Description ：普通缓存放入并设置时间
     * @author : xcl
     * @date :/12/12:52
     */
    public  boolean set(String key, Object value, long time) {
        try {
            if (time > 0) {
                redisTemplate.opsForValue().set(keyPrefix+key, value, time, TimeUnit.MILLISECONDS);
            } else {
                set(keyPrefix+key, value);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, delta] [键,要增加几(大于0)]
     * @return : long
     * @throws :
     * @Description ：递增
     * @author : xcl
     * @date :/12/12:53
     */
    public  long incr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递增因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(keyPrefix+key, delta);
    }
    /**
     * @param : [key, delta]
     * @return : long
     * @Description ：递减
     * @author : xcl
     * @date :/12/12:54
     */
    public  long decr(String key, long delta) {
        if (delta < 0) {
            throw new RuntimeException("递减因子必须大于0");
        }
        return redisTemplate.opsForValue().increment(keyPrefix+key, -delta);
    }
    // ================================Map=================================
    /**
     * @param : [key, item] [键 不能为null,item 项 不能为null]
     * @return : java.lang.Object
     * @Description ：HashGet
     * @author : xcl
     * @date : 2018/12/12 10:02
     */
    public Object hget(String key, String item) {
        return redisTemplate.opsForHash().get(keyPrefix+key, item);
    }
    /**
     * @param : [key, item, value]
     * @return : boolean true 成功 false失败
     * @throws :
     * @Description ：向一张hash表中放入数据,如果不存在将创建
     * @author : xcl
     * @date : 2018/12/12 10:05
     */
    public  boolean hset(String key, String item, Object value) {
        try {
            redisTemplate.opsForHash().put(keyPrefix+key, item, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, item, value, time]
     *          时间(秒) 注意:如果已存在的hash表有时间,这里将会替换原有的时间
     * @return : boolean true 成功 false失败
     * @Description ：向一张hash表中放入数据,如果不存在将创建
     * @author : xcl
     * @date : 2018/12/12 10:05
     */
    public  boolean hset(String key, String item, Object value, long time) {
        try {
            redisTemplate.opsForHash().put(keyPrefix+key, item, value);
            if (time > 0) {
                expire(keyPrefix+key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key]
     * @return : java.util.Map<java.lang.Object,java.lang.Object> 对应的多个键值
     * @Description ：获取hashKey对应的所有键值
     * @author : xcl
     * @date : 2018/12/12 10:03
     */
    public Map<String, String> hmget(String key) {
        return redisTemplate.opsForHash().entries(keyPrefix+key);
    }
    /**
     * @param : [key, map]
     * @return : boolean true 成功 false 失败
     * @Description ：HashSet
     * @author : xcl
     * @date : 2018/12/12 10:03
     */
    public  boolean hmset(String key, Map<String, String> map) {
        try {
            redisTemplate.opsForHash().putAll(keyPrefix+key, map);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;

        }
    }
    /**
     * @param : [key, map, time]
     * @return : boolean
     * @throws :
     * @Description ：HashSet 并设置时间
     * @author : xcl
     * @date : 2018/12/12 10:04
     */
    public  boolean hmset(String key, Map<String, String> map, long time) {
        System.out.println("=======================");
        try {
            redisTemplate.opsForHash().putAll(keyPrefix+key, map);
            if (time > 0) {
                expire(keyPrefix+key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, item][键 不能为null,项 可以使多个 不能为null]
     * @return : void
     * @Description ：删除hash表中的值
     * @author : xcl
     * @date : 2018/12/12 10:06
     */
    public  void hdel(String key, Object... item) {
        redisTemplate.opsForHash().delete(keyPrefix+key, item);
    }
    /**
     * @param : [key, item]
     * @return : boolean
     * @throws :
     * @Description ：判断hash表中是否有该项的值
     * @author : xcl
     * @date : 2018/12/12 10:07
     */
    public  boolean hHasKey(String key, String item) {
        return redisTemplate.opsForHash().hasKey(keyPrefix+key, item);
    }
    /**
     * @param : [key, item, by]
     * @return : double
     * @throws :
     * @Description ：hash递增 如果不存在,就会创建一个 并把新增后的值返回
     * @author : xcl
     * @date : 2018/12/12 10:07
     */
    public  double hincr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(keyPrefix+key, item, by);
    }
    /**
     * @param : [key, item, by]
     * @return : double
     * @throws :
     * @Description ：hash递减
     * @author : xcl
     * @date : 2018/12/12 10:08
     */
    public  double hdecr(String key, String item, double by) {
        return redisTemplate.opsForHash().increment(keyPrefix+key, item, -by);
    }
    // ============================set=============================
    /**
     * @param : [key]
     * @return : java.util.Set<java.lang.Object>
     * @throws :
     * @Description ：根据key获取Set中的所有值
     * @author : xcl
     * @date : 2018/12/12 10:08
     */
    public Set<Object> sGet(String key) {
        try {
            return redisTemplate.opsForSet().members(keyPrefix+key);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @param : [key, value]
     * @return : boolean true 存在 false不存在
     * @Description ：根据value从一个set中查询,是否存在
     * @author : xcl
     * @date : 2018/12/12 10:09
     */
    public  boolean sHasKey(String key, Object value) {
        try {
            return redisTemplate.opsForSet().isMember(keyPrefix+key, value);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, values] [键,值 可以是多个]
     * @return : long 成功个数
     * @Description ：将数据放入set缓存
     * @author : xcl
     * @date : 2018/12/12 10:09
     */
    public  long sSet(String key, Object... values) {
        try {
            return redisTemplate.opsForSet().add(keyPrefix+key, values);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @param : [key, time, values]
     * @return : long
     * @Description ：将set数据放入缓存
     * @author : xcl
     * @date : 2018/12/12 10:10
     */
    public  long sSetAndTime(String key, long time, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().add(keyPrefix+key, values);
            if (time > 0) {
                expire(keyPrefix+key, time);
            }
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @param : [key]
     * @return : long
     * @Description ：获取set缓存的长度
     * @author : xcl
     * @date : 2018/12/12 10:10
     */
    public  long sGetSetSize(String key) {
        try {
            return redisTemplate.opsForSet().size(keyPrefix+key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @param : [key, values]
     * @return : long 移除的个数
     * @Description ：移除值为value的
     * @author : xcl
     * @date : 2018/12/12 10:11
     */
    public  long setRemove(String key, Object... values) {
        try {
            Long count = redisTemplate.opsForSet().remove(keyPrefix+key, values);
            return count;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    // ===============================list=================================
    /**
     * @param : [key, start, end][键,开始,结束 到 -1代表所有值]
     * @return : java.util.List<java.lang.Object>
     * @throws :
     * @Description ：获取list缓存的内容
     * @author : xcl
     * @date : 2018/12/12 10:11
     */
    public List<Object> lGet(String key, long start, long end) {
        try {
            return redisTemplate.opsForList().range(keyPrefix+key, start, end);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @param : [key]
     * @return : long
     * @Description ：获取list缓存的长度
     * @author : xcl
     * @date : 2018/12/12 10:12
     */
    public  long lGetListSize(String key) {
        try {
            return redisTemplate.opsForList().size(keyPrefix+key);
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
    /**
     * @param : [key, index]
     *          [键,索引 index>=0时， 表头，1 第二个元素，依次类推；index<0时，-1，表尾，-2倒数第二个元素，依次类推]
     * @return : java.lang.Object
     * @Description ：通过索引 获取list中的值
     * @author : xcl
     * @date : 2018/12/12 10:12
     */
    public Object lGetIndex(String key, long index) {
        try {
            return redisTemplate.opsForList().index(keyPrefix+key, index);
        } catch (Exception e) {
            e.printStackTrace();
            return null;
        }
    }
    /**
     * @param : [key, value]
     * @return : boolean
     * @Description ：将list放入缓存
     * @author : xcl
     * @date : 2018/12/12 10:13
     */
    public  boolean lSet(String key, Object value) {
        try {
            redisTemplate.opsForList().rightPush(keyPrefix+key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, value, time]
     * @return : boolean
     * @Description ：将list放入缓存
     * @author : xcl
     * @date : 2018/12/12 10:13
     */
    public  boolean lSet(String key, Object value, long time) {
        try {
            redisTemplate.opsForList().rightPush(keyPrefix+key, value);
            if (time > 0) {
                expire(keyPrefix+key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, value]
     * @return : boolean
     * @throws :
     * @Description ：将list放入缓存
     * @author : xcl
     * @date : 2018/12/12 10:14
     */
    public  boolean lSet(String key, List<Object> value) {
        try {
            redisTemplate.opsForList().rightPushAll(keyPrefix+key, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, value, time]
     * @return : boolean
     * @Description ：将list放入缓存
     * @author : xcl
     * @date : 2018/12/12 10:15
     */
    public  boolean lSet(String key, List<Object> value, long time) {
        try {
            redisTemplate.opsForList().rightPushAll(keyPrefix+key, value);
            if (time > 0) {
                expire(keyPrefix+key, time);
            }
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @param : [key, index, value]
     * @return : boolean
     * @throws :
     * @Description ：根据索引修改list中的某条数据
     * @author : xcl
     * @date : 2018/12/12 10:15
     */
    public  boolean lUpdateIndex(String key, long index, Object value) {
        try {
            redisTemplate.opsForList().set(keyPrefix+key, index, value);
            return true;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }
    /**
     * @Description ：移除N个值为value
     * @param : [key, count, value]
     * @return : long 移除的个数
     * @author : xcl
     * @date : 2018/12/12 10:16
     */
    public  long lRemove(String key, long count, Object value) {
        try {
            Long remove = redisTemplate.opsForList().remove(keyPrefix+key, count, value);
            return remove;
        } catch (Exception e) {
            e.printStackTrace();
            return 0;
        }
    }
}
