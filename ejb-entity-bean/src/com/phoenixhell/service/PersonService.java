package com.phoenixhell.service;

import java.util.List;

import com.phoenixhell.person.Person;



public interface PersonService {
	public void save(Person person);
	public void update(Person person);
	public void delete(Integer id);
	public Person getPerson(Integer id);
	public List<Person> getPersons();
}
