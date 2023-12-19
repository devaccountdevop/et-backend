package com.es.service.impl;

import java.nio.charset.StandardCharsets;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import com.es.entity.Signup;

@Service
public class ForgotPasswordService {

	@Autowired
	private JavaMailSender javaMailSender;


	public void EmailService(JavaMailSender javaMailSender) {
		this.javaMailSender = javaMailSender;

	}

	public String sendEmail(Signup signup) {
		try {
			MimeMessage message = javaMailSender.createMimeMessage();
			MimeMessageHelper helper = new MimeMessageHelper(message, true, StandardCharsets.UTF_8.name());

			helper.setFrom("estimationtool@gmail.com");
			helper.setTo(signup.getEmail());
			helper.setSubject("Reset your password for Estimation-tool");

			String htmlContent = readHtmlContent("templates/email.html", signup.getUserName());

			helper.setText(htmlContent, true);

			javaMailSender.send(message);
			return "Email Sent Successfully!";
		} catch (MessagingException e) {

			e.printStackTrace();
			return "Failed to send email";
		}
	}

	private String readHtmlContent(String path, String username) {
		try {
			ClassPathResource resource = new ClassPathResource(path);
			byte[] fileContent = new byte[(int) resource.contentLength()];
			resource.getInputStream().read(fileContent);
			String htmlContent = new String(fileContent, StandardCharsets.UTF_8);

			htmlContent = htmlContent.replace("http://localhost:4200/resetPassword/username",
					"http://localhost:4200/resetPassword/" + username);

			return htmlContent;
		} catch (Exception e) {
			e.printStackTrace();
			return "";
		}
	}

}
