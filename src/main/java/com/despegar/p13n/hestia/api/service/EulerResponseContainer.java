package com.despegar.p13n.hestia.api.service;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class EulerResponseContainer<T> {

    private Map<String, Object> meta = new LinkedHashMap<String, Object>();
    private T data;
    private List<EulerApiError> errors = new ArrayList<EulerApiError>();

    public Map<String, Object> getMeta() {
        return this.meta;
    }

    public void setMeta(Map<String, Object> meta) {
        this.meta = meta;
    }

    public T getData() {
        return this.data;
    }

    public void setData(T data) {
        this.data = data;
    }

    public boolean hasErrors() {
        return !this.errors.isEmpty();
    }

    public List<EulerApiError> getErrors() {
        return this.errors;
    }

    public void setErrors(List<EulerApiError> errors) {
        this.errors = errors;
    }

    @Override
    public String toString() {
        return "EulerResponseContainer [meta=" + this.meta + ", data=" + this.data + ", errors=" + this.errors + "]";
    }

}
