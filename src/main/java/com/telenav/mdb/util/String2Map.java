package com.telenav.mdb.util;

import java.util.HashMap;
import java.util.Map;

public class String2Map {

	public static String map2Str(Map<String, String> map) {
		return map.toString();
	}

	public static Map<String, String> str2map(String str) {
		final HashMap<String, String> retVal = new HashMap<String, String>();
		str = str.substring(1, str.length() - 1);
		String[] pair = str.split(",");
		for (String p : pair) {
			p = p.trim();
			int idx = p.indexOf('=');
			retVal.put(p.substring(0, idx), p.substring(idx + 1));
		}

		return retVal;
	}

	public static void main(String[] args) {
		Map<String, String> map = new HashMap<String, String>();
		map.put("color", "red");
		map.put("symbols", "e");
		map.put("empty", "");

		String str = map2Str(map);
		System.out.println(str);

		Map<String, String> map2 = str2map(str);
		System.out.println(map2);
	}

}
