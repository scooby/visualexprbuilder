package veb;

import space.Vector;
import space.Num;

public interface Drawable<T extends Num> {
	Vector<T> getSize();
	Drawable<T> merge(Drawable<T> o);
	boolean isEmpty();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
