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
import java.awt.geom.Rectangle2D;


public class NibPath {
	public static ReversiblePathFactory rpf(final Rectangle2D r, final int bits)
	{
		return rpf(r.getX(), r.getY(), r.getWidth(), r.getHeight(),
				bits);
	}
	public static ReversiblePathFactory rpf(final double x, final double y, final double w,
			final double h, int bits)
	{
		//System.out.println("RPF called with " + x + ", " + y + ", " + w + ", " + h);
		final ReversiblePathFactory f = new ReversiblePathFactory();
		/* A nib is roughly an hourglass shape with a rounded
	    bottom:

	     x------------------>x+w
	    y1     O       O     7
	    | •••  |       |  •••
	    |   •••|       |•••
	    |      2       6
	    |     •|       |•
	    |  O • |       | • O
	    |  | • O       O • |
	    |  |•             •|
	    |  3               5
	    |  |••••       ••••|
	    V  |   •••• ••••   |
	  y+h  O  O----4----O  O

	The control points are all just vertical, except for
	control points on point 4 which are horizontal
	Points 2, 3, 5 and 6 can be moved. Any control points
	must be within the area to avoid twists.

	We can get nice nib variation by just moving points
	within a square area. We have 32 bits for 4 points,
	so 4 bits of variance per dimension per point..

	Point     Area
	1         0.0, 0.0 : 0.0, 0.0
	 cp      -0.1,-0.1 : 0.1, 0.1
	2         0.1, 0.2 : 0.4, 0.4
	 cp       0.0 -0.2   0.0 +0.2
	3         0.0, 0.6 : 0.3, 0.9
	 cp       0.0 -0.2   0.0 +0.2
	4         0.5, 1.0 : 0.5, 1.0
	 cp      -0.3  0.0  +0.3  0.0
	5         0.7, 0.6 : 1.0, 0.9
	 cp       0.0 +0.3   0.0 -0.3
	6         0.6, 0.2 : 0.9, 0.4
	 cp       0.0 +0.2   0.0 -0.2
	7         1.0, 0.0 : 1.0, 0.0
		 */
		f.appendTriplet(x, y, x, y, x + w * 0.1, y + h * 0.1); // point 1
		double xb = (bits & 15) / 15.0, yb = (bits >>> 4 & 15) / 15.0;
		bits = bits >>> 8;
		f.appendSymmetricTriplet( // point 2
				x + w * (0.1 + 0.3 * xb), y + h * (0.2 + 0.2 * yb),
				0.0, 0.2 * h);
		xb = (bits & 15) / 15.0; yb = (bits >>> 4 & 15) / 15.0;
		bits = bits >>> 8;
		f.appendSymmetricTriplet( // point 3
				x + w * (0.0 + 0.3 * xb), y + h * (0.6 + 0.3 * yb),
				0.0, 0.3 * h);
		f.appendSymmetricTriplet( // point 4
				x + w * 0.5, y + h * 1.0,
				0.3 * w, 0.0);
		xb = (bits & 15) / 15.0; yb = (bits >>> 4 & 15) / 15.0;
		bits = bits >>> 8;
		f.appendSymmetricTriplet( // point 5
				x + w * (0.7 + 0.3 * xb), y + h * (0.6 + 0.3 * yb),
				0.0, -0.3 * h);
		xb = (bits & 15) / 15.0; yb = (bits >>> 4 & 15) / 15.0;
		bits = bits >>> 8;
		f.appendSymmetricTriplet( // point 6
				x + w * (0.6 + 0.3 * xb), y + h * (0.2 + 0.2 * yb),
				0.0, -0.2 * h);
		f.appendTriplet(x + w * 0.9, y + h * 0.1, x + w, y, x + w, y); // point 7
		//System.out.println("RPF constructed");
		return f;
	}
}
