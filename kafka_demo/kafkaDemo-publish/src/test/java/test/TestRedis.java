package test;

import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;

/**
 * Created by xuran on 15/7/26.
 */
public class TestRedis{

    Jedis jedis = null;

    @Before
    public void init() {
        jedis = new Jedis("127.0.0.1", 6379);
    }

    @Test
    public void test() {
        jedis.setex("test", 10, "测试"); // 设置10秒超时
        String value = jedis.get("test");
        System.out.println(value);
    }

    @Test
    public void testGet() {
        String value = jedis.get("test");
        System.out.println(value);
    }

    @Test
    public void testList() {
//        long value = jedis.lpush("10252", "hello", "world");
//        System.out.println(value);
        System.out.println(jedis.lpop("10252"));
    }

}