package org.example.services.emailApp;
import org.example.data.model.EmailApp;
import org.example.data.model.Mail;
import org.example.data.model.MailType;
import org.example.data.repository.EmailAppRepository;
import org.example.data.repository.MailRepository;
import org.example.dto.request.SendMailRequest;
import org.example.exceptions.InvalidLoginDetail;
import org.example.exceptions.UserExistException;
import org.example.services.mail.MailService;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.example.util.Mapper.mapMail;

@Service
public class EmailAppServiceImpl implements EmailAppService{

    @Autowired
    EmailAppRepository emailAppRepository;
    @Autowired
    MailService mailService;

    @Override
    public void create(String domainName, Long id) {
        EmailApp emailApp = new EmailApp();
        emailApp.setDomainName(domainName);
        emailApp.setUserId(id);
        emailAppRepository.save(emailApp);
    }
    @Override
    public EmailApp findEmailAppByDomainName(String domainName) {
        for (EmailApp emailApp: emailAppRepository.findAll()){
            if (emailApp.getDomainName().equals(domainName)) return emailApp;
        }
        return null;
    }
    @Override
    public void login(String domainName) {
        EmailApp emailApp = findEmailAppByDomainName(domainName);
        emailApp.setLogOut(true);
        emailAppRepository.save(emailApp);
    }

    @Override
    public void sendMail(SendMailRequest mailRequest) {
        if (findEmailAppByDomainName(mailRequest.getReceiverDomainName()) == null) throw new UserExistException("Receiver should have register to account");
        EmailApp senderEmailApp = findEmailAppByDomainName(mailRequest.getSenderDomainName());
        EmailApp receiverEmailApp = findEmailAppByDomainName(mailRequest.getReceiverDomainName());
        Mail senderMail = mapMail(mailRequest.getBody(), mailRequest.getTitle());
        senderMail.setEmailApp(senderEmailApp);
        senderMail.setMailType(MailType.SENT);
        senderEmailApp.getMails().add(senderMail);
        mailService.save(senderMail);
        Mail receiverMail = mapMail(mailRequest.getBody(), mailRequest.getTitle());
        receiverMail.setEmailApp(receiverEmailApp);
        receiverMail.setMailType(MailType.INBOX);
        receiverEmailApp.getMails().add(receiverMail);
        mailService.save(receiverMail);
        emailAppRepository.save(senderEmailApp);
        emailAppRepository.save(receiverEmailApp);
    }

    @Override
    public List<Mail> findSentMail(String domainName) {
        if (findEmailAppByDomainName(domainName) == null) throw new UserExistException("User does not exist");
        EmailApp emailApp = findEmailAppByDomainName(domainName);
        if (!emailApp.isLogOut()) throw new InvalidLoginDetail("User have not login into account");
        List<Mail> mails = new ArrayList<>();
        for (Mail mail : emailApp.getMails()){
            if (mail.getMailType().equals(MailType.SENT)) mails.add(mail);
        }
       return mails;
    }

    @Override
    public List<Mail> findInboxMail(String domainName) {
        if (findEmailAppByDomainName(domainName) == null) throw new UserExistException("User does not exist");
        EmailApp emailApp = findEmailAppByDomainName(domainName);
        if (!emailApp.isLogOut()) throw new InvalidLoginDetail("User have not login into account");
        List<Mail> mails = new ArrayList<>();
        for (Mail mail : emailApp.getMails()){
            if (mail.getMailType().equals(MailType.INBOX)) mails.add(mail);
        }
        return mails;
    }
}

