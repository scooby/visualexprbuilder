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

import com.mallardsoft.tuple.*;
/** A piecegroup has many pieces in a tree organization.
 * All pieces are assembled into a single unit.
 * Rows alternate border row (which includes the nib descender), content row
 * Columns alternate side column and content column.
 
 * Like columns may be collapsed.
    content squiggly squiggly content -> content squiggly content
 
 * Unlike columns require a spacer.
    content squiggly angle content -> content squiggly spacer angle content
 */
 
public class Group {
    private ExpressionUI eui;
    private ExprBuildComponent ebc;
    public Group(ExprBuildComponent _ebc) {
	ebc = _ebc;
	eui = _ebc.getUI();
    }
    
    private HashMap<Pair<int, int>, Piece> gmap;
    private IdentityHashMap<Node, Pair<int, int>> nmap;
    
    private void build_grid(Node _n) {

	// Set up the grid of nodes
	
	LinkedList<Node> q;
	q.offer(_n);
	
	while(!q.isEmpty()) {
	    Node n = q.remove();
	    
	    int r = t.second + 1; int c = t.third;
	    if(t.hasInputs()) {
		for(Pair<Class<?>, Node> x : t.first.getIns()) {
		    q.offer(new Triple(n, r, c++));
		}

	// Add the pieces in
	
	// Collapse sides or add spacers
	// Figure out sizes
	// 
    }
    private SortedMap<float, int> cols;
    private SortedMap<float, int> rows;
    private ArrayList<Node> memberNodes;
    private Shape area;
    private Shape lines;
    
    private class Piece {
	public enum Part {
	    north_part, east_part, south_part, west_part, center_part
	}
	Piece(Node _n, Part _p) {
	    n = _n; p = _p;
	}
	public void calc_dims() {
	    switch(ns) {
	    case squiggly_sides:
		left.d = right.d = squiggly_side;
		top.d = bot.d = squiggly_row;
		content.d = label_area;
		break;
	    case straight_sides:
		left.d = right.d = straight_side;
		top.d = n.hasInputs() ? squiggly_row : straight_row;
		bot.d = squiggly_row;
		content.d = label_area;
		break;
	    case pointy_sides:
		left.d = pointy_left_side;
		right.d = pointy_right_side;
		top.d = n.hasInputs() ? squiggly_row : straight_row;
		bot.d = squiggly_row;
		content.d = label_area;
		break;
	    case semicircle_top:
		left.d = right.d = top.d = empty;
		bot.d = squiggly_row;
		content.d = semicircle_top;
		break;
	    case semicircle_bottom:
		left.d = right.d = bot.d = empty;
		top.d = squiggly_row;
		content.d = semicircle_bottom;
		break;
	    default:
		throw new RuntimeException("What?!");
	    }
	}
	public float w;
	public float h;
	public Node n;
	public int r;
	public int c;
	public Part p;
    }
}
