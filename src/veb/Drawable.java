package veb;

public interface Drawable {
	Vector getSize();
	Drawable merge(Drawable o);
	boolean isEmpty();
	@Override
	int hashCode();
	@Override
	boolean equals(Object o);
}
