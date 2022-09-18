package com.phoenixhell.helloworld;

import java.util.Date;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class Main {
	public static final String  persistenceUnitName="jpa-helloworld";
	
	public static void main(String[] args) {
		//1 第一步创建EntityManager Factory
		EntityManagerFactory entityManagerFactory=Persistence.createEntityManagerFactory(persistenceUnitName);
		
		
		//2 创建EntityManager
		EntityManager entityManager=entityManagerFactory.createEntityManager();
		
		//3 开启事务
		entityManager.getTransaction().begin();
		
		//4 进行持久化操作
		Customer customer = new Customer();
		customer.setAge(12);
		customer.setEmail("shadow@gmail.com");
		customer.setLastName("sahdow silent");
		customer.setBirthDate(new Date());
		customer.setCreatedTime(new Date());
		
		entityManager.persist(customer);
		
		//5 提交事务
		entityManager.getTransaction().commit();

		//6 关闭entityManager
		entityManager.close();
		
		//7 关闭EntityManager Factory
		entityManagerFactory.close();
	}
}
