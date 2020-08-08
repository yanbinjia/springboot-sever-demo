/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-04T12:01:38.392+08:00
 */

package com.demo.server.common.utils.mail;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.Map;

@Component
public class MailUtil {
    private static final Logger logger = LoggerFactory.getLogger(MailUtil.class);

    @Autowired
    private JavaMailSender mailSender;

    @PostConstruct // 初始化执行顺序:构造方法->@Autowired->@PostConstruct
    public void init() {
    }

    public boolean sendSimpleMail(String from, String to, String subject, String content) {
        boolean result = false;
        try {
            SimpleMailMessage message = new SimpleMailMessage();
            message.setFrom(from);
            message.setTo(to);
            message.setSubject(subject);
            message.setText(content);
            mailSender.send(message);

            result = true;
            logger.info("sendSimpleMail ok. from={},to={},subject={}", from, to, subject);
        } catch (Exception e) {
            logger.error("sendSimpleMail error.", e);
        }
        return result;
    }

    public boolean sendAttachmentsMail(String from, String to, String subject, String content,
                                       Map<String, File> attachmentMap) {
        boolean result = false;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content);
            // 添加附件
            if (attachmentMap != null) {
                attachmentMap.forEach((k, v) -> {
                    try {
                        helper.addAttachment(k, new FileSystemResource(v));
                    } catch (MessagingException e) {
                        logger.error("sendAttachmentsMail addAttachment error.", e);
                    }
                });
            }
            mailSender.send(mimeMessage);

            result = true;
            logger.info("sendAttachmentsMail ok. from={},to={},subject={}", from, to, subject);
        } catch (Exception e) {
            logger.error("sendAttachmentsMail error.", e);
        }

        return result;
    }

    public boolean sendHtmlMail(String from, String to, String subject, String content) {
        boolean result = false;
        try {
            MimeMessage mimeMessage = mailSender.createMimeMessage();
            MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
            helper.setFrom(from);
            helper.setTo(to);
            helper.setSubject(subject);
            helper.setText(content, true);
            mailSender.send(mimeMessage);

            result = true;
            logger.info("sendInlineMail ok. from={},to={},subject={}", from, to, subject);
        } catch (Exception e) {
            logger.error("sendInlineMail error.", e);
        }
        return result;
    }
}
