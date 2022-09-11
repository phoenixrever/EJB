package com.phoenixhell.ejbcleint;

import java.util.Properties;

import javax.naming.InitialContext;

import com.phoenixhell.ejbServer.FirstEjb;



public class FirstEjbCleint {
	public static void main(String[] args) throws Exception {
		// new InitialContext 时候需要通过配置文件告诉EJB 在哪里 通过JNDI查找到服务器（应该
		//配置文件好像没有作用 先写到代码里面试试
		
//		Properties props = new Properties();
//		props.setProperty("java.naming.factory.initial", "org.jnp.interfaces.NamingContextFactory");
//		//props.setProperty("java.naming.factory.url.pkgs","org.jboss.naming:org.jnp.interfaces");
//		props.setProperty("java.naming.provider.url", "localhost:1099");
		
		//使用jndi配置文件
		InitialContext context = new InitialContext();
		FirstEjb firstEjb = (FirstEjb) context.lookup("FirstEjbBean/remote");
		String name = firstEjb.sayHello("shadow silent");
		System.out.println(name);
	}
}
