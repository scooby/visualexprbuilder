package veb.swing;

import java.util.Map;
import java.util.HashMap;

import space.Area;
import space.Float;
import space.Vector;
import veb.Drawable;
import veb.Surface;

/**
 * @author ben
 * This is created to arrange the decorations and Swing objects as laid out by veb.Layout
 */
public class SwingSurface implements Surface<Float> {
	final private Map<Area, SwingDecoration> decMap;
	
	public SwingSurface() {
		decMap = new HashMap<Area, SwingDecoration>();
	}
	@Override
	public void draw(Vector<Float> start, Vector<Float> extent, Area where, Drawable<Float> dec) {
		// look up all possible connections
		// hook into existing pieces in the map
		
	}
}
