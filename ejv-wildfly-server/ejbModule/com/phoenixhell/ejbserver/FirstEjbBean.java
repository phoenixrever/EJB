package com.phoenixhell.ejbserver;

import javax.ejb.LocalBean;
import javax.ejb.Remote;
import javax.ejb.Stateless;

/**
 * Session Bean implementation class FirstEJB
 */
@Stateless
@Remote
public class FirstEjbBean implements FirstEjb {

	@Override
	public String sayHello() {
	
		return "hell world";
	}

}
