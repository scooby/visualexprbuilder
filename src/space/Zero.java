package space;

import java.util.Iterator;

final public class Zero extends NumImpl {
	public Zero() {
	}
	static private Zero z = new Zero();
	static public Zero zero() {
		return z;
	}
	@Override
	public long longVal() {
		return 0;
	}

	@Override
	public double doubleVal() {
		return 0.0;
	}

	@Override
	public Num abs() {
		return this;
	}
	@Override
	public int sign() {
		return 0;
	}
	@Override
	public Num factor(int i) {
		return new Int(i);
	}

	@Override
	public Num add(Num v) {
		if(v == null || v instanceof Zero)
			return this;
		return v;
	}

	@Override
	public Num sub(Num v) {
		if(v == null || v instanceof Zero)
			return this;
		return v;
	}

	@Override
	public Num mult(Num v) {
		if(v == null || v instanceof Zero)
			return this;
		return v.factor(0);
	}

	@Override
	protected Iterable<Num> _spread(int o) {
		return new Iterable<Num>() {
			@Override
			public Iterator<Num> iterator() {
				return new Iterator<Num>() {
					boolean fired = false;
					@Override
					public boolean hasNext() {
						return !fired;
					}
					@Override
					public Num next() {
						fired = true;
						return new Zero();
					}
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}			
				};
			}
		};
	}

	@Override
	protected Integral boxInt(long v) {
		return new Long(v);
	}

	@Override
	protected Rational boxRat(double v) {
		return new Double(v);
	}
	
	/* (non-Javadoc)
	 * @see java.lang.Object#toString()
	 */
	@Override
	public String toString() {
		return String.format("Zero []");
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	final public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (int) (longVal() ^ (longVal() >>> 32));
		return result;
	}
	/** 
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	final public boolean equals(Object obj) {
		if (obj == null)
			return false;
		if (obj instanceof Zero)
			return true;
		if (obj instanceof IntegralImpl) {
			IntegralImpl other = (IntegralImpl) obj;
			return other.longVal() == 0;
		}
		if (obj instanceof RationalImpl) {
			RationalImpl other = (RationalImpl) obj;
			return other.doubleVal() == 0.0d;
		}
		return false;
	}
}
