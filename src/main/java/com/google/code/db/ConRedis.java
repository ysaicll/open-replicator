package com.google.code.db;

import redis.clients.jedis.Jedis;

public class ConRedis {
		public void pushMQ(String host,int port,String MQname,String content){
			Jedis jedis = new Jedis(host, port);
			jedis.rpush(MQname, content);
		}
		public String popMQ(String host,int port,String MQname){
			Jedis jedis = new Jedis(host, port);
			return jedis.lpop(MQname);
		}
}
