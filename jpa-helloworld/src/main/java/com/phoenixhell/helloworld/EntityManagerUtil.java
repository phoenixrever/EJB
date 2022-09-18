package com.phoenixhell.helloworld;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.Query;

/**
 * 自己通过EntityManagerFactory创建出来的EntityManager需要手动关闭，否则将会一直占用连接，久而久之导致连接占满，应用服务器假死

 *
 */
public class EntityManagerUtil {
	
	public static final String persistenceUnitName = "jpa-helloworld";
	
	// EntityManagerFactory 是线程安全的
	private static final EntityManagerFactory emf;
//	private static final EntityManager em= Persistence.createEntityManagerFactory(persistenceUnitName).createEntityManager();
	
	
	// 使用ThreadLocal来保证EntityManager的线程安全 每一个线程都创建一个EntityManager
	private static final ThreadLocal<EntityManager> threadLocal;
	/** 初始化 */
	static {
		// 初始化EntityManagerFactory对象
		emf = Persistence.createEntityManagerFactory(persistenceUnitName);
//		System.out.println(emf);
		threadLocal = new ThreadLocal<EntityManager>();
	}

	// 通过ThreadLocal获取EntityManager对象
	public static EntityManager getEntityManager() {
		// 获取当前线程关联的EntityManager对象
		EntityManager em = threadLocal.get();
		// 如果当前线程关联的EntityManager为null，或没有打开
		if (em == null || !em.isOpen()) {
			// 创建新的EntityManager
			em = emf.createEntityManager();
			threadLocal.set(em);
		}
		return em;
	}

	// 关闭EntityManager对象
	public static void closeEntityManager() {
		EntityManager em = threadLocal.get();
		threadLocal.set(null);
		if (em != null) {
			em.close();
		}
//		em.close();
	}

	// 开始事务
	public static void beginTransaction() {
		getEntityManager().getTransaction().begin();
	}

	// 提交事务
	public static void commit() {
		getEntityManager().getTransaction().commit();
	}

	// 创建查询
	public static Query createQuery(String jpql) {
		return getEntityManager().createQuery(jpql);
	}
}
