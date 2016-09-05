package com.oracle.util;

public class ManyUtil {
	/**
	 * ·Ç¿Õ true 
	 * @param str
	 * @return
	 */
	public static boolean isEmpty(String str) {
		if (str != null &&!"".equals(str.trim())) {
			return true;
		} else {
			return false;
		}
	}

}
