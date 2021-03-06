package mf.org.apache.xerces.impl.dtd.models;

public abstract class CMNode {
    private boolean fCompactedForUPA;
    private CMStateSet fFirstPos;
    private CMStateSet fFollowPos;
    private CMStateSet fLastPos;
    private int fMaxStates;
    private final int fType;

    protected abstract void calcFirstPos(CMStateSet cMStateSet);

    protected abstract void calcLastPos(CMStateSet cMStateSet);

    public abstract boolean isNullable();

    public CMNode(int type) {
        this.fFirstPos = null;
        this.fFollowPos = null;
        this.fLastPos = null;
        this.fMaxStates = -1;
        this.fCompactedForUPA = false;
        this.fType = type;
    }

    public final int type() {
        return this.fType;
    }

    public final CMStateSet firstPos() {
        if (this.fFirstPos == null) {
            this.fFirstPos = new CMStateSet(this.fMaxStates);
            calcFirstPos(this.fFirstPos);
        }
        return this.fFirstPos;
    }

    public final CMStateSet lastPos() {
        if (this.fLastPos == null) {
            this.fLastPos = new CMStateSet(this.fMaxStates);
            calcLastPos(this.fLastPos);
        }
        return this.fLastPos;
    }

    final void setFollowPos(CMStateSet setToAdopt) {
        this.fFollowPos = setToAdopt;
    }

    public final void setMaxStates(int maxStates) {
        this.fMaxStates = maxStates;
    }

    public boolean isCompactedForUPA() {
        return this.fCompactedForUPA;
    }

    public void setIsCompactUPAModel(boolean value) {
        this.fCompactedForUPA = value;
    }
}
