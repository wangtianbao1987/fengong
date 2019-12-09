package com.pchira.fengong.servlet;

import java.io.IOException;
import java.sql.Connection;
import java.util.UUID;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.pachira.info.utils.CommonUtils;
import com.pchira.fengong.utils.DBUtils;
import com.pchira.fengong.utils.WebUtils;

@WebServlet("/mark")
public class Mark extends HttpServlet {
	private static final long serialVersionUID = 1L;

	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			String filePath = request.getParameter("filePath");
			String type = request.getParameter("type");
			String roleName = request.getParameter("roleName");
			int startItem = Integer.parseInt(request.getParameter("startItem"));
			int startWordsIndex = Integer.parseInt(request.getParameter("startWordsIndex"));
			int endItem = Integer.parseInt(request.getParameter("endItem"));
			int endWordsIndex = Integer.parseInt(request.getParameter("endWordsIndex"));
			String recevCont = request.getParameter("recevCont");
			String id = UUID.randomUUID().toString();
			String sql = "insert into t_mark(id,filePath,type,roleName,startItem,startWordsIndex,endItem,endWordsIndex,recevCont,location) values (?,?,?,?,?,?,?,?,?,?)";
			
			conn = DBUtils.getConnection();
			DBUtils.executeSql(conn, sql, new Object[] {
					id,filePath,type,roleName,startItem,startWordsIndex,endItem,endWordsIndex,recevCont,roleName+"-"+startItem+":"+startWordsIndex+","+endItem+":"+endWordsIndex
			});
			
			response.getWriter().print("id=" + id);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.print("异常：" + e.getMessage(), request, response);
		} finally {
			CommonUtils.close(conn);
		}
	}

}
