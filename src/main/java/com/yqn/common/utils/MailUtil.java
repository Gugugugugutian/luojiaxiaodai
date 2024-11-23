package com.yqn.common.utils;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.Resource;
import javax.mail.internet.MimeMessage;
import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;

@Component
public class MailUtil {
    @Resource
    private JavaMailSender mailSender;

    public void sendEmail(String to, String subject, String text) throws MessagingException {
        MimeMessage message = mailSender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8"); // 设置编码为 UTF-8
        helper.setFrom("2530141349@qq.com"); // 替换为你的邮箱
        helper.setTo(to);
        helper.setSubject(subject);
        helper.setText(text, true);
        mailSender.send(message);

    }
}
