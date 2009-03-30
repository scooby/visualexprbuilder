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

public class SquigglyPath implements PathIterator {
    double x, y, l, p, wl, a, off, last_off;
    float m;
    boolean h;
    ExpressionUI ui;
    int segment;
    public SquigglyPath(double _x, double _y, boolean horizontal, 
	double length, double phase, ExpressionUI _ui, boolean moveto) {
	if(_ui == null)
	    throw new NullPointerException();
	phase = phase < 0.0 ?  1.0 - phase : phase;
	phase -= Math.floor(phase);
	p = phase;
	m = l < 0 ? -1.0 : 1.0;
	l = Math.abs(l);
	ui = _ui;
	x = _x; y = _y;
	wl = ui.squigglyWavelength();
	a = ui.squigglyAmplitude();
	last_off = 0.0;
	off = 0.0;
	update_off();
	segment = moveto ? -1 : 0;
	h = horizontal;
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
	    return PathIterator.MOVE_TO;
	}
	
	// Is this point above the centerline? The final point is on the
	// centerline.
	double up = off == l ? 0.0 : (p < 0.5 ? a : -a);
	// Was the previous point above the centerline? Since we just negate
	// up, we're really just checking for the first segment.
	double last_up = last_off == 0.0 ? 0.0 : -up;
	// Control points are halfway between points
	double ctrl_off = (off + last_off) / 2.0;
	// last point's second control point
	coords[0] = x + (h ? m * ctrl_off : last_up);
	coords[1] = y + (h ? last_up : m * ctrl_off);
	coords[2] = x + (h ? m * ctrl_off : up);
	coords[3] = y + (h ? up : m * ctrl_off);
	coords[4] = x + (h ? m * off : up);
	coords[5] = y + (h ? up : m * off);
	return PathIterator.CUBIC_TO;
    }
    public int currentSegment(float[] coords) {
	if(segment == -1) {
	    coords[0] = (float) x;
	    coords[1] = (float) y;
	    return PathIterator.MOVE_TO;
	}
	
	// Is this point above the centerline? The final point is on the
	// centerline.
	float up = off == l ? 0.0 : (p < 0.5 ? (float) a : (float) -a);
	// Was the previous point above the centerline? Since we just negate
	// up, we're really just checking for the first segment.
	float last_up = last_off == 0.0 ? 0.0 : (float) -up;
	// Control points are halfway between points
	float ctrl_off = (float) (off + last_off) / 2.0;
	// last point's second control point
	coords[0] = (float) x + (h ? m * ctrl_off : last_up);
	coords[1] = (float) y + (h ? last_up : m * ctrl_off);
	coords[2] = (float) x + (h ? m * ctrl_off : up);
	coords[3] = (float) y + (h ? up : m * ctrl_off);
	coords[4] = (float) x + (h ? m * off : up);
	coords[5] = (float) y + (h ? up : m * off);
	return PathIterator.CUBIC_TO;
    }
    public boolean isDone() {
	return off >= l;
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
      * Use this value as the starting phase for another line.
      */
    public double phase() {
	return p;
    }
}
