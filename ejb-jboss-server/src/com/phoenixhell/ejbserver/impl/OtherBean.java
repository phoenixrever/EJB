package com.phoenixhell.ejbserver.impl;

import javax.ejb.Local;
import javax.ejb.Stateless;

import com.phoenixhell.ejbserver.Other;

/**
 * 功能  要在FirstBean 里面调用本类的sayOther方法
 */


//默认就是本地 local bean name 供注入的时候选择名字
@Stateless(name = "OtherBean")
public class OtherBean implements Other {

	public String sayOther(String ohter) {
		return "say "+ohter;
	}

}
