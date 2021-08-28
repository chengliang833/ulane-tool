package wang.ulane.email;

import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.FileDataSource;
import javax.mail.Authenticator;
import javax.mail.BodyPart;
import javax.mail.Message.RecipientType;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class SendEmail {
	private final Properties props = new Properties();
	
	public SendEmail(String mail_user, String mail_password, String mail_smtp_host, String mail_smtp_port) {
		// 表示SMTP发送邮件，需要进行身份验证
//		String mail_smtp_auth="true";
//		String mail_smtp_host="smtp.qq.com";
//		String mail_smtp_port="465";
//		String mail_user="327991647@qq.com";
//		String mail_password="";
		
//		String mail_smtp_auth="true";
//		String mail_smtp_host="smtp-mail.outlook.com";
//		String mail_smtp_port="25";
//		String mail_user="eshonulane@outlook.com";
//		String mail_password="";
		
		props.put("mail.smtp.auth", "true");
		props.put("mail.smtp.host", mail_smtp_host);
		props.put("mail.smtp.port", mail_smtp_port);
		// 发件人的账号
		props.put("mail.user", mail_user);
		// 访问SMTP服务时需要提供的密码
		props.put("mail.password", mail_password);
		
		props.put("mail.debug","true"); 
		props.put("mail.smtp.starttls.enable","true"); 
		props.put("mail.smtp.EnableSSL.enable","true");
		props.setProperty("mail.smtp.socketFactory.class","javax.net.ssl.SSLSocketFactory");  
		props.setProperty("mail.smtp.socketFactory.fallback","false");
		props.setProperty("mail.smtp.socketFactory.port", "465");
		
	}
	
	public void send(String to, String title, String content) throws MessagingException{
		send(new String[]{to}, null, title, null, content);
	}
	public void send(String[] tos, String title, String content) throws MessagingException{
		send(tos, null, title, null, content);
	}
	public void send(String[] tos, String[] ccs, String title, String fileName, String content)
			throws MessagingException {

		// 构建授权信息，用于进行SMTP进行身份验证
		Authenticator authenticator = new Authenticator() {
			@Override
			protected PasswordAuthentication getPasswordAuthentication() {
				// 用户名、密码
				String userName = props.getProperty("mail.user");
				String password = props.getProperty("mail.password");
				return new PasswordAuthentication(userName, password);
			}
		};
		// 使用环境属性和授权信息，创建邮件会话
		Session mailSession = Session.getInstance(props, authenticator);
		// 创建邮件消息
		MimeMessage message = new MimeMessage(mailSession);
		// 设置发件人
		InternetAddress form = new InternetAddress(props.getProperty("mail.user"));
		message.setFrom(form);

		// 设置收件人
		InternetAddress[] to = new InternetAddress[tos.length];
		int index = 0;
		for (String to_email : tos) {
			to[index++] = new InternetAddress(to_email);
		}
		message.setRecipients(RecipientType.TO, to);

		// 设置抄送
		if(ccs!=null){
			InternetAddress[] cc = new InternetAddress[ccs.length];
			index = 0;
			for (String cc_email : ccs) {
				cc[index++] = new InternetAddress(cc_email);
			}
			message.setRecipients(RecipientType.CC, cc);
		}
		
		// 设置邮件标题
		message.setSubject(title, "UTF-8");

		// 附件
		if(fileName!=null){
			try {
				MimeMultipart mp = new MimeMultipart();
				if (fileName != null) {
					BodyPart bp = new MimeBodyPart();
					FileDataSource fileds = new FileDataSource(fileName);
					bp.setDataHandler(new DataHandler(fileds));
					bp.setFileName(fileds.getName());
					mp.addBodyPart(bp);
				}
				BodyPart bp2 = new MimeBodyPart();
				bp2.setContent(content, "text/html;charset=UTF-8");
				mp.addBodyPart(bp2);
				message.setContent(mp);
			} catch (Exception e) {
				log.error("增加邮件附件：发生错误！"+e.getMessage(), e);
			}
		}else{
	        message.setContent(content, "text/html;charset=UTF-8");
		}
		
		message.saveChanges();

		// 发送邮件
		Transport.send(message);
	}

	
	public static void main(String[] args) throws MessagingException{
		System.out.println("start...");
		new SendEmail("username@a.com", "password", "smtp.ulane.cn", "465").send("327991647@qq.om", "测试邮件", "邮件内容");
//		new SendEmail().send("key",new String[]{"959043365@qq.com"},null,null,"key");
//		new SendEmail().send("标题",new String[]{"959043365@qq.com"},null,null,"内容");
		System.out.println("end...");
	}
	
}
