package org.apache.commons.lang.exception;

import java.io.PrintStream;
import java.io.PrintWriter;

public class NestableError extends Error implements Nestable {
    private static final long serialVersionUID = 1;
    private Throwable cause;
    protected NestableDelegate delegate;

    public NestableError() {
        this.delegate = new NestableDelegate(this);
        this.cause = null;
    }

    public NestableError(String msg) {
        super(msg);
        this.delegate = new NestableDelegate(this);
        this.cause = null;
    }

    public NestableError(Throwable cause) {
        this.delegate = new NestableDelegate(this);
        this.cause = null;
        this.cause = cause;
    }

    public NestableError(String msg, Throwable cause) {
        super(msg);
        this.delegate = new NestableDelegate(this);
        this.cause = null;
        this.cause = cause;
    }

    public Throwable getCause() {
        return this.cause;
    }

    public String getMessage() {
        if (super.getMessage() != null) {
            return super.getMessage();
        }
        if (this.cause != null) {
            return this.cause.toString();
        }
        return null;
    }

    public String getMessage(int index) {
        if (index == 0) {
            return super.getMessage();
        }
        return this.delegate.getMessage(index);
    }

    public String[] getMessages() {
        return this.delegate.getMessages();
    }

    public Throwable getThrowable(int index) {
        return this.delegate.getThrowable(index);
    }

    public int getThrowableCount() {
        return this.delegate.getThrowableCount();
    }

    public Throwable[] getThrowables() {
        return this.delegate.getThrowables();
    }

    public int indexOfThrowable(Class type) {
        return this.delegate.indexOfThrowable(type, 0);
    }

    public int indexOfThrowable(Class type, int fromIndex) {
        return this.delegate.indexOfThrowable(type, fromIndex);
    }

    public void printStackTrace() {
        this.delegate.printStackTrace();
    }

    public void printStackTrace(PrintStream out) {
        this.delegate.printStackTrace(out);
    }

    public void printStackTrace(PrintWriter out) {
        this.delegate.printStackTrace(out);
    }

    public final void printPartialStackTrace(PrintWriter out) {
        super.printStackTrace(out);
    }
}
