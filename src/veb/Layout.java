package veb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Layout {
	final private Direction overall_dir;
	final private Vector origin;
	final private GenericTree tree;
	final private Surface surface;
	
	public Layout(Direction d, Vector o, GenericTree gt, Surface s) {
		overall_dir = d;
		origin = o;
		tree = gt;
		surface = s;
	}
	/**
	 * The algorithm is as such:
	 *  Organize the nodes into a grid to determine relative offsets
	 *  Expand along each axis into margin/decoration/content/decoration/margin
	 *  Collapse dmd to d where possible.
	 */
	public void layout() {
		Map <Offset, Content> grid = new HashMap<Offset, Content>();
		grid(grid, tree.getRoot(), 0, 0);
		Map <Offset, Drawable> dgrid = new HashMap<Offset, Drawable>();
		for(Offset o : grid.keySet()) {
			Content n = grid.get(o);
			for(int s = -2; s < 3; s++) {
				for(int t = -2; t < 3; t++) {
					Drawable d = n.getDecoration(s, t);
					dgrid.put(o.slot(s, t), d);
				}
			}
		}
	}
	private int grid(Map<Offset, Content> g, Content n, int x, int y) {
		int sx = x, cx = x;
		for(Content d : tree.getChildrenOf(n)) {
			cx = grid(g, d, cx, y + 1);
		}
		if(cx == sx)
			cx += 1;
		for(x = sx; x < cx; x++) {
			g.put(new Offset(x, y), n);
		}
		return cx;
	}
	private final class Offset {
		public final int x;
		public final int y;
		public final int s;
		public final int t;
		
		public Offset(int x, int y) { this.x = x; this.y = y; this.s = 0; this.t = 0;}
		public Offset(int x, int y, int s, int t) { this.x = x; this.y = y; this.s = s; this.t = t;}
		public Offset slot(int _s, int _t) {
			return s == _s && t == _t ? this : new Offset(x, y, _s, _t);
		}
		/* (non-Javadoc)
		 * @see java.lang.Object#hashCode()
		 */
		@Override
		public int hashCode() {
			final int prime = 101;
			return prime * (prime * (prime + x) + y) + s;
		}

		/* (non-Javadoc)
		 * @see java.lang.Object#equals(java.lang.Object)
		 */
		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null || !(obj instanceof Offset))
				return false;
			Offset other = (Offset) obj;
			return x == other.x && y == other.y && s == other.s;
		}
	}
}
