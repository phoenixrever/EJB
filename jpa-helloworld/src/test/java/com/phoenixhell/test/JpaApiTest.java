package com.phoenixhell.test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.FetchType;
import javax.persistence.Persistence;
import javax.transaction.Transaction;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.phoenixhell.helloworld.Customer;
import com.phoenixhell.helloworld.Girl;
import com.phoenixhell.helloworld.Order;

public class JpaApiTest {
	public static final String persistenceUnitName = "jpa-helloworld";

	private EntityManagerFactory entityManagerFactory;
	private EntityManager entityManager;
	private EntityTransaction entityTransaction;

	@Before
	public void init() {
		entityManagerFactory = Persistence.createEntityManagerFactory(persistenceUnitName);
		entityManager = entityManagerFactory.createEntityManager();
		entityTransaction = entityManager.getTransaction();
		entityTransaction.begin();
	}

	@After
	public void close() {
		entityTransaction.commit();
		entityManager.close();
		entityManagerFactory.close();
	}

	// search by id
	@Test
	public void find() {
		Customer customer = entityManager.find(Customer.class, 27);
		System.out.println("--------find-----------");
		System.out.println(customer);
	}
	
	// 同find 方法 但是注意 System.out先输出了 说明只有用到结果了（这边是输出customer）
	// 就是getReference 先创建一个查找代理 直到真正使用这个对象来才从数据库中查找
	// 才会从数据库中查找 相当于懒加载吧  只要是懒加载就会有一个问题
	/**
	 * 在创建好代理后不立即使用 而是 把entityTransaction 和 entityManager 给关闭了
	 * 异常 ： could not initialize proxy  - no Session
	 */
	@Test
	public void getReference() {
		Customer customer = entityManager.getReference(Customer.class, 1);
		//使用前关闭连接 no Session 异常
//		entityTransaction.commit();
//		entityManager.close();
		System.out.println("--------getReference-----------");
		System.out.println(customer);
	}
	
	
	// save 使对象由临时状态变为持久化状态
	// 注意 不能设置id 否则会抛出异常
	// 事务问题 为什么这边不能自动回滚事务 需要手动回滚
	@Test
	public void persist() {
		try {
			System.out.println("----save----");
			Customer customer= new Customer();
			customer.setAge(100);
			customer.setBirthDate(new Date());
			customer.setCreatedTime(new Date());
			customer.setEmail("google@gmail.com");
			customer.setLastName("test trasaction2");
			
			entityManager.persist(customer);
			
			
			//注意 如果我们在保存下面修改customer的话是会多出update的
			Customer customer1= new Customer();
			customer1.setAge(99);
			customer1.setBirthDate(new Date());
			customer1.setCreatedTime(new Date());
			customer1.setEmail("google@gmail.com");
			customer1.setLastName("test trasaction2");
			
			int n=1/0;
			
			entityManager.persist(customer1);
		} catch (Exception e) {
			entityTransaction.rollback();
			e.printStackTrace();
		}
	}
	
