package org.apache.commons.lang.mutable;

import com.android.volley.DefaultRetryPolicy;
import org.apache.commons.lang.math.NumberUtils;

public class MutableFloat extends Number implements Comparable, Mutable {
    private static final long serialVersionUID = 5787169186L;
    private float value;

    public MutableFloat(float value) {
        this.value = value;
    }

    public MutableFloat(Number value) {
        this.value = value.floatValue();
    }

    public MutableFloat(String value) throws NumberFormatException {
        this.value = Float.parseFloat(value);
    }

    public Object getValue() {
        return new Float(this.value);
    }

    public void setValue(float value) {
        this.value = value;
    }

    public void setValue(Object value) {
        setValue(((Number) value).floatValue());
    }

    public boolean isNaN() {
        return Float.isNaN(this.value);
    }

    public boolean isInfinite() {
        return Float.isInfinite(this.value);
    }

    public void increment() {
        this.value += DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    }

    public void decrement() {
        this.value -= DefaultRetryPolicy.DEFAULT_BACKOFF_MULT;
    }

    public void add(float operand) {
        this.value += operand;
    }

    public void add(Number operand) {
        this.value += operand.floatValue();
    }

    public void subtract(float operand) {
        this.value -= operand;
    }

    public void subtract(Number operand) {
        this.value -= operand.floatValue();
    }

    public int intValue() {
        return (int) this.value;
    }

    public long longValue() {
        return (long) this.value;
    }

    public float floatValue() {
        return this.value;
    }

    public double doubleValue() {
        return (double) this.value;
    }

    public Float toFloat() {
        return new Float(floatValue());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MutableFloat) && Float.floatToIntBits(((MutableFloat) obj).value) == Float.floatToIntBits(this.value);
    }

    public int hashCode() {
        return Float.floatToIntBits(this.value);
    }

    public int compareTo(Object obj) {
        return NumberUtils.compare(this.value, ((MutableFloat) obj).value);
    }

    public String toString() {
        return String.valueOf(this.value);
    }
}
