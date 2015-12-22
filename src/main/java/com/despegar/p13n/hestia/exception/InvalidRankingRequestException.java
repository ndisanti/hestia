package com.despegar.p13n.hestia.exception;

import com.despegar.library.api.exception.ApiException;

/**
 * Invalid ranking requests exception.
 *
 * @author jcastro
 * @since Jan 4, 2013
 */
public class InvalidRankingRequestException
    extends ApiException {

    /**
     * 
     */
    private static final long serialVersionUID = 3247929092366807183L;

    public InvalidRankingRequestException(String message) {
        super(HestiaApiErrorCode.INVALID_RANKING_REQUEST.getCode(), message);
    }

    public InvalidRankingRequestException(String message, Exception e) {
        super(HestiaApiErrorCode.INVALID_RANKING_REQUEST.getCode(), message, e);
    }

}
