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
import java.awt.geom.Dimension2D;
import java.awt.geom.Rectangle2D;

abstract public class ExpressionUI extends javax.swing.plaf.ComponentUI {
    public enum NodeStyle {
	squiggly_sides, straight_sides, pointy_sides, semicircle_top, semicircle_bottom
    }
    // This should be small, 2 or 3 pixels. Note the actual border width is
    // twice this.
    abstract public float squigglyAmplitude();
    // The squiggles aren't actually a sine wave, but somewhat close to.
    abstract public float squigglyWavelength();
    abstract public Stroke borderStroke();
    // Pointy sides are essentially the same as a less-than or greater-than
    // sign. This is the interior angle used to determine the width of the
    // sides column, i.e. width = (height / 2) / sin(pointyAngle / 2)
    abstract public float pointyAngle();
    abstract public Paint borderPaint();
    abstract public Paint areaPaint(Node n);
    // At some point, it would be nice to make this extensible, but it's 
    // hard to allow for entirely new ways of drawing stuff
    public NodeStyle getStyle(Node n) {
	if(n instanceof Operator)
	    return squiggly_sides;
	if(n instanceof Literal)
	    return straight_sides;
	if(n instanceof Capture || n instanceof Recall)
	    return pointy_sides;
	if(n instanceof UnsetArg)
	    return semicircle_top;
	if(n instanceof UnusedReturn)
	    return semicircle_bottom;
    }
}
