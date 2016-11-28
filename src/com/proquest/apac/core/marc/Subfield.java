package com.proquest.apac.core.marc;

import java.io.Serializable;

/**
 * MARC子字段
 * @author wavecheng
 * @since  2009-1-12 上午10:54:27
 */
public interface Subfield extends Serializable {

	/**
	 * 字段ID
	 * @return 字段ID
	 */
	public char getId();

	/**
	 * 设置字段ID
	 * @param id 字段ID
	 */
	public void setId(char id);

	/**
	 * 字段内容
	 * @return 字段内容
	 */
	public String getValue();

	/**
	 * 设置字段内容
	 * @param value 字段内容
	 */
	public void setValue(String value);
	
}
