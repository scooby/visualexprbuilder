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
	private final int ret;
	private final int wind;
	private final double[] coords;
	private boolean done;
	public SegmentPath(final int r, final double x1, final double y1, final double x2, final double y2,
			final double x3, final double y3, final int w)
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
	@Override
	public int getWindingRule() {
		return wind;
	}
	@Override
	public boolean isDone() {
		return done;
	}
	@Override
	public void next() {
		done = true;
	}
	@Override
	public int currentSegment(final double[] c) {
		System.arraycopy(coords, 0, c, 0, 6);
		return ret;
	}
	@Override
	public int currentSegment(final float[] c) {
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
	public static PathIterator moveto(final double x, final double y) {
		return new SegmentPath(PathIterator.SEG_MOVETO, x, y, 0, 0, 0, 0,
				PathIterator.WIND_EVEN_ODD);
	}
	public static PathIterator movetoNz(final double x, final double y) {
		return new SegmentPath(PathIterator.SEG_MOVETO, x, y, 0, 0, 0, 0,
				PathIterator.WIND_NON_ZERO);
	}
	public static PathIterator moveto(final Point2D p) {
		return moveto(p.getX(), p.getY());
	}
	public static PathIterator movetoNz(final Point2D p) {
		return movetoNz(p.getX(), p.getY());
	}
	public static PathIterator lineto(final double x, final double y) {
		return new SegmentPath(PathIterator.SEG_LINETO, x, y, 0, 0, 0, 0,
				PathIterator.WIND_EVEN_ODD);
	}
	public static PathIterator linetoNz(final double x, final double y) {
		return new SegmentPath(PathIterator.SEG_LINETO, x, y, 0, 0, 0, 0,
				PathIterator.WIND_NON_ZERO);
	}
	public static PathIterator lineto(final Point2D p) {
		return lineto(p.getX(), p.getY());
	}
	public static PathIterator linetoNz(final Point2D p) {
		return linetoNz(p.getX(), p.getY());
	}
}
