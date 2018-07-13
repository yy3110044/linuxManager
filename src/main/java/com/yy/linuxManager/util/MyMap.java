package com.yy.linuxManager.util;

import java.util.HashMap;

public class MyMap extends HashMap<String, Object> {
	private static final long serialVersionUID = 6746390068252624576L;

	public MyMap set(String key, Object value) {
		super.put(key, value);
		return this;
	}
}