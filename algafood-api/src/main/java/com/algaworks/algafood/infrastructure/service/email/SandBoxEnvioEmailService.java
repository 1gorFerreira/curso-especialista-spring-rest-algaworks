package com.algaworks.algafood.infrastructure.service.email;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.v1.services.EnvioEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SandBoxEnvioEmailService implements EnvioEmailService{

	@Autowired
	private Configuration freemarkerConfig;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private JavaMailSender mailSender;
	
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
	    String corpo = processarTemplate(mensagem);
	    
	    MimeMessage mimeMessage = mailSender.createMimeMessage();
	    
	    MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
	    helper.setFrom(emailProperties.getRemetente());
	    helper.setTo(emailProperties.getSandbox().getDestinatario());
	    helper.setSubject(mensagem.getAssunto());
	    helper.setText(corpo, true);
	    
	    return mimeMessage;
	}
	
	private String processarTemplate(Mensagem mensagem) {
		try {
			Template template = freemarkerConfig.getTemplate(mensagem.getCorpo());
			
			return FreeMarkerTemplateUtils.processTemplateIntoString(
					template, mensagem.getVariaveis());
		} catch (Exception e) {
			throw new EmailException("Não foi possível montar o template do e-mail", e);
		}
	}
	
	// OU
	
//	public class SandboxEnvioEmailService extends SmtpEnvioEmailService {
//
//	    @Autowired
//	    private EmailProperties emailProperties;
//	    
//	    // Separei a criação de MimeMessage em um método na classe pai (criarMimeMessage),
//	    // para possibilitar a sobrescrita desse método aqui
//	    @Override
//	    protected MimeMessage criarMimeMessage(Mensagem mensagem) throws MessagingException {
//	        MimeMessage mimeMessage = super.criarMimeMessage(mensagem);
//	        
//	        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
//	        helper.setTo(emailProperties.getSandbox().getDestinatario());
//	        
//	        return mimeMessage;
//	    }        
//	}     

}
