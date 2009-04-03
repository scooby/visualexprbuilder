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
import java.lang.reflect.InvocationTargetException;

abstract class Operator implements Node {
    abstract public List<? extends Node> getIns();
    abstract public Node getOut();
    abstract public Class<?> getType();
    abstract public Object getValue();
    abstract public String getLabel();
    public NodeStyle getStyle() {
	return squiggly_sides;
    }
    abstract public bool canAddIns(); // Should this node have an input adder
    abstract public void addIn(Node n);
    abstract public void setIn(int i, Node n);
    abstract public void setOut(Node n);
}
