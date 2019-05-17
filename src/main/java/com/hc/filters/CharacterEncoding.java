package com.hc.filters;

import java.io.IOException;
import java.util.Enumeration;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CharacterEncoding implements Filter {
	@Override
	public void init(FilterConfig filterConfig) throws ServletException {

	}

	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
		HttpServletRequest httpRequest = (HttpServletRequest) request;
		HttpServletResponse httpResponse = (HttpServletResponse) response;
		httpRequest.setCharacterEncoding("UTF-8");
		Enumeration<String> names = httpRequest.getParameterNames();
		while(names.hasMoreElements()) {
			String eleName = names.nextElement();
			String eleValue = httpRequest.getParameter(eleName);
			eleValue = new String(eleValue.getBytes(), "UTF-8");
			httpRequest.setAttribute(eleName, eleValue);
		}
		httpResponse.setHeader("Access-Control-Allow-Origin", httpRequest.getHeader("Origin"));
		httpResponse.setHeader("Access-Control-Allow-Credentials", "true");
		httpResponse.setHeader("Access-Control-Allow-Methods", "GET, POST, OPTIONS");
		httpResponse.setContentType("text/html;charset=utf-8");
		chain.doFilter(httpRequest, httpResponse);
	}

	@Override
	public void destroy() {

	}

}
