package com.google.code.or;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.omg.CORBA.INTERNAL;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.code.db.ConMonetDB;
import com.google.code.db.ConRedis;
import com.google.code.db.Constant;
import com.google.code.or.binlog.BinlogEventListener;
import com.google.code.or.binlog.BinlogEventV4;

public class OpenReplicatorTest {
	private static final Logger LOGGER = LoggerFactory.getLogger(OpenReplicatorTest.class);

	public static void main(String args[]) throws Exception {
	
		final OpenReplicator or = new OpenReplicator();
	    final ConRedis cr = new ConRedis();
	    Map<Integer, String>DBMap = new HashMap<>();
	    Map<Integer, String>TableMap = new HashMap<>();
		Map<Integer, Integer>tmpColume = new HashMap<>();
	    or.setUser(Constant.MySQLUser);
		or.setPassword(Constant.MySQLPwd);
		or.setHost(Constant.MySQLhost);
		or.setPort(Constant.MySQLport);
		or.setServerId(Constant.Serverid);
		or.setBinlogPosition(Constant.BinlogPosition);
		or.setBinlogFileName(Constant.BinlogFileName);
		or.setBinlogEventListener(new BinlogEventListener() {
		    public void onEvents(BinlogEventV4 event){
		        String events = event.toString();
		    	String header = events.substring(0, events.indexOf('='));
		    	System.out.println(events);
		    	String SQL = "";
		    	switch (header) {
				case "QueryEventheader":
					int index = events.lastIndexOf("sql");
					String statement = events.substring(index + 4);
					if(!statement.contains("BEGIN")) {
					SQL = SQL + statement + Constant.Semicolon + "\n";
					}
					System.out.println(SQL);
					break;
                case "XidEventheader":
                	//TODO trans to MQ
                    break;
                case "TableMapEventheader":
                	/*match column type*/
                	break;
                case "WriteRowsEventheader":
                	/*match column values*/
                	StringBuffer values = new StringBuffer("VALUES ");
                	Pattern pattern = Pattern.compile("(?<=ns\\=\\[).*?(?=\\])");
                	Matcher matcher = pattern.matcher(events);
                	while(matcher.find()){
                		values.append(Toolmethod.addPar(matcher.group(0)))
                		      .append(Constant.Comma);
                	}
                	System.out.println(Constant.Insval + " tbname " + values.toString().substring(0, values.toString().length()-1) + Constant.Semicolon);
                case "RotateEventheader":
                	//DO Nothing
                	break;
                case "FormatDescriptionEventheader":
                	//DO Nothing
                	break;
				default:
					break;
				}
		        	//LOGGER.info("{}", event);		        			        	
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
