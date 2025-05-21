package com.zenyte.utils.efficientarea;

import java.awt.geom.AffineTransform;
import java.awt.geom.PathIterator;
import java.util.NoSuchElementException;

class AreaIterator implements PathIterator {
    private final AffineTransform transform;
    private final Vector<Curve> curves;
    private int index;
    private Curve prevcurve;
    private Curve thiscurve;

    public AreaIterator(Vector<Curve> curves, AffineTransform at) {
        this.curves = curves;
        this.transform = at;
        if (curves.size() >= 1) {
            thiscurve = curves.get(0);
        }
    }

    public int getWindingRule() {
        // REMIND: Which is better, EVEN_ODD or NON_ZERO?
        //         The paths calculated could be classified either way.
        //return WIND_EVEN_ODD;
        return WIND_NON_ZERO;
    }

    public boolean isDone() {
        return (prevcurve == null && thiscurve == null);
    }

    public void next() {
        if (prevcurve != null) {
            prevcurve = null;
        } else {
            prevcurve = thiscurve;
            index++;
            if (index < curves.size()) {
                thiscurve = curves.get(index);
                if (thiscurve.getOrder() != 0 &&
                        prevcurve.getX1() == thiscurve.getX0() &&
                        prevcurve.getY1() == thiscurve.getY0()) {
                    prevcurve = null;
                }
            } else {
                thiscurve = null;
            }
        }
    }

    public int currentSegment(float[] coords) {
        double[] dcoords = new double[6];
        int segtype = currentSegment(dcoords);
        int numpoints = (segtype == SEG_CLOSE ? 0
                : (segtype == SEG_QUADTO ? 2
                : (segtype == SEG_CUBICTO ? 3
                : 1)));
        for (int i = 0; i < numpoints * 2; i++) {
            coords[i] = (float) dcoords[i];
        }
        return segtype;
    }

    public int currentSegment(double[] coords) {
        int segtype;
        int numpoints;
        if (prevcurve != null) {
            // Need to finish off junction between curves
            if (thiscurve == null || thiscurve.getOrder() == 0) {
                return SEG_CLOSE;
            }
            coords[0] = thiscurve.getX0();
            coords[1] = thiscurve.getY0();
            segtype = SEG_LINETO;
            numpoints = 1;
        } else if (thiscurve == null) {
            throw new NoSuchElementException("area iterator out of bounds");
        } else {
            segtype = thiscurve.getSegment(coords);
            numpoints = thiscurve.getOrder();
            if (numpoints == 0) {
                numpoints = 1;
            }
        }
        if (transform != null) {
            transform.transform(coords, 0, coords, 0, numpoints);
        }
        return segtype;
    }
}
