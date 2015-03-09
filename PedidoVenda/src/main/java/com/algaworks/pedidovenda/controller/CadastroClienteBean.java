package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.service.CadastroClienteService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Cliente cliente;
	
	@Inject
	private CadastroClienteService cadastroClienteService;
	
	public CadastroClienteBean(){
		cliente = new Cliente();
	}
	
	public void limpar() {
		cliente = new Cliente();
	}
	
	public void salvar(){
		this.cliente = cadastroClienteService.salvar(cliente);
		limpar();
		FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
	}

	public Cliente getCliente() {
		return cliente;
	}

	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}
	
	public boolean isEditando() {
		return this.cliente.getId() != null;
	}

}
