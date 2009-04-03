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
import java.util.Arrays;

public class UnusedReturn implements Node {
    protected Class<?> t;
    protected Node i;
    protected List<? extends Node> il;
    public UnsetArg(Node node, Class<?> type) { 
	t = type; 
	i = node;
	il = Arrays.asList(i);
    }
    public List<? extends Node> getIns() { return il; }
    public Class<?> getType()            { return t; }
    public Node getOut()                 { return null; }
    public Object getValue()             { return null; }
    public NodeStyle getStyle()          { return semicircle_bottom; }
    public String getLabel()             { return t.getSimpleName(); }
    public boolean canAddIns()           { return false; }
    public void addIn(Node n)            { throw new RuntimeException(); }
    public void setIn(int idx, Node n)     { 
	if(idx != 0)
	    throw new RuntimeException();
	i = n;
	il.set(0, n);
    }
    public void setOut(Node n)           { 
	if(n != null)
	    throw new RuntimeException();
    }
}}
