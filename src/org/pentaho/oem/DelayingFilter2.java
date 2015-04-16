package org.pentaho.oem;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.InitializingBean;
import org.springframework.security.Authentication;
import org.springframework.security.context.SecurityContext;
import org.springframework.security.context.SecurityContextHolder;

public class DelayingFilter2 implements Filter, InitializingBean {

	@Override
	public void doFilter(ServletRequest req, ServletResponse res,
			FilterChain chain) throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;

		System.err.println("-- cookies2 --");
		Cookie[] cookies = request.getCookies();
		for (Cookie cookie : cookies) {
			System.err.println(cookie.getName() + " => "+cookie.getValue());
		}

		SecurityContext context = SecurityContextHolder.getContext();
		System.err.println("context lookup ok? " + (context!=null));
		
		Authentication authentication = context!=null ? context.getAuthentication() : null;
		System.err.println("authentication lookup ok? " + (authentication!=null));

		chain.doFilter(req, res);

	}

	@Override
	public void init(FilterConfig arg0) throws ServletException {
	}

	@Override
	public void destroy() {
	}

	@Override
	public void afterPropertiesSet() throws Exception {
	}

}