package com.pchira.fengong.utils;

public class NumUtils {
	public static int jian(Object o1, Object o2) {
		try {
			int k1 = obj2Int(o1);
			int k2 = obj2Int(o2);
			return k1 - k2;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int chu(Object o1, Object o2) {
		try {
			int k1 = obj2Int(o1);
			int k2 = obj2Int(o2);
			return k1 / k2;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int jia(Object... os) {
		try {
			int res = 0;
			for(Object o : os) {
				res += obj2Int(o);
			}
			return res;
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static int obj2Int(Object o) {
		try {
			return Integer.parseInt(o.toString());
		} catch (Exception e) {
			return 0;
		}
	}
	
	
	
}
