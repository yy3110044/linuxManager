package com.yy.linuxManager.util;

import com.fasterxml.jackson.annotation.JsonInclude;

@JsonInclude(JsonInclude.Include.NON_NULL)//去掉null值的属性
public class ResponseObject {
	private int code;
	private String msg;
	private Object result;
	
	public ResponseObject() {}
	
	public ResponseObject(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}
	
	public ResponseObject(int code, String msg, Object result) {
		this(code, msg);
		this.result = result;
	}

	public int getCode() {
		return code;
	}

	public String getMsg() {
		return msg;
	}

	public Object getResult() {
		return result;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

	public void setResult(Object result) {
		this.result = result;
	}
}