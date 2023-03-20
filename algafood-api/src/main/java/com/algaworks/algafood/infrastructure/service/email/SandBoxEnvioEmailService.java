package com.algaworks.algafood.infrastructure.service.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.services.EnvioEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SandBoxEnvioEmailService implements EnvioEmailService{

	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private ProcessadorEmailTemplate processadorEmailTemplate;
	
	@Override
	public void enviar(Mensagem mensagem) {
		try {
	        MimeMessage mimeMessage = criarMimeMessage(mensagem);
	        
	        log.info("[SANDBOX] De: {}, Para: {}", emailProperties.getRemetente(), emailProperties.getSandbox().getDestinatario());
	        mailSender.send(mimeMessage);
	    } catch (Exception e) {
	        throw new EmailException("Não foi possível enviar e-mail", e);
	    }
	}
	
	private MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
	    String corpo = processadorEmailTemplate.processarTemplate(mensagem);
	    
	    
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
	    helper.setFrom(emailProperties.getRemetente());
	    helper.setTo(emailProperties.getSandbox().getDestinatario());
	    helper.setSubject(mensagem.getAssunto());
	    helper.setText(corpo, true);
	    
	    return mimeMessage;
	}
	
}
