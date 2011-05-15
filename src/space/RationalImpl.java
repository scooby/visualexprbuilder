package space;
import java.util.Iterator;


public abstract class RationalImpl extends NumImpl {
	/* (non-Javadoc)
	 * @see space.Num#add(space.Num)
	 */
	@Override
	public Num add(Num v) {
		return boxRat(doubleVal() + v.doubleVal());
	}
	/* (non-Javadoc)
	 * @see space.Num#sub(space.Num)
	 */
	@Override
	public Num sub(Num v) {
		return boxRat(doubleVal() - v.doubleVal());
	}
	/* (non-Javadoc)
	 * @see space.Num#mult(space.Num)
	 */
	@Override
	public Num mult(Num v) {
		return boxRat(doubleVal() * v.doubleVal());
	}
	/* (non-Javadoc)
	 * @see space.NumImpl#boxInt(long)
	 */
	protected Iterable<Num> _spread(final int o) {
		assert o > 0; // Positive
		return new Iterable<Num>() {
			@Override
			public Iterator<Num> iterator() {
				return new Iterator<Num>() {
					final double v = doubleVal();
					final Num s = boxRat(v / o);
					int i = 0;
					@Override
					public boolean hasNext() { return i < o; }
					@Override
					public Num next() {
						i++;
						return s;
					}
					@Override
					public void remove() {
						throw new UnsupportedOperationException();
					}
				};
			}
		};
	}
}
