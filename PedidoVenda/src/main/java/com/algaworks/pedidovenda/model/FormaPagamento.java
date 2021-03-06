package com.algaworks.pedidovenda.model;

public enum FormaPagamento {
	
	DINHEIRO("Dinheiro"),
	CARTAO_CREDITO("Cartão de crédito"),
	CARTAO_DEBITO("Cartão de débito"),
	CHEQUE("Cheque"),
	BOLETO_BANCARIO("Boleto bancário"),
	DEPOSITO_BANCARIO("Despósito bancário"),
	PARCELAMENTO("Parcelamento");
	
	private String descricao;
	
	private FormaPagamento(String descricao) {
		this.descricao = descricao;
	}

	public String getDescricao() {
		return descricao;
	}
}
