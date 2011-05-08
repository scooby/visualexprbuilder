package veb;

public interface Drawable {
	Vector getSize(); 
	void draw(Surface surf, Vector origin, Vector size);
	Drawable merge(Drawable o);
	boolean isEmpty();
}