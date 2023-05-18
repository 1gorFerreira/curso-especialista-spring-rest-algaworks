package com.algaworks.algafood.infrastructure.service.email;

import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.services.EnvioEmailService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SmtpEnvioEmailService implements EnvioEmailService {

	@Autowired
	private JavaMailSender mailSender;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private ProcessadorEmailTemplate processadorEmailTemplate;
	
	@Override
	public void enviar(Mensagem mensagem) {
	    try {
	        MimeMessage mimeMessage = criarMimeMessage(mensagem);
	        
	        log.info("[E-MAIL] De: {}, Para: {}", emailProperties.getRemetente(), mensagem.getDestinatarios());
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
	    helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
	    helper.setSubject(mensagem.getAssunto());
	    helper.setText(corpo, true);
	    
	    return mimeMessage;
	}
	
}
