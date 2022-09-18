package com.phoenixhell.test;


import javax.persistence.EntityManager;

import com.phoenixhell.helloworld.Customer;
import com.phoenixhell.helloworld.EntityManagerUtil;

/**
 * 
 * Test 里面是不饿能测试多线程的 原因没记住
 * 可以用 countDownLatch、thread.join()保证任务中的线程全部执行完毕
 * 最好的方法就是再main里面测试
 *
 */
public class ConcurrentEntityManagerTest {
	public static void main(String[] args) {
//		for (int i = 0; i < 10; i++) {
//			new Thread(() -> {
//				EntityManager entityManager = EntityManagerUtil.getEntityManager();
//				System.out.println("线程" + Thread.currentThread().getName() + "--->" + entityManager);
//				Customer customer = entityManager.find(Customer.class, 27);
//				System.out.println(customer);
//				EntityManagerUtil.closeEntityManager();
//			}, "manger" + i).start();
//		}
		
		
		new Thread(() -> {
			EntityManager entityManager = EntityManagerUtil.getEntityManager();
			System.out.println("线程" + Thread.currentThread().getName() + "--->" + entityManager);
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			
			EntityManagerUtil.closeEntityManager();
		}, "manger").start();
		
		new Thread(() -> {
			try {
				Thread.sleep(5000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			EntityManager entityManager = EntityManagerUtil.getEntityManager();
			Customer customer = entityManager.find(Customer.class, 27);
			System.out.println(customer);
			System.out.println("线程" + Thread.currentThread().getName() + "--->" + entityManager);

			EntityManagerUtil.closeEntityManager();
		}, "manger").start();
		
	}
}
