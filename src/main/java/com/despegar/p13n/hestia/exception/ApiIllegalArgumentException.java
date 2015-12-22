package com.despegar.p13n.hestia.exception;

import com.despegar.library.api.exception.ApiException;

public class ApiIllegalArgumentException extends ApiException {

    /**
     * 
     */
    private static final long serialVersionUID = -7429333444107593603L;

    public ApiIllegalArgumentException(String msg) {
        super(HestiaApiErrorCode.ILLEGAL_ARGUMENT_EXCEPTION.getCode(), msg);
    }


}
