package com.vaadin.application.backend.service;

import com.vaadin.application.backend.entity.Company;
import com.vaadin.application.backend.repository.CompanyRepository;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class CompanyService {
    private CompanyRepository companyRepository;
    public CompanyService(CompanyRepository companyRepository) {
        this.companyRepository = companyRepository;
    }
    public List<Company> findAll() {
        return companyRepository.findAll();
    }

    public Map<String, Integer> getStats() {
        HashMap<String, Integer> stats = new HashMap<>();
        //Loops through each company and returns a Map containing the company name
        //and number of employees
        findAll().forEach(company -> stats.put(company.getName(), company.getEmployees
                ().size()));
        return stats;
    }
}