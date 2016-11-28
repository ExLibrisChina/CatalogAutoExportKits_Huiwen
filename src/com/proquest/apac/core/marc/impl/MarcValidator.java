package com.proquest.apac.core.marc.impl;

import java.util.List;
import java.util.Map;
import java.util.TreeMap;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proquest.apac.core.marc.Marc;

/**
 * 检查MARC是否有效，字段是否存在，如果是子字段其内容不能为空
 * @author PChen
 *
 */
public class MarcValidator {

	private static Logger logger = LoggerFactory.getLogger(MarcValidator.class);
	private Map<String,String> errorMap = new TreeMap<String, String>();
	
	public Map<String, String> getErrorMap() {
		return errorMap;
	}
	
	/**
	 * check if a MARC with specific fields
	 * @param marc
	 * @param fieldToCheckCommaString field or subfield (like 010:check if it exist, 010a check if its value is blank)
	 * @return false if it missing something, true for everything is ok. Detailed info can be obtained by getErrorMap()
	 */
	public boolean validate(Marc marc, String fieldToCheckCommaString) {
		boolean ok = true;
		String []necessaryField = StringUtils.split(fieldToCheckCommaString, ",");
		for(String s : necessaryField){
			if(s.length() == 4 ){
				if(StringUtils.isBlank(marc.getSubField(s))){
					errorMap.put(s, "field value is blank");
					ok = false ;
				}
			}
			else if(marc.getField(s) == null){
				errorMap.put(s, "field is missing");
				ok = false;
			}
		}
		return ok;
	}
}
