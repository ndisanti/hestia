package com.despegar.p13n.hestia.api.service;

import java.util.List;

public class EulerConnectorException
    extends RuntimeException {

    /**
     * 
     */
    private static final long serialVersionUID = 8610707546713199178L;

    private String message;
    private List<EulerApiError> serverErrors;
    private Throwable cause;

    public EulerConnectorException(String msg, List<EulerApiError> serverErrors) {
        this.message = msg;
        this.serverErrors = serverErrors;
    }

    public EulerConnectorException(String msg, Throwable e) {
        this.message = msg;
        this.cause = e;
    }

    public EulerConnectorException(String msg) {
        this.message = msg;
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("EulerConnectorException: ").append(this.message).append(" => ");
        if (this.serverErrors != null && !this.serverErrors.isEmpty()) {
            builder.append("Server errors:");
            for (EulerApiError error : this.serverErrors) {
                builder.append("[(").append(error.getApiErrorCode()).append(" - ").append(error.getCode()).append(") ")
                    .append(error.getDescription()).append("], ");
            }
        }
        if (this.cause != null) {
            builder.append(" Caused by: ").append(this.cause.toString());
        }
        return builder.toString();
    }


}
