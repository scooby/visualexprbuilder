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
    double x, y, length, phase, wave, amplitude;
    boolean h;
    ExpressionUI ui;
    double offset;
    boolean hasClosed;
    boolean hasOpened;
    public SquigglyPath(double _x, double _y, boolean horizontal, 
	double _length, double _phase, ExpressionUI _ui) {
	if(_ui == null)
	    throw new NullPointerException();
	if(phase < 0.0 || phase >= 1.0)
	    phase = 0.0; // throw new IllegalArgumentException("Phase must be from 0.0 inclusive to 1.0 exclusive.");
	if(length < 0.0)
	    length = 0.0; // throw new IllegalArgumentException("Length must be positive.");
	ui = _ui;
	x = _x; y = _y; 
	length = _length; phase = _phase;
	wavelength = ui.squigglyWavelength();
	amplitude = ui.squigglyAmplitude();
	offset = phase > 0.5 ? -0.5 ? 0.0;
	hasClosed = false;
	h = horizontal;
    }
    /*
	How do we do this?
	
	We're mimicking a sine wave.
	
	 O--*--O       O--*--O       O--*--O
	*      ..     ..     ..     ..     ..      *
                O--*--O       O--*--O       O--*--O
	<-wavelength--><-wavelength-><-wavelength-->
	
	Each star is a point. Each O is a control point, the -- indicates
	which point it belongs to.
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
	
	So we're going to have a moveto the starting point, two points
	per wavelength and a lineto the final point.
    */
    
    public int getWindingRule() {
	return PathIterator.WIND_NON_ZERO;
    }
    public int currentSegment(double[] coords) {
	if(!hasOpened) {
	    coords[0] = x;
	    coords[1] = y;
	    return PathIterator.MOVE_TO;
	}
	if(hasClosed)
	    throw new RuntimeException("Called currentSegement after isDone is true.");
	if(offset >= length) {
	    // MOVE_TO the end point
	    coords[0] = x + (h ? l : 0.0);
	    coords[1] = y + (h ? 0.0 : l);
	    return PathIterator.MOVE_TO;
	}
	double currentPhase = offset + phase - floor(offset + phase);
	double nextPhase = currentPhase < 0.25 ? 0.25 : currentPhase < 0.75 ? 0.75 : 1.25;
	double nextOffset = offset + (nextPhase - currentPhase) * wavelength;
	if(nextOffset >= length) {
	    offset = length;
	    coords[0] = x + (h ? l : 0.0);
	    coords[1] = y + (h ? 0.0 : l);
	    return PathIterator.MOVE_TO;
	}
	// Is this point above the centerline?
	double upPoint = currentPhase < 0.25 || currentPhase > 0.75 ? amplitude : -amplitude; 
	double controloffset1 = nextOffset - 0.25 * wavelength;
	// Make sure the control points don't go outside the line range
	if(controloffset1 < 0.0)
	    controloffset1 = 0.0;
	double controloffset2 = nextOffset + 0.25 * wavelength;
	if(controloffset2 > length)
	    controloffset2 = length;
	coords[0] = x + (h ? controloffset1 : upPoint);
	coords[1] = y + (h ? upPoint : controloffset1);
	coords[2] = x + (h ? controloffset2 : upPoint);
	coords[3] = y + (h ? upPoint : controloffset2);
	coords[4] = x + (h ? nextOffset : upPoint);
	coords[5] = y + (h ? upPoint : nextOffset);
	return PathIterator.CURVE_TO;
    }
    public int currentSegment(float[] coords) {
	if(!hasOpened) {
	    coords[0] = x;
	    coords[1] = y;
	    return PathIterator.MOVE_TO;
	}
	if(hasClosed)
	    throw new RuntimeException("Called currentSegement after isDone is true.");
	if(offset >= length) {
	    // MOVE_TO the end point
	    coords[0] = x + (h ? l : 0.0);
	    coords[1] = y + (h ? 0.0 : l);
	    return PathIterator.MOVE_TO;
	}
	double currentPhase = offset + phase - floor(offset + phase);
	double nextPhase = currentPhase < 0.25 ? 0.25 : currentPhase < 0.75 ? 0.75 : 1.25;
	double nextOffset = offset + (nextPhase - currentPhase) * wavelength;
	if(nextOffset >= length) {
	    offset = length;
	    coords[0] = x + (h ? l : 0.0);
	    coords[1] = y + (h ? 0.0 : l);
	    return PathIterator.MOVE_TO;
	}
	// Is this point above the centerline?
	double upPoint = currentPhase < 0.25 || currentPhase > 0.75 ? amplitude : -amplitude; 
	double controloffset1 = nextOffset - 0.25 * wavelength;
	if(controloffset1 < 0.0)
	    controloffset1 = 0.0;
	double controloffset2 = nextOffset + 0.25 * wavelength;
	if(controloffset2 > length)
	    controloffset2 = length;
	coords[0] = (float) (x + (h ? controloffset1 : upPoint));
	coords[1] = (float) (y + (h ? upPoint : controloffset1));
	coords[2] = (float) (x + (h ? controloffset2 : upPoint));
	coords[3] = (float) (y + (h ? upPoint : controloffset2));
	coords[4] = (float) (x + (h ? nextOffset : upPoint));
	coords[5] = (float) (y + (h ? upPoint : nextOffset));
	return PathIterator.CURVE_TO;
    }
    public boolean isDone() {
	return hasClosed;
    }
    public void next() {
	if(hasClosed)
	    return;
	if(!hasOpened) {
	    hasOpened = true;
	    return;
	}
	if(offset >= length) {
	    hasClosed = true;
	    return;
	}
	double currentPhase = offset + phase - floor(offset + phase);
	double nextPhase = currentPhase < 0.25 ? 0.25 : currentPhase < 0.75 ? 0.75 : 1.25;
	double nextOffset = offset + (nextPhase - currentPhase) * wavelength;
	offset = nextOffset;
	if(offset >= length) {
	    hasClosed = true;
	    return;
	}
	return;
    }
    /**
     * Use this value as the starting phase for another line.
     */
    public double finalPhase() {
	return length / wavelength + phase 
		- floor(length / wavelength + phase);
    }
}
