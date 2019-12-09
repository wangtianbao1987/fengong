package com.pachira.fengong.test;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import org.junit.Test;

import com.pachira.info.Analysis;
import com.pachira.info.entity.FindObj;
import com.pachira.info.entity.RecogniItem;
import com.pachira.info.entity.RecognizeText;
import com.pachira.info.utils.CommonUtils;
import com.pchira.fengong.utils.DBUtils;

public class DBTest {
	
	@Test
	public void testConnection() {
		Connection conn = DBUtils.getConnection();
		System.out.println(conn);
		CommonUtils.close(conn);
	}
	
	@Test
	public void initTable() {
		Connection conn = null;
		try {
			File[] files = new File("E:\\测试数据\\200files").listFiles();
			List<Object[]> paramValss1 = new ArrayList<Object[]>();
			List<Object[]> paramValss2 = new ArrayList<Object[]>();
			for (File xmlFile : files) {
				RecognizeText rtext = Analysis.analysis(xmlFile);
				List<RecogniItem> items = rtext.getItems();
				StringBuffer sessions = new StringBuffer();
				StringBuffer role0 = new StringBuffer();
				StringBuffer role1 = new StringBuffer();
				Object[] itemVal1 = new Object[12];
				for (RecogniItem item : items) {
					sessions.append(item.getRoleName()).append(" - ").append(item.getText()).append("\n");
					if ("R0".equals(item.getRoleName())) {
						role0.append(item.getText()).append("\n");
					} else {
						role1.append(item.getText()).append("\n");
					}
	            }
				itemVal1[0] = xmlFile.getAbsolutePath();
				itemVal1[1] = 0;
				itemVal1[2] = 0;
				itemVal1[3] = role0.toString();
				itemVal1[4] = role1.toString();
				itemVal1[5] = sessions.toString();
				itemVal1[6] = 0;
				itemVal1[7] = 0;
				itemVal1[8] = 0;
				itemVal1[9] = 0;
				itemVal1[10] = 0;
				itemVal1[11] = 0;
				paramValss1.add(itemVal1);
				
				List<FindObj> findObjs = rtext.getFindObjs();
				for(int i=0;i<findObjs.size();i++) {
					FindObj findObj = findObjs.get(i);
					Object[] itemVal2 = new Object[10];
					itemVal2[0] = xmlFile.getAbsolutePath();
					itemVal2[1] = findObj.getType();
					itemVal2[2] = findObj.getMatchStr();
					itemVal2[3] = findObj.getShowStr();
					RecogniItem startItem = findObj.getStartItem();
					RecogniItem endItem = findObj.getEndItem();
					itemVal2[4] = startItem.getRoleName();
					itemVal2[5] = startItem.getIndexRoleItem();
					itemVal2[6] = findObj.getStartWords();
					itemVal2[7] = endItem.getIndexRoleItem();
					itemVal2[8] = findObj.getEndWords();
					itemVal2[9] = startItem.getRoleName() + "-" + startItem.getIndexRoleItem() + ":" + findObj.getStartWords() + "," + endItem.getIndexRoleItem() + ":" + findObj.getEndWords();
					paramValss2.add(itemVal2);
				}
				
			}
			conn = DBUtils.getConnection();
			String sql1 = "insert into t_files(filePath,receiveStatus,telUnHit,role0,role1,sessions,idCardUnHit,jobNumUnHit,sexUnHit,ageUnHit,addressUnHit,yinlianUnHit) values (?,?,?,?,?,?,?,?,?,?,?,?)";
			DBUtils.executeSql(conn, sql1, paramValss1);
			String sql2 = "insert into t_hit_data(filePath,type,matchStr,showStr,roleName,startItem,startWordsIndex,endItem,endWordsIndex,location) values (?,?,?,?,?,?,?,?,?,?)";
			DBUtils.executeSql(conn, sql2, paramValss2);
			System.out.println("OVER");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(conn);
		}
	}
	
	@Test
	public void resetHitData() {
		Connection conn = null;
		try {
			File[] files = new File("E:\\测试数据\\200files").listFiles();
			List<Object[]> paramValss2 = new ArrayList<Object[]>();
			for (File xmlFile : files) {
				RecognizeText rtext = Analysis.analysis(xmlFile);
				List<FindObj> findObjs = rtext.getFindObjs();
				for(int i=0;i<findObjs.size();i++) {
					FindObj findObj = findObjs.get(i);
					Object[] itemVal2 = new Object[10];
					itemVal2[0] = xmlFile.getAbsolutePath();
					itemVal2[1] = findObj.getType();
					itemVal2[2] = findObj.getMatchStr();
					itemVal2[3] = findObj.getShowStr();
					RecogniItem startItem = findObj.getStartItem();
					RecogniItem endItem = findObj.getEndItem();
					itemVal2[4] = startItem.getRoleName();
					itemVal2[5] = startItem.getIndexRoleItem();
					itemVal2[6] = findObj.getStartWords();
					itemVal2[7] = endItem.getIndexRoleItem();
					itemVal2[8] = findObj.getEndWords();
					itemVal2[9] = startItem.getRoleName() + "-" + startItem.getIndexRoleItem() + ":" + findObj.getStartWords() + "," + endItem.getIndexRoleItem() + ":" + findObj.getEndWords();
					paramValss2.add(itemVal2);
				}
			}
			conn = DBUtils.getConnection();
			String sql1 = "TRUNCATE TABLE t_hit_data";
			DBUtils.executeSql(conn, sql1);
			String sql2 = "insert into t_hit_data(filePath,type,matchStr,showStr,roleName,startItem,startWordsIndex,endItem,endWordsIndex,location) values (?,?,?,?,?,?,?,?,?,?)";
			DBUtils.executeSql(conn, sql2, paramValss2);
			System.out.println("OVER");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(conn);
		}
	}
	
	
	
	
}
