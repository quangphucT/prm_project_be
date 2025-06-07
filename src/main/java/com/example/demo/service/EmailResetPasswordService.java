package com.example.demo.service;

import com.example.demo.model.EmailDetails;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;

import org.springframework.stereotype.Service;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

@Service
public class EmailResetPasswordService {
    @Autowired
    TemplateEngine templateEngine;
    @Autowired
    JavaMailSender javaMailSender;


    public void sendMailResetPassword(EmailDetails emailDetails){
        try {
            Context context = new Context();
            context.setVariable("name", emailDetails.getReceiver().getEmail());
            context.setVariable("button", "Click Here To Reset Password");
            context.setVariable("link", emailDetails.getLink());

            String template = templateEngine.process("reset-password", context);

            //Thằng mineMessage giúp mình tạo ra một simple messages
            MimeMessage mineMessage = javaMailSender.createMimeMessage();
            MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mineMessage);

            mimeMessageHelper.setFrom("phucp9698@gmail.com");
            mimeMessageHelper.setTo(emailDetails.getReceiver().getEmail());
            mimeMessageHelper.setText(template, true);
            mimeMessageHelper.setSubject(emailDetails.getSubject());
            javaMailSender.send(mineMessage);
        }catch(MessagingException e){
            System.out.println("Send email failed!!");
        }
    }
}
