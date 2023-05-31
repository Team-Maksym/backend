package starlight.backend.config;

import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import starlight.backend.email.model.EmailProps;

import java.util.Properties;

@Configuration
@AllArgsConstructor
public class JavaMailSender {
    EmailProps emailProps;

    @Bean
    public org.springframework.mail.javamail.JavaMailSender getJavaMailSender() {
        org.springframework.mail.javamail.JavaMailSenderImpl mailSender = new org.springframework.mail.javamail.JavaMailSenderImpl();
        mailSender.setHost("smtp-mail.outlook.com");
        mailSender.setPort(587);

        mailSender.setUsername(emailProps.username());
        mailSender.setPassword(emailProps.password());

        Properties props = mailSender.getJavaMailProperties();
        props.put("mail.transport.protocol", "smtp");
        props.put("mail.smtp.auth", "true");
        props.put("mail.smtp.starttls.enable", "true");
        props.put("mail.debug", "true");

        return mailSender;
    }
}
