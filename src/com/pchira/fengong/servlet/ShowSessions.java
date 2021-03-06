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

@WebServlet("/showSessions")
public class ShowSessions extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			conn = DBUtils.getConnection();
			String sql = "select id,filePath,userName,receiveStatus,telUnHit,idCardUnHit,jobNumUnHit,sexUnHit,ageUnHit,addressUnHit,yinlianUnHit,sessions from t_files where id=" + id;
			List<Map<String, Object>> findList = DBUtils.queryBySql(conn, sql);
			if(findList == null || findList.isEmpty()) {
				WebUtils.print("id传入有误", request, response);
				return;
			}
			Map<String, Object> map = findList.get(0);
			
			sql = "select type,roleName,startItem,startWordsIndex,endItem,endWordsIndex,recevCont,location from t_mark where filePath=?";
			List<Map<String, Object>> markList = DBUtils.queryBySql(conn, sql, new Object[] {map.get("filePath")});
			sql = "select type,roleName,startItem,startWordsIndex,endItem,endWordsIndex,showStr,location from t_hit_data where filePath=?";
			List<Map<String, Object>> hitList = DBUtils.queryBySql(conn, sql, new Object[] {map.get("filePath")});
			
			request.setAttribute("item", map);
			request.setAttribute("markList", markList);
			request.setAttribute("hitList", hitList);
			
			request.getRequestDispatcher("showSessions.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.print("异常：" + e.getMessage(), request, response);
		} finally {
			CommonUtils.close(conn);
		}
	}

}
