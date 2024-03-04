package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
@Data
@Entity
public class Mail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String body;
    private LocalDate dateCreated = LocalDate.now();
    @Enumerated
    private MailType mailType;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "emailApp_id", referencedColumnName = "id")
    private EmailApp emailApp;
}
