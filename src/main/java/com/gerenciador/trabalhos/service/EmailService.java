package com.gerenciador.trabalhos.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailService {

    private final JavaMailSender mailSender;

    @Value("${spring.mail.username:}")
    private String remetente;

    public void enviarCodigoRecuperacao(String destinatario, String codigo, int expiracaoMinutos) {
        if (!StringUtils.hasText(remetente)) {
            throw new RuntimeException(
                    "E-mail remetente não configurado. Crie o arquivo .env com SPRING_MAIL_USERNAME. Veja CONFIGURACAO-EMAIL.md.");
        }

        SimpleMailMessage mensagem = new SimpleMailMessage();
        mensagem.setFrom(remetente);
        mensagem.setTo(destinatario);
        mensagem.setSubject("Código de recuperação de senha");
        mensagem.setText(
                "Olá,\n\n"
                        + "Seu código de recuperação de senha é: " + codigo + "\n\n"
                        + "Este código expira em " + expiracaoMinutos + " minutos.\n\n"
                        + "Se você não solicitou esta recuperação, ignore este e-mail.");

        mailSender.send(mensagem);
    }
}
