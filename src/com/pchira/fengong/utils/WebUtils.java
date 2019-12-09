package com.pchira.fengong.utils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class WebUtils {
	public static void print(String message, HttpServletRequest request, HttpServletResponse response) {
		try {
			request.setAttribute("message", message);
			request.getRequestDispatcher("message.jsp").forward(request, response);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
