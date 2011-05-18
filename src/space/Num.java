package space;

public interface Num {
	public long longVal();
	public int intVal();
	public double doubleVal();
	public float floatVal();
	public Num abs();
	public Num factor(int i);
	public Num scale(int s);
	public Iterable<Num> spread(int i);
	public Num add(Num v);
	public Num sub(Num v);
	public Num mult(Num v);
	public int sign();
	@Override
	public boolean equals(Object o);
	@Override
	public int hashCode(); 
}
