package com.algaworks.pedidovenda.service;

import java.io.Serializable;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import com.algaworks.pedidovenda.model.Parcela;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.StatusPedido;
import com.algaworks.pedidovenda.repository.Pedidos;
import com.algaworks.pedidovenda.util.jpa.Transactional;


public class CadastroPedidoService implements Serializable {

	private static final long serialVersionUID = 1L;
	
	private static final Double TAX = 1.867976;
	private static final int MAX_PAGAMENTOS = 18;
	
	@Inject
	private Pedidos pedidos;
	
	@Transactional
	public Pedido salvar(Pedido pedido) {
		
		if(pedido.isNovo()) {
			pedido.setDataCriacao(new Date());
			pedido.setStatus(StatusPedido.ORCAMENTO);
		}
		
		pedido.recalcularValorTotal();
		
		if (pedido.isNaoAlteravel()) {
			throw new NegocioException("Pedido não pode ser alterado no status " 
						+ pedido.getStatus().getDescricao() + ".");
		}
		
		if (pedido.getItens().isEmpty()) {
			throw new NegocioException("O pedido deve possuir pelo menos um item.");
		}
		
		if (pedido.isValorTotalNegativo()) {
			throw new NegocioException("Valor total do pedido não pode ser negativo.");
		}
		
		pedido = this.pedidos.guardar(pedido);
		return pedido;
	}
	
	@Transactional
	public Parcela salvarStatusParcela(Parcela parcela) {
		parcela = this.pedidos.guardarParcela(parcela);
		return parcela;
	}
	
	public List<Parcela> planoParcelamento(BigDecimal valorTotal, Integer parcelas, Pedido pedido) {
		List<Parcela> parcelamento = new ArrayList<>();
		double valorParcela = 0.0;
		double taxaEfetiva = TAX / 100;

		if (parcelas <= 10) {
			taxaEfetiva = taxaEfetiva - (0.200000 / 100);
		} else if (parcelas <= 24) {
			taxaEfetiva = taxaEfetiva - (0.100000 / 100);
		}

		valorParcela = valorTotal.doubleValue() * ((taxaEfetiva * Math.pow(1 + taxaEfetiva, parcelas))
				/ (Math.pow(1 + taxaEfetiva, parcelas) - 1));

		for (int i = 1; i <= parcelas; i++) {
			Parcela parcela = new Parcela();
			parcela.setParcela(i);
			parcela.setValor(new BigDecimal(valorParcela).setScale(2, RoundingMode.CEILING));
			parcela.setVencimento(addOneMonth(new Date(), i));
			parcela.setPedido(pedido);
			parcelamento.add(parcela);
		}
		return parcelamento;
		
	}
	
	public BigDecimal calcularSubtotalParcelamento(List<Parcela> parcelamento) {
		double subtotal = 0;
		
		for (Parcela parcela : parcelamento) {
			subtotal += parcela.getValor().doubleValue();
		}
		
		return new BigDecimal(subtotal);
	}
	
	private Date addOneMonth(Date date, int i) {
		Calendar calendar = Calendar.getInstance();
		calendar.setTime(date);
		calendar.add(Calendar.MONTH, i);
		date = calendar.getTime();
		return date;
	}
	
	public List<Integer> listaDeParcelas() {
		List<Integer> parcelas = new ArrayList<>();

		// Max of 60 payments
		for (int i = 0; i < MAX_PAGAMENTOS; i++) {
			parcelas.add(1 + i);
		}
		return parcelas;
	}

}
