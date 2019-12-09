package com.pchira.fengong.servlet;

import java.io.IOException;
import java.net.URLDecoder;
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

@WebServlet("/receive")
public class Receive extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			int id = Integer.parseInt(request.getParameter("id"));
			String userName = URLDecoder.decode(request.getParameter("userName"), "UTF-8");
			conn = DBUtils.getConnection();
			String sql = "select id,filePath,userName,receiveStatus,telUnHit,idCardUnHit,jobNumUnHit,sexUnHit,ageUnHit,addressUnHit,yinlianUnHit from t_files where id=" + id;
			List<Map<String, Object>> findList = DBUtils.queryBySql(conn, sql);
			if(findList == null || findList.isEmpty()) {
				WebUtils.print("id传入有误", request, response);
				return;
			}
			Map<String, Object> map = findList.get(0);
			int receiveStatus = Integer.parseInt(map.get("receiveStatus").toString());
			Object userNamex = map.get("userName");
			if (receiveStatus != 0 && !userName.equals(userNamex)) {
				WebUtils.print("已被其他用户提取：" + userNamex, request, response);
				return;
			}
			
			if (!userName.equals(userNamex)) {
				sql = "select count(1) ct from t_files where userName=?";
				findList = DBUtils.queryBySql(conn, sql, new Object[] {userName});
				if(findList != null && !findList.isEmpty()) {
					int ct = Integer.parseInt(findList.get(0).get("ct").toString());
					if (ct >= 20) {
						WebUtils.print("提取文件已达20条，不需要再做了", request, response);
						return;
					}
				}
				
				sql = "update t_files set userName=?, receiveStatus=? where id=?";
				DBUtils.executeSql(conn, sql, new Object[] {userName, 1, id});
			}
			
			sql = "select * from t_mark where filePath=?";
			List<Map<String, Object>> markList = DBUtils.queryBySql(conn, sql, new Object[] {map.get("filePath")});
			
			request.setAttribute("item", map);
			request.setAttribute("markList", markList);
			request.getRequestDispatcher("show.jsp").forward(request, response);
			
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.print("异常：" + e.getMessage(), request, response);
		} finally {
			CommonUtils.close(conn);
		}
	}

}
