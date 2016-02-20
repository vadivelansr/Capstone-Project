package com.akhooo.coofde.bean;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * Created by vadivelansr on 2/20/2016.
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class Backdrop {
    private String url;

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
