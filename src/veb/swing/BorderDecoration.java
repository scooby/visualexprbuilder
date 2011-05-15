package veb.swing;

import java.util.EnumMap;

import space.Area;
import space.Float;
import space.Vector;
import space.Vector.cardinal;
import space.Vector.corner;
import veb.Drawable;

public class BorderDecoration extends SwingDecoration {
	private EnumMap<cardinal, border> borders;
	private EnumMap<corner, Boolean> filled;
	
	/**
	 * Returns the type of border running from the center of this decoration piece
	 * to the cardinal direction.
	 * @param c the cardinal direction that has a border.
	 * @return an enum representing the type of border, or none if there is none.
	 */
	public border getBorder(cardinal c) {
		if(borders.containsKey(c))
			return borders.get(c);
		return border.none;
	}
	/**
	 * Since borders cover N, S, E and W, we indicate whether the object is filled at
	 * the corners NE, SE, SW and NW. So a N/S piece might have NE and SE filled,
	 * but a SE corner piece would only have NW filled.
	 * @param c
	 * @return
	 */
	public boolean getFilled(corner c) {
		if(filled.containsKey(c))
			return filled.get(c).booleanValue();
		return false;
	}
	public BorderDecoration(Area setup) {
		super(setup);
		this.borders = new EnumMap<cardinal, border>(cardinal.class);
		this.filled = new EnumMap<corner, Boolean>(corner.class);
	}
	public static BorderDecoration cornerPiece(Area where, corner which, border type) {
		BorderDecoration bd = new BorderDecoration(where);
		bd.borders.put(which.ns(), type);
		bd.borders.put(which.ew(), type);
		bd.filled.put(which, Boolean.TRUE);
		return bd;
	}
	/**
	 * Draws a straight line *perpendicular* to the "component" param.
	 * For example, a N/S straight line that is East of the component should set "component" to west.
	 * @param where area on the grid this piece is located
	 * @param component this is the direction the component is in.
	 * @param type type of border.
	 * @return the decoration
	 */
	public static BorderDecoration straightPiece(Area where, cardinal component, border type) {
		BorderDecoration bd = new BorderDecoration(where);
		bd.borders.put(component.cw90(), type);
		bd.borders.put(component.ccw90(), type);
		bd.filled.put(component.cw45(), Boolean.TRUE);
		bd.filled.put(component.ccw45(), Boolean.TRUE);
		return bd;
	}

	@Override
	public Drawable<Float> merge(Drawable<Float> o) {
		if(o == null || !(o instanceof BorderDecoration))
			return null;
		BorderDecoration d = (BorderDecoration) o;
		cardinal c = assignedGrid.canMergeWith(d.assignedGrid);
		if(c == null)
			return null;
		Area ma = Area.coverage(assignedGrid, d.assignedGrid);
		if(getBorder(c.cw90()) == d.getBorder(c.cw90())
				&& getBorder(c.ccw90()) == d.getBorder(c.ccw90())) {
			BorderDecoration n = new BorderDecoration(ma);
			cardinal c180 = c.turn180();
			n.borders.put(c, d.getBorder(c));
			n.borders.put(c.cw90(), getBorder(c.cw90()));
			n.borders.put(c.ccw90(), getBorder(c.ccw90()));
			n.borders.put(c180, getBorder(c180));
			n.filled.put(c.cw45(), d.getFilled(c.cw45()));
			n.filled.put(c.ccw45(), d.getFilled(c.ccw45()));
			n.filled.put(c180.cw45(), getFilled(c180.cw45()));
			n.filled.put(c180.ccw45(), getFilled(c180.ccw45()));
			return n;
		}
		return null;
	}

	@Override
	public cardinal perimetersTo(SwingDecoration other) {
		if(other == null || !(other instanceof BorderDecoration))
			return null;
		BorderDecoration d = (BorderDecoration) other;
		cardinal c = assignedGrid.canMergeWith(d.assignedGrid);
		cardinal c180 = c.turn180();
		if(getBorder(c) != border.none && d.getBorder(c180) != border.none) {
			if(getFilled(c.cw45()))
				return c.cw90();
			if(getFilled(c.ccw45()))
				return c.ccw90();
			if(d.getFilled(c180.cw45()))
				return c180.cw90();
			if(d.getFilled(c180.ccw45()))
				return c180.ccw90();
			throw new AssertionError();
		}
		return null;
	}
	enum border {
		line,
		squiggly,
		none;
	}
	@Override
	public boolean isEmpty() {
		return false;
	}
	static Vector<Float> borderThickness = new Vector<Float>(new Float(3.0f), new Float(3.0f));
	@Override
	public Vector<Float> getSize() {
		// TODO Auto-generated method stub
		return borderThickness;
	}
}