package com.algaworks.pedidovenda.util.mail;

import java.io.IOException;
import java.io.Serializable;
import java.util.Properties;

import javax.enterprise.context.RequestScoped;

import org.apache.commons.mail.EmailException;
import org.apache.commons.mail.HtmlEmail;

@RequestScoped
public class CommonsMailer implements Serializable {

	private static final long serialVersionUID = 1L;

	public HtmlEmail configuraEmail() throws IOException, EmailException {
		Properties properties = new Properties();
		properties.load(getClass().getResourceAsStream("/mail.properties"));
		
		HtmlEmail mail = new HtmlEmail();
		
		mail.setDebug(true);
		mail.setHostName(properties.getProperty("mail.server.host"));
		mail.setSslSmtpPort(properties.getProperty("mail.server.port"));
		mail.setSSL(Boolean.parseBoolean(properties.getProperty("mail.enable.ssl")));
		mail.setAuthentication(properties.getProperty("mail.username"), properties.getProperty("mail.password"));
		mail.setFrom(properties.getProperty("mail.username"));
		
		return mail;
	}
}
