package com.algaworks.algafood.infrastructure.service.email;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import com.algaworks.algafood.core.email.EmailProperties;
import com.algaworks.algafood.domain.services.EnvioEmailService;

import freemarker.template.Configuration;
import freemarker.template.Template;
import lombok.extern.slf4j.Slf4j;

@Slf4j
public class FakeEnvioEmailService implements EnvioEmailService{

	@Autowired
	private Configuration freemarkerConfig;
	
	@Autowired
	private EmailProperties emailProperties;
	
	@Autowired
	private JavaMailSender mailSender;
	
	@Override
	public void enviar(Mensagem mensagem) {
		try {
			String corpo = processarTemplate(mensagem);
			
			MimeMessage mimeMessage = mailSender.createMimeMessage();
			
			MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "UTF-8");
			helper.setFrom(emailProperties.getRemetente());
			helper.setTo(mensagem.getDestinatarios().toArray(new String[0]));
			helper.setSubject(mensagem.getAssunto());
			helper.setText(corpo, true);
			
			log.info("[FAKE E-MAIL] De: {}, Para: {}\n{}", emailProperties.getRemetente(), mensagem.getDestinatarios(), corpo);
		} catch (Exception e) {
			throw new EmailException("Não foi possível enviar e-mail", e);
		}
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
	
//	@Slf4j
//	public class FakeEnvioEmailService extends SmtpEnvioEmailService {
//
//		@Override
//		public void enviar(Mensagem mensagem) {
//			// Foi necessário alterar o modificador de acesso do método processarTemplate
//			// da classe pai para "protected", para poder chamar aqui
//			String corpo = processarTemplate(mensagem);
//
//			log.info("[FAKE E-MAIL] Para: {}\n{}", mensagem.getDestinatarios(), corpo);
//		}
//
//	}
}
