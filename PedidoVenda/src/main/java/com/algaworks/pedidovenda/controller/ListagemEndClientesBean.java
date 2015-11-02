package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;
import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.repository.Enderecos;

@Named
@ViewScoped
public class ListagemEndClientesBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private List<Endereco> enderecosFiltrados;

	@Inject
	private Enderecos enderecos;

	public void pesquisar() {
		enderecosFiltrados = enderecos.listagem();
	}

	public List<Endereco> getEnderecosFiltrados() {
		return enderecosFiltrados;
	}

	public void setEnderecosFiltrados(List<Endereco> enderecosFiltrados) {
		this.enderecosFiltrados = enderecosFiltrados;
	}

}
