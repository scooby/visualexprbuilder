package veb;
import space.Num;

public interface Content<T extends Num> extends Drawable<T> {
	/**
	 * Each content node is going to need to draw some amount of decoration or other stuff around it.
	 * This method allows it to add additional content in all directions.
	 * @param s
	 * @param t
	 * @return
	 */
	Drawable<T> getDecoration(int s, int t);
	/**
	 * This constant indicates how many layers of decoration there is around content.
	 */
	final public static int DECO_EXTENT = 2;
	/**
	 * This constant indicates how many layers are in a single row or column. It is DECO_EXTENT
	 * times 2 pus 1 because there is the content layer, and then DECO_EXTENT layers on each side.
	 */
	final public static int DECO_THICK = 2 * DECO_EXTENT + 1;
}