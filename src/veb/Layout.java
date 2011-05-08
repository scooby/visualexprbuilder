package veb;

import java.util.HashMap;
import java.util.Map;

public class Layout {
	final private Tree tree;
	final private Surface surface;
	private Grid grid;
	
	public Layout(Direction d, Tree gt, Surface s) {
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
		Map<Content, Area> map = new HashMap<Content, Area>();
		_layout(map, tree.getRoot(), 0, 0);
		grid = Grid.fromMap(map);
		grid.mergeDecorations();
		grid.simplify();	
	}
	private int _layout(Map<Content, Area> g, Content n, int x, int y) {
		int sx = x, cx = x;
		for(Content d : tree.getChildrenOf(n)) {
			cx = _layout(g, d, cx, y + 1);
		}
		if(cx == sx)
			cx += 1;
		g.put(n, new Area(sx, y, cx, y + 1));
		return cx;
	}
	public void draw() {
		Scalar[] rows = new Scalar[grid.height() + 1];
		Scalar[] cols = new Scalar[grid.width() + 1];
		for(Elem e : grid) {
			Area a = e.getA();
			Drawable d = e.getD();
			Vector v = d.getSize();
			if(v.x != null) {
				int x = a.sx;
				for(Scalar s : v.x.spread(a.width())) {
					cols[x] = s.add(cols[x]);
					x++;
				}
			}
			if(v.y != null) {
				int y = a.sy;
				for(Scalar s : v.y.spread(a.height())) {
					rows[y] = s.add(rows[y]);
					y++;
				}
			}
		}
		Scalar off = Scalar.inst(0L);
		for(int i = 0; i < rows.length - 1; i++) {
			Scalar height = rows[i] == null ? Scalar.inst(0L) : rows[i];
			rows[i] = off;
			off = off.add(height);
		}
		rows[rows.length - 1] = off;
		off = Scalar.inst(0L);
		for(int i = 0; i < cols.length - 1; i++) {
			Scalar width = cols[i] == null ? Scalar.inst(0L) : cols[i];
			cols[i] = off;
			off = off.add(width);
		}
		cols[cols.length - 1] = off;
		for(Elem e : grid) {
			Area a = e.getA();
			Drawable d = e.getD();
			Vector o = new Vector(cols[a.sx], rows[a.sy]);
			Vector p = new Vector(cols[a.ex], rows[a.ey]);
			surface.draw(o, p.subtract(o), d);
		}
	}
}