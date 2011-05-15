package space;

public final class Int extends IntegralImpl {
	final private int val;
	public Int(int val) { this.val = val; }
	@Override
	public NumImpl boxInt(long val) { return new Int((int) val); }
	@Override
	public NumImpl boxRat(double val) { return new Float((float) val); }
	@Override
	public long longVal() {	return val;	}
	@Override
	public double doubleVal() {	return (double) val; }
	@Override
	public Num abs() { return val < 0 ? new Int(-val) : this; }
	@Override
	public Num factor(int i) { return new Int(i); }
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Int [val=%s]", val);
	}
}
