package veb.swing;

import java.util.ArrayList;
import java.util.List;

import veb.Area;
import veb.Drawable;
import veb.Surface;
import veb.Vector;

/**
 * @author ben
 * This is created to arrange the decorations and Swing objects as laid out by veb.Layout
 */
public class SwingSurface implements Surface {
	final private List<SwingDecoration> pdx;
	
	public SwingSurface() {
		pdx = new ArrayList<SwingDecoration>();
	}
	@Override
	public void draw(final Vector start, final Vector extent, final Area where, final Drawable dec) {
		
	}
	/**
	 * @return the pdx
	 */
	public List<SwingDecoration> getPdx() {
		return pdx;
	}
}
