package com.despegar.p13n.hestia.rest.interceptors;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.despegar.library.routing.uow.UowConstants;
import com.despegar.library.routing.uow.UowData;
import com.despegar.library.routing.uow.UowHelper;

/**
 * 
 * Sets x-uow in context.
 * 
 */
public class XUOWInterceptor
    extends HandlerInterceptorAdapter {

    private static final Logger LOGGER = LoggerFactory.getLogger(XUOWInterceptor.class);
    public static final String PREFIX = "xselling-service";


    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String uowHeader = request.getHeader(UowConstants.UNIT_OF_WORK_HEADER);
        UowData uowData = null;
        if (StringUtils.isBlank(uowHeader)) {
            uowData = UowHelper.createAndSetNewUnitOfWork(PREFIX);
        } else {
            String requestId = request.getHeader(UowConstants.REQUEST_ID_HEADER);
            uowData = UowHelper.setExistingUnitOfWork(uowHeader, requestId);
        }
        if (LOGGER.isDebugEnabled()) {
            LOGGER.debug("CURRENT X-UOW HEADER: " + uowData);
        }

        return true;
    }


    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
        throws Exception {
        UowHelper.clearUnitOfWork();
        super.afterCompletion(request, response, handler, ex);
    }



}
