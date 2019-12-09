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
	
	public static double chu(Object o1, Object o2) {
		try {
			Double k1 = obj2Double(o1);
			double k2 = obj2Double(o2);
			if (k2 == 0) {
				return 0D;
			}
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
			return (int)obj2Double(o);
		} catch (Exception e) {
			return 0;
		}
	}
	
	public static double obj2Double(Object o) {
		try {
			return Double.parseDouble(o.toString());
		} catch (Exception e) {
			return 0D;
		}
	}
	
	
}
