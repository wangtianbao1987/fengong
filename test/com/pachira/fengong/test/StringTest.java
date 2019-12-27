package com.pachira.fengong.test;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.junit.Test;

import com.pachira.info.utils.Sheng;

public class StringTest {
	
	@Test
	public void test01() {
		String regex = "";
		for(String name : Sheng.names) {
			regex += ("|"+name);
		}
		regex = regex.substring(1);
		System.out.println(regex);
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher("河南省南阳市镇平县遮山镇钟起营村7组18号");
		while(m.find()) {
			String str = m.group();
			System.out.println(str);
		}
		System.out.println("end..");
	}

}
