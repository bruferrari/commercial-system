package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.util.List;
import java.util.Locale;

import javax.faces.bean.ViewScoped;
import javax.inject.Inject;
import javax.inject.Named;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.Endereco;
import com.algaworks.pedidovenda.service.CadastroClienteService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.mail.Mailer;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@ViewScoped
public class CadastroClienteBean implements Serializable {

	private static final long serialVersionUID = 1L;

	private Cliente cliente;
	private Endereco novoEndereco;

	@Inject
	private CadastroClienteService cadastroClienteService;
	
	@Inject
	private Mailer mailer;

	public CadastroClienteBean() {
		cliente = new Cliente();
		novoEndereco = new Endereco();
	}

	public void limpar() {
		cliente = new Cliente();
	}

	public void salvar() {
		this.cliente = cadastroClienteService.salvar(cliente);
		limpar();
		FacesUtil.addInfoMessage("Cliente salvo com sucesso!");
	}

	public void adicionarNovoEndereco() {
		if (novoEndereco != null) {
			this.cliente.getEnderecos().add(novoEndereco);
			novoEndereco.setCliente(this.cliente);
			novoEndereco = new Endereco();
		} else {
			throw new NullPointerException();
		}
	}

	public void removerEndereco() {
		if (novoEndereco != null) {
			this.cliente.getEnderecos().remove(novoEndereco);
		}
	}
	
	public void editarEndereco() {
		this.cliente.getEnderecos()
				.set(this.cliente.getEnderecos().indexOf(novoEndereco), novoEndereco);
	}
	
	public void enviarEmail() {
		MailMessage message = mailer.novaMensagem();
		
		message.to(this.cliente.getEmail())
				.subject("Cadastro Sistema - " + this.cliente.getNome())
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/cliente.template")))
				.put("cliente", cliente)
				.put("locale", new Locale("pt", "BR"))
				.send();
		
		FacesUtil.addInfoMessage("Cadastro enviado por e-mail com sucesso!");
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

	public Endereco getEndereco() {
		return novoEndereco;
	}

	public void setEndereco(Endereco endereco) {
		this.novoEndereco = endereco;
	}

}
