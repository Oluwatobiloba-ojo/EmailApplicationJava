package org.example.services.user;

import org.example.data.model.Mail;
import org.example.data.repository.EmailAppRepository;
import org.example.data.repository.MailRepository;
import org.example.data.repository.UserRepository;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.dto.request.SendMailRequest;
import org.example.exceptions.ActionDoneException;
import org.example.exceptions.InvalidFormatDetailException;
import org.example.exceptions.InvalidLoginDetail;
import org.example.exceptions.UserExistException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

@SpringBootTest
@TestPropertySource("/test.properties")
class UserServiceTest {

    @Autowired
    UserService userService;
    @Autowired
    UserRepository userRepository;
    @Autowired
    EmailAppRepository emailAppRepository;
    @Autowired
    MailRepository mailRepository;

    RegisterRequest registerRequest;
    LoginRequest loginRequest;
    @AfterEach
    public void startAfterAll(){
        userRepository.deleteAll();
        emailAppRepository.deleteAll();
        mailRepository.deleteAll();
    }

    @BeforeEach
    public void startBeforeAllTest(){
        registerRequest = new RegisterRequest("tobi","ojo", "tobi123", "2049", "09124757587");
        loginRequest = new LoginRequest("tobi123", "2049");
    }
    @Test
    public void testThatWhenUserRegisterWithAnInvalidPasswordThrowsException(){
        registerRequest.setPassword("mideuye");
        assertThrows(InvalidFormatDetailException.class, ()-> userService.register(registerRequest));
    }
    @Test
    public void testThatWhenUserRegisterWithAnInvalidPhoneNUmberThrowsException(){
        registerRequest.setPhoneNumber("phone");
        assertThrows(InvalidFormatDetailException.class, ()-> userService.register(registerRequest));
    }
    @Test
    public void testThatSameUserCanNotRegisterWithSameDomainName(){
        userService.register(registerRequest);
        assertThrows(UserExistException.class, () -> userService.register(registerRequest));
    }
    @Test
    public void testThatWhenUserLoginWithAnWrongPasswordThrowsException(){
        userService.register(registerRequest);
        loginRequest.setPassword("wrongPassword");
        assertThrows(InvalidLoginDetail.class, ()-> userService.login(loginRequest));
    }
    @Test
    public void testThatWhenUserLoginToAnInvalidDomainNameThrowsException(){
        userService.register(registerRequest);
        loginRequest.setDomainName("wrongDomainName");
        assertThrows(InvalidLoginDetail.class, () -> userService.login(loginRequest));
    }
    @Test
    public void testThatWhenUserWantToLoginTwiceThrowException(){
        userService.register(registerRequest);
        userService.login(loginRequest);
        assertThrows(ActionDoneException.class, ()-> userService.login(loginRequest));
    }
    @Test
    public void testThatUserCanSendMailToAUserWhichDoesNotHaveAccountThrowsException(){
        userService.register(registerRequest);
        userService.login(loginRequest);
        SendMailRequest mailRequest = new SendMailRequest();
        mailRequest.setTitle("Job application");
        mailRequest.setBody("My offer for a job");
        mailRequest.setModeOfMail("send");
        mailRequest.setSenderDomainName(registerRequest.getDomainName());
        mailRequest.setReceiverDomainName("wrong domain user");
        assertThrows(UserExistException.class, () -> userService.send(mailRequest));
    }
    @Test
    public void testThatWhenTwoUserHasRegisterIntoMailAppAndOneWantToSendAnMailToTheOtherUser(){
        userService.register(registerRequest);
        userService.login(loginRequest);
        registerRequest.setDomainName("opeoluwa123");
        userService.register(registerRequest);
        loginRequest.setDomainName(registerRequest.getDomainName());
        userService.login(loginRequest);
        SendMailRequest mailRequest = new SendMailRequest();
        mailRequest.setTitle("Job application");
        mailRequest.setBody("My offer for a job");
        mailRequest.setModeOfMail("send");
        mailRequest.setSenderDomainName("tobi123");
        mailRequest.setReceiverDomainName(registerRequest.getDomainName());
        userService.send(mailRequest);
        assertEquals(2, mailRepository.count());
        List<Mail> sentMails = userService.findSentMailBox("tobi123");
        assertEquals(1, sentMails.size());
        List<Mail> inboxMails = userService.findInboxMailBox(registerRequest.getDomainName());
        assertEquals(1, inboxMails.size());
    }



}