package com.test.bean;

import java.util.List;

import javax.ejb.Stateless;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.persistence.TypedQuery;

@Stateless
public class Ejb {
	@PersistenceContext
	private EntityManager em;
	
	public<T> void setData(T obj){
		em.persist(obj);
	}
		
	public<T> void updateData(T obj){
		em.merge(obj);
	}
	
	public<T> T getDataById(int id, Class<T> t){
		return (T) em.find(t, id);
	}
	
	public<T> List<T> getAllData(Class<T> t){
		TypedQuery<T> q=em.createQuery("select c from "+t.getName()+" c",t);
		return q.getResultList();
	}
}
