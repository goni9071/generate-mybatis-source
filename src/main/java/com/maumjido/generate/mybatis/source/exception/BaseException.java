package com.maumjido.generate.mybatis.source.exception;

public class BaseException extends RuntimeException {

	private static final long serialVersionUID = -6945386572067148265L;

	public BaseException() {
	}

	BaseException(String who, String where, String what, String how, Throwable cause) {
		super(String.format("%s,%s,%s,%s", who, where, what, how), cause);
	}

	BaseException(String who, String where, String what, String how) {
		super(String.format("%s,%s,%s,%s", who, where, what, how));
	}

	BaseException(String who, String where, String what) {
		super(String.format("%s,%s,%s", who, where, what));
	}

	public BaseException(String who, String where, String what, Throwable cause) {
		super(String.format("%s,%s,%s", who, where, what), cause);
	}
}
