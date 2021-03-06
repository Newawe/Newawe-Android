package org.apache.http.impl.io;

import java.io.IOException;
import java.io.InterruptedIOException;
import java.net.Socket;
import mf.org.w3c.dom.traversal.NodeFilter;
import org.apache.http.io.EofSensor;
import org.apache.http.params.HttpParams;

public class SocketInputBuffer extends AbstractSessionInputBuffer implements EofSensor {
    private static final Class SOCKET_TIMEOUT_CLASS;
    private boolean eof;
    private final Socket socket;

    static {
        SOCKET_TIMEOUT_CLASS = SocketTimeoutExceptionClass();
    }

    private static Class SocketTimeoutExceptionClass() {
        try {
            return Class.forName("java.net.SocketTimeoutException");
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    private static boolean isSocketTimeoutException(InterruptedIOException e) {
        if (SOCKET_TIMEOUT_CLASS != null) {
            return SOCKET_TIMEOUT_CLASS.isInstance(e);
        }
        return true;
    }

    public SocketInputBuffer(Socket socket, int buffersize, HttpParams params) throws IOException {
        if (socket == null) {
            throw new IllegalArgumentException("Socket may not be null");
        }
        this.socket = socket;
        this.eof = false;
        if (buffersize < 0) {
            buffersize = socket.getReceiveBufferSize();
        }
        if (buffersize < NodeFilter.SHOW_DOCUMENT_FRAGMENT) {
            buffersize = NodeFilter.SHOW_DOCUMENT_FRAGMENT;
        }
        init(socket.getInputStream(), buffersize, params);
    }

    protected int fillBuffer() throws IOException {
        int i = super.fillBuffer();
        this.eof = i == -1;
        return i;
    }

    public boolean isDataAvailable(int timeout) throws IOException {
        boolean result = hasBufferedData();
        if (!result) {
            int oldtimeout = this.socket.getSoTimeout();
            try {
                this.socket.setSoTimeout(timeout);
                fillBuffer();
                result = hasBufferedData();
            } catch (InterruptedIOException e) {
                if (!isSocketTimeoutException(e)) {
                    throw e;
                }
            } finally {
                this.socket.setSoTimeout(oldtimeout);
            }
        }
        return result;
    }

    public boolean isEof() {
        return this.eof;
    }
}
