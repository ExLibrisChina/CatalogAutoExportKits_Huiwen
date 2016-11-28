package com.proquest.apac.summontool.impl.huiwen;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.ResultSetExtractor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.support.JdbcDaoSupport;

import com.proquest.apac.summontool.MarcDao;

/**
 * @author PChen
 * 汇文系统的marc更新与删除实现
 * 通过查找 log_detl表，按日期获取log_type=20005(update),20017(delete)20014(merged,disappear)的marc记录号
 * 再关联marc表识别对应的是CN还是US后，输出更新的ID列表
 */
public class HuiwenMarcDaoImpl extends JdbcDaoSupport implements MarcDao {

	private String marcUseFlag = "3";
	private String dbUserPrefix = "";
	
	private final Logger logger = LoggerFactory.getLogger(HuiwenMarcDaoImpl.class);
	
	public void setMarcUseFlag(String marcUseFlag) {
		this.marcUseFlag = marcUseFlag;
	}

	public void setDbUserPrefix(String dbUserPrefix) {
		this.dbUserPrefix = dbUserPrefix;
	}

	private class MarcRecNoExtractor implements ResultSetExtractor{
		@Override
		public Object extractData(ResultSet rs) throws SQLException,
				DataAccessException {
			List<String> idList = new ArrayList<String>();
			while(rs.next()){
				idList.add(rs.getString(1));
			}
			return idList;
		}
	}
	
	@Override
	public List<String> getUpdatedCNMarcIdList(Date lastCheckedDate) {
		String lastDate = DateFormatUtils.format(lastCheckedDate, "yyyy-MM-dd");
		String sql = " select distinct marc_rec_no from " + dbUserPrefix + "marc where marc_use_flag>=? and marc_type='C' and marc_rec_no in "
						+ "(select marc_rec_no from " + dbUserPrefix + "log_detl where log_type='20005' and log_date>=? ) ";
		return (List<String>)getJdbcTemplate().query(sql, new Object[]{marcUseFlag,lastDate},new MarcRecNoExtractor());
	}

	@Override
	public List<String> getUpdatedUSMarcIdList(Date lastCheckedDate) {
		String lastDate = DateFormatUtils.format(lastCheckedDate, "yyyy-MM-dd");
		String sql = " select distinct marc_rec_no from " + dbUserPrefix + "marc where marc_use_flag>=? and marc_type='U' and marc_rec_no in "
				+ "(select marc_rec_no from " + dbUserPrefix + "log_detl where log_type='20005' and log_date>=? ) ";
		return (List<String>)getJdbcTemplate().query(sql, new Object[]{marcUseFlag,lastDate},new MarcRecNoExtractor());
	}

	@Override
	public List<String> getDeletedCNMarcIdList(Date lastCheckedDate) {
		String lastDate = DateFormatUtils.format(lastCheckedDate, "yyyy-MM-dd");
		String sql = "select distinct marc_rec_no from " + dbUserPrefix + "marc where marc_type='C' and marc_rec_no in "
					+ " (select marc_rec_no from " + dbUserPrefix + "log_detl where (log_type='20017' or log_type='20014') and log_date>=? )";
		return (List<String>)getJdbcTemplate().query(sql, new Object[]{lastDate},new MarcRecNoExtractor());

	}

	//20017=delete 20014=merged(disappear) 
	@Override
	public List<String> getDeletedUSMarcIdList(Date lastCheckedDate) {
		String lastDate = DateFormatUtils.format(lastCheckedDate, "yyyy-MM-dd");
		String sql = "select distinct marc_rec_no from " + dbUserPrefix + "marc where marc_type='U' and marc_rec_no in "
				+ " (select marc_rec_no from " + dbUserPrefix + "log_detl where (log_type='20017' or log_type='20014') and log_date>=? )";
		return (List<String>)getJdbcTemplate().query(sql, new Object[]{lastDate},new MarcRecNoExtractor());
	}

