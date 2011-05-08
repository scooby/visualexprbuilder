/**
 * 
 */
package veb;

import java.util.Collections;
import java.util.Iterator;

/**
 * @author ben
 *
 */
public abstract class Scalar {
	public abstract double getDouble();
	public long getLong() { return (long) getDouble(); }
	public int getInt() { return (int) getDouble(); }
	public abstract Scalar scale(int s);
	public static Scalar inst(double x) { return new ScalarDouble(x); }
	public static Scalar inst(long x) { return new ScalarLong(x); } 
	public static Scalar inst(int x) { return new ScalarInt(x); }
	public abstract Scalar add(Scalar other);
	public abstract boolean isIntegral();
	@Override
	public abstract boolean equals(Object o);
	@Override
	public abstract int hashCode();
	@Override
	public String toString() {
		return String.format("Scalar[%s]", isIntegral() ? getLong() : getDouble());
	}
	public Iterable<Scalar> spread(int o) {
		if(o == 0)
			return Collections.emptyList();
		if(o < 0)
			return scale(-1)._spread(-o);
		return _spread(o);
	}
	public Scalar divide(int divisor) {
		if(isIntegral())
			return Scalar.inst(getLong() / divisor + (getLong() % divisor == 0 ? 0 : 1));
		else
			return Scalar.inst(getDouble() / divisor);
	}
	public Scalar subtract(Scalar other) {
		return add(other.scale(-1));
	}
	protected Iterable<Scalar> _spread(final int o) {
		assert o > 0; // Positive
		if(isIntegral()) {
			return new Iterable<Scalar>() {
				@Override
				public Iterator<Scalar> iterator() {
					return new Iterator<Scalar>() {
						double d = getDouble();
						int i = 0;
						@Override
						public boolean hasNext() {
							return i < o;
						}
						@Override
						public Scalar next() {
							i++;
							return Scalar.inst(((long) d * i / o)
									- ((long) d * (i-1) / o));
						}
						@Override
						public void remove() {
							throw new AssertionError();
						}
					};
				}
			};
		} else {
			return new Iterable<Scalar>() {
				@Override
				public Iterator<Scalar> iterator() {
					return new Iterator<Scalar>() {
						double d = getDouble();
						Scalar step = Scalar.inst(getDouble() / o);
						@Override
						public boolean hasNext() {
							return d >= 0.0d;
						}
						@Override
						public Scalar next() {
							if(d > step.getDouble()) {
								d -= step.getDouble();
								return step;
							} else {
								Scalar last = Scalar.inst(d);
								d = 0.0d;
								return last;
							}
						}
						@Override
						public void remove() {
							throw new AssertionError();
						}
					};
				}
			};
		}
	}
}