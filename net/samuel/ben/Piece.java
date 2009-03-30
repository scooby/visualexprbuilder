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
    Piece(Node _n, Group _g) {
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
    public Piece left() {
    }
    public Piece right() {
    }
    private float w;
    private float h;
    private Node n;
    private Group g;
    private Part p;
}
