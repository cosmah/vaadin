package com.vaadin.application.backend.repository;

import com.vaadin.application.backend.entity.Company;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CompanyRepository extends JpaRepository<Company, Long> {
}