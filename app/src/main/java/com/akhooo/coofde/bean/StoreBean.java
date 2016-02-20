package com.akhooo.coofde.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vadivelansr on 2/8/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreBean {
    private String name;
    private String backdrop;

    public String getBackdrop() {
        return backdrop;
    }

    public void setBackdrop(String backdrop) {
        this.backdrop = backdrop;
    }

    public StoreBean() {
    }

    public String getName() {

        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }

    private String logoUrl;
}
