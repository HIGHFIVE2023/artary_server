package com.highfive.artary.service;

import com.highfive.artary.domain.User;
import com.highfive.artary.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.mail.MailException;
import org.springframework.mail.MailSendException;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.UnsupportedEncodingException;
import java.util.Random;

@PropertySource("classpath:application.properties")
@Slf4j
@RequiredArgsConstructor
@Service
public class MailService {

    private final JavaMailSender javaMailSender;
    private final UserService userService;
    private final UserRepository userRepository;
    private final String ePw = createKey();

    @Value("${spring.mail.username}")
    private String id;

    public MimeMessage createMessage(String to) throws MessagingException, UnsupportedEncodingException {
        log.info("보내는 대상: " + to);
        log.info("인증 번호: " + ePw);

        String encodedEPw = StringEscapeUtils.escapeHtml4(ePw);

        MimeMessage message = javaMailSender.createMimeMessage();
        message.addRecipients(MimeMessage.RecipientType.TO, to);
        message.setSubject("아트어리 임시 비밀번호 발급 안내 메일입니다.");

        String msg = "";
        msg += "<h1 style=\"font-size: 30px; padding-right: 30px; padding-left: 30px;\">아트어리 임시 비밀번호</h1>";
        msg += "<p style=\"font-size: 17px; padding-right: 30px; padding-left: 30px;\">로그인 후 반드시 비밀번호를 수정해주세요.</p>";
        msg += "<div style=\"padding-right: 30px; padding-left: 30px; margin: 32px 0 40px;\"><table style=\"border-collapse: collapse; border: 0; background-color: #F4F4F4; height: 70px; table-layout: fixed; word-wrap: break-word; border-radius: 6px;\"><tbody><tr><td style=\"text-align: center; vertical-align: middle; font-size: 30px;\">";
        msg += encodedEPw;
        msg += "</td></tr></tbody></table></div>";

        message.setText(msg, "utf-8", "html");
        message.setFrom(new InternetAddress(id, "아트어리"));

        return message;
    }

    // 임시 비밀번호 발급
    public static String createKey() {
        String characters = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789!@#$%^&*()-_=+[{]};:',<.>/?";
        int length = 10;

        Random random = new Random();
        StringBuilder key = new StringBuilder();

        key.append(characters.charAt(random.nextInt(characters.length() - 10)));
        key.append(characters.charAt(26 + random.nextInt(10)));
        key.append(characters.charAt(characters.length() - 1));

        for (int i = 3; i < length; i++) {
            key.append(characters.charAt(random.nextInt(characters.length())));
        }

        for (int i = 0; i < key.length(); i++) {
            int swapIndex = random.nextInt(key.length());
            char temp = key.charAt(swapIndex);
            key.setCharAt(swapIndex, key.charAt(i));
            key.setCharAt(i, temp);
        }

        return key.toString();
    }

    // 메일 발송
    public String sendSimpleMessage(String to) throws Exception {
        MimeMessage message = createMessage(to);
        try {
            javaMailSender.send(message);
            User user = userRepository.findByEmail(to).orElse(null);
            if (user != null) {
                userService.updatePassword(user.getId(), ePw);
            }
        } catch (MailException es) {
            es.printStackTrace();
            throw new MailSendException("Failed to send email", es);
        }
        return ePw;
    }


}