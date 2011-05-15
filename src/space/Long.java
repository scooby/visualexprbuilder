package space;
public final class Long extends IntegralImpl {
	final private long val;
	public Long(long val) { this.val = val; }
	@Override
	public NumImpl boxInt(long val) { return new Long(val); }
	@Override
	public NumImpl boxRat(double val) { return new Double(val); }
	@Override
	public long longVal() {	return val;	}
	@Override
	public double doubleVal() {	return (double) val; }
	@Override
	public Num abs() { return val < 0 ? new Long(-val) : this; }
	@Override
	public Num factor(int i) { return new Long(i); }
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Long [val=%s]", val);
	}
}
