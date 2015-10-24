import java.awt.geom.Line2D;


public class Rectangle {
	private int x1;
	private int y1;
	private int x2;
	private int y2;
	
	private Line2D left;
	private Line2D top;
	private Line2D right;
	private Line2D bottom;
	
	public Rectangle(int x1, int y1, int x2, int y2, Line2D left, Line2D top, Line2D right, Line2D bottom) {
		this.x1 = x1;
		this.y1 = y1;
		this.x2 = x2;
		this.y2 = y2;
		this.left = left;
		this.top = top;
		this.right = right;
		this.bottom = bottom;
	}

	public int getX1() {
		return x1;
	}

	public void setX1(int x1) {
		this.x1 = x1;
	}

	public int getY1() {
		return y1;
	}

	public void setY1(int y1) {
		this.y1 = y1;
	}

	public int getX2() {
		return x2;
	}

	public void setX2(int x2) {
		this.x2 = x2;
	}

	public int getY2() {
		return y2;
	}

	public void setY2(int y2) {
		this.y2 = y2;
	}

	public Line2D getLeft() {
		return left;
	}

	public void setLeft(Line2D left) {
		this.left = left;
	}

	public Line2D getTop() {
		return top;
	}

	public void setTop(Line2D top) {
		this.top = top;
	}

	public Line2D getRight() {
		return right;
	}

	public void setRight(Line2D right) {
		this.right = right;
	}

	public Line2D getBottom() {
		return bottom;
	}

	public void setBottom(Line2D bottom) {
		this.bottom = bottom;
	}
}
