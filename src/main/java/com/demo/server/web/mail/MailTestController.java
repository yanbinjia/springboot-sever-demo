package com.demo.server.web.mail;

import java.io.File;

import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.demo.server.interceptor.SignPass;
import com.demo.server.interceptor.TokenPass;

@RestController
@RequestMapping("/mailtest")
public class MailTestController {

	String from = "mail-from@demo.com";
	String to = "mail-to@demo.com";
	String subject = "[测试邮件]";

	@Autowired
	private JavaMailSender mailSender;

	@RequestMapping("/simple")
	@TokenPass
	@SignPass
	public void sendSimpleMail() throws Exception {
		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom(from);
		message.setTo(to);
		message.setSubject(subject + "简单邮件");
		message.setText("测试邮件内容");
		mailSender.send(message);
	}

	@RequestMapping("/attchments")
	@TokenPass
	@SignPass
	public void sendAttachmentsMail() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject + "附件邮件");
		helper.setText("有附件的邮件");
		FileSystemResource file = new FileSystemResource(new File("附件-1.jpg"));
		helper.addAttachment("附件-1.jpg", file);
		helper.addAttachment("附件-2.jpg", file);
		mailSender.send(mimeMessage);
	}

	@RequestMapping("/inline")
	@TokenPass
	@SignPass
	public void sendInlineMail() throws Exception {
		MimeMessage mimeMessage = mailSender.createMimeMessage();
		MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, true);
		helper.setFrom(from);
		helper.setTo(to);
		helper.setSubject(subject + "嵌入静态资源");
		helper.setText("<html><body><img src=\"cid:weixin\" ></body></html>", true);
		FileSystemResource file = new FileSystemResource(new File("weixin.jpg"));

		helper.addInline("weixin", file);

		mailSender.send(mimeMessage);
	}

}
