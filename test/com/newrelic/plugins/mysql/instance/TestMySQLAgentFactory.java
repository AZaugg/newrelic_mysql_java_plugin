package com.newrelic.plugins.mysql.instance;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;

import com.newrelic.metrics.publish.configuration.ConfigurationException;

public class TestMySQLAgentFactory {

	@SuppressWarnings("unchecked")
	@Test
	public void verifyMetricCategoryConfiguration() {

		MySQLAgentFactory factory = new MySQLAgentFactory();
		Map<String, Object> categories = null;
		try {
			categories = factory.readCategoryConfiguration();
		} catch (ConfigurationException e) {
			fail(e.getMessage());
		}
		assertEquals(7, categories.size());
		Object status = categories.get("status");
		assertNotNull(status);
		Map<String, String> map = (Map<String,String>)status;
		assertEquals("SHOW GLOBAL STATUS", map.get("SQL"));
		assertEquals("set", map.get("result"));

		Object slave = categories.get("slave");
		assertNotNull(slave);
		map = (Map<String,String>)slave;
		assertEquals("SHOW SLAVE STATUS", map.get("SQL"));
		assertEquals("row",map.get("result"));

		Object mutex = categories.get("innodb_mutex");
		assertNotNull(mutex);
		map = (Map<String,String>)mutex;
		assertEquals("SHOW ENGINE INNODB MUTEX", map.get("SQL"));
		assertEquals("special",map.get("result"));

		Object doesnotexist = categories.get("doesnotexist");
		assertNull(doesnotexist);
		
	}
	
	@Test
	public void verifyMetricCategoryValueAttribute() {
		MySQLAgentFactory factory = new MySQLAgentFactory();
		Map<String, Object> categories = null;
		try {
			categories = factory.readCategoryConfiguration();
		} catch (ConfigurationException e) {
			fail(e.getMessage());
		}
		assertEquals(7, categories.size());

		Object status = categories.get("status");
		assertNotNull(status);
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String,String>)status;
		String valueMetrics = map.get("value_metrics"); 
		assertNotNull(valueMetrics);
		Set<String> metrics = new HashSet<String>(Arrays.asList(valueMetrics.toLowerCase().split(MySQLAgent.COMMA)));
		assertEquals(19,metrics.size());
		assertTrue(metrics.contains("uptime"));
		assertFalse(metrics.contains("com_select"));
		String counterMetrics = map.get("counter_metrics"); 
		assertNotNull(counterMetrics);
		metrics = new HashSet<String>(Arrays.asList(counterMetrics.toLowerCase().split(MySQLAgent.COMMA)));
		assertEquals(196,metrics.size());

	}
}
