package com.algaworks.pedidovenda.controller;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Map;

import javax.enterprise.context.RequestScoped;
import javax.inject.Inject;
import javax.inject.Named;

import org.primefaces.model.chart.CartesianChartModel;
import org.primefaces.model.chart.ChartSeries;
import org.primefaces.model.chart.PieChartModel;

import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Pedidos;
import com.algaworks.pedidovenda.repository.Usuarios;
import com.algaworks.pedidovenda.security.UsuarioLogado;
import com.algaworks.pedidovenda.security.UsuarioSistema;

@Named
@RequestScoped
public class GraficoPedidosCriadosBean {

	private static DateFormat DATE_FORMAT = new SimpleDateFormat("dd/MM");

	@Inject
	private Pedidos pedidos;

	@Inject
	@UsuarioLogado
	private UsuarioSistema usuarioLogado;

	private CartesianChartModel model;
	private PieChartModel pieChartModel;

	public void preRender() {
		this.model = new CartesianChartModel();
		this.pieChartModel = new PieChartModel();

		adicionarSerie("Todos os pedidos", null);
		adicionarSerie("Meus pedidos", usuarioLogado.getUsuario());
		
		adicionarGraficoPizza();
	}

	private void adicionarSerie(String rotulo, Usuario criadoPor) {
		Map<Date, BigDecimal> valoresPorData = this.pedidos.valoresTotaisPorData(15, criadoPor);
		
		ChartSeries series = new ChartSeries(rotulo);
		
		for (Date data : valoresPorData.keySet()) {
			series.set(DATE_FORMAT.format(data), valoresPorData.get(data));
		}
		
		this.model.addSeries(series);
	}
	
	private void adicionarGraficoPizza() {
		Map<String, BigDecimal> valoresUsuario = this.pedidos.valorUsuario();
		
		for (String string : valoresUsuario.keySet()) {
			this.pieChartModel.set(string, valoresUsuario.get(string));
		}
		
//		this.pieChartModel.set("EX1", new BigDecimal("19000.00"));
//		this.pieChartModel.set("EX2", new BigDecimal("7000.00"));
//		this.pieChartModel.set("EX3", new BigDecimal("26000.00"));
		
		
	}

	public CartesianChartModel getModel() {
		return model;
	}

	public PieChartModel getPieChartModel() {
		return pieChartModel;
	}

}
