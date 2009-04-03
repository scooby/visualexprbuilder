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
import java.util.Iterator;
/**
 * Given an Iterable of PathIterators, this class will glom them all
 * together to return one large path.
 * 
 * It doesn't check to see if getWindingRule is consistent, but then the
 * PathIterator contract doesn't say anything about that.
 */
public class CompoundPath implements PathIterator {
    private Iterator<? extends PathIterator> pii;
    PathIterator pi;
    public CompoundPath(Iterable<? extends PathIterator> pathCollection) {
	pii = pathCollection.iterator();
	pi = null;
    }
    public int currentSegment(double[] coords) {
	updatePi();
	if(pi == null)
	    return -1; // invalid segment type
	return pi.currentSegment(coords);
    }
    public int currentSegment(float[] coords) {
	updatePi();
	if(pi == null)
	    return -1; // ditto
	return pi.currentSegment(coords);
    }
    public int getWindingRule() {
	updatePi();
	if(pi == null)
	    return -1; // invalid winding rule
	return pi.getWindingRule();
    }
    public boolean isDone() {
	updatePi();
	return pi != null;
    }
    public void next() {
	updatePi();
	pi.next();
	if(pi.isDone())
	    pi = null;
    }
    private void updatePi() {
	if(pi == null)
	    while(pii.hasNext()) {
		pi = pii.next();
		if(!pi.isDone())
		    return;
	    }
    }
}
