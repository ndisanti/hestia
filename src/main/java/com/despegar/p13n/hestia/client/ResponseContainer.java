package com.despegar.p13n.hestia.client;

import java.io.Serializable;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class ResponseContainer<T>
    implements Serializable {


    private static final long serialVersionUID = 1L;
    private List<ErrorMessage> errors;
    private Map<String, String> meta = new LinkedHashMap<String, String>();
    private T data;


    public ResponseContainer() {
    }


    public Map<String, String> getMeta() {
        return this.meta;
    }

    public T getData() {
        return this.data;
    }

    public void setMeta(Map<String, String> meta) {
        this.meta = meta;
    }

    public void setData(T data) {
        this.data = data;
    }



    public List<ErrorMessage> getErrors() {
        return this.errors;
    }


    public void setErrors(List<ErrorMessage> errors) {
        this.errors = errors;
    }


    public boolean hasErrors() {
        return this.errors != null && !this.errors.isEmpty();
    }

    /**
     * Has Meta?
     * 
     * @return
     */
    public boolean hasMeta() {
        return this.meta != null && !this.meta.isEmpty();
    }
}
