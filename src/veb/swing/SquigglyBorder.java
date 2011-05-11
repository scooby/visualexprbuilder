package veb.swing;

import veb.Drawable;
import veb.Vector;

public abstract class SquigglyBorder implements Drawable {

	@Override
	abstract public Vector getSize();

	@Override
	abstract public Drawable merge(Drawable o);

	@Override
	public boolean isEmpty() {
		// TODO Auto-generated method stub
		return false;
	}

}
