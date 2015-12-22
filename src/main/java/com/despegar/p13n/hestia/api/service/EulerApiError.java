package com.despegar.p13n.hestia.api.service;

public class EulerApiError {

    private String description;
    private String apiErrorCode;
    private String code;

    public String getDescription() {
        return this.description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getApiErrorCode() {
        return this.apiErrorCode;
    }

    public void setApiErrorCode(String apiErrorCode) {
        this.apiErrorCode = apiErrorCode;
    }

    public String getCode() {
        return this.code;
    }

    public void setCode(String code) {
        this.code = code;
    }
}
