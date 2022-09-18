package com.phoenixhell.helloworld;



import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import lombok.Data;

//表名最好小写
@Table(name = "jpa_customer")
@Entity
@Data
public class Customer {

	// 数据库名字和属性名一样可以不写@Column注解
	@Column(name = "id")
	/**
	 * GenerationType.AUTO hibernate will look for the default hibernate_sequence table ,
	 *  so change generation to IDENTITY 
	 */
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Id
	private Integer id;
	
	@Column(name = "last_name")
	private String lastName;
	
	
	private String email;
	
	
	private int age;
	
	/**
	 	java.sql.Date	    日期型，精确到年月日，例如“2008-08-08”
		java.sql.Time	    时间型，精确到时分秒，例如“20:00:00”
		java.sql.Timestamp	时间戳，精确到纳秒，例如“2008-08-08 20:00:00.000000001”
	 */
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date createdTime;
	
	@Temporal(TemporalType.DATE)
	private Date birthDate;
	
	
	//单向customer 一 对多  girl
	//CascadeType.REMOVE 删除1 的时候同时删除其关联的多
	@JoinColumn(name = "customer_id")
	@OneToMany(fetch = FetchType.EAGER,cascade = CascadeType.REMOVE)
	private List<Girl> girls;

}
