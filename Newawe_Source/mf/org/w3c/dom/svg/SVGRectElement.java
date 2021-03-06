package mf.org.w3c.dom.svg;

import mf.org.w3c.dom.events.EventTarget;

public interface SVGRectElement extends SVGElement, SVGTests, SVGLangSpace, SVGExternalResourcesRequired, SVGStylable, SVGTransformable, EventTarget {
    SVGAnimatedLength getHeight();

    SVGAnimatedLength getRx();

    SVGAnimatedLength getRy();

    SVGAnimatedLength getWidth();

    SVGAnimatedLength getX();

    SVGAnimatedLength getY();
}
