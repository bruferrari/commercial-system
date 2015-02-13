package com.algaworks.pedidovenda.repository.filter;


import java.io.Serializable;


import com.algaworks.pedidovenda.validation.SKU;


public class UsuarioFilter implements Serializable {


	private static final long serialVersionUID = 1L;


	private String nome;


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}


}
