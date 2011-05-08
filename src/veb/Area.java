package veb;

import java.util.Iterator;

public final class Area implements Iterable<Area> {
	final public int sx;
	final public int ex;
	final public int sy;
	final public int ey;
	public Area(int sx, int sy, int ex, int ey) {
		assert ex >= sx && ey >= sy && sx >= 0 && sy >= 0;
		this.sx = sx;
		this.ex = ex;
		this.sy = sy;
		this.ey = ey;
	}
	public int width() { return ex - sx; }
	public int height() { return ey - sy; }
	public Area scale(int s) {
		return new Area(sx * s, sy * s, ex * s, ey * s);
	}
	public Area inset(int i) {
		return new Area(sx + i, sy + i, ex - i, ey - i);
	}
	public Area offset(int x, int y) {
		return new Area(sx + x, sy + y, ex + x, ey + y);
	}
	public boolean overlap(Area o) {
		if(o == null)
			return false;
		return sx < o.ex && o.sx < ex && sy < o.ey && o.sy < ey;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Area [sx=%s, ex=%s, sy=%s, ey=%s]", sx, ex, sy,
				ey);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ex;
		result = prime * result + ey;
		result = prime * result + sx;
		result = prime * result + sy;
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Area))
			return false;
		Area other = (Area) obj;
		if (ex != other.ex)
			return false;
		if (ey != other.ey)
			return false;
		if (sx != other.sx)
			return false;
		if (sy != other.sy)
			return false;
		return true;
	}
	public Iterator<Area> iterator() {
		return new Iterator<Area>() {
			int x = sx;
			int y = sy;
			@Override
			public boolean hasNext() {
				return y < ey;
			}
			@Override
			public Area next() {
				Area a = new Area(x, y, x + 1, y + 1);
				x++;
				if(x >= ex) {
					x = sx;
					y++;
				}
				return a;
			}
			@Override
			public void remove() {
				throw new AssertionError();
			}
		};
	}
	public int xdist(Area o) {
		if(sx < o.ex && o.sx < ex)
			return 0;
		if(o.ex < sx)
			return o.ex - sx;
		if(o.sx > ex)
			return o.sx - ex;
		throw new AssertionError(o);
	}
	public int ydist(Area o) {
		if(sy < o.ey && o.sy < ey)
			return 0;
		if(o.ey < sy)
			return o.ey - sy;
		if(o.sy > ey)
			return o.sy - ey;
		throw new AssertionError(o);
	}
	public Area adjacent(int axis) {
		switch(axis) {
		case 0:
			return new Area(ex, sy, ex + 1, ey);
		case 1:
			return new Area(sx, ey, ex, ey + 1);
		}
		throw new IllegalArgumentException();
	}
	public Area zeroize() {
		return new Area(0, 0, ex - sx, ey - sy);
	}
	public final static Area ZERO_AREA = new Area(0, 0, 0, 0);
	public static Area coverage(Iterable<Area> ax) {
		Iterator<Area> i = ax.iterator();
		if(!i.hasNext()) {
			return ZERO_AREA;
		}
		int lx, ly, hx, hy;
		Area a = i.next();
		lx = a.sx; ly = a.sy; hx = a.ex; hy = a.ey;
		while(i.hasNext()) {
			if(a.sx < lx) lx = a.sx;
			if(a.sy < ly) ly = a.sy;
			if(a.ex > hx) hx = a.ex;
			if(a.ey > hy) hy = a.ey;
		}
		return new Area(lx, ly, hx, hy);
	}
	public static Area coverage(Area... ax) {
		return coverage(ax);
	}
	public boolean coveredBy(Iterable<Area> ax) {
		int rowwidth = width();
		int cells = rowwidth * height();
		boolean[] c = new boolean[cells];
		for(Area a : ax) {
			for(Area p : a) {
				if(!overlap(p))
					return false;
				int i = (p.sy - sy) * rowwidth + p.sx - sx;
				assert !c[i];
				c[i] = true;
				cells--;
			}
		}
		return cells == 0;
	}
	public boolean extendedBy(Iterable<Area> ax) {
		Area ext = coverage(ax);
		if(!ext.coveredBy(ax))
			return false;
		return (ex == ext.sx && sy == ext.sy && ey == ext.ey)
			|| (ey == ext.sy && sx == ext.sx && ex == ext.ex);
	}
}
