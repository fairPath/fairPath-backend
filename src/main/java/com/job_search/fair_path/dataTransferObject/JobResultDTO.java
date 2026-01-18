package com.job_search.fair_path.dataTransferObject;

public class JobResultDTO implements Comparable<JobResultDTO> {
    private String title;
    private String companyName;
    private String dateCreated;
    private String location;
    private String redirectUrl;
    private String jobDescription;
    private Double salaryMin;
    private Double salaryMax;
    private Integer rating;

    public JobResultDTO(String title, String companyName, String dateCreated, String location, String redirectUrl,
            String jobDescription, Double salaryMin, Double salaryMax) {
        this.title = title;
        this.companyName = companyName;
        this.dateCreated = dateCreated;
        this.location = location;
        this.redirectUrl = redirectUrl;
        this.jobDescription = jobDescription;
        this.salaryMin = salaryMin;
        this.salaryMax = salaryMax;
    }

    @Override
    public String toString() {
        return "{" +
                "title='" + title + '\'' +
                ", companyName=" + companyName + ", dateCreated=" + dateCreated + ", location=" + location
                + ", redirectUrl=" + redirectUrl + ", salaryMin="
                + salaryMin + ", salaryMax=" + salaryMax + ", rating=" + rating +
                '}';
    }

    public String printRating() {
        return ">>>>>>>>>>>>>" +
                ", companyName=" + companyName + ", rating=" + rating +
                "<<<<<<<<<<<<<<<<<";
    }

    @Override
    public int compareTo(JobResultDTO other) {
        return Integer.compare(other.rating, this.rating); // Sort by rating
    }

    public String getTitle() {
        return title;
    }

    public String getCompanyName() {
        return companyName;
    }

    public String getDateCreated() {
        return dateCreated;
    }

    public String getLocation() {
        return location;
    }

    public String getRedirectUrl() {
        return redirectUrl;
    }

    public String getJobDescription() {
        return jobDescription;
    }

    public Double getSalaryMin() {
        return salaryMin;
    }

    public Double getSalaryMax() {
        return salaryMax;
    }

}
