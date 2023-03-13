package com.infodesire.jglu;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

import java.util.List;

import static org.junit.Assert.*;

public class RedisBasedCacheWithRealRedisIntegrationTest {

    private static String prefix;
    private static UnifiedJedis jedis;


    @BeforeClass
    public static void beforeClass() {
        prefix = "RedisBasedCacheWithRealRedisIntegrationTest." + System.currentTimeMillis() + ".";
        jedis = new JedisPooled("localhost", 6379);
    }
    
    @AfterClass
    public static void afterClass() {
        for( String key : jedis.keys( prefix + "*" ) ) {
            jedis.del( key );
        }
        jedis.close();
    }

    @Test
    public void testCaching() {

        RedisBasedCache redisCache = new RedisBasedCache( jedis, prefix );
        assertFalse( redisCache.has( "value1" ) );

        redisCache.put( "value1", "First value!" );
        assertTrue( redisCache.has( "value1" ) );

        assertEquals( "First value!", redisCache.get( "value1" ) );
        List<String> keys = redisCache.find( "value*" );
        assertEquals( 1, keys.size() );
        assertEquals( "value1", keys.get( 0 ) );

        redisCache.remove( "value1" );
        assertFalse( redisCache.has( "value1" ) );
        assertEquals( 0, redisCache.find( "value1" ).size() );

    }

}