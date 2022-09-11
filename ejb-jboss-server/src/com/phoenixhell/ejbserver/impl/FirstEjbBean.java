package com.phoenixhell.ejbserver.impl;

import javax.activation.DataSource;
import javax.annotation.Resource;
import javax.ejb.EJB;
import javax.ejb.Local;
import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.ejb.TimerService;
import javax.naming.InitialContext;
import javax.naming.NamingException;

import com.phoenixhell.ejbserver.FirstEjb;
import com.phoenixhell.ejbserver.FirstEjbLocal;
import com.phoenixhell.ejbserver.Other;

/**
 * Session Bean implementation class FirstEjb
 */

//不写的话默认实现就是本地接口
@Remote(FirstEjb.class)
@Local(FirstEjbLocal.class)
//name 供注入的时候选择名字
@Stateless(name = "FirstEjbBean")
public class FirstEjbBean implements FirstEjb, FirstEjbLocal {

	//多个bean可以指定名字 只有一个实现类可以不写
	@EJB(beanName = "OtherBean")
	private Other other;

	// @EJB Other other; 和上面方法一样
	
	
	//定时服务 数据源 不能使用EJB注解 因为是容器提供的服务
	@Resource
	private TimerService timerService;
	
	//mappedName 用于指定数据源的jndi 名称
	@Resource(mappedName = "java:xxxx")
	private DataSource dataSource;
	
	

	public String sayHello(String name) {
		// 在FirstBean 里面调用本类的sayOther方法 使用jndi查找ejb bean

//		try {
//
//			InitialContext context = new InitialContext();
//			Other other = (Other) context.lookup("OtherBean/local");
//			return "hello " + name + "------" + other.sayOther();
//		} catch (NamingException e) {
//			e.printStackTrace();
//		}

		// 使用依赖注入方式得到OtherBean

		return "hello " + name + "------" + other.sayOther("@EJB 注入成功");
	}
}
