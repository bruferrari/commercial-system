package com.algaworks.pedidovenda.repository;

import java.io.Serializable;
import java.util.List;

import javax.inject.Inject;
import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import javax.persistence.PersistenceException;

import org.apache.commons.lang3.StringUtils;
import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Restrictions;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.repository.filter.ClienteFilter;
import com.algaworks.pedidovenda.service.NegocioException;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class Clientes implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private EntityManager manager;
	
	// Método genérico para salvar ou editar um cliente
	public Cliente guardar(Cliente cliente){
		return manager.merge(cliente);
	}
	
	// Método para remoção de um cliente
	@Transactional
	public void remover(Cliente cliente) {
		try {
			
			cliente = porId(cliente.getId());
			manager.remove(cliente);
			manager.flush();
		
		} catch (PersistenceException e) {
			throw new NegocioException("");
		}
	}
	
	public Cliente porDocumentoReceitaFederal(String documentoReceitaFederal) {
		try {
			return manager.createQuery("from Cliente where documentoReceitaFederal = : doc", Cliente.class)
					.setParameter("doc", documentoReceitaFederal)
					.getSingleResult();
		} catch(Exception e) {
			return null;
		}
	}
	
	public Cliente porNome(String nome) {
		try {
			return manager.createQuery("from Cliente where upper(nome) = :nome", Cliente.class)
					.setParameter("nome", nome.toUpperCase())
					.getSingleResult();
		} catch (NoResultException e) {
			return null;
		}
	}
	
	@SuppressWarnings("unchecked")
	public List<Cliente> filtrados(ClienteFilter filtro) {
		Session session = manager.unwrap(Session.class);
		Criteria criteria = session.createCriteria(Cliente.class);
		
		if (StringUtils.isNotBlank(filtro.getNome())) {
			criteria.add(Restrictions.ilike("nome", filtro.getNome(), MatchMode.ANYWHERE));
		}
		return criteria.addOrder(Order.asc("nome")).list();
	}

	public Cliente porId(Long id) {
		return manager.find(Cliente.class, id);
	}
}
