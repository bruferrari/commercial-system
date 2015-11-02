package com.algaworks.pedidovenda.controller;

import java.io.Serializable;
import java.math.BigDecimal;
import java.util.List;

import javax.enterprise.event.Observes;
import javax.enterprise.inject.Produces;
import javax.faces.application.FacesMessage;
import javax.faces.bean.ViewScoped;
import javax.faces.context.FacesContext;
import javax.inject.Inject;
import javax.inject.Named;

import org.apache.commons.lang3.StringUtils;
import org.primefaces.component.datatable.DataTable;
import org.primefaces.event.CellEditEvent;

import com.algaworks.pedidovenda.model.Cliente;
import com.algaworks.pedidovenda.model.EnderecoEntrega;
import com.algaworks.pedidovenda.model.FormaPagamento;
import com.algaworks.pedidovenda.model.ItemPedido;
import com.algaworks.pedidovenda.model.Parcela;
import com.algaworks.pedidovenda.model.Pedido;
import com.algaworks.pedidovenda.model.Produto;
import com.algaworks.pedidovenda.model.StatusParcela;
import com.algaworks.pedidovenda.model.Usuario;
import com.algaworks.pedidovenda.repository.Clientes;
import com.algaworks.pedidovenda.repository.Produtos;
import com.algaworks.pedidovenda.repository.Usuarios;
import com.algaworks.pedidovenda.service.CadastroPedidoService;
import com.algaworks.pedidovenda.util.jsf.FacesUtil;
import com.algaworks.pedidovenda.validation.SKU;

@Named
@ViewScoped
public class CadastroPedidoBean implements Serializable {

	private static final long serialVersionUID = 1L;

	@Produces
	@PedidoEdicao
	private Pedido pedido;

	private List<Usuario> vendedores;

	@Inject
	private Usuarios usuarios;

	@Inject
	private Clientes clientes;

	@Inject
	private Produtos produtos;

	@Inject
	private CadastroPedidoService cadastroPedidoService;
	private Produto produtoLinhaEditavel;
	private String sku;
	private List<Integer> listaParcelas;
	private List<Parcela> listaParcelamento;
	private Integer numeroDeParcelas;
	private BigDecimal subtotalParcelado;
	private Parcela parcela;

	public CadastroPedidoBean() {
		limpar();
	}

	public void inicializar() {
		if (FacesUtil.isNotPostback()) {
			this.vendedores = this.usuarios.vendedores();

			this.pedido.adicionarItemVazio();

			this.recalcularPedido();

			this.listarQuantidadeDeParcelas();
			
			if (this.pedido.isParcelado()) {
				listaParcelamento = this.pedido.getParcelas();
				subtotalParcelado = calcularSubtotalParcelamento();
			}
		}
	}

	public void limpar() {
		pedido = new Pedido();
		pedido.setEnderecoEntrega(new EnderecoEntrega());
	}

	// Método que observa pedidoAlteradoEvent e atualiza a instância de
	// Pedido do CadastroPedidoBean, fazendo com que o Pedido de
	// EmissaoPedidoBean
	// e o Pedido de CadastroPedidoBean sejam a mesma instância de objeto
	public void pedidoAlterado(@Observes PedidoAlteradoEvent event) {
		this.pedido = event.getPedido();
	}

	public void listarQuantidadeDeParcelas() {
		listaParcelas = cadastroPedidoService.listaDeParcelas();
	}

	public void simularParcelamento() {
		listaParcelamento = cadastroPedidoService.planoParcelamento(this.pedido.getValorTotal(), 
					numeroDeParcelas, this.pedido);
		subtotalParcelado = calcularSubtotalParcelamento();
		this.pedido.setParcelas(listaParcelamento);
	}

	private BigDecimal calcularSubtotalParcelamento() {
		 return cadastroPedidoService.calcularSubtotalParcelamento(listaParcelamento);
	}

	public StatusParcela[] getStatusParcela() {
		return StatusParcela.values();
	}
	
	public void limparSimulacaoParcelas() {
		listaParcelamento.clear();
		subtotalParcelado = BigDecimal.ZERO;
	}

