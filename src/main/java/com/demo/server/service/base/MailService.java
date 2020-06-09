package com.demo.server.service.base;

import java.io.File;
import java.util.Map;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class MailService {
	@Autowired
	private JavaMailSender mailSender;

	public boolean sendSimpleMail(String from, String to, String subject, String content) throws Exception {
		boolean result = false;
		try {
			SimpleMailMessage message = new SimpleMailMessage();
			message.setFrom(from);
			message.setTo(to);
			message.setSubject(subject);
			message.setText(content);
			mailSender.send(message);

			result = true;
			log.info("sendSimpleMail ok. from={},to={},subject={}", from, to, subject);
		} catch (Exception e) {
			log.error("sendSimpleMail error.", e);
		}
		return result;
	}

	public boolean sendAttachmentsMail(String from, String to, String subject, String content,
			Map<String, File> attachmentMap) throws Exception {
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
						log.error("sendAttachmentsMail addAttachment error.", e);
					}
				});
			}
			mailSender.send(mimeMessage);

			result = true;
			log.info("sendAttachmentsMail ok. from={},to={},subject={}", from, to, subject);
		} catch (Exception e) {
			log.error("sendAttachmentsMail error.", e);
		}

		return result;
	}

	public boolean sendHtmlMail(String from, String to, String subject, String content) throws Exception {
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
			log.info("sendInlineMail ok. from={},to={},subject={}", from, to, subject);
		} catch (Exception e) {
			log.error("sendInlineMail error.", e);
		}
		return result;
	}
}