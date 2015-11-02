package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.repository.Enderecos;
import com.algaworks.pedidovenda.service.CadastroEnderecoService;

@Named
@ViewScoped
public class CadastroEnderecoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Endereco endereco;

	@Inject
	private CadastroEnderecoService cadastroEnderecoService;

	@Inject
	private Cliente cliente;

	@Inject
	private Enderecos enderecos;

	public CadastroEnderecoBean() {
		endereco = new Endereco();
	}

	public void salvar() {
		endereco.setCliente(cliente);
		cadastroEnderecoService.salvar(endereco);
	}

	public void excluir() {
		this.enderecos.remover(endereco);
	}

	public Endereco getEndereco() {
		return endereco;
	}

	public void setEndereco(Endereco endereco) {
		this.endereco = endereco;
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}

}
