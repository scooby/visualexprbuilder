/**
 * 
 */
package veb;

/**
 * @author ben
 *
 */
public final class ScalarDouble extends Scalar {
	final double val;
	public ScalarDouble(double x) { val = x; }
	@Override
	public double getDouble() {
		return val;
	}
	@Override
	public Scalar scale(int s) {
		return new ScalarDouble(val * s);
	}
	@Override
	public boolean isIntegral() {
		// TODO Auto-generated method stub
		return false;
	}
	@Override
	public Scalar add(Scalar other) {
		return new ScalarDouble(val + other.getDouble());
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		long temp;
		temp = Double.doubleToLongBits(val);
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
		if (!(obj instanceof ScalarDouble))
			return false;
		ScalarDouble other = (ScalarDouble) obj;
		if (Double.doubleToLongBits(val) != Double.doubleToLongBits(other.val))
			return false;
		return true;
	}
}
