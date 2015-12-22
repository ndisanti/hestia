package com.despegar.p13n.hestia.client;

import java.io.Serializable;

public class ErrorMessage
    implements Serializable {

    private static final long serialVersionUID = 1L;

    private String description;
    private String apiErrorCode;
    private String code;

    public ErrorMessage() {
    }

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

    @Override
    public String toString() {
        return "ErrorMessage [description=" + this.description + ", apiErrorCode=" + this.apiErrorCode + ", code="
            + this.code + "]";
    }



}
