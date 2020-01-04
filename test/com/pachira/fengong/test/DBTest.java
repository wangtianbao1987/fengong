package com.pachira.fengong.test;

import java.io.File;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
				List<RecogniItem> items = rtext.getItems();
				List<FindObj> findObjs = rtext.getFindObjs();
				for(int i=0;i<findObjs.size();i++) {
					FindObj findObj = findObjs.get(i);
					Object[] itemVal2 = new Object[11];
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
					
					StringBuffer sb = new StringBuffer();
					int start = findObj.getStartItem().getIndexRoleItem();
					int end = findObj.getEndItem().getIndexRoleItem();
					String rn = items.get(0).getRoleName();
					int py = 0;
					if (!rn.equals(startItem.getRoleName())) {
						py = 1;
					}
					for(int j = start; j <= end; j++) {
						RecogniItem item = items.get(j * 2 + py);
						String text = item.getText();
						int startWords = findObj.getStartWords();
						int endWords = findObj.getEndWords();
						if(j == start) {
							text = text.substring(startWords);
							endWords = endWords-startWords;
						}
						if(j == end) {
							text = text.substring(0, endWords);
						}
						sb.append(text + "★");
					}
					itemVal2[10] = sb.toString();
					paramValss2.add(itemVal2);
				}
			}
			conn = DBUtils.getConnection();
			String sql1 = "TRUNCATE TABLE t_hit_data";
			DBUtils.executeSql(conn, sql1);
			String sql2 = "insert into t_hit_data(filePath,type,matchStr,showStr,roleName,startItem,startWordsIndex,endItem,endWordsIndex,location,str) values (?,?,?,?,?,?,?,?,?,?,?)";
			DBUtils.executeSql(conn, sql2, paramValss2);
			System.out.println("OVER");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(conn);
		}
	}
	
	@Test
	public void setMarkContent() {
		Connection conn = null;
		try {
			
			String sql1 = "select distinct filePath from t_mark";
			conn = DBUtils.getConnection();
			List<Map<String, Object>> list1 = DBUtils.queryBySql(conn, sql1);
			StringBuilder sb1 = new StringBuilder();
			Object[] filePaths = new Object[list1.size()];
			for(int i=0;i<list1.size();i++) {
				Map<String, Object> map1 = list1.get(i);
				sb1.append(",?");
				filePaths[i] = map1.get("filePath");
			}
			String sql2 = "select filePath, role0, role1 from t_files where filePath in ("+sb1.substring(1)+")";
			List<Map<String, Object>> list2 = DBUtils.queryBySql(conn, sql2, filePaths);
			Map<String, Map<String, Object>> MAP2 = new HashMap<String, Map<String, Object>>();
			for(Map<String, Object> map2 : list2) {
				MAP2.put(map2.get("filePath").toString(), map2);
			}
			
			String sql3 = "select id, filePath, roleName, startItem, endItem from t_mark";
			List<Map<String, Object>> list3 = DBUtils.queryBySql(conn, sql3);
			String sql4 = "update t_mark set markContent=? where id=?";
			List<Object[]> PARAM4 = new ArrayList<Object[]>();
			for(Map<String, Object> map3 : list3) {
				try {
					Object[] param4 = new Object[2];
					String filePath = map3.get("filePath").toString();
					Map<String, Object> map2 = MAP2.get(filePath);
					String roleName = map3.get("roleName").toString();
					String[] speaks = null;
					if("R0".equals(roleName)) {
						speaks = map2.get("role0").toString().split("\n");
					} else {
						speaks = map2.get("role1").toString().split("\n");
					}
					StringBuilder sb = new StringBuilder();
					int startItem = Integer.parseInt(map3.get("startItem").toString());
					int endItem = Integer.parseInt(map3.get("endItem").toString());
					for(int i = startItem; i <= endItem; i++) {
						sb.append(speaks[i]).append("★");
					}
					param4[0] = sb.toString();
					param4[1] = map3.get("id").toString();
					PARAM4.add(param4);
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
			DBUtils.executeSql(conn, sql4, PARAM4);
			
			System.out.println("OVER");
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(conn);
		}
	}
	
	@Test
	public void setHit() {
		Connection conn = null;
		try {
			String sql = "UPDATE t_mark set hit=1 where EXISTS(SELECT 1 from t_hit_data t where t.type=t_mark.type and t.startItem=t_mark.startItem and t.roleName=t_mark.roleName)";
			conn = DBUtils.getConnection();
			DBUtils.executeSql(conn, sql);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(conn);
		}
	}
	
	
}
