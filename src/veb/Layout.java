package veb;

import java.util.HashMap;
import java.util.Map;

import space.Num;
import space.Area;
import space.NumArray;
import space.NumImpl;
import space.Vector;
import space.Zero;

public class Layout<T extends Num> {
	final private Tree<T> tree;
	final private Surface<T> surface;
	private Grid<T> grid;

	public Layout(final Tree<T> gt, final Surface<T> s, T f) {
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
		final Map<Content<T>, Area> map = new HashMap<Content<T>, Area>();
		_layout(map, tree.getRoot(), 0, 0);
		grid = Grid.fromMap(map);
		grid.mergeDecorations();
		grid.simplify();
	}
	private int _layout(final Map<Content<T>, Area> g, final Content<T> n, final int x, final int y) {
		final int sx = x;
		int cx = x;
		for(final Content<T> d : tree.getChildrenOf(n))
			cx = _layout(g, d, cx, y + 1);
		if(cx == sx)
			cx += 1;
		g.put(n, new Area(sx, y, cx, y + 1));
		return cx;
	}

	public void draw() {
		final NumArray<T> rows = new NumArray<T>(grid.height() + 1);
		final NumArray<T> cols = new NumArray<T>(grid.width() + 1);
		for(final Elem<T> e : grid) {
			final Area a = e.getA();
			final Drawable<T> d = e.getD();
			final Vector<T> v = d.getSize();
			if(v.x != null) {
				int x = a.sx;
				for(final T s : NumImpl.spread(v.x, a.width())) {
					cols.add(x, s);
					x++;
				}
			}
			if(v.y != null) {
				int y = a.sy;
				for(final T s : NumImpl.spread(v.y, a.height())) {
					rows.add(y, s);
					y++;
				}
			}
		}
		T off = (T) Zero.zero();
		for(int i = 0; i < rows.size(0) - 1; i++)
			off = NumImpl.add(off, rows.pop(i, off));
		rows.set(rows.size(0) - 1, off);
		off = (T) Zero.zero();
		for(int i = 0; i < cols.size(0) - 1; i++)
			off = NumImpl.add(off, cols.pop(i, off));
		cols.set(cols.size(0) - 1, off);
		for(final Elem<T> e : grid) {
			final Area a = e.getA();
			final Drawable<T> d = e.getD();
			final Vector<T> o = new Vector<T>(cols.get(a.sx), rows.get(a.sy));
			final Vector<T> p = new Vector<T>(cols.get(a.ex), rows.get(a.ey));
			surface.draw(o, p, a, d);
		}
	}
}