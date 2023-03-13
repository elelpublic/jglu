package com.infodesire.jglu;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

public class RedisPerformanceEvaluationTest {

    private static String prefix;
    private static UnifiedJedis jedis;


    @BeforeClass
    public static void beforeClass() {
        prefix = "RedisPerformanceEvaluationTest." + System.currentTimeMillis() + ".";
        jedis = new JedisPooled("localhost", 6379);
    }
    
    @AfterClass
    public static void afterClass() {
        jedis.close();
    }

    @Test
    public void testBiggerData() {

        long t0 = System.currentTimeMillis();
        long c0 = 0;

        for( int i = 0; i < 100000; i++ ) {
            jedis.set( prefix + i, "" + i );
            if( i % 1000 == 0 ) {
                long t1 = System.currentTimeMillis();
                long time = t1 - t0;
                long count = i - c0;
                long rate = ( count / time ) * 1000;
                System.out.println( rate + " set/s" );
                t0 = t1;
                c0 = i;
            }
        }

    }

}