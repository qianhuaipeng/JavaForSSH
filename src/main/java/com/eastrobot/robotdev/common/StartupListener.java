/*
 * Power by www.xiaoi.com
 */
package com.eastrobot.robotdev.common;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.web.context.ContextLoaderListener;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import java.util.HashMap;

public class StartupListener extends ContextLoaderListener{

	public static HashMap<String, Integer> visitRecordCache = new HashMap<String, Integer>();


	@Override
	public void contextInitialized(ServletContextEvent event) {
		ServletContext servletContext = event.getServletContext();
		System.setProperty("webAppRoot" , servletContext.getRealPath("/"));
		//LOGGER 如果作为静态变量放在方法体外部会报错
		Log LOGGER = LogFactory.getLog(StartupListener.class);
		LOGGER.info("webAppRoot startup now , root location is [" + System.getProperty("webAppRoot") + "]");
		super.contextInitialized(event);
	}
}
