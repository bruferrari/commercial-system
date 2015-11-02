package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Grupo;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Grupos;
import com.algaworks.pedidovenda.service.CadastroUsuarioService;
import com.algaworks.pedidovenda.service.NegocioException;
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
	private Grupos grupos;

	public CadastroUsuarioBean() {
		limpar();
	}

	public void inicializar() {
		grupo = grupos.grupos();
		
	}

	public void limpar() {
		usuario = new Usuario();
		grupo = new ArrayList<Grupo>();
	}

	public void salvar() {

		adicionarGrupo();
		guardarUsuario();
		limpar();
		FacesUtil.addInfoMessage("Usuário cadastrado com sucesso!");
	}
	
	public void removerGrupoUsuario() {
			this.usuario.getGrupos().remove(this.grupoSelecionado);
			guardarUsuario();
	}

	public void guardarUsuario() {
		this.usuario = cadastroUsuarioService.salvar(this.usuario);
	}
	
	public boolean isEditando() {
		return this.usuario.getId() != null;
	}

	public void adicionarGrupo() {
		if(!this.usuario.getGrupos().contains(grupoSelecionado)) {
			this.usuario.getGrupos().add(grupoSelecionado);
			guardarUsuario();
		} else {
			throw new NegocioException("Esse usuário já faz parte desse grupo.");
		}
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

	public void setUsuario(Usuario usuario) {
		this.usuario = usuario;
	}

}
