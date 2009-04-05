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

/** A piecegroup has many pieces in a tree organization.
 * All pieces are assembled into a single unit.
 * Rows alternate border row (which includes the nib descender), content row
 * Columns alternate side column and content column.
 *
 * Like columns may be collapsed.
 *  content squiggly squiggly content -> content squiggly content 
**/
 
public class Group {
    private ExpressionUI eui;
    private ExprBuildComponent ebc;
    private double nibDescent;
    private double nibExtent;
    private double nibSpacing;
    public Group(ExprBuildComponent _ebc) {
	ebc = _ebc;
	eui = _ebc.getUI();
	nibDescent = eui.nibDescent();
	nibExtent = eui.nibExtent();
	nibSpacing = eui.nibSpacing();
    }
    
    /**
     * Set up the grid of nodes
    **/
    private void build_grid(Node root, Graphics2D g) {
	// Make sure it's really the root. The root is often an
	// UnusedReturn.
	while(root.getOut() != null)
	    root = root.getOut();
	LinkedList<Node> queue;
	queue.offer(root);
	ninf.put(root, new NodeInfo(root, 0, 0));
	ArrayList<Node> all;
	int maxrow = 0;
	// Do node-widths, row numbers, and parent / older sibling links	
	while(!queue.isEmpty()) {
	    Node n = queue.remove();
	    NodeInfo ni = ninf.get(n);
	    int o = 0;
	    NodeInfo os = null;
	    for(Node child : n.getIns()) {
		queue.offer(child);
		os = ninf.put(child, 
		    new NodeInfo(child, ni.row + 1, o++, ni, os));
	    }
	    if(o == 0)
		n.nwidth = 1; // By default it is 0, and it gets set later...
	    else
		maxrow = Math.max(maxrow, ni.row + 1);
	    all.add(n);
	}
	gmap = new ArrayList<SortedMap<int, NodeInfo>>(maxrow + 1);
	for(int i = maxrow + 1; i > 0; i--)
	    gmap.add(new SortedMap<int, NodeInfo>());
	Rectangle2D labelRect = new Rectangle2D.double();
	// Iterate backwards to hit the leaves first.
	// This makes leaves tell parents how wide they need to be.
	ListIterator<Node> li = all.list_iterator(all.size());
	while(li.hasPrevious()) {
	    Node x = li.previous();
	    NodeInfo xi = ninf.get(x);
	    eui.contentArea(g, x, labelRect);
	    xi.cont_width = labelRect.getWidth();
	    xi.cont_height = labelRect.getHeight();
	    Node px = x.getOut();
	    if(px == null)
		continue;
	    NodeInfo pxi = ninf.get(px);
	    pxi.span += xi.span;
	    pxi.chld_width += Math.max(xi.chld_width, xi.cont_width);
	}
	// Iterate forwards as a node's column is older sibling + nwidth
	li = all.list_iterator(0);
	while(li.hasNext()) {
	    Node x = li.next();
	    NodeInfo xi = ninf.get(x);
	    if(xi.off == 0)
		xi.col = 0;
	    else
		xi.col = xi.old_sib.col + xi.old_sib.span;
	    gmap.get(xi.row).put(xi.col, xi);
	}
    }
    
    private ArrayList<SortedMap<int, NodeInfo>> gmap;
    private IdentityHashMap<Node, NodeInfo> ninf;
    private class NodeInfo {
	public NodeInfo(Node n, int r, int o, NodeInfo p, NodeInfo os) { 
	    node = n;
	    row = r;
	    off = o;
	    parent = p;
	    old_sib = os;
	    nwidth = 0;
	}
	public Node node;
	public NodeInfo parent;
	public NodeInfo old_sib;
	public int row;
	public int col;
	public int off;
	public int span;
	public double cont_width;
	public double cont_height;
	public double chld_width;
    }
}
