package mf.javax.xml.xpath;

import java.io.PrintStream;
import java.io.PrintWriter;

public class XPathException extends Exception {
    private static final long serialVersionUID = -1837080260374986980L;
    private final Throwable cause;

    public XPathException(String message) {
        super(message);
        if (message == null) {
            throw new NullPointerException("message can't be null");
        }
        this.cause = null;
    }

    public XPathException(Throwable cause) {
        this.cause = cause;
        if (cause == null) {
            throw new NullPointerException("cause can't be null");
        }
    }

    public Throwable getCause() {
        return this.cause;
    }

    public void printStackTrace(PrintStream s) {
        if (getCause() != null) {
            getCause().printStackTrace(s);
            s.println("--------------- linked to ------------------");
        }
        super.printStackTrace(s);
    }

    public void printStackTrace() {
        printStackTrace(System.err);
    }

    public void printStackTrace(PrintWriter s) {
        if (getCause() != null) {
            getCause().printStackTrace(s);
            s.println("--------------- linked to ------------------");
        }
        super.printStackTrace(s);
    }
}
