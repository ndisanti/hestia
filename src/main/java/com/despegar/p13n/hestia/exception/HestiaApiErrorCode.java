package com.despegar.p13n.hestia.exception;
import com.despegar.library.api.HttpCode;

public enum HestiaApiErrorCode {

		INVALID_RANKING_REQUEST(1, HttpCode.SC_INTERNAL_SERVER_ERROR.getCode()),

	    ILLEGAL_ARGUMENT_EXCEPTION(4, HttpCode.SC_INTERNAL_SERVER_ERROR.getCode());

	    
	    private int code;
	    private String defaultMessage;
	    private int httpCode;

	    private HestiaApiErrorCode(int code, int httpCode) {
	        this.code = code;
	        this.httpCode = httpCode;
	    }

	    public int getCode() {
	        return Integer.valueOf(String.format("%d%d", this.httpCode, this.code));
	    }

	    public String getDescription() {
	        return this.defaultMessage;
	    }
	
	
}
