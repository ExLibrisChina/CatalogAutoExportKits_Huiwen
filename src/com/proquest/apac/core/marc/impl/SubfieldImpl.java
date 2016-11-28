package com.proquest.apac.core.marc.impl;

import com.proquest.apac.core.marc.MarcChar;
import com.proquest.apac.core.marc.Subfield;

public class SubfieldImpl implements Subfield {
	
	private char id;

	private String value;

	public SubfieldImpl(char id, String value) {
		this.id = id;
		this.value = value;
	}

	public char getId() {
		return id;
	}

	public void setId(char id) {
		this.id = id;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}
	
	public String toString(){
		return id + value + String.valueOf(MarcChar.SUBFIELD_DELIMITER);
	}
	
	
}
