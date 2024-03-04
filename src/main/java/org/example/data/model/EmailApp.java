package org.example.data.model;

import jakarta.persistence.*;
import lombok.Data;

import java.util.List;
@Data
@Entity
public class EmailApp {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String domainName;
    private Long userId;
    private boolean isLogOut=false;
    @OneToMany(mappedBy = "emailApp", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Mail> mails;
}
