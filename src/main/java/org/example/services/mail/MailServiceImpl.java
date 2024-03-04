package org.example.services.mail;

import org.example.data.model.Mail;
import org.example.data.repository.MailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class MailServiceImpl implements MailService{
    @Autowired
    MailRepository mailRepository;

    @Override
    public void save(Mail mail) {
        mailRepository.save(mail);
    }
}
