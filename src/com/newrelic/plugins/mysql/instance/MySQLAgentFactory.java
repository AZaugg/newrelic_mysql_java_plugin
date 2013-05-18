package com.newrelic.plugins.mysql.instance;

import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import org.json.simple.JSONArray;
import org.json.simple.JSONObject;

import com.newrelic.metrics.publish.Agent;
import com.newrelic.metrics.publish.AgentFactory;
import com.newrelic.metrics.publish.binding.Context;
import com.newrelic.metrics.publish.configuration.ConfigurationException;

/**
 * This class produces the necessary Agents to perform
 * gathering and reporting metrics for the MySQL plugin
 * 
 * @author Ronald Bradford me@ronaldbradford.com
 *
 */
public class MySQLAgentFactory extends AgentFactory {
	/**
	 * Construct an Agent Factory based on the default properties file
	 */
	public MySQLAgentFactory() {
		super(MySQLAgent.AGENT_CONFIG_FILE);
	}
	
	/**	
	 * Configure an agent based on an entry in the properties file.
	 * There may be multiple agents per Plugin
	 * 
	 */
	@Override
	public Agent createConfiguredAgent(Map<String, Object> properties) throws ConfigurationException {
		String name = (String) properties.get("name");
		String host = (String) properties.get("host");
		String user = (String) properties.get("user");
		String passwd = (String) properties.get("passwd");
		String conn_properties = (String) properties.get("properties");
		String metrics = (String) properties.get("metrics");

		/**
		 * Use pre-defined defaults to simplify configuration
		 */
		if (host == null || "".equals(host)) host = MySQLAgent.AGENT_DEFAULT_HOST;
		if (user == null || "".equals(user)) user = MySQLAgent.AGENT_DEFAULT_USER;
		if (passwd == null) passwd = MySQLAgent.AGENT_DEFAULT_PASSWD;
		if (conn_properties == null || "".equals(conn_properties)) conn_properties = MySQLAgent.AGENT_DEFAULT_PROPERTIES;
		if (metrics == null || "".equals(metrics)) metrics = MySQLAgent.AGENT_DEFAULT_METRICS;
     
		return new MySQLAgent(name,host,user,passwd, conn_properties, metrics, readCategoryConfiguration());
	}
	
	/**
	 * Read metric category information that enables the dynamic definition 
	 * of MySQL metrics that can be collected.
	 * 
	 * @return Map  Categories and the meta data about the categories
	 * @throws ConfigurationException
	 */
	public Map<String,Object> readCategoryConfiguration() throws ConfigurationException {
		Map<String, Object> metricCategories = new HashMap<String, Object>();

		JSONArray json;
		String filename = MySQLAgent.CATEGORY_CONFIG_FILE;
		try {
			json = readJSONFile(filename);
			for (int i = 0; i < json.size(); i++) {
		    	JSONObject obj = (JSONObject) json.get(i);
		    	String category = (String)obj.get("category");
		    	metricCategories.put(category, obj);
			}

		} catch (ConfigurationException e) {
			throw logAndThrow(Context.getLogger(), "Error parsing config file " + filename);
		}

		return metricCategories; 
	}
	
	private ConfigurationException logAndThrow(Logger logger,String message) {
		logger.severe(message);
		return new ConfigurationException(message);
	}  
}
