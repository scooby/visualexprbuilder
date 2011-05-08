package veb;

public interface Content extends Drawable {
	Drawable getDecoration(int s, int t);
	final public static int decoExtent = 2;
	final public static int decoThick = 2 * decoExtent + 1;
}