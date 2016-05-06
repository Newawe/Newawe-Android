package mf.org.apache.html.dom;

import mf.org.w3c.dom.html.HTMLIFrameElement;

public class HTMLIFrameElementImpl extends HTMLElementImpl implements HTMLIFrameElement {
    private static final long serialVersionUID = 2393622754706230429L;

    public String getAlign() {
        return capitalize(getAttribute("align"));
    }

    public void setAlign(String align) {
        setAttribute("align", align);
    }

    public String getFrameBorder() {
        return getAttribute("frameborder");
    }

    public void setFrameBorder(String frameBorder) {
        setAttribute("frameborder", frameBorder);
    }

    public String getHeight() {
        return getAttribute("height");
    }

    public void setHeight(String height) {
        setAttribute("height", height);
    }

    public String getLongDesc() {
        return getAttribute("longdesc");
    }

    public void setLongDesc(String longDesc) {
        setAttribute("longdesc", longDesc);
    }

    public String getMarginHeight() {
        return getAttribute("marginheight");
    }

    public void setMarginHeight(String marginHeight) {
        setAttribute("marginheight", marginHeight);
    }

    public String getMarginWidth() {
        return getAttribute("marginwidth");
    }

    public void setMarginWidth(String marginWidth) {
        setAttribute("marginwidth", marginWidth);
    }

    public String getName() {
        return getAttribute("name");
    }

    public void setName(String name) {
        setAttribute("name", name);
    }

    public String getScrolling() {
        return capitalize(getAttribute("scrolling"));
    }

    public void setScrolling(String scrolling) {
        setAttribute("scrolling", scrolling);
    }

    public String getSrc() {
        return getAttribute("src");
    }

    public void setSrc(String src) {
        setAttribute("src", src);
    }

    public String getWidth() {
        return getAttribute("width");
    }

    public void setWidth(String width) {
        setAttribute("width", width);
    }

    public HTMLIFrameElementImpl(HTMLDocumentImpl owner, String name) {
        super(owner, name);
    }
}