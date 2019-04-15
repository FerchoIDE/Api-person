package com.consistent.service.application.models;

import java.util.List;

import com.liferay.document.library.kernel.model.DLFileEntry;
import com.liferay.document.library.kernel.model.DLFolder;

public class Files {

	Long pk;
	String name;
	String path;
	String shourtCut;
	String url;
	
	
	public String getUrl() {
		return url;
	}
	public void setUrl(String url) {
		this.url = url;
	}
	public Long getPk() {
		return pk;
	}
	public void setPk(Long pk) {
		this.pk = pk;
	}
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public String getShourtCut() {
		return shourtCut;
	}
	public void setShourtCut(String shourtCut) {
		this.shourtCut = shourtCut;
	}
	
	
	
}
