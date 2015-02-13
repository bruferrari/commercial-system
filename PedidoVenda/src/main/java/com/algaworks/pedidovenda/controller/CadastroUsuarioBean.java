package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.GruposRepository;
import com.algaworks.pedidovenda.service.CadastroUsuarioService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;

@Named
@ViewScoped
public class CadastroUsuarioBean implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private Usuario usuario;
	
	private Grupo grupoSelecionado;
	private List<Grupo> grupo = new ArrayList<Grupo>();
	
	@Inject
	private CadastroUsuarioService cadastroUsuarioService;
	
	@Inject
	private GruposRepository gruposRepository;
	
	public CadastroUsuarioBean() {
		limpar();
	}
	
	public void inicializar() {
		System.out.println("Inicializando...");
		grupo = gruposRepository.grupos();
		grupoSelecionado = new Grupo();
	}
	
	public void limpar() {
		usuario = new Usuario();
		grupo = new ArrayList<Grupo>();
	}

	public void salvar() {
		
		adicionarGrupo();
		this.usuario = cadastroUsuarioService.salvar(this.usuario);
		limpar();
		FacesUtil.addInfoMessage("Usuário cadastrado com sucesso!");
	}
	
	public void adicionarGrupo(){
		this.usuario.getGrupos().add(grupoSelecionado);
	}

	public Usuario getUsuario() {
		return usuario;
	}

	public List<Grupo> getGrupo() {
		return grupo;
	}

	public Grupo getGrupoSelecionado() {
		return grupoSelecionado;
	}

	public void setGrupoSelecionado(Grupo grupoSelecionado) {
		this.grupoSelecionado = grupoSelecionado;
	}

}
