package veb.swing;

import java.util.Collections;
import java.util.Set;

import space.Float;
import space.Vector;
import space.Vector.cardinal;
import veb.Drawable;

public class EmptyDecoration extends SwingDecoration {
	
	public EmptyDecoration() {
		super();
	}

	@Override
	public Drawable<Float> merge(cardinal c, Drawable<Float> o) {
		if(o != null && o instanceof EmptyDecoration) {
			//EmptyDecoration ed = (EmptyDecoration) o;
			//if(assignedGrid.canMergeWith(ed.assignedGrid) != null)
			return this;
		}
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

	@Override
	public Set<cardinal> connections() {
		// TODO Auto-generated method stub
		return Collections.emptySet();
	}

	@Override
	public cardinal perimetersTo(cardinal c, SwingDecoration other) {
		// TODO Auto-generated method stub
		return null;
	}

}
