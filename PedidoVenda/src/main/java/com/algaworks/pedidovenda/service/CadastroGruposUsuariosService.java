package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroGruposUsuariosService implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Grupos grupos;
	
	@Transactional
	public Grupo salvar(Grupo grupo) {
		return grupos.guardar(grupo);
	}
}
