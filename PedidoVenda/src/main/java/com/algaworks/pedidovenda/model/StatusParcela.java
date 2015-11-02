package com.algaworks.pedidovenda.model;

public enum StatusParcela {

		PAGO("Pago"), PENDENTE("Pendente");

		private String descricao;

		private StatusParcela(String descricao) {
			this.descricao = descricao;
		}

		public String getDescricao() {
			return descricao;
		}

		public void setDescricao(String descricao) {
			this.descricao = descricao;
		}
}
