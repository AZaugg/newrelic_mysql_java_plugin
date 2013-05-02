package com.newrelic.plugins.mysql;

import java.math.BigInteger;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Map;

import static org.junit.Assert.*;

import org.junit.Before;
import org.junit.Test;

/**
 * This class performs test cases on the MySQL class
 * 
 * @author Ronald Bradford me@ronaldbradford.com 
 * 
 */

public class TestMySQL {
	
    @Before
	public void setUp() {
	}
    
 	@Test
	public void verifyValidConnection() {
		Connection c = MySQL.getConnection(MySQL.AGENT_DEFAULT_HOST, MySQL.AGENT_DEFAULT_USER, MySQL.AGENT_DEFAULT_PASSWD, MySQL.AGENT_DEFAULT_PROPERTIES);
		assertNotNull(c);
	}

 	/*
	@Test
	public void verifyInvalidConnection() {
		Connection c = MySQL.getConnection(MySQL.AGENT_DEFAULT_HOST, MySQL.AGENT_DEFAULT_USER, MySQL.AGENT_DEFAULT_PASSWD + "X");
		assertNull(c);
	}
 	 */
 	
	@Test
	public void verifyTransformedStringMetrics() {
		assertEquals("FRED",MySQL.transformStringMetric("FRED"));
		assertEquals("1",MySQL.transformStringMetric("ON"));
		assertEquals("1",MySQL.transformStringMetric("TRUE"));
		assertEquals("0",MySQL.transformStringMetric("OFF"));
		assertEquals("0",MySQL.transformStringMetric("NONE"));
		assertEquals("-1",MySQL.transformStringMetric("NULL"));
		assertEquals("FALSE",MySQL.transformStringMetric("FALSE"));
	}
	
	@Test
	public void verifyValidMetricValues() {
		assertTrue(MySQL.validMetricValue("0"));
		assertTrue(MySQL.validMetricValue("456.7789"));
	}

	@Test
	public void verifyInValidMetricValues() {
		assertFalse(MySQL.validMetricValue(""));
		assertFalse(MySQL.validMetricValue("5.25.45a"));
	}

	@Test
	public void verifyValidMetricValuesThatGetTranformed() {
		assertTrue(MySQL.validMetricValue("ON"));
		assertTrue(MySQL.validMetricValue("TRUE"));
		assertTrue(MySQL.validMetricValue("OFF"));
		assertTrue(MySQL.validMetricValue("NONE"));
		assertTrue(MySQL.validMetricValue("NULL"));
	}

	@Test
	public void runSQLSingleStatusValid() {
		Connection c = MySQL.getConnection(MySQL.AGENT_DEFAULT_HOST, MySQL.AGENT_DEFAULT_USER, MySQL.AGENT_DEFAULT_PASSWD, MySQL.AGENT_DEFAULT_PROPERTIES);
		assertNotNull(c);
		Map<String, Number> results = MySQL.runSQL(c, "status", "SHOW GLOBAL STATUS LIKE 'Com_xa_rollback'");
		assertEquals(1,results.size());	
		assertEquals(0,results.get("status/com_xa_rollback").intValue());		// A status likely to never have a value
	}

	@Test
	public void runSQLSingleStatusValue() {
		Connection c = MySQL.getConnection(MySQL.AGENT_DEFAULT_HOST, MySQL.AGENT_DEFAULT_USER, MySQL.AGENT_DEFAULT_PASSWD, MySQL.AGENT_DEFAULT_PROPERTIES);
		assertNotNull(c);
		Map<String, Number> results = MySQL.runSQL(c, "status", "SHOW GLOBAL STATUS LIKE 'Uptime'");
		assertEquals(1,results.size());	
		assertTrue(results.get("status/uptime").intValue() > 0);		// A status that will always be > 0
	}

	@Test
	public void runSQLSingleStatusInvalid() {
		Connection c = MySQL.getConnection(MySQL.AGENT_DEFAULT_HOST, MySQL.AGENT_DEFAULT_USER, MySQL.AGENT_DEFAULT_PASSWD, MySQL.AGENT_DEFAULT_PROPERTIES);
		assertNotNull(c);
		Map<String, Number> results = MySQL.runSQL(c, "status", "SHOW GLOBAL VARIABLES LIKE 'version'");
		assertEquals(0,results.size());									// This is removed because value is a string
	}
	

	@Test
	public void runSQLSingleStatusTranslated() {
		Connection c = MySQL.getConnection(MySQL.AGENT_DEFAULT_HOST, MySQL.AGENT_DEFAULT_USER, MySQL.AGENT_DEFAULT_PASSWD, MySQL.AGENT_DEFAULT_PROPERTIES);
		assertNotNull(c);
		Map<String, Number> results = MySQL.runSQL(c, "status", "SHOW GLOBAL STATUS LIKE 'Compression'");
		assertEquals(1,results.size());	
		assertEquals(0,results.get("status/compression").intValue());		//Translated from OFF
	}

   public void closeConnection(Connection c) {
		try {
			if (c != null)	c.close();
		} catch (SQLException e) {
		}
    }

   @Test
   public void runTranslateStringToNumber() {
	   assertEquals(5,MySQL.translateStringToNumber("5").intValue()); 
	   assertEquals(java.math.BigInteger.class, MySQL.translateStringToNumber("5").getClass());
	   assertEquals(5.0f,MySQL.translateStringToNumber("5.0"));
	   assertEquals(java.lang.Float.class, MySQL.translateStringToNumber("5.0").getClass());
	   BigInteger x = new BigInteger("37107795968");
	   assertEquals(x,MySQL.translateStringToNumber("37107795968"));
   }
   
	@Test
	public void verifyFloat() {
		String val="0.00";
		assertTrue(val.matches("\\d*\\.\\d*"));
	}

	@Test
	public void verifyNotFloat() {
		String val="70797";
		assertFalse(val.matches("\\d*\\.\\d*"));
	}



}



