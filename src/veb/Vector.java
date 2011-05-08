package veb;

public final class Vector {
	public final Scalar x;
	public final Scalar y;
	public Vector(Scalar x, Scalar y) {
		this.x = x;
		this.y = y;
	}
	public Vector(double x, double y) {
		this.x = Scalar.inst(x);
		this.y = Scalar.inst(y);
	}
	public Vector(long x, long y) {
		this.x = Scalar.inst(x);
		this.y = Scalar.inst(y);
	}
	public Vector(int x, int y) {
		this.x = Scalar.inst(x);
		this.y = Scalar.inst(y);
	}
	public Vector add(Vector o) {
		return new Vector(x.add(o.x), y.add(o.y));
	}
	public Vector subtract(Vector o) {
		return new Vector(x.subtract(o.x), y.subtract(o.y));
	}
	public Vector scale(int s) {
		return new Vector(x.scale(s), y.scale(s));
	}
	public Vector rotatecw() {
		return new Vector(y.scale(-1), x);
	}
	public Vector rotateccw() {
		return new Vector(y, x.scale(-1));
	}
	public Scalar getAxis(int a) {
		if(a == 0)
			return x;
		else if(a == 1)
			return y;
		else
			throw new AssertionError(a);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((x == null) ? 0 : x.hashCode());
		result = prime * result + ((y == null) ? 0 : y.hashCode());
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
		if (!(obj instanceof Vector))
			return false;
		Vector other = (Vector) obj;
		if (x == null) {
			if (other.x != null)
				return false;
		} else if (!x.equals(other.x))
			return false;
		if (y == null) {
			if (other.y != null)
				return false;
		} else if (!y.equals(other.y))
			return false;
		return true;
	}
}


