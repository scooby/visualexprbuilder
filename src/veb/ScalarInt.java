package veb;

public final class ScalarInt extends Scalar {
	private final int val;
	public ScalarInt(int x) {
		val = x;
	}
	@Override
	public double getDouble() {
		return (double) val;
	}
	@Override
	public long getLong() {
		return val;
	}
	@Override
	public int getInt() {
		return val;
	}
	@Override
	public Scalar scale(int s) {
		return new ScalarInt(val * s);
	}
	@Override
	public Scalar add(Scalar other) {
		if(other == null)
			return this;
		if(other instanceof ScalarInt)
			return new ScalarInt(val + ((ScalarInt) other).val);
		return other.add(this);
	}
	@Override
	public boolean isIntegral() {
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + val;
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
		if (!(obj instanceof ScalarInt))
			return false;
		ScalarInt other = (ScalarInt) obj;
		if (val != other.val)
			return false;
		return true;
	}
}
