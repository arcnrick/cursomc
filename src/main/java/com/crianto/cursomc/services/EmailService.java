package com.crianto.cursomc.services;

import org.springframework.mail.SimpleMailMessage;

import com.crianto.cursomc.domain.Pedido;

public interface EmailService {

	void sendOrderConfirmationEmail(Pedido obj);
	
	void sendEmail(SimpleMailMessage msg);
	
}
