package space;
import java.util.Collections;

public abstract class NumImpl implements Rational, Integral {
	@Override
	public Integral addInt(Num o) {	
		if(o == null)
			return this;
		return boxInt(longVal() + o.longVal());	
	}
	@Override
	public Integral subInt(Num o) {	
		if(o == null)
			return this;
		return boxInt(longVal() - o.longVal());	
	}
	@Override
	public Integral multInt(Num o) { 
		if(o == null)
			return boxInt(0);
		return boxInt(longVal() * o.longVal()); 
	}
	@Override
	public Integral divInt(Num o) {	
		if(o == null)
			throw new ArithmeticException();
		return boxInt(longVal() / o.longVal());	
	}
	@Override
	public Rational addRat(Num o) { 
		if(o == null)
			return this;
		return boxRat(doubleVal() + o.doubleVal()); 
	}
	@Override
	public Rational subRat(Num o) { 
		if(o == null)
			return this;
		return boxRat(doubleVal() - o.doubleVal()); 
	}
	@Override
	public Rational multRat(Num o) { 
		if(o == null)
			return boxRat(0);
		return boxRat(doubleVal() * o.doubleVal()); 
	}
	@Override
	public Rational divRat(Num o) { 
		if(o == null)
			throw new ArithmeticException();
		return boxRat(doubleVal() / o.doubleVal()); 
	}
	public Iterable<Num> spread(final int o) {
		if(o == 0)
			return Collections.emptyList();
		if(o < 0)
			return ((NumImpl) multInt(new Long(-1)))._spread(-o);
		return _spread(o);
	}
	@SuppressWarnings("unchecked")
	public static <T extends Num> Iterable<T> spread(T val, final int o) {
		return (Iterable<T>) val.spread(o);
	}
	protected abstract Iterable<Num> _spread(final int o);
	protected abstract Integral boxInt(long v);
	protected abstract Rational boxRat(double v);
	@Override 
	public Num scale(int s) {
		return mult(factor(s));
	}
	@Override
	public abstract boolean equals(Object o);
	@Override
	public abstract int hashCode();
	@Override
	public abstract String toString();
	/**
	 * Typesafe version of add.
	 * @param <T> We always get the same type back as the LHS.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Num> T add(T a, Num b) {
		return (T) a.add(b);
	}
	/**
	 * Typesafe version of subtract.
	 * @param <T> We always get the same type back as the LHS.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Num> T sub(T a, Num b) {
		return (T) a.sub(b);
	}
	/**
	 * Typesafe version of multiply.
	 * @param <T> We always get the same type back as the LHS.
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public static <T extends Num> T mult(T a, Num b) {
		return (T) a.mult(b);
	}
}