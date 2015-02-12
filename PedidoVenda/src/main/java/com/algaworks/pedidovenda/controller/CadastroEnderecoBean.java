package com.algaworks.pedidovenda.controller;

import java.io.Serializable;

import javax.faces.bean.ViewScoped;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Endereco;

@Named
@ViewScoped
public class CadastroEnderecoBean implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	private Endereco endereco;

	public CadastroEnderecoBean() {
		endereco = new Endereco();
	}

	public void salvar() {

	}

	public Endereco getEndereco() {
		return endereco;
	}

}
