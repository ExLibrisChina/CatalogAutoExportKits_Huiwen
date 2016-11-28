package com.proquest.apac.core.marc;

/**
 * 常用的MARC符号定义
 * @author wavecheng
 * @since  2009-1-12 下午02:18:30
 */
public interface MarcChar {

	/**
	 * 字段结束符 0x001E char(30)
	 */
	public static final char FIELD_TERMINATOR = 0x001E;
	
	/**
	 * 记录结束符 0x001D char(29)
	 */
	public static final char RECORD_TERMINATOR = 0x001D;
	
	/**
	 * 子字段结束符 0x001F char(31)
	 */
	public static final char SUBFIELD_DELIMITER = 0x001F;
	
	/**
	 * US MARC 标识("U")
	 */
	public static final char US_MARC = 'U';
	
	/**
	 * CN MARC 标识("C")
	 */
	public static final char CN_MARC = 'C';
	
}
