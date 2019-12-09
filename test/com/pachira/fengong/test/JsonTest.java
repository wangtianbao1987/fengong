package com.pachira.fengong.test;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

public class JsonTest {
	
	@Test
	public void test01() {
		Map<String,Object> map = new HashMap<String,Object>();
		map.put("aaa", "bbb");
		List<Map<String, Object>> list = new ArrayList<>();
		list.add(map);
		JSONObject jobj = JSONObject.fromObject(map);
		System.out.println(jobj.toString());
		JSONArray jarr = JSONArray.fromObject(list);
		System.out.println(jarr.toString());
	}
}
