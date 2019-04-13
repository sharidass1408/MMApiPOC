package com.client.mmapi.dao;

import java.util.List;

public class JSONFacilityResponseData {

	boolean success;
	
	List<MMFacilityData> items;

	public boolean isSuccess() {
		return success;
	}

	public void setSuccess(boolean success) {
		this.success = success;
	}

	public List<MMFacilityData> getItems() {
		return items;
	}

	public void setItems(List<MMFacilityData> items) {
		this.items = items;
	}
	
	
	
}
