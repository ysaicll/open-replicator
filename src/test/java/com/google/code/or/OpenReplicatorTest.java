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

import com.google.code.db.ConMariadb;
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
		final Map<Integer, Integer>tmpColumn = new HashMap<>();
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
//		    	System.out.println(events);
		    	//SQL appears in log
		    	String NorSQL = "";
		    	switch (header) {
				case "QueryEventheader":
					int index = events.lastIndexOf("sql");
					String statement = events.substring(index + 4);
					if (statement.contains("BEGIN")) {
					    break;
					}
					if(!statement.contains("BEGIN")) {
					    NorSQL = NorSQL + statement + Constant.Semicolon;
					}					
					System.out.println(NorSQL);			
					break;
                case "XidEventheader":
                	//TODO trans to MQ
                    break;
                case "TableMapEventheader":
                	/*match column type*/
                	int i = 1;
                	Pattern p_map = Pattern.compile("(?<=es\\=\\[).*?(?=\\])");
                	Matcher m_map = p_map.matcher(events);
                	if(m_map.find()){
                		String types[] = m_map.group().split("\\,"); 
                	for(String type : types){
                	   //System.out.println(type.replace(" ", ""));
                	   tmpColumn.put(i, Integer.parseInt(type.replace(" ", ""))); 
                	   i ++;
                	   }
                	}
                	break;
                case "WriteRowsEventheader":
                	/*match column values*/
                	StringBuffer values = new StringBuffer("VALUES ");
                	Pattern p_write = Pattern.compile("(?<=ns\\=\\[).*?(?=\\])");
                	Matcher m_write = p_write.matcher(events);
                	String result = "";
                	while(m_write.find()){
                		String value[] = m_write.group().split("\\,");
                		for (int j=0; j<value.length; j++){
                			if(tmpColumn.get(j+1) > 9){
                				result += Toolmethod.addApos(value[j].replace(" ", "")) + Constant.Comma;
                			}else {
								result += value[j].replace(" ", "") + Constant.Comma;
							}                			
                		}       
                		values.append(Toolmethod.addPar(result.substring(0, result.length() - 1)))
          		              .append(Constant.Comma);
            			result = "";
                	}
                	tmpColumn.clear();
            		i = 1;
                	//SQL that was rebuild
                	String SpSQL = Constant.Insval + " tbname " + values.toString().substring(0, values.toString().length()-1)
                			+ Constant.Semicolon;                	
                	System.out.println(SpSQL);
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
