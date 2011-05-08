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
import java.awt.geom.PathIterator;
import java.awt.geom.Point2D;
/**
 * Used in conjunction with more complex paths under CompoundPath, this
 * class provides a way to do simple operations like MOVETO and CLOSE.
 */
public class SegmentPath implements PathIterator {
    private int ret;
    private int wind;
    private boolean done;
    private double[] coords;
    public SegmentPath(int r, double x1, double y1, double x2, double y2, 
	double x3, double y3, int w) 
    {
	done = false;
	ret = r;
	coords = new double[6];
	coords[0] = x1;
	coords[1] = y1;
	coords[2] = x2;
	coords[3] = y2;
	coords[4] = x3;
	coords[5] = y3;
	wind = w;
    }
    public int getWindingRule() {
	return wind;
    }
    public boolean isDone() {
	return done;
    }
    public void next() {
	done = true;
    }
    public int currentSegment(double[] c) {
	System.arraycopy(coords, 0, c, 0, 6);
	return ret;
    }
    public int currentSegment(float[] c) {
	for(int i = 0; i < 6; ++i)
	    c[i] = (float) coords[i];
	return ret;
    }
    public static PathIterator close() {
	return new SegmentPath(PathIterator.SEG_CLOSE, 0, 0, 0, 0, 0, 0,
	    PathIterator.WIND_EVEN_ODD);
    }
    public static PathIterator closeNz() {
	return new SegmentPath(PathIterator.SEG_CLOSE, 0, 0, 0, 0, 0, 0,
	    PathIterator.WIND_NON_ZERO);
    }
    public static PathIterator moveto(double x, double y) {
	return new SegmentPath(PathIterator.SEG_MOVETO, x, y, 0, 0, 0, 0,
	    PathIterator.WIND_EVEN_ODD);
    }
    public static PathIterator movetoNz(double x, double y) {
	return new SegmentPath(PathIterator.SEG_MOVETO, x, y, 0, 0, 0, 0,
	    PathIterator.WIND_NON_ZERO);
    }
    public static PathIterator moveto(Point2D p) {
	return moveto(p.getX(), p.getY());
    }
    public static PathIterator movetoNz(Point2D p) {
	return movetoNz(p.getX(), p.getY());
    }
    public static PathIterator lineto(double x, double y) {
	return new SegmentPath(PathIterator.SEG_LINETO, x, y, 0, 0, 0, 0,
	    PathIterator.WIND_EVEN_ODD);
    }
    public static PathIterator linetoNz(double x, double y) {
	return new SegmentPath(PathIterator.SEG_LINETO, x, y, 0, 0, 0, 0,
	    PathIterator.WIND_NON_ZERO);
    }
    public static PathIterator lineto(Point2D p) {
	return lineto(p.getX(), p.getY());
    }
    public static PathIterator linetoNz(Point2D p) {
	return linetoNz(p.getX(), p.getY());
    }
}
