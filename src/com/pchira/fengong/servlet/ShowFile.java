package com.pchira.fengong.servlet;

import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
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
import com.pchira.fengong.utils.FileUtils;
import com.pchira.fengong.utils.WebUtils;

@WebServlet("/showFile")
public class ShowFile extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		OutputStream out = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String sql = "select id,filePath,userName,receiveStatus,telUnHit,idCardUnHit,jobNumUnHit,sexUnHit,ageUnHit,addressUnHit,yinlianUnHit from t_files where id=" + id;
			conn = DBUtils.getConnection();
			List<Map<String, Object>> findList = DBUtils.queryBySql(conn, sql);
			if(findList == null || findList.isEmpty()) {
				WebUtils.print("id传入有误", request, response);
				return;
			}
			Map<String, Object> map = findList.get(0);
			String filePath = map.get("filePath").toString();
			out = response.getOutputStream();
			FileUtils.copyFile(new File(filePath), out);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.print("异常：" + e.getMessage(), request, response);
		} finally {
			CommonUtils.close(conn);
			CommonUtils.close(out);
		}
	}

}
