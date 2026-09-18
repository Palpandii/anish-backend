package com.ascrackers.backend.dto;

import com.ascrackers.backend.model.Banner;

public class BannerDto {

    private Long id;
    private String url;

    public BannerDto() {}

    public BannerDto(Long id, String url) {
        this.id = id;
        this.url = url;
    }

    public static BannerDto from(Banner banner) {
        return new BannerDto(banner.getId(), banner.getUrl());
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getUrl() { return url; }
    public void setUrl(String url) { this.url = url; }

}
