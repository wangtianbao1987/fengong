package com.pachira.fengong.test;

import java.io.File;
import java.util.List;

import org.junit.Test;

import com.pachira.info.Analysis;
import com.pachira.info.entity.FindObj;
import com.pachira.info.entity.RecognizeText;
import com.pachira.info.utils.ReadXML;
import com.pachira.info.utils.StringUtils;

public class SingleTest {
	
	@Test
	public void targetStrTest() {
		RecognizeText rtext = ReadXML.readXML(new File("E:\\测试数据\\200files\\20191022030744_22802642_013400192996_1385000.wav.xml"));
		FindObj baseFind = new FindObj();
        baseFind.setType("sex");
        baseFind.setTypeName("性别");
        List<FindObj> list = Analysis.targetStr(rtext, baseFind);
        System.out.println(list);
	}
	
	@Test
	public void indexOfTest() {
		List<Integer> list = StringUtils.indexOf("你好很高兴你好啊啊啊你好我问问你好", "你好");
		System.out.println(list);
	}
}
