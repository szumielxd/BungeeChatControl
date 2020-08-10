package me.szumielxd.bungeechatcontrol;

public class Pair<L, R> {
	
	
	private final L left;
	private final R right;
	
	
	public Pair(L val1, R val2) {
		assert val1 != null;
		assert val2 != null;
		
		this.left = val1;
		this.right = val2;
	}
	
	
	public L getLeft() {
		return left;
	}
	
	
	public R getRight() {
		return right;
	}
	
	
	@Override
	public int hashCode() {
		return left.hashCode() ^ right.hashCode();
	}

	@Override
	public boolean equals(Object o) {
		if (!(o instanceof Pair)) return false;
		Pair<?, ?> pair = (Pair<?, ?>) o;
		return this.left.equals(pair.getLeft()) && this.right.equals(pair.getRight());
	}
	
	@Override
	public String toString() {
		return "{"+this.getLeft()+" && "+this.getRight()+"}";
	}
	

}
