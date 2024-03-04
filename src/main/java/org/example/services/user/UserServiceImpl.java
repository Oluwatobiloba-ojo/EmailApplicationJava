package org.example.services.user;

import org.example.data.model.EmailApp;
import org.example.data.model.Mail;
import org.example.data.model.User;
import org.example.data.repository.UserRepository;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.dto.request.SendMailRequest;
import org.example.exceptions.ActionDoneException;
import org.example.exceptions.InvalidFormatDetailException;
import org.example.exceptions.InvalidLoginDetail;
import org.example.exceptions.UserExistException;
import org.example.services.emailApp.EmailAppService;
import org.example.util.Mapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

import static org.example.util.Validation.validatePassword;
import static org.example.util.Validation.validatePhoneNumber;

@Service
public class UserServiceImpl implements UserService{
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailAppService emailAppService;

    @Override
    public void register(RegisterRequest registerRequest) {
        if (emailAppService.findEmailAppByDomainName(registerRequest.getDomainName()) != null) throw new UserExistException("Domain name already taken");
        if (!validatePassword(registerRequest.getPassword())) throw new InvalidFormatDetailException("Invalid format of password, must contain four digit");
        if (!validatePhoneNumber(registerRequest.getPhoneNumber())) throw new InvalidFormatDetailException("invalid format of phone number");
        User user = Mapper.mapUser(registerRequest);
        userRepository.save(user);
        emailAppService.create(registerRequest.getDomainName(), user.getId());
    }

    @Override
    public void login(LoginRequest loginRequest) {
        EmailApp emailApp = emailAppService.findEmailAppByDomainName(loginRequest.getDomainName());
        if (emailApp == null) throw new InvalidLoginDetail("Invalid login details");
        if (emailApp.isLogOut()) throw new ActionDoneException("Action has been done already");
        User user = findUserById(emailApp.getUserId());
        if (!user.getPassword().equals(loginRequest.getPassword())) throw new InvalidLoginDetail("Invalid login details");
        emailAppService.login(loginRequest.getDomainName());
    }

    @Override
    public void send(SendMailRequest mailRequest) {
        EmailApp emailApp = emailAppService.findEmailAppByDomainName(mailRequest.getSenderDomainName());
        if (emailApp == null) throw new UserExistException("Sender should have register account");
        if (!emailApp.isLogOut()) throw new InvalidLoginDetail("Sender have not login yet");
        emailAppService.sendMail(mailRequest);
    }

    @Override
    public List<Mail> findSentMailBox(String domainName) {
       return emailAppService.findSentMail(domainName);
    }

    @Override
    public List<Mail> findInboxMailBox(String domainName) {
        return emailAppService.findInboxMail(domainName);
    }

    private User findUserById(Long userId) {
        return userRepository.findUserById(userId);
    }

}
