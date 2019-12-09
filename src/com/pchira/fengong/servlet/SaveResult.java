package com.pchira.fengong.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pachira.info.utils.CommonUtils;
import com.pchira.fengong.utils.DBUtils;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

@WebServlet("/saveResult")
public class SaveResult extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			String data = request.getParameter("data");
			JSONObject jobj = JSONObject.fromObject(data);
			int telUnHit = jobj.getInt("telUnHit");
			int idCardUnHit = jobj.getInt("idCardUnHit");
			int jobNumUnHit = jobj.getInt("jobNumUnHit");
			int sexUnHit = jobj.getInt("sexUnHit");
			int ageUnHit = jobj.getInt("ageUnHit");
			int addressUnHit = jobj.getInt("addressUnHit");
			int yinlianUnHit = jobj.getInt("yinlianUnHit");
			int fileId = jobj.getInt("id");
			String sql = "update t_files set receiveStatus=?, telUnHit=?,idCardUnHit=?,jobNumUnHit=?,sexUnHit=?,ageUnHit=?,addressUnHit=?,yinlianUnHit=? where id=?";
			conn = DBUtils.getConnection();
			DBUtils.executeSql(conn, sql, new Object[] {2, telUnHit,idCardUnHit,jobNumUnHit,sexUnHit,ageUnHit,addressUnHit,yinlianUnHit, fileId});
			
			JSONArray hits = jobj.getJSONArray("hits");
			List<Object[]> params = new ArrayList<Object[]>();
			sql = "update t_hit_data set hit=?, succ=? where id=?";
			for(int i=0;i<hits.size();i++) {
				Object[] param = new Object[3];
				JSONObject hit = hits.getJSONObject(i);
				param[0] = hit.get("hit");
				param[1] = hit.get("succ");
				param[2] = hit.get("id");
				params.add(param);
			}
			DBUtils.executeSql(conn, sql, params);
			
			response.getWriter().print("1");
		} catch (Exception e) {
			e.printStackTrace();
			response.getWriter().print("异常：" + e.getMessage());
		} finally {
			CommonUtils.close(conn);
		}
		
	}

}
