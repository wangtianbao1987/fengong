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

@WebServlet("/result")
public class Result extends HttpServlet {
	private static final long serialVersionUID = 1L;
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			String sql = "select type, count(1) ct, sum(hit) hitSum, sum(succ) succSum from t_hit_data where hit is not null group by type";
			
			conn = DBUtils.getConnection();
			List<Map<String, Object>> list = DBUtils.queryBySql(conn, sql);
			
			sql = "select sum(telUnHit) telUnHitSum, sum(idCardUnHit) idCardUnHitSum, sum(jobNumUnHit) jobNumUnHitSum, sum(sexUnHit) sexUnHitSum, sum(ageUnHit) ageUnHitSum, sum(addressUnHit) addressUnHit, sum(yinlianUnHit) yinlianUnHitSum from t_files where receiveStatus=2";
			Map<String, Object> res = DBUtils.queryBySql(conn, sql).get(0);
			
			for(Map<String, Object> item : list) {
				String type = item.get("type").toString();
				res.put(type, item);
			}
			request.setAttribute("res", res);
			request.getRequestDispatcher("result.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
			WebUtils.print("异常：" + e.getMessage(), request, response);
		} finally {
			CommonUtils.close(conn);
		}
	}

}
