/**
 * 
 */
package veb;

import space.Area;
import space.Vector;
import space.Num;

/**
 * @author ben
 *
 */
public interface Surface<T extends Num> {
	void draw(Vector<T> origin, Vector<T> size, Area where, Drawable<T> dec);
}