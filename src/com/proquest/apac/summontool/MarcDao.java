package com.proquest.apac.summontool;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * @author PChen
 * 从数据库中获取MARC的数据的接口
 */
public interface MarcDao {
	
	/**
	 * 获取更新MARC的ID列表
	 * @param lastCheckedDate 上次检查的日期
	 * @return 从上次检查日期到现在的不同的MARC Id列表
	 */
	List<String> getUpdatedCNMarcIdList(Date lastCheckedDate);
	
	List<String> getUpdatedUSMarcIdList(Date lastCheckedDate);
	
	/**
	 * 获取删除MARC的ID列表
	 * @param lastCheckedDate 上次检查的日期
	 * @return 从上次检查日期到现在的不同MARC ID列表
	 */
	List<String> getDeletedCNMarcIdList(Date lastCheckedDate);
	
	List<String> getDeletedUSMarcIdList(Date lastCheckedDate);
	/**
	 * 获取完整的MARC流
	 * @param marcId 标识号
	 * @return 该条MARC的数据流
	 */
	String getSingleMarcData(String marcId);
	
	/**
	 * 拼接馆藏地信息
	 * @param marcId 标识号
	 * @return 拼接成一个字符串的馆藏地代码列表
	 */
	String getItemLocationByMarcId(String marcId);
	
	/**
	 * @return 馆藏地对照表
	 */
	Map<String,String> getLocationMap();
	
	/**
	 * @return CN 全marc列表
	 */
	List<String> getCNMarcFullList();
	
	/**
	 * @return US 全marc列表
	 */
	List<String> getUSMarcFullList();
	
	/**
	 * 检查能否链接数据库及能否访问相应的表
	 * @throws Exception
	 */
	void testAccessbility() throws Exception;
}
