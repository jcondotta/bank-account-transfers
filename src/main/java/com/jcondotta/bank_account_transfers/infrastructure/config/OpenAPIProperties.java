package com.jcondotta.bank_account_transfers.infrastructure.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;
import java.util.Map;

@Configuration
@ConfigurationProperties(prefix = "openapi")
public class OpenAPIProperties {
    private String version;
    private String title;
    private String description;
    private Map<String, String> contact;
    private Map<String, String> license;
    private String termsOfService;
    private List<Map<String, String>> serverUrls;
    private List<Map<String, String>> tags;
    private Map<String, String> security;
    private Map<String, String> rateLimits;

    // Getters and Setters
    public String getVersion() { return version; }
    public void setVersion(String version) { this.version = version; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public Map<String, String> getContact() { return contact; }
    public void setContact(Map<String, String> contact) { this.contact = contact; }

    public Map<String, String> getLicense() { return license; }
    public void setLicense(Map<String, String> license) { this.license = license; }

    public String getTermsOfService() { return termsOfService; }
    public void setTermsOfService(String termsOfService) { this.termsOfService = termsOfService; }

    public List<Map<String, String>> getServerUrls() { return serverUrls; }
    public void setServerUrls(List<Map<String, String>> serverUrls) { this.serverUrls = serverUrls; }

    public List<Map<String, String>> getTags() { return tags; }
    public void setTags(List<Map<String, String>> tags) { this.tags = tags; }

    public Map<String, String> getSecurity() { return security; }
    public void setSecurity(Map<String, String> security) { this.security = security; }

    public Map<String, String> getRateLimits() { return rateLimits; }
    public void setRateLimits(Map<String, String> rateLimits) { this.rateLimits = rateLimits; }
}
