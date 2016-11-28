package com.proquest.apac.core.marc;

import java.io.Serializable;
import java.util.List;

/**
 * MARC字段
 * @author wavecheng
 * @since  2009-1-12 上午10:46:26
 */
public interface Field extends Serializable ,Comparable {
	


	/**
	 * @return 字段名称
	 */
	public String getName();

	/**
	 * 设置字段名称
	 * @param name 
	 */
	public void setName(String name);

	/**
	 * @return 字段全部内容，包括结束符
	 */
	public String getData();

	/**
	 * 设置字段内容
	 * @param data 
	 */
	public void setData(String data);

	/**
	 * 设置子字段内容
	 * @param subFields 
	 */
	public void setSubFields(List<Subfield> subFields);

	/**
	 * @return 获取全部子字段
	 */
	public List<Subfield> getAllSubFields();

	/**
	 * 查找给定的子字段,如果传入的为'_',返回全部子字段
	 * @param id 子字段ID
	 * @return 子字段列表
	 */
	public List<Subfield> getSubfields(char id);

}
