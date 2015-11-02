package com.algaworks.pedidovenda.controller;

import java.io.IOException;
import java.io.Serializable;
import java.util.Locale;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;
import org.apache.commons.mail.SimpleEmail;
import org.apache.velocity.tools.generic.NumberTool;

import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.util.mail.CommonsMailer;
import com.algaworks.pedidovenda.util.mail.Mailer;
import com.outjected.email.api.MailMessage;
import com.outjected.email.impl.templating.velocity.VelocityTemplate;

@Named
@RequestScoped
public class EnvioPedidoEmailBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Inject
	private CommonsMailer commonsMailer;
	
	@Inject
	private Mailer mailer;
	
	@Inject
	@PedidoEdicao
	private Pedido pedido;
	
	public void enviarPedidoCommons() {
		try {
			HtmlEmail mail = commonsMailer.configuraEmail();
			
			mail.addTo(this.pedido.getCliente().getEmail())
				.setSubject("Pedido " + this.pedido.getId())
				.setMsg("<strong>Valor total: </strong>" + this.pedido.getValorTotal())
				.send();
			
			FacesUtil.addInfoMessage("Pedido enviado por e-mail com sucesso!");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (EmailException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public void enviarPedido() {
		MailMessage message = mailer.novaMensagem();
		
		message.to(this.pedido.getCliente().getEmail())
				.subject("Pedido " + this.pedido.getId())
				.bodyHtml(new VelocityTemplate(getClass().getResourceAsStream("/emails/pedido.template")))
				.put("pedido", this.pedido)
				.put("numberTool", new NumberTool())
				.put("locale", new Locale("pt", "BR"))
				.send();
		
		FacesUtil.addInfoMessage("Pedido enviado por e-mail com sucesso!");
		
	}
	
}
