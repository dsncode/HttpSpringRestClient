package com.dsncode.http.rest.client;

import java.nio.charset.Charset;
import java.util.HashMap;
import java.util.Map;

import org.apache.commons.codec.binary.Base64;
import org.apache.log4j.Logger;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.StringHttpMessageConverter;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.RestTemplate;

import com.dsncode.http.rest.beans.BasicHttpResponse;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;

/**
 * @author "Daniel Silva Navarro"
 * @email daniel@dsncode.com
 * @url http://www.dsncode.com
 * 
 */
public class BasicHttpClientAgent {


	private String base64Creds=null;
	private RestTemplate restTemplate;
	private HttpHeaders header;
	private HttpEntity<String> headers;
	private static ObjectMapper om = new ObjectMapper();
	private static Logger logger = Logger.getLogger(BasicHttpClientAgent.class);
	
	private BasicHttpClientAgent()
	{
		restTemplate = new RestTemplate();
		this.header = buildHeadersTemplate();
		headers = new HttpEntity<String>(this.header);
		restTemplate.getMessageConverters().add(0, new StringHttpMessageConverter(Charset.forName("UTF-8")));
	}

	public static HttpHeaders buildHeadersTemplate()
	{
		HttpHeaders header = new HttpHeaders();
		header.setContentType(MediaType.APPLICATION_JSON);
		header.set("Connection", "Close");
		header.set("charset", "UTF-8");
		return header ;
	}
	
	public static BasicHttpClientAgent getInstance()
	{
		BasicHttpClientAgent agent = new BasicHttpClientAgent();
		return agent;
	}
	
	private static String encodeURLParams(String url, Map<String,String> uriParams)
	{
		StringBuilder sb = new StringBuilder(url);
		sb.append("?");
		if(uriParams!=null && uriParams.size()>0)
		{
			for(String key : uriParams.keySet())
			{
				sb.append(key);
				sb.append("=");
				sb.append(uriParams.get(key));
				sb.append("&");
			}
		}
		
		sb.deleteCharAt(sb.length()-1);
		String urlEncoded = sb.toString();
		return urlEncoded;
		
	}
	
	public void setCredentials(String username, String password)
	{
		
//		String plainCreds = user+":"+password;
//		byte[] plainCredsBytes = plainCreds.getBytes();
//		byte[] base64CredsBytes = Base64.encodeBase64(plainCredsBytes);
//		base64Creds = new String(base64CredsBytes);
		
		this.header = buildHeadersTemplate();
		
		String auth = username + ":" + password;
        byte[] encodedAuth = Base64.encodeBase64(
           auth.getBytes(Charset.forName("US-ASCII")) );
        String authHeader = "Basic " + new String( encodedAuth );
        header.set( "Authorization", authHeader );
        headers = new HttpEntity<String>(header);
	}
	
	public BasicHttpResponse fetchGet(String url)
	{
		Map<String,String> request_map=new HashMap<String,String>(0);
		
		return this.fetchGet(url, request_map);
	}
	
	public BasicHttpResponse fetchGet(String url, Map<String,String> uriParams)
	{
		
		if(uriParams==null)
			uriParams = new HashMap<String,String>(0);
		
		 BasicHttpResponse ans = new BasicHttpResponse();
		 
		 try
		 {
		 	 ResponseEntity<String> response = restTemplate.exchange(encodeURLParams(url,uriParams), HttpMethod.GET, headers, String.class);
		 	 ans.setSuccess(true);
		 	 ans.setHttpStatusCodeText(response.getStatusCode().toString());
		 	 ans.setHttpStatusCode(response.getStatusCode().value());
		 	 ans.setData(responseToJsonNode(response.getBody()));
		 }
		 catch(HttpClientErrorException | HttpServerErrorException e)
		 {
			 ans.setSuccess(false);
			 ans.setHttpStatusCodeText(e.getStatusText());
			 ans.setHttpStatusCode(e.getStatusCode().value());
			 ans.setData(responseToJsonNode(e.getResponseBodyAsString()));
		 }
		 return ans;
	}
	
		
	public BasicHttpResponse fetchPost(String url, JsonNode request)
	{
		BasicHttpResponse response = null;
		
		if(request!=null)
			response = fetchPost(url,request.toString());
		else
			response = fetchPost(url,"");
		return response;
	}
	
	public BasicHttpResponse fetchPost(String url, String raw_params)
	{
		BasicHttpResponse ans = new BasicHttpResponse();
				
		if(raw_params==null)
			raw_params = "";
		
		HttpEntity<String> headers = new HttpEntity<String>(raw_params,header);
		
		try
		 {
		 	 ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.POST, headers, String.class);
		 	 
		 	 ans.setSuccess(true);
		 	 ans.setHttpStatusCodeText(response.getStatusCode().toString());
		 	 ans.setHttpStatusCode(response.getStatusCode().value());
		 	 ans.setData(responseToJsonNode(response.getBody()));
		 	 
		 }
		 catch(HttpClientErrorException | HttpServerErrorException e)
		 {
			 ans.setSuccess(false);
			 ans.setHttpStatusCodeText(e.getStatusText());
			 ans.setHttpStatusCode(e.getStatusCode().value());
			 ans.setData(responseToJsonNode(e.getResponseBodyAsString()));
		 }		
			
		return ans;
	}

	private static JsonNode responseToJsonNode(String response)
	{
		JsonNode node = null;
		
		try
		{
			node = om.readValue(response, JsonNode.class);
		}
		catch(Exception e)
		{
			ObjectNode on = om.createObjectNode();
			on.put("info", response);
			
			node = on;
		}
		return node;
	}
	
	
}
