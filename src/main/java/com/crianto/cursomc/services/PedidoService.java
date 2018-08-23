package com.crianto.cursomc.services;

import java.util.Arrays;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crianto.cursomc.domain.ItemPedido;
import com.crianto.cursomc.domain.PagamentoComBoleto;
import com.crianto.cursomc.domain.Pedido;
import com.crianto.cursomc.domain.enums.EstadoPagamento;
import com.crianto.cursomc.repositories.ItemPedidoRepository;
import com.crianto.cursomc.repositories.PagamentoRepository;
import com.crianto.cursomc.repositories.PedidoRepository;
import com.crianto.cursomc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {

	/* declarar dependencia com notação Autowired... isso faz com que através da injeção de dependência
	ou inversão de controle, tal dependência é automaticamente instanciada pelo Spring */ 
	@Autowired
	private PedidoRepository repo;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;

	@Autowired
	private ProdutoService produtoService;

	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;

	public Pedido find(Integer id) {
		Optional<Pedido> obj = repo.findById(id);
		return obj.orElseThrow(() -> new ObjectNotFoundException(
				"Objeto não encontrado. Id: " + id + ", Tipo: " + Pedido.class.getName()));		
	}

	@Transactional
	public Pedido insert(Pedido obj) {
		obj.setId(null);
		obj.setInstante(new Date());
		obj.setCliente(clienteService.find(obj.getCliente().getId()));		
		obj.getPagamento().setEstado(EstadoPagamento.PENDENTE);
		obj.getPagamento().setPedido(obj); // aki é a associação de mão duplas, para que o pagamento conheça o pedido dele
		
		if (obj.getPagamento() instanceof PagamentoComBoleto) { // se for do tipo ComBoleto
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante()); 
		}
		
		obj = repo.save(obj);
		pagamentoRepository.saveAll(Arrays.asList(obj.getPagamento()));
		
		for (ItemPedido ip : obj.getItens()) {
			ip.setDesconto(0.0);
			ip.setProduto(produtoService.find(ip.getProduto().getId()));
			ip.setPreco(ip.getProduto().getPreco());
			ip.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		//System.out.println(obj);
		emailService.sendOrderConfirmationEmail(obj);
		
		return obj;
	}
	
}
