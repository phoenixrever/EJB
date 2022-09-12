package com.phoenixhell.service;

import java.util.List;

import javax.ejb.Remote;
import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import com.phoenixhell.person.Person;


@Stateless
@Remote(PersonService.class)
public class PersonServiceBean implements PersonService {
	//注入EntityManager 对象 name属性是persistence-unit name 
	@PersistenceContext(name = "MySqlDS")
	private EntityManager entityManager;
	
	
	//第二个参数就是标注了@Id 的属性值
	public Person getPerson(Integer id) {
		
		//没有找到会返回null
		Person  person = (Person) entityManager.find(PersonService.class, id);

		return person;
	}



	public List<Person> getPersons() {
		//这是一条面向对象的语句 不是sql语句  
		//Person 是@Entity注解的name 属性指定的值 如果没有指定 默认是类名
		//Person 是大小写区分的
		
		//忽略返回类型警告
		@SuppressWarnings("unchecked")
		List<Person> persons =  entityManager.createQuery("select o from Person o").getResultList();;
		return persons;
	}
	
	
	/**
	 * 实体bean 四种状态
	 * 	1 新建
	 *  2 托管
	 *  3 游离
	 *  4 删除
	 */
	public void save(Person person) {
		//保存新建状态bean
		entityManager.persist(person);
	}

	public void update(Person person) {
		//person 必须处于游离状态 才需要调用merge方法
		//如果处于托管状态 直接调用对象的set方法就可以进行修改 无需调用merge方法
		entityManager.merge(person);

	}

	//实体bean 必须处于托管状态才能被删除
	public void delete(Integer id) {
		//1 可以采用getPerson 找到实体bean在删除 这里采用其他方法
		
		//简单来说就是懒加载 返回一个代理对象 比上面的性能好点 
		//删除不需要经过上面getPerson 还要经过数据库进行数据装载操作
		entityManager.remove(entityManager.getReference(Person.class, id));

	}
}
