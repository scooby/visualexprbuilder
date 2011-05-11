package veb;

import java.util.HashMap;
import java.util.Map;

public class Layout {
	final private Tree tree;
	final private Surface surface;
	private Grid grid;

	public Layout(final Direction d, final Tree gt, final Surface s) {
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
		final Map<Content, Area> map = new HashMap<Content, Area>();
		_layout(map, tree.getRoot(), 0, 0);
		grid = Grid.fromMap(map);
		grid.mergeDecorations();
		grid.simplify();
	}
	private int _layout(final Map<Content, Area> g, final Content n, final int x, final int y) {
		final int sx = x;
		int cx = x;
		for(final Content d : tree.getChildrenOf(n))
			cx = _layout(g, d, cx, y + 1);
		if(cx == sx)
			cx += 1;
		g.put(n, new Area(sx, y, cx, y + 1));
		return cx;
	}
	public void draw() {
		final Scalar[] rows = new Scalar[grid.height() + 1];
		final Scalar[] cols = new Scalar[grid.width() + 1];
		for(final Elem e : grid) {
			final Area a = e.getA();
			final Drawable d = e.getD();
			final Vector v = d.getSize();
			if(v.x != null) {
				int x = a.sx;
				for(final Scalar s : v.x.spread(a.width())) {
					cols[x] = s.add(cols[x]);
					x++;
				}
			}
			if(v.y != null) {
				int y = a.sy;
				for(final Scalar s : v.y.spread(a.height())) {
					rows[y] = s.add(rows[y]);
					y++;
				}
			}
		}
		Scalar off = Scalar.inst(0L);
		for(int i = 0; i < rows.length - 1; i++) {
			final Scalar height = rows[i] == null ? Scalar.inst(0L) : rows[i];
			rows[i] = off;
			off = off.add(height);
		}
		rows[rows.length - 1] = off;
		off = Scalar.inst(0L);
		for(int i = 0; i < cols.length - 1; i++) {
			final Scalar width = cols[i] == null ? Scalar.inst(0L) : cols[i];
			cols[i] = off;
			off = off.add(width);
		}
		cols[cols.length - 1] = off;
		for(final Elem e : grid) {
			final Area a = e.getA();
			final Drawable d = e.getD();
			final Vector o = new Vector(cols[a.sx], rows[a.sy]);
			final Vector p = new Vector(cols[a.ex], rows[a.ey]);
			surface.draw(o, p, a, d);
		}
	}
}