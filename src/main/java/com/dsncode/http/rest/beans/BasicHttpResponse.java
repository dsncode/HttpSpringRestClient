package com.dsncode.http.rest.beans;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;

/**
 * @author "Daniel Silva Navarro"
 * @email daniel@dsncode.com
 * @url http://www.dsncode.com
 * 
 */
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BasicHttpResponse {

	private boolean success;
	private String httpStatusCodeText;
	private int httpStatusCode;
	private String info="";
	
	//@JsonDeserialize(as=.class)
	private JsonNode data;
	private HashMap<String,HashSet<String>> searchKeywords;
	
	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public String getHttpStatusCodeText() {
		return httpStatusCodeText;
	}

	public void setHttpStatusCodeText(String statusCodeText) {
		this.httpStatusCodeText = statusCodeText;
	}

	public String getInfo() {
		return info;
	}

	public void setInfo(String info) {
		this.info = info;
	}

	public int getHttpStatusCode() {
		return httpStatusCode;
	}

	public void setHttpStatusCode(int statusCode) {
		this.httpStatusCode = statusCode;
	}
	
	public JsonNode getData()
	{
		return this.data;
	}
	
	
	public void setData(JsonNode data)
	{
		this.data = data;
	}
	
	public void setSearcKeywords(HashMap<String,HashSet<String>> searchKeywords)
	{
		this.searchKeywords=searchKeywords;
	}
	
	public HashMap<String,HashSet<String>> getSearchKeywords()
	{
		return this.searchKeywords;
	}
	
}
