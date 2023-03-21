package com.infodesire.jglu.pubsub;

import com.infodesire.jglu.util.SocketUtils;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.JedisPubSub;
import redis.clients.jedis.UnifiedJedis;
import redis.embedded.RedisServer;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.junit.Assert.assertEquals;

public class PubSubTest {

    private RedisServer redisServer;
    private UnifiedJedis jedis;

    @Before
    public void setUp() throws Exception {
        int port = SocketUtils.getFreePort();
        redisServer = new RedisServer( port );
        redisServer.start();
        jedis = new JedisPooled( "localhost", port );
    }

    @After
    public void tearDown() throws Exception {
        jedis.close();
        redisServer.stop();
    }

    @Test
    public void simple() throws InterruptedException {

        final List<String> incoming = new ArrayList<>();

        JedisPubSub pubSub = new JedisPubSub() {
            @Override
            public void onMessage( String channel, String message ) {
                incoming.add( message );
            }
            @Override
            public void onSubscribe(String channel, int subscribedChannels) {
                incoming.add( "subscribed to " + channel + " subscription count " + subscribedChannels  );
            }

            @Override
            public void onUnsubscribe(String channel, int subscribedChannels) {
                incoming.add( "unsubscribed from " + channel + " subscription count " + subscribedChannels  );
            }
        };

        ExecutorService background = Executors.newFixedThreadPool( 1 );
        background.submit( () -> {
            jedis.subscribe( pubSub, "TestChannel" );
        } );
        Thread.sleep( 100 );

        jedis.publish( "TestChannel", "Hello World." );
        Thread.sleep( 100 );

        int counter = 0;

        assertEquals( 2, incoming.size() );
        assertEquals( "subscribed to TestChannel subscription count 1", incoming.get( counter++ ) );
        assertEquals( "Hello World.", incoming.get( counter++ ) );

        background.shutdown();

    }

}