	public void onCellEdit(CellEditEvent event) {
		Object oldValue = event.getOldValue();
		Object newValue = event.getNewValue();
		
		this.parcela = (Parcela)((DataTable)event.getComponent()).getRowData();
		
		if (newValue != null && !newValue.equals(oldValue)) {
			FacesMessage msg = new FacesMessage(FacesMessage.SEVERITY_INFO, 
					"O status da parcela foi alterado para '" + parcela.getStatus() + "'",
					"Old: " + oldValue + ", New:" + newValue);
			
			FacesContext.getCurrentInstance().addMessage(null, msg);
			
			this.parcela = this.cadastroPedidoService.salvarStatusParcela(parcela);
		}
	}

	public void salvar() {
		this.pedido.removerItemVazio();

		try {
			this.pedido = this.cadastroPedidoService.salvar(pedido);
			FacesUtil.addInfoMessage("Pedido salvo com sucesso!");
		} finally {
			this.pedido.adicionarItemVazio();
		}
	}

	public void recalcularPedido() {
		if (this.pedido != null) {
			this.pedido.recalcularValorTotal();
		}
	}

	public void carregarProdutoPorSku() {
		if (StringUtils.isNotEmpty(this.sku)) {
			this.produtoLinhaEditavel = this.produtos.porSku(this.sku);
			this.carregarProdutoLinhaEditavel();
		}
	}

	public void carregarProdutoLinhaEditavel() {
		ItemPedido item = this.pedido.getItens().get(0);
		if (this.existeItemComProduto(this.produtoLinhaEditavel)) {
			FacesUtil.addErrorMessage("Já existe um item no pedido com o produto informado.");
		} else {
			item.setProduto(this.produtoLinhaEditavel);
			item.setValorUnitario(this.produtoLinhaEditavel.getValorUnitario());

			this.pedido.adicionarItemVazio();
			this.produtoLinhaEditavel = null;
			this.sku = null;

			this.pedido.recalcularValorTotal();
		}
	}

	private boolean existeItemComProduto(Produto produto) {
		boolean existeItem = false;

		for (ItemPedido item : this.getPedido().getItens()) {
			if (produto.equals(item.getProduto())) {
				existeItem = true;
				break;
			}
		}

		return existeItem;
	}

	public List<Produto> completarProduto(String nome) {
		return this.produtos.porNome(nome);
	}

	public void atualizarQuantidade(ItemPedido item, int linha) {
		if (item.getQuantidade() < 1) {
			if (linha == 0) {
				item.setQuantidade(1);
			} else {
				this.getPedido().getItens().remove(linha);
			}
		}

		this.pedido.recalcularValorTotal();
	}

	public Parcela getParcela() {
		return parcela;
	}

	public void setParcela(Parcela parcela) {
		this.parcela = parcela;
	}

	public BigDecimal getSubtotalParcelado() {
		return subtotalParcelado;
	}

	public void setSubtotalParcelado(BigDecimal subtotalParcelado) {
		this.subtotalParcelado = subtotalParcelado;
	}

	public List<Parcela> getListaParcelamento() {
		return listaParcelamento;
	}

	public void setListaParcelamento(List<Parcela> listaParcelamento) {
		this.listaParcelamento = listaParcelamento;
	}

	public Integer getNumeroDeParcelas() {
		return numeroDeParcelas;
	}

	public void setNumeroDeParcelas(Integer numeroDeParcelas) {
		this.numeroDeParcelas = numeroDeParcelas;
	}

	public List<Integer> getListaParcelas() {
		return listaParcelas;
	}

	public void setListaParcelas(List<Integer> listaParcelas) {
		this.listaParcelas = listaParcelas;
	}

	public FormaPagamento[] getFormasPagamento() {
		return FormaPagamento.values();
	}

	public List<Cliente> completarCliente(String nome) {
		return this.clientes.porNome(nome);
	}

	public Pedido getPedido() {
		return pedido;
	}

	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}

	public List<Usuario> getVendedores() {
		return vendedores;
	}

	public boolean isEditando() {
		return this.pedido.getId() != null;
	}

	public Produto getProdutoLinhaEditavel() {
		return produtoLinhaEditavel;
	}

	public void setProdutoLinhaEditavel(Produto produtoLinhaEditavel) {
		this.produtoLinhaEditavel = produtoLinhaEditavel;
	}

	@SKU
	public String getSku() {
		return sku;
	}

	public void setSku(String sku) {
		this.sku = sku;
	}

}