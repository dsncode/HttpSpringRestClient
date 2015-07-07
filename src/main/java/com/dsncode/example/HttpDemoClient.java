package com.dsncode.example;

import org.apache.log4j.Logger;

import com.dsncode.http.rest.beans.BasicHttpResponse;
import com.dsncode.http.rest.client.BasicHttpClientAgent;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author "Daniel Silva Navarro"
 * @email daniel@dsncode.com
 * @url http://www.dsncode.com
 * 
 */
public class HttpDemoClient {

	private static ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(HttpDemoClient.class);

	public static void main(String args[])
	{
		BasicHttpClientAgent client = BasicHttpClientAgent.getInstance();
		BasicHttpResponse response = null;
		ObjectNode request = om.createObjectNode();
		
		//1. get example
//		String get_url = "http://localhost:9080/some_get_ws";
//		BasicHttpResponse response = client.fetchGet(get_url);
//		logger.info(response.getData());
		
		//2. post example
		String post_url = "http://localhost:9080/some_post_ws";
		request = om.createObjectNode();
		response = client.fetchPost(post_url, request);
		logger.info(response.getData());
		
		//3. post & json post object
		request = om.createObjectNode();
		request.put("id", "86EB-24F5928E1975");
		response = client.fetchPost(post_url, request);
		logger.info(response.getData());
	}
	
}
