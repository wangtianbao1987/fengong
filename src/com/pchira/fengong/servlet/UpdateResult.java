package com.pchira.fengong.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.List;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pachira.info.utils.CommonUtils;
import com.pchira.fengong.utils.DBUtils;
import com.pchira.fengong.utils.WebUtils;

@WebServlet("/updateResult")
public class UpdateResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
    public UpdateResult() {
        super();
    }

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			conn = DBUtils.getConnection();
			
			DBUtils.executeSql(conn, "update t_hit_data set hit=null,succ=null");
			DBUtils.executeSql(conn, "update t_files set telUnHit=0,idCardUnHit=0,jobNumUnHit=0,sexUnHit=0,ageUnHit=0,addressUnHit=0,yinlianUnHit=0");
			DBUtils.executeSql(conn, "update t_hit_data set hit=1 where exists (select 1 from t_files t3 where t3.receiveStatus=2 and t3.filePath=t_hit_data.filePath) and exists (select 1 from t_mark t2 where t2.filePath=t_hit_data.filePath and t2.roleName=t_hit_data.roleName and t2.type=t_hit_data.type and t2.startItem=t_hit_data.startItem and t2.startWordsIndex=t_hit_data.startWordsIndex)");
			DBUtils.executeSql(conn, "update t_hit_data set succ=1 where exists (select 1 from t_files t3 where t3.receiveStatus=2 and t3.filePath=t_hit_data.filePath) and exists (select 1 from t_mark t2 where t2.filePath=t_hit_data.filePath and t2.location=t_hit_data.location)");
			DBUtils.executeSql(conn, "update t_hit_data set hit=0 where exists (select 1 from t_files t3 where t3.receiveStatus=2 and t3.filePath=t_hit_data.filePath) and hit is null");
			DBUtils.executeSql(conn, "update t_hit_data set succ=0 where exists (select 1 from t_files t3 where t3.receiveStatus=2 and t3.filePath=t_hit_data.filePath) and succ is null");
			
			
			List<Map<String, Object>> list = DBUtils.queryBySql(conn, "select filePath from t_files where receiveStatus=2");
			String[] types = "tel,idCard,jobNum,sex,age,address,yinlian".split(",");
			for(Map<String, Object> item : list) {
				String filePath = item.get("filePath").toString();
				for(String type : types) {
					String sql1 = "select count(1) ct from t_mark where filePath=? and type=?";
					Object[] params1 = new Object[] {filePath, type};
					String sql2 = "select count(1) ct from t_hit_data where filePath=? and type=? and hit=1";
					int unHitCount = Integer.parseInt(DBUtils.queryBySql(conn, sql1, params1).get(0).get("ct").toString())
							- Integer.parseInt(DBUtils.queryBySql(conn, sql2, params1).get(0).get("ct").toString());
					
					Object[] params3 = new Object[] {unHitCount,filePath};
					String sql3 = "update t_files set "+type+"UnHit=? where filePath=?";
					DBUtils.executeSql(conn, sql3, params3);
					
				}
			}
			WebUtils.print("OK", request, response);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.print("异常：" + e.getMessage(), request, response);
		} finally {
			CommonUtils.close(conn);
		}
	}

}
