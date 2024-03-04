package org.example.services.user;

import org.example.data.model.Mail;
import org.example.dto.request.LoginRequest;
import org.example.dto.request.RegisterRequest;
import org.example.dto.request.SendMailRequest;

import java.util.List;
import java.util.Map;

public interface UserService {
    void register(RegisterRequest registerRequest);

    void login(LoginRequest loginRequest);

    void send(SendMailRequest mailRequest);
    List<Mail> findSentMailBox(String tobi123);
    List<Mail> findInboxMailBox(String domainName);
}
