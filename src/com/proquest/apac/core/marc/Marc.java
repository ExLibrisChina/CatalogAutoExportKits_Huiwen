package com.proquest.apac.core.marc;

import java.io.Serializable;
import java.util.List;

/**
 * MARC
 * @author wavecheng
 * @since  2009-1-12 上午10:54:57
 */
public interface Marc extends Serializable {

	
	/**
	 * 获取给定的字段和子字段的字符串，以空额区分值域。'_'表任意
	 * @param field marc字段，如"200","2__" , "20_"
	 * @param sub 子字段,如'a','A','_'
	 * @return 以空格区分的值串
	 */
	public String getSubField(String field, char sub);

	/**
	 * 获取给定的字段和子字段的字符串，以空格区分值域。'_'表任意
	 * @param field marc字段，如"200a","2__a" , "20__"
	 * @return 以空格区分的值串
	 */
	public String getSubField(String full);

	
	/**
	 * 获取字段内容
	 * @param field 字段 
	 * @return
	 */
	public List<Field> getField(String field);
	
	/**
	 * 根据给定的字段获取相对应的数据
	 * @param strs 子字段数组
	 * @return 字段内容
	 */
	public List<Field> getMultiFields(String[] fields);

	/**
	 * 删除marc中特定的一些字段
	 * @param strs 字段数组
	 * @return 新的marc对象
	 */
	public Marc removeFields(String[] field);

	
	/**
	 * 删除MARC的一个字段
	 * @param field 字段名
	 * @return 新MARC
	 */
	public Marc removeField(String field);
	
	
	/**
	 * 增加一个MARC字段
	 * @param f MARC字段
	 * @return 新MARC
	 */
	public Marc addField(Field f);
	
	
	/**
	 * 枚举全部字段
	 * @return 全部字段
	 */
	public List<Field> listFields();
	
	
	/**
	 * 字符串形式的MARC数据流
	 * @return MARC字符串
	 */
	public String getStrMarc();

}
