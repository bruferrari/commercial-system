package com.algaworks.pedidovenda.controller;

import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.RequestScoped;

@ManagedBean
@RequestScoped
public class PesquisaUsuarioBean {

	private List<Integer> usuariosFiltrados;

	public PesquisaUsuarioBean() {
		usuariosFiltrados = new ArrayList<>();
		for (int i = 0; i < 50; i++) {
			usuariosFiltrados.add(i);
		}

	}

	public List<Integer> getusuariosFiltrados() {
		return usuariosFiltrados;
	}

}
