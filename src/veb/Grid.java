package veb;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import space.Num;
import space.Area;

final public class Grid<T extends Num> implements Iterable<Elem<T>>{
	final private Area dims;
	final private int rowwidth;
	final private List<Elem<T>> ex;
	final private Map<Drawable<T>, Set<Area>> m;
	private Grid(final Area a) {
		assert a.sx == 0 && a.sy == 0;
		dims = a;
		rowwidth = width();
		ex = new ArrayList<Elem<T>>(width() * height());
		m = new HashMap<Drawable<T>, Set<Area>>();
	}
	public int width() {
		return dims.width();
	}
	public int height() {
		return dims.height();
	}
	private void _set(final Area p, Elem<T> e) {
		ex.set(p.sy * rowwidth + p.sx, e);
	}
	private Elem<T> _pop(Area p) {
		final int i = p.sy * rowwidth + p.sx;
		Elem<T> r = ex.get(i);
		ex.set(i, null);
		return r;
	}
	private Elem<T> _get(Area p) {
		return ex.get(p.sy * rowwidth + p.sx);
	}
	/*private Elem<T> _get(final int x, final int y) {
		return ex.get(y * rowwidth + x);
	}*/
	private void setCell(Area p, final Elem<T> e) {
		_set(p, e);
		final Drawable<T> d = e.getD();
		if(!m.containsKey(d))
			m.put(d, new HashSet<Area>());
		m.get(d).add(e.getA());
	}
	/*public Area getArea(final int x, final int y) {
		return _get(x, y).getA();
	}
	public Drawable<T> getDrawable(final int x, final int y) {
		return _get(x, y).getD();
	}*/
	final static private int dt = Content.DECO_THICK;
	final static private int de = Content.DECO_EXTENT;
	private void setArea(final Area a, final Content<T> c) {
		final Area ai = a.inset(de);
		final Elem<T> e = new Elem<T>(ai, c);
		for(final Area p : a)
			if(p.overlap(ai))
				setCell(p, e);
			else {
				final Drawable<T> d = c.getDecoration(ai.xdist(p), ai.ydist(p));
				setCell(p, new Elem<T>(p, d));
			}
	}
	private void setCells(final Area a, final Drawable<T> d) {
		final Elem<T> e = new Elem<T>(a, d);
		for(final Area p : a)
			setCell(p, e);
	}
	private void removeArea(final Area a) {
		for(final Area p: a) {
			final Elem<T> e = _pop(p);
			final Drawable<T> d = e.getD();
			final Area ea = e.getA();
			final Set<Area> ax = m.get(d);
			ax.remove(ea);
			if(ax.isEmpty())
				m.remove(d);
		}
	}
	public static <TT extends Num> Grid<TT> fromMap(final Map<Content<TT>, Area> m) {
		if(m.isEmpty())
			return new Grid<TT>(new Area(0, 0, 0, 0));
		final Area ac = Area.coverage(m.values());
		final Area dims = ac.zeroize().scale(dt);
		final Grid<TT> g = new Grid<TT>(dims);
		for(final Entry<Content<TT>, Area> e : m.entrySet())
			g.setArea(e.getValue().offset(-ac.sx, -ac.sy).scale(dt), e.getKey());
		return g;
	}
	@Override
	public Iterator<Elem<T>> iterator() {
		final ArrayList<Elem<T>> al = new ArrayList<Elem<T>>();
		for(final Entry<Drawable<T>, Set<Area>> da: m.entrySet()) {
			final Drawable<T> d = da.getKey();
			for(final Area a : da.getValue())
				al.add(new Elem<T>(a, d));
		}
		return al.iterator();
	}
	public void mergeDecorations() {
		for(int axis = 0; axis < 2; axis++)
			for(final Entry<Drawable<T>, Set<Area>> da: m.entrySet()) {
				final Drawable<T> d = da.getKey();
				if(d == null || d instanceof Content)
					continue;
				for(final Area a: da.getValue()) {
					Area aa;
					do {
						aa = a.adjacent(axis);
						if(aa.width() > 1 || aa.height() > 1)
							break;
						final Elem<T> e = _get(aa);
						final Drawable<T> dd = e.getD();
						if(dd == null || dd.isEmpty())
							continue;
						final Drawable<T> dm = d.merge(dd);
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
			for(final Entry<Drawable<T>, Set<Area>> da: m.entrySet()) {
				final Drawable<T> d = da.getKey();
				if(d == null || d instanceof Content)
					continue;
				areaLoop: for(final Area a: da.getValue()) {
					Area aa = a.adjacent(axis);
					if(!aa.overlap(dims))
						break;
					final ArrayList<Area> al = new ArrayList<Area>();
					for(final Area p: aa) {
						final Elem<T> e = _get(p);
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
