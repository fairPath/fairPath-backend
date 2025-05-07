package com.job_search.fair_path.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name = "inclusive_companies")
public class InclusiveCompanyEntity {
    @Id
    private String company_name;
    private Integer rating;

    public String getCompanyName() {
        return company_name;
    }

    public Integer getRating() {
        return rating;
    }

}
