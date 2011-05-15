package space;

public final class Vector<T extends Num> {
	public final T x;
	public final T y;
	public Vector(final T x, final T y) {
		this.x = x;
		this.y = y;
	}
	@SuppressWarnings("unchecked")
	public Vector<T> add(final Vector<T> o) {
		return new Vector<T>((T) x.add(o.x), (T) y.add(o.y));
	}
	@SuppressWarnings("unchecked")
	public Vector<T> subtract(final Vector<T> o) {
		return new Vector<T>((T) x.sub(o.x), (T) y.sub(o.y));
	}
	@SuppressWarnings("unchecked")
	public Vector<T> scale(final int s) {
		return new Vector<T>((T) x.scale(s), (T) y.scale(s));
	}
	@SuppressWarnings("unchecked")
	public Vector<T> scale(final int sx, final int sy) {
		return new Vector<T>((T) x.scale(sx), (T) y.scale(sy));
	}
	@SuppressWarnings("unchecked")
	public Vector<T> rotatecw() {
		return new Vector<T>((T) y.scale(-1), x);
	}
	@SuppressWarnings("unchecked")
	public Vector<T> rotateccw() {
		return new Vector<T>(y, (T) x.scale(-1));
	}
	public T getAxis(final int a) {
		if(a == 0)
			return x;
		else if(a == 1)
			return y;
		else
			throw new AssertionError(a);
	}
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + (x == null ? 0 : x.hashCode());
		result = prime * result + (y == null ? 0 : y.hashCode());
		return result;
	}
	public Vector<Int> unitInt() {
		return new Vector<Int>(new Int(x.sign()), new Int(y.sign()));
	}
	public cardinal getCardinal() {
		switch(x.sign()) {
		case -1:
			return y.sign() == 0 ? cardinal.west : null;
		case 1:
			return y.sign() == 0 ? cardinal.east : null;
		default:
			switch(y.sign()) {
			case -1:
				return cardinal.south;
			case 1:
				return cardinal.north;
			default:
				return null;
			}
		}
	}
	public corner getCorner() {
		switch(x.sign()) {
		case -1:
			switch(y.sign()) {
			case -1:
				return corner.sw;
			case 1:
				return corner.nw;
			default:
				return null;
			}
		case 1:
			switch(y.sign()) {
			case -1:
				return corner.se;
			case 1:
				return corner.ne;
			default:
				return null;
			}
		default:
			return null;
		}
	}
	public enum cardinal {
		north(0, 1),
		east(1, 0),
		south(0, -1),
		west(-1, 0);
		public Vector<Int> v;
		cardinal(int a, int b) {
			v = new Vector<Int>(new Int(a), new Int(b));
		}
		public cardinal cw90() {
			return values()[(ordinal() + 1) % 4];
		}
		public cardinal ccw90() {
			return values()[(ordinal() + 3) % 4];
		}
		public corner cw45() {
			return corner.values()[ordinal()];
		}
		public corner ccw45() {
			return corner.values()[(ordinal() + 3) % 4];
		}
		public cardinal turn180() {
			return values()[(ordinal() + 2) % 2];
		}
	}
	public enum corner {
		ne(1, 1),
		se(1, -1),
		sw(-1, -1),
		nw(-1, 1);
		public Vector<Int> v;
		corner(int a, int b) {
			v = new Vector<Int>(new Int(a), new Int(b));
		}
		public cardinal ns() {
			if(this == ne || this == nw)
				return cardinal.north;
			return cardinal.south;
		}
		public cardinal ew() {
			if(this == ne || this == se)
				return cardinal.east;
			return cardinal.west;
		}
		public corner cw90() {
			return values()[(ordinal() + 1) % 4];
		}
		public corner ccw90() {
			return values()[(ordinal() + 3) % 4];
		}
		public cardinal cw45() {
			return cardinal.values()[(ordinal() + 1) % 4];
		}
		public cardinal ccw45() {
			return cardinal.values()[ordinal()];
		}
	}
}


