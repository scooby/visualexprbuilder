package veb.swing;

import java.util.Set;

import space.Float;
import space.Vector;
import space.Vector.cardinal;
import veb.Drawable;

public abstract class SwingDecoration implements Drawable<Float> {
	protected Vector<Float> assignedOrigin;
	protected Vector<Float> assignedExtent;
	//protected Area assignedGrid;
	
	public SwingDecoration() {
		//assignedGrid = null;
		assignedOrigin = null;
		assignedExtent = null;
	}
	
	/**
	 * @return the assignedOrigin
	 */
	public Vector<Float> getAssignedOrigin() {
		return assignedOrigin;
	}

	/**
	 * @param assignedOrigin the assignedOrigin to set
	 */
	public void setAssignedOrigin(Vector<Float> assignedOrigin) {
		this.assignedOrigin = assignedOrigin;
	}

	/**
	 * @return the assignedExtent
	 */
	public Vector<Float> getAssignedExtent() {
		return assignedExtent;
	}

	/**
	 * @param assignedExtent the assignedExtent to set
	 */
	public void setAssignedExtent(Vector<Float> assignedExtent) {
		this.assignedExtent = assignedExtent;
	}
	
	public abstract Set<cardinal> connections();

	public abstract cardinal perimetersTo(cardinal c, SwingDecoration other);
}
