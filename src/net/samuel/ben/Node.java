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

import java.util.List;

// Represents an object managed by the ExpressionComponent
interface Node {
	public List<? extends Node> getIns();
    public Node getOut();
    public Class<?> getType();
    public Object getValue();
    public String getLabel();
    public boolean canAddIns(); // Should this node have an input adder
    public void addIn(Node n);
    public void setIn(int i, Node n);
    public void setOut(Node n);
    public NodeStyle getStyle();
}
