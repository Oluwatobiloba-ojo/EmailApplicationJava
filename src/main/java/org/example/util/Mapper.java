package org.example.util;

import org.example.data.model.Mail;
import org.example.data.model.User;
import org.example.dto.request.RegisterRequest;

public class Mapper {
    public static User mapUser(RegisterRequest registerRequest) {
        User user = new User();
        user.setPassword(registerRequest.getPassword());
        user.setName(registerRequest.getFirstName() + " " + registerRequest.getLastName());
        user.setPhoneNumber(registerRequest.getPhoneNumber());
        return user;
    }

    public static Mail mapMail(String body, String title) {
        Mail mail = new Mail();
        mail.setBody(body);
        mail.setTitle(title);
        return mail;
    }
}
