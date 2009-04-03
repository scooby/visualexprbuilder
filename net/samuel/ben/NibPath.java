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

package net.samuel.ben;
import java.awt.geom.PathIterator;

public class NibPath implements PathIterator {
    double x, y, w, h;
    double c1x, c1y, c2x, c2y, px, py;
    int index;
    int bits;
    public NibPath(double _x, double _y, double _w, double _h, Class<?> c) {
	x = _x; y = _y;
	w = _w; h = _h;
	bits = c.hashCode();
	index = 2; // moveTo ? 1 : 2;
	setSeg();
    }
    public int getWindingRule() {
	return PathIterator.WIND_NON_ZERO;
    }
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
    2         0.1, 0.1 : 0.4, 0.4
     cp       0.0 -0.3   0.0 +0.3
    3         0.0, 0.6 : 0.3, 0.9
     cp       0.0 -0.3   0.0 +0.3
    4         0.5, 1.0 : 0.5, 1.0
     cp      -0.3  0.0  +0.3  0.0
    5         0.7, 0.6 : 1.0, 0.9
     cp       0.0 -0.3   0.0 +0.3
    6         0.6, 0.1 : 0.9, 0.4
     cp       0.0 -0.3   0.0 +0.3
    7         1.0, 0.0 : 1.0, 0.0
    */
    private void setSeg() {
	switch(index) {
	case 1:
	    c1x = px = x;
	    c1y = py = y;
	    return; // move to
	case 2:
	    px = c2x = x + w * (0.1 + 0.3 * (bits & 15) / 15.0);
	    py = c2y = y + h * (0.1 + 0.3 * ((bits >> 4) & 15) / 15.0);
	    c1x = px;
	    c1y = Math.max(y, py - 0.3 * h);
	    bits = bits >> 8;
	    return; // quad to
	case 3:
	    c1x = px;
	    c1y = Math.min(y + h, py + 0.3 * h);
	    px = x + w * (0.0 + 0.3 * (bits & 15) / 15.0);
	    py = y + h * (0.6 + 0.3 * ((bits >> 4) & 15) / 15.0);
	    c2x = px;
	    c2y = Math.max(y, py - 0.3 * h);
	    bits = bits >> 8;
	    return; // cubic to
	case 4:
	    c1x = px;
	    c1y = Math.min(y + h, py + 0.3 * h);
	    px = x + w * 0.5;
	    py = y + h * 1.0;
	    c2x = px - 0.3 * w;
	    c2y = py;
	    return; // cubic to
	case 5:
	    c1x = px + 0.3 * w;
	    c1y = py;
	    px = x + w * (0.7 + 0.3 * (bits & 15) / 15.0);
	    py = y + h * (0.6 + 0.3 * ((bits >> 4) & 15) / 15.0);
	    c2x = px;
	    c2y = Math.max(y, py - 0.3 * h);
	    bits = bits >> 8;
	    return; // cubic to
	case 6:
	    c1x = px;
	    c1y = Math.min(y + h, py + 0.3 * h);
	    px = x + w * (0.6 + 0.3 * (bits & 15) / 15.0);
	    py = y + h * (0.1 + 0.3 * ((bits >> 4) & 15) / 15.0);
	    c2x = px;
	    c2y = Math.max(y, py - 0.3 * h);
	    bits = bits >> 8;
	    // bits should be -1 or 0.
	    return; // cubic to
	case 7:
	    c1x = px;
	    c1y = Math.min(y + h, py + 0.3 * h);
	    c2x = x + w;
	    c2y = y;
	    return; // quad to
	default:
	}
    }
    public int currentSegment(double[] coords) {
	coords[0] = c1x;
	coords[1] = c1y;
	coords[2] = c2x;
	coords[3] = c2y;
	coords[4] = px;
	coords[5] = py;
	return index == 1 ? PathIterator.SEG_MOVETO : index == 2 || index == 7 ? PathIterator.SEG_QUADTO : PathIterator.SEG_CUBICTO;
    }
    public int currentSegment(float[] coords) {
	coords[0] = (float) c1x;
	coords[1] = (float) c1y;
	coords[2] = (float) c2x;
	coords[3] = (float) c2y;
	coords[4] = (float) px;
	coords[5] = (float) py;
	return index == 1 ? PathIterator.SEG_MOVETO : index == 2 || index == 7 ? PathIterator.SEG_QUADTO : PathIterator.SEG_CUBICTO;
    }
    public boolean isDone() {
	return index > 7;
    }
    public void next() {
	++index;
	setSeg();
    }
}
