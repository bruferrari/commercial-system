package com.algaworks.pedidovenda.service;

import java.io.Serializable;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.repository.Enderecos;
import com.algaworks.pedidovenda.util.jpa.Transactional;

public class CadastroEnderecoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Inject
	private Enderecos enderecos;

	@Transactional
	public Endereco salvar(Endereco endereco) {
		return this.enderecos.guardar(endereco);
	}
}
