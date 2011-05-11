package veb;

public enum Direction {
	north(0, 1),
	east(1, 0),
	south(0, -1),
	west(-1, 0);
	private final int x;
	private final int y;
	private final Vector v;
	private static final int yflip = 1;
	private static Direction[] members = new Direction[] { north, east, south, west };
	private Direction(final int x, final int y) {
		this.x = x;
		this.y = y * yflip;
		v = new Vector(x, y);
	}
	public int getX() {
		return x;
	}
	public int getY() {
		return y;
	}
	public Vector getV() {
		return v;
	}
	public Direction cw() {
		return members[(ordinal() + 1) % 4];
	}
	public Direction ccw() {
		return members[(ordinal() + 3) % 4];
	}
}
