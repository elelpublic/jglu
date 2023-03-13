package com.infodesire.jglu.busdata;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class StoreLargeNumberOfBusObjectsTest {

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
    public void testStoringAndReading() {

        long t0 = System.currentTimeMillis();
        long c0 = 0;

        int OBJECT_COUNT = 1000000;

        LocalDate date = LocalDate.of( 2023, 3, 7 );

        for( int i = 0; i < OBJECT_COUNT; i++ ) {

            String id = "" + i;
            BusObject busObject = new BusObject( prefix + id );
            busObject.set( "Id", new BusValue( id ) );
            busObject.set( "Name", new BusValue( "Project " + id ) );
            busObject.set( "Start", new BusValue( date ) );
            busObject.set( "End", new BusValue( date.plusDays( 100 ) ) );
            busObject.set( "Active", new BusValue( true ) );
            busObject.set( "Description", new BusValue( "Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                    "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                    "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata " +
                    "sanctus est Lorem ipsum dolor sit amet. Lorem ipsum dolor sit amet, consetetur sadipscing elitr, " +
                    "sed diam nonumy eirmod tempor invidunt ut labore et dolore magna aliquyam erat, sed diam voluptua. " +
                    "At vero eos et accusam et justo duo dolores et ea rebum. Stet clita kasd gubergren, no sea takimata " +
                    "sanctus est Lorem ipsum dolor sit amet." ) );
            List<String> employees = new ArrayList<>();
            for( int x = 0; x < 10; x++ ) {
                employees.add( "" + ( i + x ) );
            }
            busObject.set( "Employees", new BusValue( employees ) );

            BusUtils.set( jedis, busObject );

            date = date.plusDays( 1 );

            c0++;

            if( i % 1000 == 0 ) {
                long t1 = System.currentTimeMillis();
                int rate = (int) Math.round( ( (double) c0 / (double) ( t1 - t0 ) ) * 1000 );
                int percent = (int) Math.round( ( (double) i / (double) OBJECT_COUNT ) * 100.0 );
                System.out.println( "#" + i + "/" + OBJECT_COUNT + " (" + percent + " %) Create rate: " + rate + " objects/s" );
                t0 = System.currentTimeMillis();
                c0 = 0;
            }

        }


    }

}