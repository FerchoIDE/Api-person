package com.consistent.service.application.models;

import java.util.Map;

public class Input {

	String type;
	String name;
	Map<String, String> values;
	String ancestor;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Map<String, String> getValues() {
		return values;
	}
	public void setValues(Map<String, String> values) {
		this.values = values;
	}
	public String getAncestor() {
		return ancestor;
	}
	public void setAncestor(String ancestor) {
		this.ancestor = ancestor;
	}
	
	
	
}
