package veb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

final public class Grid implements Iterable<Elem>{
	final private Area dims;
	final private int rowwidth;
	final private Elem[] ex;
	final private Map<Drawable, Set<Area>> m;
	private Grid(final Area a) {
		assert a.sx == 0 && a.sy == 0;
		dims = a;
		rowwidth = width();
		ex = new Elem[width() * height()];
		m = new HashMap<Drawable, Set<Area>>();
	}
	public int width() {
		return dims.width();
	}
	public int height() {
		return dims.height();
	}
	private void setCell(final int x, final int y, final Elem e) {
		ex[y * rowwidth + x] = e;
		final Drawable d = e.getD();
		if(!m.containsKey(d))
			m.put(d, new HashSet<Area>());
		m.get(d).add(e.getA());
	}
	public Area getArea(final int x, final int y) {
		return ex[y * rowwidth + x].getA();
	}
	public Drawable getDrawable(final int x, final int y) {
		return ex[y * rowwidth + x].getD();
	}
	final static private int dt = Content.DECO_THICK;
	final static private int de = Content.DECO_EXTENT;
	private void setArea(final Area a, final Content c) {
		final Area ai = a.inset(de);
		final Elem e = new Elem(ai, c);
		for(final Area p : a)
			if(p.overlap(ai))
				setCell(p.sx, p.sy, e);
			else {
				final Drawable d = c.getDecoration(ai.xdist(p), ai.ydist(p));
				setCell(p.sx, p.sy, new Elem(p, d));
			}
	}
	private void setCells(final Area a, final Drawable d) {
		final Elem e = new Elem(a, d);
		for(final Area p : a)
			setCell(p.sx, p.sy, e);
	}
	private void removeArea(final Area a) {
		for(final Area p: a) {
			final int i = p.sx + rowwidth * p.sy;
			final Elem e = ex[i];
			ex[i] = null;
			final Drawable d = e.getD();
			final Area ea = e.getA();
			final Set<Area> ax = m.get(d);
			ax.remove(ea);
			if(ax.isEmpty())
				m.remove(d);
		}
	}
	public static Grid fromMap(final Map<Content, Area> m) {
		if(m.isEmpty())
			return new Grid(new Area(0, 0, 0, 0));
		final Area ac = Area.coverage(m.values());
		final Area dims = ac.zeroize().scale(dt);
		final Grid g = new Grid(dims);
		for(final Entry<Content, Area> e : m.entrySet())
			g.setArea(e.getValue().offset(-ac.sx, -ac.sy).scale(dt), e.getKey());
		return g;
	}
	@Override
	public Iterator<Elem> iterator() {
		final ArrayList<Elem> al = new ArrayList<Elem>();
		for(final Entry<Drawable, Set<Area>> da: m.entrySet()) {
			final Drawable d = da.getKey();
			for(final Area a : da.getValue())
				al.add(new Elem(a, d));
		}
		return al.iterator();
	}
	public void mergeDecorations() {
		for(int axis = 0; axis < 2; axis++)
			for(final Entry<Drawable, Set<Area>> da: m.entrySet()) {
				final Drawable d = da.getKey();
				if(d == null || d instanceof Content)
					continue;
				for(final Area a: da.getValue()) {
					Area aa;
					do {
						aa = a.adjacent(axis);
						if(aa.width() > 1 || aa.height() > 1)
							break;
						final Elem e = ex[aa.sy * rowwidth + aa.sx];
						final Drawable dd = e.getD();
						if(dd == null || dd.isEmpty())
							continue;
						final Drawable dm = d.merge(dd);
						if(dm == null)
							continue;
						aa = Area.coverage(a, aa);
						removeArea(aa);
						setCells(aa, dm);
					} while(aa.overlap(dims));
				}
			}
	}
	public void simplify() {
		for(int axis = 0; axis < 2; axis++)
			for(final Entry<Drawable, Set<Area>> da: m.entrySet()) {
				final Drawable d = da.getKey();
				if(d == null || d instanceof Content)
					continue;
				areaLoop: for(final Area a: da.getValue()) {
					Area aa = a.adjacent(axis);
					if(!aa.overlap(dims))
						break;
					final ArrayList<Area> al = new ArrayList<Area>();
					for(final Area p: aa) {
						final Elem e = ex[p.sy * rowwidth + p.sx];
						if(e.getD() != d)
							continue areaLoop;
						al.add(e.getA());
					}
					if(!a.extendedBy(al))
						continue;
					al.add(a);
					aa = Area.coverage(al);
					removeArea(aa);
					setCells(aa, d);
				}
			}
	}
}
