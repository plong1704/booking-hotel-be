package com.example.dacn.services.impl;

import com.example.dacn.services.IMailService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

@Service
public class JavaMailSenderService implements IMailService {

    @Autowired
    private JavaMailSender javaMailSender;

    @Override
    public void sendOTPMessage(String to, String subject, String message) throws MessagingException {
        MimeMessage msg = javaMailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(msg, true);

        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(message, true);
        javaMailSender.send(msg);
    }
}
