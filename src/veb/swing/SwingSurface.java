package veb.swing;

import java.util.Map;
import java.util.HashMap;

import veb.Area;
import veb.Drawable;
import veb.Surface;
import veb.Vector;

/**
 * @author ben
 * This is created to arrange the decorations and Swing objects as laid out by veb.Layout
 */
public class SwingSurface implements Surface {
	final private Map<Area, SwingDecoration> decMap;
	
	public SwingSurface() {
		decMap = new HashMap<Area, SwingDecoration>();
	}
	@Override
	public void draw(final Vector start, final Vector extent, final Area where, final Drawable dec) {
		// look up all possible connections
		// hook into existing pieces in the map
	}
}
