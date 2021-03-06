package org.apache.commons.lang.exception;

public class CloneFailedException extends NestableRuntimeException {
    private static final long serialVersionUID = 20091223;

    public CloneFailedException(String message) {
        super(message);
    }

    public CloneFailedException(Throwable cause) {
        super(cause);
    }

    public CloneFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
