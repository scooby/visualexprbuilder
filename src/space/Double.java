package space;
public final class Double extends RationalImpl {
	final double val;
	public Double(double val) { this.val = val; }
	@Override
	public long longVal() {	return (long) val;}
	@Override
	public double doubleVal() {	return val; }
	@Override
	public Num abs() { return val < 0.0d ? new Double(-val) : this; }
	@Override
	public int sign() {
		return java.lang.Double.compare(doubleVal(), 0.0d);
	}
	@Override
	public Num factor(int i) { return new Double(i); }
	@Override
	protected NumImpl boxRat(double val) {
		return new Double(val);
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
		long temp;
		temp = java.lang.Double.doubleToLongBits(val);
		result = prime * result + (int) (temp ^ (temp >>> 32));
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
		if (!(obj instanceof Double))
			return false;
		Double other = (Double) obj;
		if (java.lang.Double.doubleToLongBits(val) != java.lang.Double
				.doubleToLongBits(other.val))
			return false;
		return true;
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Double [val=%s]", val);
	}
}
