package veb;

import space.Area;
import space.Num;

/**
 * A 2-tuple of an Area and a Drawable.
 * @author ben
 *
 * @param <T> The unit type being used by the Drawable.
 */
public class Elem<T extends Num> {
	private final Area a;
	private final Drawable<T> d;
	public Elem(final Area a, final Drawable<T> d) {
		this.a = a;
		this.d = d;
	}
	public Area getA() {
		return a;
	}
	public Drawable<T> getD() {
		return d;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Elem [a=%s, d=%s]", a, d);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((a == null) ? 0 : a.hashCode());
		result = prime * result + ((d == null) ? 0 : d.hashCode());
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof Elem))
			return false;
		@SuppressWarnings("rawtypes")
		Elem other = (Elem) obj;
		if (a == null) {
			if (other.a != null)
				return false;
		} else if (!a.equals(other.a))
			return false;
		if (d == null) {
			if (other.d != null)
				return false;
		} else if (!d.equals(other.d))
			return false;
		return true;
	}

}