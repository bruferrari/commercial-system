package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.enterprise.context.SessionScoped;
import javax.inject.Inject;
import javax.inject.Named;
import javax.validation.constraints.NotNull;

import com.algaworks.pedidovenda.model.Categoria;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.repository.Categorias;
import com.algaworks.pedidovenda.service.CadastroProdutoService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@SessionScoped
public class CadastroProdutoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private Categorias categorias;
	
	@Inject
	private CadastroProdutoService cadastroProdutoService;

	private Produto produto;
	private Categoria categoriaPai;

	private List<Categoria> categoriasRaizes;
	private List<Categoria> subcategorias;

	public CadastroProdutoBean() {
		limpar();
	}

	public void inicializar() {
		System.out.println("Inicializando...");

		if (FacesUtil.isNotPostback()) {
			categoriasRaizes = categorias.raizes();
		}
	}

	public void carregarSubcategorias() {

		subcategorias = categorias.subcaCategoriasDe(categoriaPai);

	}

	private void limpar(){
		produto = new Produto();
		categoriaPai = null;
		subcategorias = new ArrayList<Categoria>();
	}
	
	public void salvar() {
		this.produto = cadastroProdutoService.salvar(this.produto);
		limpar();
		FacesUtil.addInfoMessage("Produto salvo com sucesso!");
	}

	public Produto getProduto() {
		return produto;
	}

	public List<Categoria> getCategoriasRaizes() {
		return categoriasRaizes;
	}

	@NotNull
	public Categoria getCategoriaPai() {
		return categoriaPai;
	}

	public void setCategoriaPai(Categoria categoriaPai) {
		this.categoriaPai = categoriaPai;
	}

	public List<Categoria> getSubcategorias() {
		return subcategorias;
	}

}