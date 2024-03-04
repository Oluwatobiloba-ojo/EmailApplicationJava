package org.example.dto.request;

import lombok.Data;

@Data
public class SendMailRequest {
    private String title;
    private String body;
    private String senderDomainName;
    private String receiverDomainName;
    private String modeOfMail;
}
