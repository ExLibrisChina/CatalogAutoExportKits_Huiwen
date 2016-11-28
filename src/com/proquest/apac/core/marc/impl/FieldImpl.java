package com.proquest.apac.core.marc.impl;

import java.util.ArrayList;
import java.util.List;

import com.proquest.apac.core.marc.Field;
import com.proquest.apac.core.marc.MarcChar;
import com.proquest.apac.core.marc.Subfield;


public class FieldImpl implements Field {

	private String name;
	private String data;
	private String indicator = "";
	
	private List<Subfield> subFields = new ArrayList<Subfield>();

	public FieldImpl(String name, String data) {
		this.name = name;
		this.data = data ;
		parse(data);
	}

	/**
	 * 解析字段内容为子字段对象数组
	 * @param data
	 */
	private void parse(String data) {
		String[] subs = data.split(new String(new char[] { MarcChar.SUBFIELD_DELIMITER }));
		for (String str : subs) {
			//marc中可能出现填写不规范的异常,忽略是最好的选择
			if(str.length() >= 1)
				subFields.add(new SubfieldImpl(str.charAt(0), str.substring(1)));
		}
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
	public String getData() {
		return data;
	}

	public void setData(String data) {
		this.data = data;
		parse(data);
	}

	public void setSubFields(List<Subfield> subFields) {
		this.subFields = subFields;
	}

	
	public List<Subfield> getAllSubFields() {
		return this.subFields;
	}

	public List<Subfield> getSubfields(char id) {
		//返回全部
		if('_' == id)
			return getAllSubFields();
		
		//遍历比较，返回部分
		List<Subfield> retList = new ArrayList<Subfield>();
		for(Subfield s : subFields){
			if(s.getId() == id){
				retList.add(s);
			}
		}
		return retList;
	}

	public int compareTo(Object o) {
		if(o instanceof Field){
			Field f = (Field)o;
			return this.getName().compareTo(f.getName());
		}
		return 0;
	}

	public String toString(){
		StringBuffer sb = new StringBuffer(this.name);
		sb.append(this.data);
		return sb.append(MarcChar.FIELD_TERMINATOR).toString();
	}
}
