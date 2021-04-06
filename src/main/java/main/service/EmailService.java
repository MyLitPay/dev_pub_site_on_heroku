package main.service;

import javax.mail.MessagingException;

public interface EmailService {
    void send(String to, String subject, String text) throws MessagingException;
}
