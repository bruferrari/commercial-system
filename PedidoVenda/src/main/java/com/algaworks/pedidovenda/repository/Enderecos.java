package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.Order;

import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Enderecos implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private EntityManager manager;
	
	public Endereco guardar(Endereco endereco) {
		return manager.merge(endereco);
	}
	
	@Transactional
	public void remover(Endereco endereco) {
		try {
			endereco = this.porId(endereco.getId());
			manager.remove(endereco);
			manager.flush();
		} catch (PersistenceException e) {
			throw new NegocioException("Não foi possível remover o endereço");
		}
	}

	public Endereco porId(Long id) {
		try {
			return manager.createQuery("from Endereco where id = :id", Endereco.class).setParameter("id", id)
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}

	@SuppressWarnings("unchecked")
	public List<Endereco> listagem() {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Endereco.class);
		return criteria.addOrder(Order.asc("logradouro")).list();
	}
}
