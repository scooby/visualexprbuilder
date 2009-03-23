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
import java.awt.Stroke;
import java.awt.BasicStroke;
import java.awt.Paint;
import java.awt.Color;

public class BasicExpressionUI extends ExpressionUI {
    // This should be small, 2 or 3 pixels. Note the actual border width is
    // twice this.
    public float squigglyAmplitude() { return 2.0; }
    // The squiggles are a rough approximation of a sine wave
    public float squigglyWavelength() { return 32.0; }
    // Pointy sides are essentially the same as a less-than or greater-than
    // sign. This is the interior angle used to determine the width of the
    // sides column, i.e. width = (height / 2) / sin(pointyAngle / 2)
    public float pointyAngle() { return Math.toRadians(160); }
    public Stroke borderStroke() { return BasicStroke(0.75); }
    public Paint borderPaint() { return Color.black; }
    public Paint areaPaint(Node n) { return Color.white; }
}
