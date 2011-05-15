package space;
import java.util.Iterator;


public abstract class IntegralImpl extends NumImpl {
	/* (non-Javadoc)
	 * @see space.Num#add(space.Num)
	 */
	@Override
	public Num add(Num v) {
		return boxInt(longVal() + v.longVal());
	}
	/* (non-Javadoc)
	 * @see space.Num#sub(space.Num)
	 */
	@Override
	public Num sub(Num v) {
		return boxInt(longVal() - v.longVal());
	}
	/* (non-Javadoc)
	 * @see space.Num#mult(space.Num)
	 */
	@Override
	public Num mult(Num v) {
		return boxInt(longVal() * v.longVal());
	}
	/* (non-Javadoc)
	 * @see space.NumImpl#boxInt(long)
	 */
	@Override
	public int sign() {
		final long lv = longVal();
		return lv > 0 ? 1 : (lv == 0 ? 0 : -1);
	}
	@Override
	protected Iterable<Num> _spread(final int o) {
		assert o > 0; // Positive
		return new Iterable<Num>() {
			@Override
			public Iterator<Num> iterator() {
				return new Iterator<Num>() {
					final long v = longVal();
					int i = 0;
					@Override
					public boolean hasNext() { return i < o; }
					@Override
					public Num next() {
						i++;
						return boxInt(v * i / o
								- v * (i-1) / o);
					}
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
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
	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	final public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (!(obj instanceof IntegralImpl))
			return false;
		IntegralImpl other = (IntegralImpl) obj;
		if (longVal() != other.longVal())
			return false;
		return true;
	}
}
