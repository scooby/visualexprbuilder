/**
 * 
 */
package veb;

/**
 * @author ben
 *
 */
public final class ScalarLong extends Scalar {
	private final long val;
	public ScalarLong(final long x) {
		val = x;
	}
	@Override
	public double getDouble() {
		// TODO Auto-generated method stub
		return val;
	}
	@Override
	public long getLong() {
		return val;
	}
	@Override
	public Scalar scale(final int s) {
		return new ScalarLong(val * s);
	}
	@Override
	public boolean isIntegral() {
		// TODO Auto-generated method stub
		return true;
	}
	@Override
	public Scalar add(final Scalar other) {
		if(other == null)
			return this;
		if(!other.isIntegral())
			return other.add(this);
		return new ScalarLong(val + other.getLong());
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (val ^ val >>> 32);
		return result;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(final Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof ScalarLong))
			return false;
		final ScalarLong other = (ScalarLong) obj;
		if (val != other.val)
			return false;
		return true;
	}
}
