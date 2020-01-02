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

/**
 * Servlet implementation class ShowMarks
 */
@WebServlet("/showMarks")
public class ShowMarks extends HttpServlet {
	private static final long serialVersionUID = 1L;
       
    /**
     * @see HttpServlet#HttpServlet()
     */
    public ShowMarks() {
        super();
        // TODO Auto-generated constructor stub
    }

	/**
	 * @see HttpServlet#doGet(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		doPost(request, response);
	}

	/**
	 * @see HttpServlet#doPost(HttpServletRequest request, HttpServletResponse response)
	 */
	protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
		Connection conn = null;
		try {
			String sql = "select filePath,type,recevCont,markContent from t_mark order by type";
			conn = DBUtils.getConnection();
			List<Map<String, Object>> markList = DBUtils.queryBySql(conn, sql);
			request.setAttribute("markList", markList);
			request.getRequestDispatcher("showMarks.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			CommonUtils.close(conn);
		}
	}

}
