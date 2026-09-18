package com.ascrackers.backend.dto;

import jakarta.validation.constraints.NotBlank;

public class BannerRequest {

    @NotBlank
    private String url;

    public BannerRequest() {}

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }
}
