package veb.swing;

import space.Float;
import space.Area;
import space.Vector;
import space.Vector.cardinal;
import veb.Drawable;

public abstract class SwingDecoration implements Drawable<Float> {
	protected Vector<Float> assignedOrigin;
	protected Vector<Float> assignedExtent;
	protected Area assignedGrid;
	protected SwingDecoration[] connections;
	
	public SwingDecoration(Area setup) {
		assignedGrid = setup;
		assignedOrigin = null;
		assignedExtent = null;
	}
	
	@Override
	abstract public Drawable<Float> merge(Drawable<Float> o);
	
	abstract public cardinal perimetersTo(SwingDecoration other);
}
