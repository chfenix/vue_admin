package cn.solwind.admin;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import javax.annotation.Resource;

@SpringBootTest
public class MailTest {

    @Resource
    JavaMailSender javaMailSender;

    @Test
    public void testSimpleMail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom("chfenix@163.com");
        message.setTo("3300427@qq.com");
        message.setSubject("标题：测试标题");
        message.setText("测试内容部份");
        javaMailSender.send(message);
    }
}