	// delete 只能移除持久化对象
	@Test
	public void remove() {
		//下面的操作是不能移除的 属于游离对象 不知道游离对象是什么有空再去研究hibernate 暂时先记着
//		Customer customer= new Customer();
//		customer.setId(3);
//		entityManager.remove(customer);
		
		Customer customer2 = entityManager.find(Customer.class, 1);
		
		entityManager.remove(customer2);
	}
	
	
	//saveOrUpdate 
	/**
	 * 从数据库中查出的对象 叫做持久化对象  实际是个代理对象里面封装了数据库相关操作
	 * 自己new的对象就是纯粹的对象 没有和数据库关联 没有主键ID
	 */
	@Test
	public void merge1() {
		//传入   自己new的对象
		Customer customer= new Customer();
		customer.setAge(100);
		customer.setBirthDate(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("google@gmail.com");
		customer.setLastName("sliver");
		
		//返回插入的对象 带有ID 值
		Customer customer1 = entityManager.merge(customer);
		System.out.println("--------游离对象-----------");
		System.out.println(customer);
		System.out.println(customer1);
	}
	
	//saveOrUpdate 
	/**
	 *  游离对象: 自己new 的对象 但是有ID
	 *  1 若在entityManager缓存中没有该对象
	 *  2 若在数据库中有对应的记录
	 *  3 JPA 会查询该对象 先select 返回这个对象 再把游离属性的对象赋值到查出来的对象 
	 *  4 对查询到对象在进行update 操作
	 */
	@Test
	public void merge2() {
		//传入 持久化对象 
		Customer customer= new Customer();
		customer.setAge(55100);
		customer.setBirthDate(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("google@gmail.com");
		customer.setLastName("update");
		//1 没有id 55 会创建一个新对象 id自增 并不会用我们传入的值
		//customer.setId(55);
		
		
		//2 有的话会更新这条数据
		customer.setId(3);
		
		//返回插入的对象 带有ID 值 
		Customer customer1 = entityManager.merge(customer);
		System.out.println(customer);
		System.out.println(customer1);
	}
	
	//saveOrUpdate 
	/**
	 *  游离对象: 自己new 的对象 但是有ID
	 *  1 若在entityManager有该对象
	 *  3 JPA 把游离属性的对象赋值到entityManager缓存的对象 
	 *  4 对entityManager缓存的对象进行update 操作
	 */
	@Test
	public void merge3() {
		//传入 游离对象
		Customer customer= new Customer();
		customer.setAge(55100);
		customer.setBirthDate(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("google@gmail.com");
		customer.setLastName("update");
		customer.setId(4);
		
		//查找对象放入缓存中 此时就有了对该对象的数据库方法 
		Customer customer2 = entityManager.find(Customer.class, 1);


		//更新对象 发现已经缓存已经有了操作此id的数据库连接 直接复制游离对象更新值
		Customer customer1 = entityManager.merge(customer);
		System.out.println(customer);
		System.out.println(customer1);
	}
	
	//flush
	/**
	 * 默认情况下我们修改了一个代理对象的值在commit的时候才会update数据库  
	 * flush 可以立即执行update操作
	 * 
	 * 但是千万注意 这是一个事务 commit、之前 数据库中的记录是不会变的
	 */
	@Test
	public void flush() {
		Customer customer = entityManager.find(Customer.class,2);
		System.out.println("--------find-----------");
		System.out.println(customer);
		
		customer.setLastName("shadow silent");
		
	}
	
	
	//refresh
	@Test
	public void refresh() {
		//由于缓存机制只会执行一次sql
		Customer customer1 = entityManager.find(Customer.class,2);
		Customer customer2 = entityManager.find(Customer.class,2);

		//refresh customer2重新获取数据库中的数据 不会走缓存
		entityManager.refresh(customer2);

		System.out.println("--------find-----------");
		System.out.println(customer1);
		System.out.println(customer2);
	}
	
	//orm 映射 多orders 对应 一个 customer
	//test只要运行起来 所有xml配置文件下的Entity 都会运行建表
	@Test
	public void orm() {

	}
	
	
	//=========================多对一映射=============================
	
	//测试 多对一 insert
	@Test
	public void manyToOneInsert() {
		Customer customer= new Customer();
		customer.setAge(11);
		customer.setBirthDate(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("google@gmail.com");
		customer.setLastName("order");
		
		Order order1 = new Order(null,"order1",customer);
		Order order2 = new Order(null,"order2",customer);
		
		//这边保存的顺序不影响
		//在customer 中保存 然后再在order中保存 不然order没有customer外键
		//先保存order的情况下 外键customer id 会保存成null customer保存完毕后更新order外键
		//显示增加了sql执行步骤不建议这样做 要按照执行顺序
		entityManager.persist(customer);
		entityManager.persist(order1);
		entityManager.persist(order2);
		     
		System.out.println(order1);
		System.out.println(order2);
	}
	
	//多对1 关联查询
	@Test
	public void manyToOneFind() {
		//关联查询 left outer join
		/**
		 * Order(id=3, orderName=order1, 
		 * customer=Customer(id=8, lastName=order, 
		 * email=google@gmail.com, age=11, createdTime=2022-09-17 17:08:14.27, 
		 * birthDate=2022-09-17))

		 */
		Order order3 = entityManager.find(Order.class, 3);
		
		//fetch = FetchType.LAZY 懒加载情况下不会join
		//会分段查询 先查询order 在查询customer
		System.out.println(order3);
		Customer custome3 =order3.getCustomer();
		System.out.println(custome3);
	}
	
	//多对1 删除
	@Test
	public void manyToOneRemove() {
		//order可以直接删除 customer的id 有外键在使用 不能直接删除
		Customer customer =  new Customer();
		customer.setId(8);
		entityManager.remove(customer);
	}
	
	//多对1 修改 
	//时刻记住你获取的不是一个实体类 是和一个数据库代理对象 对数据的操作就是对数据库的操作
	//包括关联对象 也会修改关联表
	
	@Test
	public void manyToOneUpdate() {
		//order可以直接删除 customer的id 有外键在使用 不能直接删除
		Order order12 = entityManager.find(Order.class, 9);
		order12.getCustomer().setLastName("updateByOrder111");
		System.out.println(order12);
	}
	
	//=========================一对多映射=============================

	//插入
	@Test
	public void oneToManyPersis() {
		Customer customer= new Customer();
		customer.setAge(11);
		customer.setBirthDate(new Date());
		customer.setCreatedTime(new Date());
		customer.setEmail("google1@gmail.com");
		customer.setLastName("girls");
		
		
		Girl girl1 = new Girl(null,"cindy");
		Girl girl2 = new Girl(null,"merry");
		
		ArrayList<Girl> girls =   new ArrayList<Girl>();
		 
		girls.add(girl1);
		girls.add(girl2);

		customer.setGirls(girls);
		
		//先保存customer 才有customer id  一对多关联时候 顺序关系不大
		//因为多的一段在插入时候不会插入外键 最好都会来update 外键
		entityManager.persist(customer);
		entityManager.persist(girl1);
		entityManager.persist(girl2);

		
		System.out.println(customer);
	
	}

	
	//查找
	@Test
	public void oneToManyFind() {

		//默认对关联多的一方使用懒加载策略 可以改变默认的加载策略 改成eager
		Customer customer= entityManager.find(Customer.class, 23);
		
		System.out.println(customer);
	
	}
	
	//删除
	@Test
	public void oneToManyDelete() {
		//可以先删除 1 的一端  先把多的一端 外键置空 在删除 一的 一端
		//如果同时想删除多的一段 
		Customer customer = entityManager.find(Customer.class, 22);
		entityManager.remove(customer);
		
		System.out.println(customer);
	
	}
	
	//更新
	@Test
	public void oneToManyUpdate() {
		Customer customer = entityManager.find(Customer.class, 27);
		
		customer.getGirls().iterator().next().setName("updatedName");
	}
	
	
	
	//===========================二级缓存====================================
	
	//二级缓存的目的就是可以在不同的entityManager 公用缓存 和mybatis 二级缓存一样
	@Test
	public void secondLevelCache() {
		Customer customer1 = entityManager.find(Customer.class, 27);
		//默认JPA 一级缓存  只会执行一次sql
		Customer customer2 = entityManager.find(Customer.class, 27);
		
		
	}
	
	
}
