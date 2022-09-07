package com.ecom.mobile.accessories.services;

import java.util.Date;
import java.util.Properties;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

@Service
public class EMailService {

	private static final Logger LOGGER = LoggerFactory.getLogger(EMailService.class);

	public static void main(String[] args) {
		final String fromEmail = "shanmukhsravan.madhavareddy@gmail.com";
		final String to = "shanmukhsravanth1@gmail.com";
		final String password = "S112644Ms.";
		EMailService t = new EMailService();
		t.sentEmail(fromEmail, to, password, "Welcome", "Hello", null);

	}

	public void sentEmail(String fromEmail, String toMail, String password, String subject, String body,
			String[] attachFiles) {
		Properties props = new Properties();
		props.put("mail.smtp.host", "smtp.gmail.com"); // SMTP Host
		props.put("mail.smtp.port", "587"); // TLS Port
		props.put("mail.smtp.auth", "true"); // enable authentication
		props.put("mail.smtp.starttls.enable", "true"); // enable STARTTLS

		// Authentication: create Authenticator object to pass in Session.getInstance
		// argument
		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(fromEmail, password);
			}
		};

		Session session = Session.getInstance(props, auth);
		MimeMessage m = new MimeMessage(session);
		try {
			m.setFrom(new InternetAddress(fromEmail));
			m.setSubject(subject);
			m.setText(body);
			m.addRecipient(Message.RecipientType.TO, new InternetAddress(toMail));
			MimeBodyPart mbb = new MimeBodyPart();
			mbb.setContent(m.getContent(), m.getContentType());
			Multipart mp = new MimeMultipart();
			mp.addBodyPart(mbb);
			addingAtttachments(mp, attachFiles);
			m.setContent(mp);
			// send
			Transport.send(m);
			LOGGER.debug("Mail Sent ::{}", new Date());
		} catch (Exception e) {
			LOGGER.debug("Fail to Sent ::{}", new Date());
			e.printStackTrace();
		}

	}

	private void addingAtttachments(Multipart mp, String[] attachFiles) throws MessagingException {
		try {
			if (attachFiles != null) {
				for (String filePath : attachFiles) {
					MimeBodyPart attachPart = new MimeBodyPart();
					attachPart.attachFile(filePath);
					mp.addBodyPart(attachPart);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
