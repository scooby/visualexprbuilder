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
import java.awt.geom.Point2D;

public class SquigglyPath implements PathIterator {
    double x, y, l, p, wl, a, off, last_off, xm, ym;
    int segment;
    public SquigglyPath(Point2D from, Point2D to, double phase, 
	double wavelength, double amplitude)
    {
	phase = phase < 0.0 ?  1.0 - phase : phase;
	phase -= Math.floor(phase);
	p = phase;
	l = from.distance(to);
	x = from.getX(); y = from.getY();
	xm = (to.getX() - from.getX()) / l;
	ym = (to.getY() - from.getY()) / l;
	wl = wavelength; a = amplitude;
	last_off = 0.0;
	off = 0.0;
	update_off();
	segment = -1; // moveto ? -1 : 0;
    }
    /** 
	Handling phase and number of segments
	At offset 0, the phase starts at "phase". So each point is at real
	phase 0.5 or 0.0
	So if phase is 0.2, the next point will be at offset 0.3
	
	This is called before each point, so it is first called by 
	constructor.
    */
    private void update_off() {
	if(off == l) {
	    off += 1;
	    return;
	}
	// phase is a multiplier of wavelength
	double i = wl * ((p < 0.5 ? 0.5 : 1.0) - p);
	// Check if we're running out of length
	if(off + i > l) {
	    // adjust phase for the finalPhase function
	    p += (l - off) / wl;
	    off = l;
	} else {
	    p = p < 0.5 ? 0.5 : 0.0;
	    off += i;
	}
    }
    /*
	Path diagram
	
	 O--*--O       O--*--O       O--*--O
	*      ••     ••     ••     ••     ••      *
		O--*--O       O--*--O       O--*--O
	<-wavelength--><-wavelength-><-wavelength-->
	
	Given the line from start to finish, each point is simply squigglyAmplitude (from the ui object) above or below the center line.
	For simplicity, we always start and finish on the centerline.
	The .. indicate where the line would cross the centerline.
	
	A wavelength goes from phase 0, inclusive to 1, exclusive..
	0: control point for phase .25 at centerline + amplitude
	    control point for phase -0.25 at centerline - amplitude
	0.25: point at centerline + amplitude
	0.5: control point for phase .25 at centerline + amplitude
	    control point for phase .75 at centerline - amplitude
	0.75: point at centerline - amplitude
	
	UPDATE: I'm doing points at 0 and 0.5, to simplify the arithmetic a little.
    */
    
    public int currentSegment(double[] coords) {
	if(segment == -1) {
	    coords[0] = x;
	    coords[1] = y;
	    return PathIterator.SEG_LINETO;
	}
	if(off > l) {
	    coords[0] = x + xm * l;
	    coords[1] = y + ym * l;
	    return PathIterator.SEG_LINETO;
	}
	
	// Is this point above the centerline? The final point is on the
	// centerline.
	double up = off == l ? 0.0 : (p < 0.5 ? a : -a);
	// Was the previous point above the centerline? Since we just negate
	// up, we're really just checking for the first segment.
	double last_up = last_off == 0.0 ? 0.0 : -up;
	// Control points are halfway between points
	double ctrl_off = (off + last_off) / 2.0;
	// To get 90 degrees left from our main:
	double xn = -ym;
	double yn = xm;
	// last point's second control point
	coords[0] = x + xm * ctrl_off + xn * last_up;	
	coords[1] = y + ym * ctrl_off + yn * last_up;
	coords[2] = x + xm * ctrl_off + xn * up;
	coords[3] = y + ym * ctrl_off + yn * up;
	coords[4] = x + xm * off + xn * up;
	coords[5] = y + ym * off + yn * up;
	return PathIterator.SEG_CUBICTO;
    }
    public int currentSegment(float[] coords) {
	if(segment == -1) {
	    coords[0] = (float) x;
	    coords[1] = (float) y;
	    return PathIterator.SEG_LINETO;
	}
	if(off > l) {
	    coords[0] = (float) (x + xm * l);
	    coords[1] = (float) (y + ym * l);
	    return PathIterator.SEG_LINETO;
	}
	
	// Is this point above the centerline? The final point is on the
	// centerline.
	float up = off == l ? 0.0f : ((float) p < 0.5f ? (float) a : (float) -a);
	// Was the previous point above the centerline? Since we just negate
	// up, we're really just checking for the first segment.
	float last_up = last_off == 0.0f ? 0.0f : (float) -up;
	// Control points are halfway between points
	float ctrl_off = (float) (off + last_off) / 2.0f;
	// To get 90 degrees left from our main:
	float xn = (float) -ym;
	float yn = (float) xm;
	// last point's second control point
	coords[0] = (float) x + (float) xm * ctrl_off + xn * last_up;	
	coords[1] = (float) y + (float) ym * ctrl_off + yn * last_up;
	coords[2] = (float) x + (float) xm * ctrl_off + xn * up;
	coords[3] = (float) y + (float) ym * ctrl_off + yn * up;
	coords[4] = (float) x + (float) xm * (float) off + xn * up;
	coords[5] = (float) y + (float) ym * (float) off + yn * up;
	return PathIterator.SEG_CUBICTO;
    }
    public boolean isDone() {
	return segment >= 0 && off > l;
    }
    public void next() {
	if(segment >= 0) {
	    last_off = off;
	    update_off();
	}
	segment++;
	return;
    }
    public int getWindingRule() {
	return PathIterator.WIND_NON_ZERO;
    }
    /**
      * Calculate the starting phase after a length.
      */
    public static double nextPhase(double p, double wl, double l) {
	p += Math.abs(l) / wl;
	p = p < 0.0 ?  1.0 - p : p;
	p -= Math.floor(p);
	return p;
    }
}
