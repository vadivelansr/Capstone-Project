package com.akhooo.coofde.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vadivelansr on 1/8/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class StoreLogo {
    private String store;
    private String logoUrl;

    public StoreLogo() {
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getLogoUrl() {
        return logoUrl;
    }

    public void setLogoUrl(String logoUrl) {
        this.logoUrl = logoUrl;
    }
}
