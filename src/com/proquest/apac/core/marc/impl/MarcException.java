package com.proquest.apac.core.marc.impl;

/**
 * @author wavecheng
 * @since  2009-1-12 上午10:16:42
 * MARC解析生成相关的一切异常
 */
public class MarcException extends RuntimeException {

	public MarcException() {
	}

	public MarcException(String message) {
		super(message);
	}

	public MarcException(Throwable cause) {
		super(cause);
	}

	public MarcException(String message, Throwable cause) {
		super(message, cause);
	}
}
