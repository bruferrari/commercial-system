package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Grupos implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	private Usuario usuario;
	
	public Grupo porId(Long id) {
		return this.manager.find(Grupo.class, id);
	}
	
	public Grupo guardar(Grupo grupo) {
		return manager.merge(grupo);
	}
	
	public List<Grupo> grupos() {
		
		return manager.createQuery("from Grupo", Grupo.class).getResultList();
	}
	
	@Transactional
	public void remover(Grupo grupo) {
		try {
//			grupo = porId(grupo.getId());
//			this.manager.remove(grupo);
//			this.manager.flush();
			this.usuario.getGrupos().remove(grupo);
		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível remover o grupo.");
		}
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
