package com.google.code.db;

import java.util.HashMap;
import java.util.Map;

/**
 * @author ys
 * @see https://dev.mysql.com/doc/refman/5.7/en/data-type-overview.html
 * @see https://www.monetdb.org/Documentation/Manuals/SQLreference/BuiltinTypes
 */
public class TypeMatch {
	public final static Map<String, String> type = new HashMap<String, String>();
	static{
		type.put("TINYINT", "TINYINT");
		type.put("SMALLINT", "SMALLINT");
		type.put("MEDIUMINT", "INT");
		type.put("INT(11)", "INT");
		type.put("INT(1)", "INT");
		type.put("BIGINT(20)", "BIGINT");
		type.put("FLOAT", "FLOAT");
		type.put("DOUBLE", "DOUBLE");
		type.put("DECIMAL", "DECIMAL");
		type.put("DATE", "DATE");
		type.put("TIME", "TIME");
		type.put("TIMESTAMP", "TIMESTAMP");
		type.put("YEAR", "DATE");
		type.put("DATETIME", "TIMESTAMP");
		type.put("CHAR", "CHAR");
		type.put("VARCHAR", "VARCHAR");
		type.put("TEXT", "TEXT");
		type.put("TINYTEXT", "TEXT");
		type.put("MEDIUMTEXT", "TEXT");
		type.put("LONGTEXT", "TEXT");
		type.put("TINYBLOB", "BLOB");
		type.put("BLOB", "BLOB");
		type.put("MEDIUMBLOB", "BLOB");
		type.put("LONGBLOB", "BLOB");
		type.put("BOOLEAN", "BOOLEAN");
	}
	public static String getType(String mytype) {
		return type.get(mytype);	
	}
}
