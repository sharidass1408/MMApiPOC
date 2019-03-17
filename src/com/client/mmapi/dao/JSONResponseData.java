package com.client.mmapi.dao;

import java.util.List;

public class JSONResponseData {
	
	boolean success;
	
	List<MMTimeStampData> items;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<MMTimeStampData> getItems() {
		return items;
	}

	public void setItems(List<MMTimeStampData> items) {
		this.items = items;
	}

}
