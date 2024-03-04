package org.example.services.emailApp;

import org.example.data.model.EmailApp;
import org.example.data.model.Mail;
import org.example.dto.request.SendMailRequest;

import java.util.List;

public interface EmailAppService {
    void create(String domainName, Long id);
    EmailApp findEmailAppByDomainName(String domainName);

    void login(String domainName);

    void sendMail(SendMailRequest mailRequest);

    List<Mail> findSentMail(String domainName);

    List<Mail> findInboxMail(String domainName);
}
