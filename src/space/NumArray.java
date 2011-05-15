package space;

import space.Num;

public class NumArray<T extends Num> {
	private final int[] dims;
	private final T[] data;
	@SuppressWarnings("unchecked")
	public NumArray(int... dims) {
		this.dims = dims;
		assert dims.length > 0 && dims.length < 5;
		int sz = size();
		Num[] d = new Num[sz];
		Zero z = new Zero();
		for(int i = 0; i < sz; i++)
			d[i] = z;
		data = (T[]) d;
	}
	public int size() {
		int sz = 1;
		for(int j : dims) {
			sz *= j;
		}
		return sz;
	}
	public int size(int a) {
		return dims[a];
	}
	public T get(int a) {
		return data[idx(a)];
	}
	public T get(int a, int b) {
		return data[idx(a, b)];
	}
	public T get(int a, int b, int c) {
		return data[idx(a, b, c)];
	}
	public T get(int a, int b, int c, int d) {
		return data[idx(a, b, c, d)];
	}
	public void set(int a, T v) {
		data[idx(a)] = v;
	}
	public void set(int a, int b, T v) {
		data[idx(a, b)] = v;
	}
	public void set(int a, int b, int c, T v) {
		data[idx(a, b, c)] = v;
	}
	public void set(int a, int b, int c, int d, T v) {
		data[idx(a, b, c, d)] = v;
	}
	public T pop(int a, T v) {
		int i = idx(a);
		T r = data[i];
		data[i] = v;
		return r;
	}
	public T pop(int a, int b, T v) {
		int i = idx(a, b);
		T r = data[i];
		data[i] = v;
		return r;
	}
	public T pop(int a, int b, int c, T v) {
		int i = idx(a, b, c);
		T r = data[i];
		data[i] = v;
		return r;
	}
	public T pop(int a, int b, int c, int d, T v) {
		int i = idx(a, b, c, d);
		T r = data[i];
		data[i] = v;
		return r;
	}
	public int idx(int... dim) {
		assert dim.length == dims.length;
		int i = 0;
		int m = 1;
		for(int j = dim.length - 1; j>= 0; j--) {
			i += m * dim[j];
			m *= dims[j];
		}
		return i;
	}
	public void add(int a, T v) {
		int i = idx(a);
		if(v == null)
			return;
		data[i] = NumImpl.add(v, data[i]);
	}
	public void add(int a, int b, T v) {
		int i = idx(a, b);
		if(v == null)
			return;
		data[i] = NumImpl.add(v, data[i]);
	}
	public void add(int a, int b, int c, T v) {
		int i = idx(a, b, c);
		if(v == null)
			return;
		data[i] = NumImpl.add(v, data[i]);
	}
	public void add(int a,int b, int c, int d, T v) {
		int i = idx(a, b, c, d);
		if(v == null)
			return;
		data[i] = NumImpl.add(v, data[i]);
	}
}
