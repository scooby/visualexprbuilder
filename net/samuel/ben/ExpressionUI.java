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
import java.awt.Graphics2D;
import java.awt.geom.Rectangle2D;

/**
 * Conventions are based on left-to-right:
 *           INPUTS (siblings)        ^
 *      senior   --->     junior      | children /
 *  ____     ____     ____      ____  | descendants
 * |    \___/    \___/    \____/    | 
 * |                                | 
 * |                                | 
 * |                                | |
 * |_____________     ______________| | parents /
 *               \___/                v ancestors
 *               OUTPUT
 */

abstract public class ExpressionUI extends javax.swing.plaf.ComponentUI {
    /**
     * The squiggles are roughly a sine wave, and that's what the amplitude
     * describes.
     * This should be small, 2 or 3 pixels. Note the actual border width is
     * twice this plus the size of the border stroke pen.
     */
    abstract public double squigglyAmplitude();
    /**
     * The squiggles are roughly a sine wave, and that's what the wavelength
     * describes.
     */
    abstract public double squigglyWavelength();
    /**
     * This can be any pen. The pen must have a well defined size to be able
     * to calculate the actual thickness of the border.
     */
    abstract public Stroke borderStroke();
    /**
     * Pointy sides are essentially the same as a less-than or greater-than
     * sign. This is the interior angle used to determine the width of the
     * sides column, i.e. width = (height / 2) / tan(pointyAngle / 2)
     * The return value should be in radians.
     */
    abstract public double pointyAngle();
    /**
     * Space between different borders.
     */
    abstract public double heteroborderSpace();
    /**
     * Paint for border strokes
     */
    abstract public Paint borderPaint();
    /**
     * Paint for piece areas
     */
    abstract public Paint areaPaint(Node n);
    /**
     * Sets bounding box for content drawn on a Graphics2D object
     * Includes padding, and for arc-based nodes
     *   includes space for arc.
     * Does *not* include space for borders or nibs.
     */
    abstract public void contentArea(Graphics2D g, Node n, Rectangle2D r);
    /**
     * thickness of the border with the senior sibling
     * this is west border width in a LTR UI
     */
    abstract public double seniorBorderSize(Graphics2D g, Node n, Rectangle2D content);
    /**
     * thickness of the border with the junior sibling
     * this is east border width in a LTR UI
     */
    abstract public double juniorBorderSize(Graphics2D g, Node n, Rectangle2D content);
    /**
     * thickness of the border with the children
     * this is the north border height in a LTR UI
     */
    abstract public double childrenBorderSize(Graphics2D g, Node n, Rectangle2D content);
    /**
     * thickness of the border with the parent
     * this is the south border height in a LTR UI
     */
    abstract public double parentBorderSize(Graphics2D g, Node n, Rectangle2D content);
    /**
     * distance the nib descends into the content area
     * this is the height of the north descender area in a LTR UI
     */
    abstract public double nibDescent();
    /**
     * width of the nib
     * this is the width of the descender itself in a LTR UI
     */
    abstract public double nibExtent();
    /**
     * minimal spacing between nibs
     * This will probably never matter because a node with any text will be
     * wider than the nib extent.
     * This is padding around the nib. Between two nibs, there will be >= this
     * value * 2, between a nib and a corner, there will be >= this value.
    **/
    abstract public double nibSpacing();
}
