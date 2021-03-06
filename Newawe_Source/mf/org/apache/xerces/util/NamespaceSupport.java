package mf.org.apache.xerces.util;

import java.util.Enumeration;
import java.util.NoSuchElementException;
import mf.org.apache.xerces.xni.NamespaceContext;

public class NamespaceSupport implements NamespaceContext {
    protected int[] fContext;
    protected int fCurrentContext;
    protected String[] fNamespace;
    protected int fNamespaceSize;
    protected String[] fPrefixes;

    protected final class Prefixes implements Enumeration {
        private int counter;
        private String[] prefixes;
        private int size;

        public Prefixes(String[] prefixes, int size) {
            this.counter = 0;
            this.size = 0;
            this.prefixes = prefixes;
            this.size = size;
        }

        public boolean hasMoreElements() {
            return this.counter < this.size;
        }

        public Object nextElement() {
            if (this.counter < this.size) {
                String[] strArr = NamespaceSupport.this.fPrefixes;
                int i = this.counter;
                this.counter = i + 1;
                return strArr[i];
            }
            throw new NoSuchElementException("Illegal access to Namespace prefixes enumeration.");
        }

        public String toString() {
            StringBuffer buf = new StringBuffer();
            for (int i = 0; i < this.size; i++) {
                buf.append(this.prefixes[i]);
                buf.append(' ');
            }
            return buf.toString();
        }
    }

    public NamespaceSupport() {
        this.fNamespace = new String[32];
        this.fContext = new int[8];
        this.fPrefixes = new String[16];
    }

    public NamespaceSupport(NamespaceContext context) {
        this.fNamespace = new String[32];
        this.fContext = new int[8];
        this.fPrefixes = new String[16];
        pushContext();
        Enumeration prefixes = context.getAllPrefixes();
        while (prefixes.hasMoreElements()) {
            String prefix = (String) prefixes.nextElement();
            declarePrefix(prefix, context.getURI(prefix));
        }
    }

    public void reset() {
        this.fNamespaceSize = 0;
        this.fCurrentContext = 0;
        this.fContext[this.fCurrentContext] = this.fNamespaceSize;
        String[] strArr = this.fNamespace;
        int i = this.fNamespaceSize;
        this.fNamespaceSize = i + 1;
        strArr[i] = XMLSymbols.PREFIX_XML;
        strArr = this.fNamespace;
        i = this.fNamespaceSize;
        this.fNamespaceSize = i + 1;
        strArr[i] = NamespaceContext.XML_URI;
        strArr = this.fNamespace;
        i = this.fNamespaceSize;
        this.fNamespaceSize = i + 1;
        strArr[i] = XMLSymbols.PREFIX_XMLNS;
        strArr = this.fNamespace;
        i = this.fNamespaceSize;
        this.fNamespaceSize = i + 1;
        strArr[i] = NamespaceContext.XMLNS_URI;
        this.fCurrentContext++;
    }

    public void pushContext() {
        if (this.fCurrentContext + 1 == this.fContext.length) {
            int[] contextarray = new int[(this.fContext.length * 2)];
            System.arraycopy(this.fContext, 0, contextarray, 0, this.fContext.length);
            this.fContext = contextarray;
        }
        int[] iArr = this.fContext;
        int i = this.fCurrentContext + 1;
        this.fCurrentContext = i;
        iArr[i] = this.fNamespaceSize;
    }

    public void popContext() {
        int[] iArr = this.fContext;
        int i = this.fCurrentContext;
        this.fCurrentContext = i - 1;
        this.fNamespaceSize = iArr[i];
    }

    public boolean declarePrefix(String prefix, String uri) {
        if (prefix == XMLSymbols.PREFIX_XML || prefix == XMLSymbols.PREFIX_XMLNS) {
            return false;
        }
        for (int i = this.fNamespaceSize; i > this.fContext[this.fCurrentContext]; i -= 2) {
            if (this.fNamespace[i - 2] == prefix) {
                this.fNamespace[i - 1] = uri;
                return true;
            }
        }
        if (this.fNamespaceSize == this.fNamespace.length) {
            String[] namespacearray = new String[(this.fNamespaceSize * 2)];
            System.arraycopy(this.fNamespace, 0, namespacearray, 0, this.fNamespaceSize);
            this.fNamespace = namespacearray;
        }
        String[] strArr = this.fNamespace;
        int i2 = this.fNamespaceSize;
        this.fNamespaceSize = i2 + 1;
        strArr[i2] = prefix;
        strArr = this.fNamespace;
        i2 = this.fNamespaceSize;
        this.fNamespaceSize = i2 + 1;
        strArr[i2] = uri;
        return true;
    }

    public String getURI(String prefix) {
        for (int i = this.fNamespaceSize; i > 0; i -= 2) {
            if (this.fNamespace[i - 2] == prefix) {
                return this.fNamespace[i - 1];
            }
        }
        return null;
    }

    public String getPrefix(String uri) {
        int i = this.fNamespaceSize;
        while (i > 0) {
            if (this.fNamespace[i - 1] == uri && getURI(this.fNamespace[i - 2]) == uri) {
                return this.fNamespace[i - 2];
            }
            i -= 2;
        }
        return null;
    }

    public int getDeclaredPrefixCount() {
        return (this.fNamespaceSize - this.fContext[this.fCurrentContext]) / 2;
    }

    public String getDeclaredPrefixAt(int index) {
        return this.fNamespace[this.fContext[this.fCurrentContext] + (index * 2)];
    }

    public Enumeration getAllPrefixes() {
        int count = 0;
        if (this.fPrefixes.length < this.fNamespace.length / 2) {
            this.fPrefixes = new String[this.fNamespaceSize];
        }
        boolean unique = true;
        for (int i = 2; i < this.fNamespaceSize - 2; i += 2) {
            String prefix = this.fNamespace[i + 2];
            for (int k = 0; k < count; k++) {
                if (this.fPrefixes[k] == prefix) {
                    unique = false;
                    break;
                }
            }
            if (unique) {
                int count2 = count + 1;
                this.fPrefixes[count] = prefix;
                count = count2;
            }
            unique = true;
        }
        return new Prefixes(this.fPrefixes, count);
    }

    public boolean containsPrefix(String prefix) {
        for (int i = this.fNamespaceSize; i > 0; i -= 2) {
            if (this.fNamespace[i - 2] == prefix) {
                return true;
            }
        }
        return false;
    }
}
