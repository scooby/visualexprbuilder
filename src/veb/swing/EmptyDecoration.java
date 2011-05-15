package veb.swing;

import space.Area;
import space.Float;
import space.Vector;
import space.Vector.cardinal;
import veb.Drawable;

public class EmptyDecoration extends SwingDecoration {
	
	public EmptyDecoration(Area setup) {
		super(setup);
	}

	@Override
	public Drawable<Float> merge(Drawable<Float> o) {
		if(o != null && o instanceof EmptyDecoration) {
			EmptyDecoration ed = (EmptyDecoration) o;
			if(assignedGrid.canMergeWith(ed.assignedGrid) != null)
				return new EmptyDecoration(Area.coverage(assignedGrid, ed.assignedGrid));
		}
		return null;
	}

	@Override
	public cardinal perimetersTo(SwingDecoration other) {
		return null;
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	static Vector<Float> marginSize = new Vector<Float>(new Float(5.0f), new Float(5.0f));
	
	@Override
	public Vector<Float> getSize() {
		return marginSize;
	}

}