	@Override
	public String getSingleMarcData(String marcId) {
		String sql = " SELECT marc01,marc02,marc03 FROM " + dbUserPrefix + "marc_data WHERE marc_rec_no=? ORDER BY data_serial";
		String marcStr = (String)getJdbcTemplate().query(sql, new Object[]{marcId},new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				StringBuffer sb = new StringBuffer();
				while(rs.next()){
					if(StringUtils.isNoneBlank(rs.getString(1)))
						sb.append(rs.getString(1));
					if(StringUtils.isNoneBlank(rs.getString(2)))
						sb.append(rs.getString(2));
					if(StringUtils.isNoneBlank(rs.getString(3)))
						sb.append(rs.getString(3));
				}
				return sb.toString();
			}
		});
		logger.debug(marcStr);
		return marcStr;
	}

	@Override
	public String getItemLocationByMarcId(String marcId) {
		String sql = " SELECT distinct location from " + dbUserPrefix + "item WHERE book_stat_code>'40' and marc_rec_no=? ";
		String locaStr = (String)getJdbcTemplate().query(sql, new Object[]{marcId},new ResultSetExtractor() {
			@Override
			public Object extractData(ResultSet rs) throws SQLException,
					DataAccessException {
				List<String> locaList = new ArrayList<String>();
				while(rs.next()){
					locaList.add(StringUtils.trim(rs.getString(1)));
				}
				return StringUtils.join(locaList,",");
			}
		});
		logger.debug("marcId:{}=>loca:{}",marcId,locaStr);
		return locaStr;
	}

	@Override
	public Map<String, String> getLocationMap() {
		String sql= "select trim(location) location,trim(location_name) location_name from " + dbUserPrefix + "location_lst order by 1 ";
		return (Map)getJdbcTemplate().queryForObject(sql, new RowMapper() {
			Map<String,String> map = new HashMap<String, String>();
			@Override
			public Object mapRow(ResultSet rs, int n) throws SQLException {
				while(rs.next()){
					map.put(rs.getString(1), rs.getString(2));
				}
				return map;
			}
		} );
	}

	@Override
	public List<String> getCNMarcFullList() {
		String sql = " select marc_rec_no from " + dbUserPrefix + "marc where marc_use_flag>=? and marc_type='C'order by 1";
		return (List<String>)getJdbcTemplate().query(sql, new Object[]{marcUseFlag},new MarcRecNoExtractor());
	}

	@Override
	public List<String> getUSMarcFullList() {
		String sql = " select marc_rec_no from " + dbUserPrefix + "marc where marc_use_flag>=? and marc_type='U' order by 1 ";
		return (List<String>)getJdbcTemplate().query(sql, new Object[]{marcUseFlag},new MarcRecNoExtractor());
	}

	@Override
	public void testAccessbility() throws Exception {
		//1.test MARC table
		String sql = " select count(*) from " + dbUserPrefix + "marc where  marc_use_flag>=? ";
		int totalMarc = getJdbcTemplate().queryForInt(sql,new Object[]{marcUseFlag});
		logger.info("total MARC count={}",totalMarc);

		//2. test location mapping table
		sql = "select count(*) from " + dbUserPrefix + "location_lst ";
		int totalLoca = getJdbcTemplate().queryForInt(sql);
		logger.info("total LOCATION count={}",totalLoca);
		
		//3. test item location code
		sql = " SELECT location from " + dbUserPrefix + "item where rownum = 1 ";
		getJdbcTemplate().execute(sql);
		
		//4. test MARC data table
		sql = " SELECT marc01 FROM " + dbUserPrefix + "marc_data WHERE rownum = 1";
		getJdbcTemplate().execute(sql);
		
		//5. test LOG_DETL table
		sql = " SELECT count(*) FROM " + dbUserPrefix + "log_detl WHERE rownum = 1";
		getJdbcTemplate().execute(sql);
	}

}
