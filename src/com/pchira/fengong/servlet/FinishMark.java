package com.pchira.fengong.servlet;

import java.io.IOException;
import java.sql.Connection;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pachira.info.utils.CommonUtils;
import com.pchira.fengong.utils.DBUtils;
import com.pchira.fengong.utils.WebUtils;

@WebServlet("/finishMark")
public class FinishMark extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			int fileId = Integer.parseInt(request.getParameter("fileId"));
			conn = DBUtils.getConnection();
			String sql = "update t_files set receiveStatus=? where id=?";
			DBUtils.executeSql(conn, sql, new Object[] {2, fileId});
			response.sendRedirect("index.jsp");
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.print("异常：" + e.getMessage(), request, response);
		} finally {
			CommonUtils.close(conn);
		}
	}

}
