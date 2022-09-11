package com.phoenixhell.ejb;


@Stateless
@Remote
public class FirstEjbBean implements FirstEjb {

	@Override
	public String sayHello(String name) {
		// TODO Auto-generated method stub
		return "hello "+name;
	}

}
