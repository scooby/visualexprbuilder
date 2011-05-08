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
**/

package net.samuel.ben;
import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Paint;
import java.awt.Stroke;
import java.awt.geom.Rectangle2D;

public class BasicExpressionUI extends ExpressionUI {
    public BasicExpressionUI() { }
    public Stroke borderStroke() { return _stroke; }
    public double heteroborderSpace() { return 3.0; }
    public Paint borderPaint() { return Color.black; }
    public Paint areaPaint() { return Color.white; }
    private final static Font _labelFont = 
    	new Font("Helvetica", Font.PLAIN, 12);
    private final static BasicStroke _stroke = new BasicStroke((float) 0.75);
    public Font labelFont() { return _labelFont; }
    private static final double SQRT2 = Math.sqrt(2.0);
    /**
     * Bounding box for content drawn on a Graphics2D object
     * Includes padding, and for arc-based nodes
     *   includes space for arc.
     * Does *not* include space for borders or nibs.
    **/
    public void contentArea(Graphics2D g, Node n, Rectangle2D r) {
	NodeStyle ns = n.getStyle();
	Rectangle2D b = labelFont().getStringBounds(n.getLabel(),
	    g.getFontRenderContext());
	/* Strictly, it's an semiellipse. We need the ellipse to be large
	enough that the label + padding doesn't touch any part of it.
	To circumscribe a square with a circle, assume the diameter
	runs corner to corner, cutting the square into two isoceles right
	triangles. By Pythagorean,  d = sqrt(2 * s^2), so d = sqrt(2) * s.
	Given that an ellipse is simply a streched circle a similarly
	stretched square will still fit.
	Our semiellipse is just half of an ellipse, but our rectangle is
	also just half of a larger rectangle so we don't need to do
	anything about that.**/
	double ellipsePad = ns == NodeStyle.semicircle_top || ns == NodeStyle.semicircle_bottom 
	    ? SQRT2 : 1.0;
	r.setRect(b.getX(), b.getY(),
	    (b.getWidth() + 2.0) * ellipsePad,
	    (b.getHeight() + 2.0) * ellipsePad);
    }
    /**
     * thickness of the border with the senior sibling
     * this is west border width in a LTR UI
    **/
    public double seniorBorderSize(Graphics2D g, Node n, Rectangle2D content)
    { 
		switch(n.getStyle()) {
		case squiggly_sides:
		    return 2.0 * squigglyAmplitude() + _stroke.getLineWidth();
		case straight_sides:
		    return _stroke.getLineWidth();
		case pointy_sides:
		    // pointyangle is twice the angle of a right triangle whose
		    // opposite side is half our height and whose adjacent is the
		    // width we're interested in.
		    // tan(pointy/2) = height/2 / return
		    return content.getHeight() / 2.0 / Math.tan(pointyAngle());
		case semicircle_top:
		case semicircle_bottom:
		    return 0.0;
		}
		throw new AssertionError();
    }
    /**
     * thickness of the border with the junior sibling
     * this is east border width in a LTR UI
    **/
    public double juniorBorderSize(Graphics2D g, Node n, Rectangle2D content)
    {
    	return seniorBorderSize(g, n, content);
    }
    /**
     * thickness of the border with the children
     * this is the north border height in a LTR UI
    **/
    public double childrenBorderSize(Graphics2D g, Node n, 
	Rectangle2D content)
    {
		switch(n.getStyle()) {
		case squiggly_sides:
		case semicircle_bottom:
		    return 2.0 * squigglyAmplitude() + _stroke.getLineWidth();
		case pointy_sides:
		    // Check if this is a capture or a recall
		    return n.getIns().isEmpty() ? _stroke.getLineWidth() :
			2.0 * squigglyAmplitude() + _stroke.getLineWidth();
		case semicircle_top:
		    return 0.0;
		case straight_sides:
		    return _stroke.getLineWidth();
		}
		throw new AssertionError();
    }
    /**
     * thickness of the border with the parent
     * this is the south border height in a LTR UI
    **/
    public double parentBorderSize(Graphics2D g, Node n, Rectangle2D content)
    {
		switch(n.getStyle()) {
		case squiggly_sides:
		case semicircle_top:
		    return 2.0 * squigglyAmplitude() + _stroke.getLineWidth();
		case straight_sides:
		case pointy_sides:
		    // Check if this is a capture or a recall
		    return n.getIns().isEmpty() ? _stroke.getLineWidth() :
			2.0 * squigglyAmplitude() + _stroke.getLineWidth();
		case semicircle_bottom:
		    return 0.0;
		}
		throw new AssertionError();
    }
    /**
     * distance the nib descends
     * this is the height of the north descender area in a LTR UI
    **/
    public double nibDescent() {
    	return 12.0;
    }
    /**
     * width of the nib
     * this is the width of the descender itself in a LTR UI
    **/
    public double nibExtent() {
    	return 8.0;
    }
    /**
     * minimal spacing between nibs
     * This will probably never matter because a node with any text will be
     * wider than the nib extent.
     * This is padding around the nib. Between two nibs, there will be >= this
     * value * 2, between a nib and a corner, there will be >= this value.
    **/
    public double nibSpacing() {
    	return 2.0;
    }
    // This should be small, 2 or 3 pixels. Note the actual border width is
    // twice this.
    public double squigglyAmplitude() { return 2.0; }
    // The squiggles are a rough approximation of a sine wave
    public double squigglyWavelength() { return 32.0; }
    /**
     * Pointy sides are essentially the same as a less-than or greater-than
     * sign. This is the interior angle used to determine the width of the
     * sides column, i.e. width = (height / 2) / tan(pointyAngle / 2)
     * The return value should be in radians.
    **/
    public double pointyAngle() { return Math.toRadians(160); }
	@Override
	public Paint areaPaint(Node n) {
		// TODO Auto-generated method stub
		return null;
	}
}
