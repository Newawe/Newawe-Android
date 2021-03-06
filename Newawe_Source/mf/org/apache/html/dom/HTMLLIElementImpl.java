package mf.org.apache.html.dom;

import mf.org.w3c.dom.html.HTMLLIElement;

public class HTMLLIElementImpl extends HTMLElementImpl implements HTMLLIElement {
    private static final long serialVersionUID = -8987309345926701831L;

    public String getType() {
        return getAttribute("type");
    }

    public void setType(String type) {
        setAttribute("type", type);
    }

    public int getValue() {
        return getInteger(getAttribute("value"));
    }

    public void setValue(int value) {
        setAttribute("value", String.valueOf(value));
    }

    public HTMLLIElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, name);
    }
}
