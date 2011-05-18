package space;

public final class Float extends RationalImpl {
	final float val;
	public Float(float val) { this.val = val; }
	@Override
	public long longVal() {	return (long) val;}
	@Override
	public double doubleVal() {	return val; }
	@Override
	public float floatVal() { return val; }
	@Override
	public Num abs() { return val < 0.0f ? new Float(-val) : this; }
	@Override
	public int sign() {
		return java.lang.Float.compare(val, 0.0f);
	}
	@Override
	public Num factor(int i) { return new Float(i); }
	@Override
	protected NumImpl boxRat(double val) {
		return new Float((float) val);
	}
	@Override
	protected Integral boxInt(long v) {
		return new Long(v);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + java.lang.Float.floatToIntBits(val);
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
		if (!(obj instanceof Float))
			return false;
		Float other = (Float) obj;
		if (java.lang.Float.floatToIntBits(val) != java.lang.Float
				.floatToIntBits(other.val))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Float [val=%s]", val);
	}
}
