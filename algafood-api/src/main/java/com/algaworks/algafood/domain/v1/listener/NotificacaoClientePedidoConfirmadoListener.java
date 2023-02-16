package com.algaworks.algafood.domain.v1.listener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.event.TransactionPhase;
import org.springframework.transaction.event.TransactionalEventListener;

import com.algaworks.algafood.domain.v1.event.PedidoConfirmadoEvent;
import com.algaworks.algafood.domain.v1.model.Pedido;
import com.algaworks.algafood.domain.v1.services.EnvioEmailService;
import com.algaworks.algafood.domain.v1.services.EnvioEmailService.Mensagem;

@Component
public class NotificacaoClientePedidoConfirmadoListener {

	@Autowired
	private EnvioEmailService envioEmailService;
	
	// AFTER_COMMIT (Padrão da @) Só vai disparar o e-mail depois que a transação for commitada;
	@TransactionalEventListener(phase = TransactionPhase.AFTER_COMMIT)
	public void aoConfirmarPedido(PedidoConfirmadoEvent event) {
		//if (true) throw new IllegalArgumentException();
		
		Pedido pedido = event.getPedido();
		
		var mensagem = Mensagem.builder()
				.assunto(pedido.getRestaurante().getNome() + " - Pedido confirmado")
				.corpo("pedido-confirmado.html")
				.variavel("pedido", pedido)
				.destinatario(pedido.getCliente().getEmail())
				.build();
		
		envioEmailService.enviar(mensagem);
	}
}
