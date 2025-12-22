package com.job_search.fair_path.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import com.job_search.fair_path.repository.InclusiveCompanyRepository;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import com.job_search.fair_path.entity.InclusiveCompanyEntity;

@RestController
public class InclusiveCompaniesController {
    @Autowired
    private InclusiveCompanyRepository repo;

    @GetMapping("/{companyName}")
    public InclusiveCompanyEntity getByCompanyName(@PathVariable String companyName) {
        return repo.findById(companyName).orElse(null);

    }

}
