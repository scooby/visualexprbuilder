package veb.swing;

import veb.Area;
import veb.Drawable;
import veb.Vector;

public abstract class SwingDecoration implements Drawable {
	protected Vector assignedOrigin;
	protected Vector assignedExtent;
	protected Area assignedGrid;
	protected SwingDecoration[] connections;
	
	@Override
	abstract public Vector getSize();

	@Override
	abstract public Drawable merge(Drawable o);

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

	abstract public boolean perimetersTo(Area otherGrid, SwingDecoration other);
}
