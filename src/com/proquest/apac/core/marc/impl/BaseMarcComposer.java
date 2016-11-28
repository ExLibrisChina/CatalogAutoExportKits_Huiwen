package com.proquest.apac.core.marc.impl;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.proquest.apac.core.marc.Field;
import com.proquest.apac.core.marc.MarcChar;
import com.proquest.apac.core.marc.MarcComposer;

public class BaseMarcComposer implements MarcComposer {

	/**
	 * 日志
	 */
	private static final Logger log = LoggerFactory.getLogger(BaseMarcComposer.class);
	
	private List<Field> fds;
	private String encoding =  "utf-8";
	private String strHeader;

	public BaseMarcComposer(String strHeader ){
		this.strHeader = strHeader;
	}
	
	public BaseMarcComposer(String strHeader , String encoding){
		this.encoding = encoding;
		this.strHeader = strHeader;
	}
	
	/**
	 * 构造数据区字符串
	 * @return 数据区字符串
	 */
	protected String composeDataStr(){
		StringBuilder sb = new StringBuilder();
		for(Field f : this.fds){
			sb.append(f.getData()).append(MarcChar.FIELD_TERMINATOR);
		}
		return sb.toString();
	}
	
	/**
	 * 构造目次区信息
	 * @return  目次区字符串
	 */
	protected String composeDirStr(){
		//创建目次区
		StringBuilder dir = new StringBuilder("") ;
		
		long lPos = 0;
		int iLength = 0 ;
		long lBegin = 0 ;
		
		for( Field f : this.fds )
		{		
			byte[] bytes = null ;
			try {
				bytes = (f.getData() + String.valueOf(MarcChar.FIELD_TERMINATOR)) .getBytes(encoding);
			} catch (UnsupportedEncodingException e) {
				log.error("不能获取编码为[" + encoding + "]的字节数组",e) ;
				bytes = f.toString().getBytes() ;
			}
			lBegin = lPos ;
			iLength = bytes.length ;
			lPos += bytes.length ;
			//String.format("%1$05d", 23) -- output = 00023 
			dir.append(f.getName()).append(String.format("%1$04d", iLength)).append(String.format("%1$05d", lBegin));
		}
		dir.append(MarcChar.FIELD_TERMINATOR) ;
		return dir.toString();
	}
	
	/**
	 * 构造头标区信息
	 * @param strDir 目次区字符串，用于计算长度
	 * @param strData 数据区字符串，用于计算长度
	 * @return 修改长度后的头标区
	 */
	protected String composeHeaderStr(String strDir, String strData){
		//创建头标区
		String tmpStrDirData = strDir + strData + String.valueOf(MarcChar.RECORD_TERMINATOR);
		byte[] b = null ;
		try {
			b = tmpStrDirData.getBytes(encoding);
		} catch (UnsupportedEncodingException e) {
			log.error("不能获取编码为[" + encoding + "]的字节数组",e) ;
			b = tmpStrDirData.getBytes() ;
		}
		
		int iWholeLen = 24 + b.length;
		int beginPos = 24 + strDir.length() ;
		return  String.format("%1$05d", iWholeLen) + strHeader.substring(5,12) 
							+ String.format("%1$05d", beginPos) + strHeader.substring(17);
	}
	
	
	public String compose(List<Field> fds) {
		this.fds = fds;
		
		//按marc字段排序
		Collections.sort(this.fds);
		
		//remove length=0 fields
		List<Field> remove = new ArrayList<Field>();
		for(Field f: this.fds){
			if(f.getData().length() == 0)
				remove.add(f);
		}
		this.fds.removeAll(remove);
		String strData = composeDataStr();
		String strDir = composeDirStr();
		
		return composeHeaderStr(strDir,strData) + strDir + strData + String.valueOf(MarcChar.RECORD_TERMINATOR);
	}
}
