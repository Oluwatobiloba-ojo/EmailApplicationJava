package org.example.data.repository;

import org.example.data.model.EmailApp;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EmailAppRepository extends JpaRepository<EmailApp, Long> {
}
