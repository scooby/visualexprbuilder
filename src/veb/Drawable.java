package veb;

import space.Vector;
import space.Num;
import space.Vector.cardinal;

public interface Drawable<T extends Num> {
	Vector<T> getSize();
	Drawable<T> merge(cardinal c, Drawable<T> o);
	boolean isEmpty();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
