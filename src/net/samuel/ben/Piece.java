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

/**
 * Represents a graphical element
 */

class Piece {
    public enum Draw {
    	full_area, semicircle_top, semicircle_bottom,
    	squiggly_side, squiggly_row, straight_side, straight_row,
    	pointy_left_side, pointy_right_side, corner, empty
    }
    public class Part {
    	public Draw d;
    }
    Piece(Node _n, Group _g) {
    	n = _n; g = _g;
    }
    public void calc_dims() {
		switch(ns) {
		case squiggly_sides:
		    left.d = right.d = Draw.squiggly_side;
		    top.d = bot.d = Draw.squiggly_row;
		    content.d = Draw.empty;
		    break;
		case straight_sides:
		    left.d = right.d = Draw.straight_side;
		    top.d = n.canAddIns() ? Draw.squiggly_row : Draw.straight_row;
		    bot.d = Draw.squiggly_row;
		    content.d = Draw.empty;
		    break;
		case pointy_sides:
		    left.d = Draw.pointy_left_side;
		    right.d = Draw.pointy_right_side;
		    top.d = n.canAddIns() ? Draw.squiggly_row : Draw.straight_row;
		    bot.d = Draw.squiggly_row;
		    content.d = Draw.empty;
		    break;
		case semicircle_top:
		    left.d = right.d = top.d = Draw.empty;
		    bot.d = Draw.squiggly_row;
		    content.d = Draw.semicircle_top;
		    break;
		case semicircle_bottom:
		    left.d = right.d = bot.d = Draw.empty;
		    top.d = Draw.squiggly_row;
		    content.d = Draw.semicircle_bottom;
		    break;
		default:
		    throw new RuntimeException("What?!");
		}
    }
    private float w;
    private float h;
    private Node n;
    private Group g;
    private Part left;
    private Part right;
    private Part top;
    private Part bot;
    private Part content;
    private NodeStyle ns;
}
