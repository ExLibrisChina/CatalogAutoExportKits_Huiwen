package com.proquest.apac.summontool.impl.nop;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.proquest.apac.summontool.MarcDao;

public class NopMarcDaoImpl implements MarcDao {

	@Override
	public List<String> getUpdatedCNMarcIdList(Date lastCheckedDate) {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getUpdatedUSMarcIdList(Date lastCheckedDate) {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getDeletedCNMarcIdList(Date lastCheckedDate) {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getDeletedUSMarcIdList(Date lastCheckedDate) {
		return new ArrayList<String>();
	}

	@Override
	public String getSingleMarcData(String marcId) {
		return "";
	}

	@Override
	public String getItemLocationByMarcId(String marcId) {
		return "";
	}

	@Override
	public Map<String, String> getLocationMap() {
		return new HashMap<String, String>();
	}

	@Override
	public List<String> getCNMarcFullList() {
		return new ArrayList<String>();
	}

	@Override
	public List<String> getUSMarcFullList() {
		return new ArrayList<String>();
	}

	@Override
	public void testAccessbility() throws Exception {
		
	}

}
