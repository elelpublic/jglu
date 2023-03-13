package com.infodesire.jglu;

import redis.clients.jedis.JedisPooled;
import redis.clients.jedis.UnifiedJedis;

import java.util.Map;

/**
 * Some boilerplate code for working with redis via lettuce
 *
 */
public class RedisUtils {

    public static boolean SIMULATE_HSET_WITH_EMBEDDED_REDIS
            = Boolean.valueOf( System.getProperty(
                    "com.infodesire.jglu.RedisUtils.SIMULATE_HSET_WITH_EMBEDDED_REDIS", "false" ) );

//    public static RedisURI createURI( String host, int port, String user, String password ) {
//        RedisURI uri = new RedisURI( host, port, RedisURI.DEFAULT_TIMEOUT_DURATION );
//        if( user != null ) {
//            uri.setUsername( user );
//        }
//        if( password != null ) {
//            uri.setPassword( password );
//        }
//        return uri;
//    }


    public static UnifiedJedis createClient( String host, int port, String user, String password ) {
        return new JedisPooled( host, port );
    }


    /**
     * Store a map in redis
     *
     * @param jedis Redis connection
     * @param key Key ob data
     * @param map Map of data
     */
    public static void setMap( UnifiedJedis jedis, String key,
                               Map<String, String> map ) {

        if( SIMULATE_HSET_WITH_EMBEDDED_REDIS ) {
            // embedded redis seems to not support setting multiple hash entries at once
            for( Map.Entry<String, String> entry : map.entrySet() ) {
                jedis.hset( key, entry.getKey(), entry.getValue() );
            }
        }
        else {
            jedis.hset( key, map );
        }

    }


    /**
     * Load map from redis
     *
     * @param jedis Redis connection
     * @param key Data key
     * @return Map at key or empty map if nothing was found on key
     *
     */
    public static Map<String, String> getMap( UnifiedJedis jedis, String key ) {
        return jedis.hgetAll( key );
    }



//    /**
//     * Subscribe to one channel
//     *
//     * @param connection Redis connection
//     * @param channel Name of channel
//     *
//     */
//    public static void subscribe( StatefulRedisPubSubConnection<String, String> connection, String channel ) {
//
//        connection.sync().subscribe( channel );
//
//    }
//
//    /**
//     * Unsubscribe from channel
//     *
//     * @param connection Redis connection
//     * @param channel Name of channel
//     *
//     */
//    public static void unsubscribe( StatefulRedisPubSubConnection<String, String> connection,
//                                  String channel ) {
//
//        connection.sync().unsubscribe( channel );
//
//    }
//
//
//    /**
//     * Subscribe to all channels with matching pattern names
//     *
//     * @param connection Redis connection
//     * @param channelPattern Redis matching pattern
//     *
//     */
//    public static void psubscribe( StatefulRedisPubSubConnection<String, String> connection,
//                                  String channelPattern ) {
//
//        connection.sync().psubscribe( channelPattern );
//
//    }
//
//    /**
//     * Unsubscribe from all channels with matching pattern names
//     *
//     * @param connection Redis connection
//     * @param channelPattern Redis matching pattern
//     *
//     */
//    public static void punsubscribe( StatefulRedisPubSubConnection<String, String> connection,
//                                    String channelPattern ) {
//
//        connection.sync().punsubscribe( channelPattern );
//
//    }
//
//    /**
//     * Add listener to connection which will receive all subscribed messages
//     *
//     * @param pubSubConnection Redis connection
//     * @param listener Listener
//     *
//     */
//    public static void addListener( StatefulRedisPubSubConnection<String, String> pubSubConnection, PubSubListener listener ) {
//        pubSubConnection.addListener( new PubSubListenerAdapter( listener ) );
//    }
//
//    /**
//     * Publish a message
//     *
//     * @param connection Redis connection
//     * @param channel Name of channel
//     * @param message Message text
//     *
//     */
//    public static void publish( StatefulRedisConnection<String, String> connection, String channel, String message ) {
//        connection.sync().publish( channel, message );
//    }

}
