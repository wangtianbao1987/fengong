package com.pchira.fengong.utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.pachira.info.utils.CommonUtils;

public class DBUtils {
	
	public static Connection getConnection() {
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			return DriverManager.getConnection("jdbc:mysql://172.22.144.135:3306/fengong?useSSL=false&characterEncoding=utf8&allowMultiQueries=true&zeroDateTimeBehavior=convertToNull", "root", "pachira");
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}
	
	public static int executeSql(Connection conn, String sql, Object[] paramVals) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < paramVals.length; i++) {
				pstmt.setObject(i+1, paramVals[i]);
			}
			return pstmt.executeUpdate();
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			CommonUtils.close(pstmt);
		}
	}
	
	public static int[] executeSql(Connection conn, String sql, List<Object[]> paramValss) {
		PreparedStatement pstmt = null;
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < paramValss.size(); i++) {
				Object[] paramVals = paramValss.get(i);
				for (int j = 0; j < paramVals.length; j++) {
					pstmt.setObject(j+1, paramVals[j]);
				}
				pstmt.addBatch();
			}
			return pstmt.executeBatch();
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		} finally {
			CommonUtils.close(pstmt);
		}
	}
	
	public static int executeSql(Connection conn, String sql) {
		Statement stmt = null;
		try {
			stmt = conn.createStatement();
			return stmt.executeUpdate(sql);
		} catch (Exception e) {
			e.printStackTrace();
			return -1;
		} finally {
			CommonUtils.close(stmt);
		}
	}
	
	
	public static List<Map<String, Object>> queryBySql(Connection conn, String sql) {
		Statement stmt = null;
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		ResultSet rs = null;
		try {
			stmt = conn.createStatement();
			rs = stmt.executeQuery(sql);
			ResultSetMetaData md = rs.getMetaData();
			int columCount = md.getColumnCount();
			String[] fields = new String[columCount];
			for(int i = 0; i < columCount; i++) {
				fields[i] = md.getColumnName(i+1);
			}
			while (rs.next()) {
				Map<String, Object> item = new HashMap<String, Object>();
				for (String fieldName : fields) {
					item.put(fieldName, rs.getObject(fieldName));
				}
				res.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(rs, stmt);
		}
		return res;
	}
	
	public static List<Map<String, Object>> queryBySql(Connection conn, String sql, Object[] paramVals) {
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		List<Map<String, Object>> res = new ArrayList<Map<String, Object>>();
		try {
			pstmt = conn.prepareStatement(sql);
			for (int i = 0; i < paramVals.length; i++) {
				pstmt.setObject(i+1, paramVals[i]);
			}
			rs = pstmt.executeQuery();
			
			ResultSetMetaData md = rs.getMetaData();
			int columCount = md.getColumnCount();
			String[] fields = new String[columCount];
			for(int i = 0; i < columCount; i++) {
				fields[i] = md.getColumnName(i+1);
			}
			while (rs.next()) {
				Map<String, Object> item = new HashMap<String, Object>();
				for (String fieldName : fields) {
					item.put(fieldName, rs.getObject(fieldName));
				}
				res.add(item);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(rs, pstmt);
		}
		return res;
	}
	
}
