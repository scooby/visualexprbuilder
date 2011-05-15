package space;

import java.util.Iterator;
import space.Vector.cardinal;

/**
 * @author ben
 *
 */
public final class Area implements Iterable<Area>, Comparable<Area> {
	/**
	 * The column the area starts on; also the westernmost column.
	 */
	final public int sx;
	/**
	 * The first column that is not part of the area.
	 * That is, for every column x, x is in the area iff sx <= x < ex.
	 */
	final public int ex;
	/**
	 * The row the area starts on; also the "southernmost" row.
	 */
	final public int sy;
	/**
	 * The first row that is not part of the area.
	 * That is, for every row y, y is in the area iff sy <= y < ey.
	 */	
	final public int ey;
	/**
	 * @param sx First column of the area.
	 * @param sy First row of the area.
	 * @param ex Column after the last column.
	 * @param ey Row after the last row.
	 */
	public Area(final int sx, final int sy, final int ex, final int ey) {
		assert ex >= sx && ey >= sy && sx >= 0 && sy >= 0;
		this.sx = sx;
		this.ex = ex;
		this.sy = sy;
		this.ey = ey;
	}
	/**
	 * @return number of columns in area.
	 */
	public int width() { return ex - sx; }
	/**
	 * @return number of rows in area.
	 */
	public int height() { return ey - sy; }
	/**
	 * @return width * height
	 */
	public int area() { return width() * height(); }
	/**
	 * @param s Scaling factor.
	 * @return New area with origin and area scaled by s.
	 */
	public Area scale(final int s) {
		if(s >= 0)
			return new Area(sx * s, sy * s, ex * s, ey * s);
		else
			return new Area(ex * s, ey * s, sx * s, sy * s);
	}
	public Area inset(final int i) {
		return new Area(sx + i, sy + i, ex - i, ey - i);
	}
	public Area offset(final int x, final int y) {
		return new Area(sx + x, sy + y, ex + x, ey + y);
	}
	public boolean overlap(final Area o) {
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
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Area))
			return false;
		final Area other = (Area) obj;
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
	/* (non-Javadoc)
	 * @see java.lang.Iterable#iterator()
	 */
	@Override
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
				final Area a = new Area(x, y, x + 1, y + 1);
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
	/**
	 * Number of empty columns between two areas.
	 * @param other area
	 * @return distance
	 */
	public int xdist(final Area o) {
		if(sx < o.ex && o.sx < ex)
			return 0;
		if(o.ex < sx)
			return o.ex - sx;
		if(o.sx > ex)
			return o.sx - ex;
		throw new AssertionError(o);
	}
	/**
	 * Number of empty rows between two areas.
	 * @param other area
	 * @return distance
	 */
	public int ydist(final Area o) {
		if(sy < o.ey && o.sy < ey)
			return 0;
		if(o.ey < sy)
			return o.ey - sy;
		if(o.sy > ey)
			return o.sy - ey;
		throw new AssertionError(o);
	}
	/**
	 * Creates an area adjacent to this one that is one row or column thick.
	 * @param axis 0: creates a column thick area to the right, 1: creates a row area below
	 * @return New area created.
	 */
	public Area adjacent(final int axis) {
		switch(axis) {
		case 0:
			return new Area(ex, sy, ex + 1, ey);
		case 1:
			return new Area(sx, ey, ex, ey + 1);
		}
		throw new IllegalArgumentException();
	}
	/**
	 * @return new area at origin
	 */
	public Area zeroize() {
		return new Area(0, 0, ex - sx, ey - sy);
	}
	public final static Area ZERO_AREA = new Area(0, 0, 0, 0);
	/**
	 * @return an area that covers all the areas in iterable.
	 */
	public static Area coverage(final Iterable<Area> ax) {
		final Iterator<Area> i = ax.iterator();
		if(!i.hasNext())
			return ZERO_AREA;
		int lx, ly, hx, hy;
		final Area a = i.next();
		lx = a.sx; ly = a.sy; hx = a.ex; hy = a.ey;
		while(i.hasNext()) {
			if(a.sx < lx) lx = a.sx;
			if(a.sy < ly) ly = a.sy;
			if(a.ex > hx) hx = a.ex;
			if(a.ey > hy) hy = a.ey;
		}
		return new Area(lx, ly, hx, hy);
	}
	/**
	 * @return an area that covers all the areas in parameters.
	 */
	public static Area coverage(final Area... ax) {
		return coverage(ax);
	}
	/**
	 * @return whether or not this area is fully covered by the parameters
	 */
	public boolean coveredBy(final Iterable<Area> ax) {
		final int rowwidth = width();
		int cells = rowwidth * height();
		final boolean[] c = new boolean[cells];
		for(final Area a : ax)
			for(final Area p : a) {
				if(!overlap(p))
					return false;
				final int i = (p.sy - sy) * rowwidth + p.sx - sx;
				assert !c[i];
				c[i] = true;
				cells--;
			}
		return cells == 0;
	}
	/**
	 * Example of extended by:
	 * <pre>
	 * +------+---+
	 * |      | A |   'this' is extended by {A, B}.
	 * | this +---+
	 * |      | B |
	 * +------+---+
	 * </pre>
	 * @return whether or not the iterable of areas extend this area
	 */
	public boolean extendedBy(final Iterable<Area> ax) {
		final Area ext = coverage(ax);
		if(!ext.coveredBy(ax))
			return false;
		return ex == ext.sx && sy == ext.sy && ey == ext.ey
		|| ey == ext.sy && sx == ext.sx && ex == ext.ex;
	}
	/**
	 * 
	 * @return the direction in which this area can merge
	 * 
	 */
	public cardinal canMergeWith(Area a) {
		if(sy == a.sy && ey == a.ey) {
			// Cases: East/west
			if(sx == a.ex)
				return cardinal.west;
			if(ex == a.sx)
				return cardinal.east;
			return null;
		} else if(sx == a.sx && ex == a.ex) {
			// North/South
			if(sy == a.ey)
				return cardinal.south;
			if(ey == a.sy)
				return cardinal.north;
			return null;
		} else
			return null;
	}
	
	/**
	 * Order by rows, then columns, then area, then width.
	 */
	@Override
	public int compareTo(final Area other) {
		int c = compare(sy, other.sy);
		if(c != 0) return c;
		c = compare(sx, other.sx);
		if(c != 0) return c;
		c = compare(area(), other.area());
		if(c != 0) return c;
		return compare(width(), other.width());
	}
	static private int compare(int a, int b) {
		return a == b ? 0 : (a < b ? -1 : 1);
	}
}