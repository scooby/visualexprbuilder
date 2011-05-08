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
	private Grid(Area a) {
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
	private void setCell(int x, int y, Elem e) {
		ex[y * rowwidth + x] = e;
		Drawable d = e.getD();
		if(!m.containsKey(d))
			m.put(d, new HashSet<Area>());
		m.get(d).add(e.getA());
	}
	public Area getArea(int x, int y) {
		return ex[y * rowwidth + x].getA();
	}
	public Drawable getDrawable(int x, int y) {
		return ex[y * rowwidth + x].getD();
	}
	final static private int dt = Content.decoThick;
	final static private int de = Content.decoExtent;	
	private void setArea(Area a, Content c) {
		Area ai = a.inset(de);
		Elem e = new Elem(ai, c);
		for(Area p : a) {
			if(p.overlap(ai))
				setCell(p.sx, p.sy, e);
			else {
				Drawable d = c.getDecoration(ai.xdist(p), ai.ydist(p));
				setCell(p.sx, p.sy, new Elem(p, d));
			}
		}
	}
	private void setCells(Area a, Drawable d) {
		Elem e = new Elem(a, d);
		for(Area p : a) {
			setCell(p.sx, p.sy, e);
		}
	}
	private void removeArea(Area a) {
		for(Area p: a) {
			int i = p.sx + rowwidth * p.sy;
			Elem e = ex[i];
			ex[i] = null;
			Drawable d = e.getD();
			Area ea = e.getA();
			Set<Area> ax = m.get(d);
			ax.remove(ea);
			if(ax.isEmpty())
				m.remove(d);
		}
	}
	public static Grid fromMap(Map<Content, Area> m) {
		if(m.isEmpty()) {
			return new Grid(new Area(0, 0, 0, 0));
		}
		Area ac = Area.coverage(m.values());
		Area dims = ac.zeroize().scale(dt);
		Grid g = new Grid(dims);
		for(Entry<Content, Area> e : m.entrySet()) {
			g.setArea(e.getValue().offset(-ac.sx, -ac.sy).scale(dt), e.getKey());
		}
		return g;
	}
	public Iterator<Elem> iterator() {
		ArrayList<Elem> al = new ArrayList<Elem>();
		for(Entry<Drawable, Set<Area>> da: m.entrySet()) {
			Drawable d = da.getKey();
			for(Area a : da.getValue()) {
				al.add(new Elem(a, d));
			}
		}
		return al.iterator();
	}
	public void mergeDecorations() {
		for(int axis = 0; axis < 2; axis++) {
			for(Entry<Drawable, Set<Area>> da: m.entrySet()) {
				Drawable d = da.getKey();
				if(d == null || d instanceof Content)
					continue;
				for(Area a: da.getValue()) {
					Area aa;
					do {
						aa = a.adjacent(axis);
						if(aa.width() > 1 || aa.height() > 1)
							break;
						Elem e = ex[aa.sy * rowwidth + aa.sx];
						Drawable dd = e.getD();
						if(dd == null || dd.isEmpty())
							continue;
						Drawable dm = d.merge(dd);
						if(dm == null)
							continue;
						aa = Area.coverage(a, aa);
						removeArea(aa);
						setCells(aa, dm);
					} while(aa.overlap(dims));
				}
			}
		}
	}
	public void simplify() {
		for(int axis = 0; axis < 2; axis++) {
			for(Entry<Drawable, Set<Area>> da: m.entrySet()) {
				Drawable d = da.getKey();
				if(d == null || d instanceof Content)
					continue;
				areaLoop: for(Area a: da.getValue()) {
					Area aa = a.adjacent(axis);
					if(!aa.overlap(dims))
						break;
					ArrayList<Area> al = new ArrayList<Area>();
					for(Area p: aa) {
						Elem e = ex[p.sy * rowwidth + p.sx];
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
}
