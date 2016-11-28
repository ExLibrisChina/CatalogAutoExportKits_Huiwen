package com.proquest.apac.core.marc.impl;

import java.util.ArrayList;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proquest.apac.core.marc.Field;
import com.proquest.apac.core.marc.Marc;
import com.proquest.apac.core.marc.MarcChar;
import com.proquest.apac.core.marc.MarcComposer;
import com.proquest.apac.core.marc.Subfield;

public class MarcImpl implements Marc {

	/**
	 * MARC字段列表
	 */
	private List<Field> fields = new ArrayList<Field>();

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(MarcImpl.class);

	/**
	 * MARC字符流
	 */
	private String strMarc; 
	
	/**
	 * 头标区
	 */
	private String strHeader ;
	
	public MarcImpl(String strMarc){
		this.strMarc = strMarc;
		analyze();
	}
	
	/**
	 * 按字段分隔符切分数据区
	 */
	protected void analyze() {
		try {	
			strHeader = strMarc.substring(0, 24); // 头标区24位
			int i = Integer.parseInt(strHeader.substring(12, 17)); // 12－16 数据起始地址
			int iFieldNum = (i - 24) / 12; // 字段数
			String strData = strMarc.substring(i); // 数据区字符串
			String datas[] = strData.split(new String( new char[] { MarcChar.FIELD_TERMINATOR }));
			String strDir = strMarc.substring(24, i - 1); // 目次区字符串			
			String strTemp = strDir; // copy目次区字符串，进行分析
			
			//marc中可能出现填写不正确的错误,以最短的长度作循环处理
			int count = (datas.length - 1) > iFieldNum ? iFieldNum : (datas.length - 1); 
			for (i = 0; i < count; i++) {
				String field = strTemp.substring(0, 3); // 字段代码
				fields.add(new FieldImpl(field, datas[i]));
				strTemp = strTemp.substring(12); // 下一个字段	
			}
		} catch (Exception e) {
			throw new MarcException("parsing MARC error, msg=[" + e.getMessage() + "], marc string=[" + strMarc +"],", e);
		}
	}

	/**
	 * 删除字段后重新构造的MARC
	 * @param delList 需要删除的字段列表
	 * @return 新MARC
	 */
	private Marc rebuildMarc(List<Field> delList){
		if(delList != null)
			this.fields.removeAll(delList);
		
		MarcComposer marcBuilder = new Utf8MarcComposer(strHeader);
		return new MarcImpl(marcBuilder.compose(this.fields));
	}
	
	public Marc removeField(String field){
		List<Field> delList = getField(field);
		return rebuildMarc(delList);
	}
	
	public Marc removeFields(String[] fields) {
		List<Field> delList = getMultiFields(fields);
		return rebuildMarc(delList);
	}

	public List<Field> getField(String field) {
		if(field == null || field.trim().length() == 0)
			return new ArrayList<Field>(0);
		
		List<Field> findLst = new ArrayList<Field>();
		for(Field f : fields){
			// 判断是否部分匹配
			int pos = field.indexOf('_');
			if (pos > 0)
				field = field.substring(0, pos);
			else
			    pos = field.length();
			
			//前方一致比较，加入查询结果集
			String comp = f.getName().substring(0, pos);
			if(field.equals(comp))
				findLst.add(f);
		}
		return findLst;
	}

	public List<Field> getMultiFields(String[] fields) {
		if(fields == null )
			return new ArrayList<Field>(0);
		
		List<Field> retList = new ArrayList<Field>();
		for(String s : fields){
			retList.addAll(getField(s));
		}
		return retList;
	}

	public String getStrMarc() {
		return this.strMarc;
	}


	public String getSubField(String field, char sub) {
		List<Field> findLst = getField(field);
		StringBuffer sb = new StringBuffer("");
		for(Field f : findLst){
			List<Subfield> subs =  f.getSubfields(sub);
			for(Subfield s : subs)
				sb.append(s.getValue()).append(" ");
		}
		return sb.toString();
	}

	public String getSubField(String full) {
		if(null == full && full.length() <4)
			throw new MarcException(full + "长度错误，必须为4位");
		
		return getSubField(full.substring(0,3),full.charAt(3));
	}

	public List<Field> listFields() {
		return this.fields;
	}

	public Marc addField(Field f) {
		this.fields.add(f);
		MarcComposer marcBuilder = new Utf8MarcComposer(strHeader);
		return new MarcImpl(marcBuilder.compose(this.fields));
	}

}
