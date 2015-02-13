package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;

import com.algaworks.pedidovenda.model.Grupo;

public class GruposRepository implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	public Grupo porId(Long id) {
		return this.manager.find(Grupo.class, id);
	}
	
	public List<Grupo> grupos() {
		
		return manager.createQuery("from Grupo", Grupo.class).getResultList();
	}
	
	public Grupo porNome (String nome) {
		try {
			return manager.createQuery("from Grupo where upper(nome) = :nome", Grupo.class)
					.setParameter("nome", nome.toUpperCase())
					.getSingleResult();
			
		} catch (NoResultException e) {
			return null;
		}
	}
	

}
