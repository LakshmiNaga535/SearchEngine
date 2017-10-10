package com.nagavardhan.server;
import org.eclipse.jetty.webapp.WebAppContext;

public class ApplicationContextBuilder {

	private WebAppContext webAppContext;

	public WebAppContext buildWebAppContext(){
		webAppContext = new WebAppContext();
		webAppContext.setDescriptor(webAppContext + "/WEB-INF/web.xml");
		webAppContext.setResourceBase(".");
		webAppContext.setContextPath("/");
		return webAppContext;
	}
}