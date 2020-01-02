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
	
	@Test
	public void test02() {
		String regex = "([0-9十两]+.{0,2}岁)|((?<=年龄.{0,2})[0-9十两]+(.?岁)?)";
		Pattern p = Pattern.compile(regex);
		String input = "对我我的年龄就是40多了。";
		Matcher m = p.matcher(input);
		while(m.find()) {
			System.out.println(m.group());
		}
		System.out.println("END.........");
	}
	
	@Test
	public void test03() {
		String regex = "([0-9]*[02-9][0-9]*(?=.{0,5}为您服务))|((?<=工号.{0,7})[0-9]*[02-9][0-9]*)";
		Pattern p = Pattern.compile(regex);
		String input = "	呃，您这边的话呢？目前所处是绿瘦集团总部啊。客服中心啊，我这边的话呢，本人姓肖哦，我的工号呢是6312";
		Matcher m = p.matcher(input);
		while(m.find()) {
			System.out.println(m.group());
		}
		System.out.println("END.........");
	}

}
