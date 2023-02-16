package com.hhwy.common.core.exception;

/**
 * 权限异常
 *
 * @author hhwy
 */
public class PreAuthorizeException extends RuntimeException {
    private static final long serialVersionUID = 1L;

    public PreAuthorizeException() {
    }

    public PreAuthorizeException(String msg) {
        super(msg);
    }
}
