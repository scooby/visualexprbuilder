/**
 * 
 */
package veb;

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
}