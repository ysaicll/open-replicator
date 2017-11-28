package com.google.code.or;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.concurrent.TimeUnit;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.db.ConMonetDB;
import com.google.code.db.ConRedis;
import com.google.code.db.Constant;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;

public class OpenReplicatorTest {
	//
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenReplicatorTest.class);

	/**
	 * 
	 */
	public static void main(String args[]) throws Exception {
	
		final OpenReplicator or = new OpenReplicator();
	    final ConRedis cr = new ConRedis();
		or.setUser(Constant.MySQLUser);
		or.setPassword(Constant.MySQLPwd);
		or.setHost(Constant.MySQLhost);
		or.setPort(Constant.MySQLport);
		or.setServerId(1);
		or.setBinlogPosition(107);
		or.setBinlogFileName("binlog.000001");
		or.setBinlogEventListener(new BinlogEventListener() {
		    public void onEvents(BinlogEventV4 event){
		        String events = event.toString();
		    	if(events.contains("Query") && !events.contains("BEGIN")){
		        	int start = events.lastIndexOf("sql");
		        	String sql = event.toString().substring(start+4, events.length()-1)+";";
		        	cr.pushMQ(Constant.Redishost,Constant.Redisport, "sql", sql);
		        	LOGGER.info("{}", sql);
		        	try {
						ConMonetDB.conMonetDB(cr.popMQ(Constant.Redishost, Constant.Redisport, "sql"));
					} catch (Exception e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
		    	}	
		    	
		    }
		});
		or.start();

		//
		LOGGER.info("press 'q' to stop");
		final BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
		for(String line = br.readLine(); line != null; line = br.readLine()) {
		    if(line.equals("q")) {
		        or.stop(Long.MAX_VALUE, TimeUnit.MILLISECONDS);
		        break;
		    }
		}
	}
}
