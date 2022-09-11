package com.phoenixhell.test;

import java.util.Properties;

import javax.naming.InitialContext;

import com.phoenixhell.ejbserver.FirstEjb;



public class FirstEjbCleint {
	public static void main(String[] args) throws Exception {
		// new InitialContext 时候需要通过配置文件告诉EJB 在哪里 通过JNDI查找到服务器（应该

		
//		Properties props = new Properties();
//		props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
//		//props.setProperty("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
//		props.setProperty("java.naming.provider.url", "localhost:1099");
//		
		//使用配置文件查找
		InitialContext context = new InitialContext();
		FirstEjb firstEjb = (FirstEjb) context.lookup("FirstEjbBean/remote");
		String name = firstEjb.sayHello("shadow silent remote");
		System.out.println(name);
	}
}
