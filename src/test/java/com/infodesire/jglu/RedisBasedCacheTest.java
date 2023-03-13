package com.infodesire.jglu;

import com.infodesire.jglu.util.SocketUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.Jedis;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;
import redis.embedded.RedisServer;

import static org.junit.Assert.*;

public class RedisBasedCacheTest {

    private RedisServer redisServer;
    private UnifiedJedis jedis;
    private RedisBasedCache cache;

    @Before
    public void setUp() throws Exception {
        int port = SocketUtils.getFreePort();
        redisServer = new RedisServer( port );
        redisServer.start();
        jedis = new JedisPooled( "localhost", port );
        cache = new RedisBasedCache( jedis, "main." );
    }

    @After
    public void tearDown() throws Exception {
        jedis.close();
        redisServer.stop();
    }

    @Test
    public void testCaching() {

        assertFalse( cache.has( "key1" ) );
        assertEquals( 0, cache.find( "key*" ).size() );
        cache.put( "key1", "value1" );
        assertTrue( cache.has( "key1" ) );
        assertEquals( "value1", cache.get( "key1" ) );
        assertEquals( 1, cache.find( "key*" ).size() );

    }
    
}