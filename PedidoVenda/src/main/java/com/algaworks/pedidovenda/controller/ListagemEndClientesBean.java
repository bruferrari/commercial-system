package com.algaworks.pedidovenda.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class ListagemEndClientesBean {

	private List<Integer> enderecosFiltrados;

	public ListagemEndClientesBean() {
		enderecosFiltrados = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			enderecosFiltrados.add(i);
		}

	}

	public List<Integer> getenderecosFiltrados() {
		return enderecosFiltrados;
	}

}
