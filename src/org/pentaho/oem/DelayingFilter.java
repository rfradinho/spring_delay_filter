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
import javax.servlet.http.HttpSession;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.InitializingBean;

public class DelayingFilter implements Filter, InitializingBean {

	public static final String PENTAHO_SESSION_KEY = "pentaho-session-context";

	protected final Log logger = LogFactory.getLog(this.getClass());


	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain) throws IOException, ServletException {

		HttpServletRequest httpRequest0 = (HttpServletRequest) req;
		MultiReadHttpServletRequest httpRequest = new MultiReadHttpServletRequest(httpRequest0);

		if (logger.isDebugEnabled()) {
			logger.debug("-------------------------------------");
		}


		String username = httpRequest.getParameter("userid");
		String password = httpRequest.getParameter("password");

		boolean hasAuthParameters = (username != null) && (password != null);

		boolean hasJSESSIONID = false;

		Cookie[] cookies = httpRequest.getCookies();
		if (cookies != null) {
			for (Cookie cookie : cookies) {
				if (cookie.getName().equals("JSESSIONID")) {
					hasJSESSIONID = true;
				}
			}
		}
		HttpSession httpSession = httpRequest.getSession(false);

		Object pentahoSession = httpSession != null ? httpSession.getAttribute(PENTAHO_SESSION_KEY) : null;

		boolean pentahoSessionExists = pentahoSession != null;

		if (logger.isDebugEnabled()) {
			logger.debug("hasAuthParameters = " + hasAuthParameters);
			logger.debug("hasJSESSIONID = " + hasJSESSIONID);
			logger.debug("httpSession lookup ok? " + (httpSession != null));
			logger.debug("pentahoSession lookup ok? " + (pentahoSession != null));
			logger.debug("pentahoSessionExists = " + pentahoSessionExists);
		}


		if (!hasAuthParameters && hasJSESSIONID && !pentahoSessionExists) {
			logger.warn("needs sleep(500) !!!");
			try {
				Thread.sleep(500);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		} else {
			logger.debug("no need for sleep().");
		}

		if (logger.isDebugEnabled()) {
			logger.debug("calling next doFilter()");
		}
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