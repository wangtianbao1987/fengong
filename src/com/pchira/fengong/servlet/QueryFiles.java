package com.pchira.fengong.servlet;

import java.io.IOException;
import java.io.PrintWriter;
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

import net.sf.json.JSONArray;

@WebServlet("/queryFiles")
public class QueryFiles extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			String sql = "select id,filePath,userName,receiveStatus,telUnHit,idCardUnHit,jobNumUnHit,sexUnHit,ageUnHit,addressUnHit,yinlianUnHit from t_files order by receiveStatus";
			conn = DBUtils.getConnection();
			List<Map<String, Object>> res = DBUtils.queryBySql(conn, sql);
			PrintWriter out = response.getWriter();
			out.print(JSONArray.fromObject(res).toString());
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(conn);
		}
	}

}
