/*
    Copyright 2009 Benjamin Samuel. All rights reserved.
    This file is part of VisualExpressionBuilder, a component for the
    Swing UI for Java. Java is a trademark of Sun.

    VisualExpressionBuilder is free software: you can redistribute it and/or
    modify it under the terms of the GNU General Public License as published
    by the Free Software Foundation, either version 3 of the License, or (at
    your option) any later version.

    VisualExpressionBuilder is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU General
    Public License for more details.
    
    You should have received a copy of the GNU General Public License along
    with VisualExpressionBuilder, in the file COPYING in the root directory of
    the distribution. If not, see <http://www.gnu.org/licenses/>.
 */

package veb.swing;
import java.awt.geom.Point2D;
import java.awt.geom.PathIterator;
import java.util.LinkedList;
import java.util.ListIterator;
import java.util.Collection;
import java.util.AbstractCollection;
import java.util.Iterator;
/**
 * Describes a path that can be iterated forwards or backwards.
 */
public class ReversiblePathFactory {
    private LinkedList<double[]> points;
    private static final boolean debuggery = false;
    public ReversiblePathFactory() {
	points = new LinkedList<double[]>();
    }
    public ReversiblePathFactory(ReversiblePathFactory copy) {
	points = new LinkedList<double[]>(copy.points);
    }
    public void appendPinch() {
	points.add(new double[0]);
    }
    public void prependPinch() {
	points.add(0, new double[0]);
    }
    public void appendPoint(Point2D p) {
	appendPoint(p.getX(), p.getY());
    }
    public void appendPoint(double x, double y) {
	double[] pa = new double[2];
	pa[0] = x;
	pa[1] = y;
	if(debuggery) write("append", pa);
	points.add(pa);
    }
    public void appendTriplet(Point2D fcp, Point2D p, Point2D scp) {
	appendTriplet(fcp.getX(), fcp.getY(), p.getX(), p.getY(),
	    scp.getX(), scp.getY());
    }
    public void appendTriplet(double fcpx, double fcpy, double px, double py,
	double scpx, double scpy)
    {
	double[] pa = new double[6];
	pa[0] = fcpx; pa[1] = fcpy;
	pa[2] = px; pa[3] = py;
	pa[4] = scpx; pa[5] = scpy;
	if(debuggery) write("append", pa);
	points.add(pa);
    }
    public void appendSymmetricTriplet(Point2D p, double dx, double dy) {
	appendTriplet(p.getX() - dx, p.getY() - dy, p.getX(), p.getY(),
	    p.getX() + dx, p.getY() + dy);
    }
    public void prependSymmetricTriplet(Point2D p, double dx, double dy) {
	prependTriplet(p.getX() - dx, p.getY() - dy, p.getX(), p.getY(),
	    p.getX() + dx, p.getY() + dy);
    }
    public void appendSymmetricTriplet(double px, double py, 
	double dx, double dy) {
	appendTriplet(px - dx, py - dy, px, py, px + dx, py + dy);
    }
    public void prependSymmetricTriplet(double px, double py, 
	double dx, double dy) {
	prependTriplet(px - dx, py - dy, px, py, px + dx, py + dy);
    }
    public void prependPoint(Point2D p) {
	prependPoint(p.getX(), p.getY());
    }
    public void prependPoint(double x, double y) {
	double[] pa = new double[2];
	pa[0] = x;
	pa[1] = y;
	points.add(0, pa);
	if(debuggery) write("prepend", pa);
    }
    public void prependTriplet(Point2D fcp, Point2D p, Point2D scp) {
	prependTriplet(fcp.getX(), fcp.getY(), p.getX(), p.getY(),
	    scp.getX(), scp.getY());
    }
    public void prependTriplet(double fcpx, double fcpy, double px, double py,
	double scpx, double scpy)
    {
	double[] pa = new double[6];
	pa[0] = fcpx; pa[1] = fcpy;
	pa[2] = px; pa[3] = py;
	pa[4] = scpx; pa[5] = scpy;
	points.add(0, pa);
	if(debuggery) write("prepend", pa);
    }
    public void append(ReversiblePathFactory rp) {
	points.addAll(rp.points);
    }
    public void appendReverse(ReversiblePathFactory rp) {
	points.addAll(rp.pointsReversed());
    }
    public void prepend(ReversiblePathFactory rp) {
	points.addAll(0, rp.points);
    }
    public void prependReverse(ReversiblePathFactory rp) {
	points.addAll(0, rp.pointsReversed());
    }
    private Collection<double[]> pointsReversed() {
	return new AbstractCollection<double[]>() {
	    public int size() { return points.size(); }
	    public Iterator<double[]> iterator() {
		final ListIterator<double[]> li =
		    points.listIterator(points.size());
		return new Iterator<double[]>() {
		    private ListIterator<double[]> it;
		    
		    public boolean hasNext() { return it.hasPrevious(); }
		    public double[] next() { return it.previous(); }
		    public void remove() {}
		};
	    }
	};
    }
    public PathIterator forwardPath(int winding) {
	return new ReversiblePath(points.listIterator(0), true, winding);
    }
    public PathIterator reversePath(int winding) {
	return new ReversiblePath(points.listIterator(points.size()), false, winding);
    }
    public PathIterator forwardPath() {
	return forwardPath(PathIterator.WIND_NON_ZERO);
    }
    public PathIterator reversePath() {
	return reversePath(PathIterator.WIND_NON_ZERO);
    }
    private static void write(String s, double[] d) {
	write(s, d, d == null ? 0 : d.length);
    }
    private static void write(String s, double[] d, int x) {
	StringBuffer sb = new StringBuffer();
	sb.append(s);
	if(d == null) {
	    sb.append(" null");
	} else {
	    for(int i = 0; i < x; i += 2)
		sb.append(" <" + d[i] + ", " + d[i + 1] + ">");
	}
	System.out.println(sb);
    }
    private static void write(String s, float[] d) {
	write(s, d, d == null ? 0 : d.length);
    }
    private static void write(String s, float[] d, int x) {
	StringBuffer sb = new StringBuffer();
	sb.append(s);
	if(d == null) {
	    sb.append(" null");
	} else {
	    for(int i = 0; i < x; i += 2)
		sb.append(" <" + d[i] + ", " + d[i + 1] + ">");
	}
	System.out.println(sb);
    }
    private class ReversiblePath implements PathIterator {
	public ReversiblePath(ListIterator<double[]> iter, 
	    boolean forward, int winding) 
	{
	    if(debuggery) System.out.println("constructor");
	    it = iter;
	    d = forward;
	    cur = null;
	    prior = null;
	    next();
	    w = winding;
	}
	ListIterator<double[]> it;
	double[] prior;
	double[] cur;
	boolean d;
	int w;
	public int currentSegment(double[] c) {
	    if(debuggery) System.out.println("currentSegment float");
	    switch(cur == null ? 0 : cur.length) {
	    case 0:
		if(debuggery) System.out.println("close");
		return PathIterator.SEG_CLOSE;
	    case 2:
		switch(prior == null ? 0 : prior.length) {
		case 0:
		case 2:
		    c[0] = cur[0];
		    c[1] = cur[1];
		    if(debuggery) write("lineto", c, 2);
		    return PathIterator.SEG_LINETO;
		case 6:
		    c[0] = prior[d ? 4 : 0];
		    c[1] = prior[d ? 5 : 1];
		    c[2] = cur[0];
		    c[3] = cur[1];
		    if(debuggery) write("quadto", c, 4);
		    return PathIterator.SEG_QUADTO;
		default:
		    throw new RuntimeException("Malformed ReversiblePath");
		}
	    case 6:
		switch(prior == null ? 0 : prior.length) {
		case 0:
		case 2:
		    c[0] = cur[d ? 0 : 4];
		    c[1] = cur[d ? 1 : 5];
		    c[2] = cur[2];
		    c[3] = cur[3];
		    if(debuggery) write("quadto", c, 4);
		    return PathIterator.SEG_QUADTO;
		case 6:
		    c[0] = prior[d ? 4 : 0];
		    c[1] = prior[d ? 5 : 1];
		    c[2] = cur[d ? 0 : 4];
		    c[3] = cur[d ? 1 : 5];
		    c[4] = cur[2];
		    c[5] = cur[3];
		    if(debuggery) write("cubeto", c, 6);
		    return PathIterator.SEG_CUBICTO;
		default:
		    throw new RuntimeException("Malformed ReversiblePath");
		}
	    default:
		throw new RuntimeException("Malformed ReversiblePath");
	    }
	}
	public int currentSegment(float[] c) {
	    if(debuggery) System.out.println("currentSegment float");
	    switch(cur == null ? 0 : cur.length) {
	    case 0:
		if(debuggery) System.out.println("close");
		return PathIterator.SEG_CLOSE;
	    case 2:
		switch(prior == null ? 0 : prior.length) {
		case 0:
		case 2:
		    c[0] = (float) cur[0];
		    c[1] = (float) cur[1];
		    if(debuggery) write("lineto", c, 2);
		    return PathIterator.SEG_LINETO;
		case 6:
		    c[0] = (float) prior[d ? 4 : 0];
		    c[1] = (float) prior[d ? 5 : 1];
		    c[2] = (float) cur[0];
		    c[3] = (float) cur[1];
		    if(debuggery) write("quadto", c, 4);
		    return PathIterator.SEG_QUADTO;
		default:
		    throw new RuntimeException("Malformed ReversiblePath");
		}
	    case 6:
		switch(prior == null ? 0 : prior.length) {
		case 0:
		case 2:
		    c[0] = (float) cur[d ? 0 : 4];
		    c[1] = (float) cur[d ? 1 : 5];
		    c[2] = (float) cur[2];
		    c[3] = (float) cur[3];
		    if(debuggery) write("quadto", c, 4);
		    return PathIterator.SEG_QUADTO;
		case 6:
		    c[0] = (float) prior[d ? 4 : 0];
		    c[1] = (float) prior[d ? 5 : 1];
		    c[2] = (float) cur[d ? 0 : 4];
		    c[3] = (float) cur[d ? 1 : 5];
		    c[4] = (float) cur[2];
		    c[5] = (float) cur[3];
		    if(debuggery) write("cubeto", c, 6);
		    return PathIterator.SEG_CUBICTO;
		default:
		    throw new RuntimeException("Malformed ReversiblePath");
		}
	    default:
		throw new RuntimeException("Malformed ReversiblePath");
	    }
	}
	public void next() {
	    if(debuggery) System.out.println("next");
	    prior = cur;
	    if(d)
		cur = it.hasNext() ? it.next() : null;
	    else
		cur = it.hasPrevious() ? it.previous() : null;
	}
	public boolean isDone() {
	    if(debuggery) System.out.println("isDone");
	    return cur == null;
	}
	public int getWindingRule() {
	    if(debuggery) System.out.println("getWindingRule");
	    return w;
	}
    } // private class ReversiblePathIterator
} // public class ReversiblePath
