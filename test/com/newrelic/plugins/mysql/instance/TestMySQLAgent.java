package com.newrelic.plugins.mysql.instance;

import static org.junit.Assert.*;

import java.sql.Connection;
import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.newrelic.plugins.mysql.MySQL;

public class TestMySQLAgent {

	@Test
	public void verifyNewRelicMetrics() {
		Map<String, Number> results = new HashMap<String, Number>();

		results.put("status/com_select", 1);
		results.put("status/qcache_hits", 2);
		results.put("status/com_insert", 4);
		results.put("status/com_insert_select", 8);
		results.put("status/com_update", 16);
		results.put("status/com_update_multi", 32);
		results.put("status/com_delete", 64);
		results.put("status/com_delete_multi", 128);
		results.put("status/com_replace",256);
		results.put("status/com_replace_select", 512);

		String metrics="status,newrelic";
		MySQLAgent agent = new MySQLAgent("test",
										  MySQL.AGENT_DEFAULT_HOST, MySQL.AGENT_DEFAULT_USER, MySQL.AGENT_DEFAULT_PASSWD, MySQL.AGENT_DEFAULT_PROPERTIES,
										  metrics, new HashMap<String,Object>());
		
		Map<String, Number> newRelicMetrics = agent.newRelicMetrics(results,metrics);
		assertNotNull(newRelicMetrics);
		assertEquals(2,newRelicMetrics.size());
		assertEquals(3,newRelicMetrics.get("newrelic/reads"));
		assertEquals(1020,newRelicMetrics.get("newrelic/writes"));
	}

}
