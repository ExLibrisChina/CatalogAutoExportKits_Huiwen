package com.proquest.apac.core.marc;

import java.util.List;

/**
 * MARC生成器
 * @author wavecheng
 * @since  2009-1-12 下午03:12:12
 */
public interface MarcComposer {

	/**
	 * 按字段列表的值构造MARC字符串流
	 * @param fds 字段列表
	 * @return MARC字符串流
	 */
	public String compose(List<Field> fds);
	
}
