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
	
	/**
	 * This method is used for calculating the area of a triangle, represented by three vertices:
	 * (x1, y1), (x2, y2), and (x3, y3)
	 * 
	 * @param x1	the x-coordinate of the first vertex of the triangle query region
	 * @param y1	the y-coordinate of the first vertex of the triangle query region
	 * @param x2	the x-coordinate of the second vertex of the triangle query region
	 * @param y2	the y-coordinate of the second vertex of the triangle query region
	 * @param x3	the x-coordinate of the third vertex of the triangle query region
	 * @param y3	the y-coordinate of the third vertex of the triangle query region
	 * 
	 * @return a double representing the area of the triangle represented by the vertices (x1, y1), (x2, y2), and (x3, y3)
	 */
	private double triangleArea(int x1, int y1, int x2, int y2, int x3, int y3) {
		// formula for the area of any triangle made of up three vertices (x1, y1), (x2, y2), and (x3, y3)
		return Math.abs(0.5 * (x1*(y2-y3) + x2*(y3-y1)+ x3*(y1-y2)));
	}
	
	public boolean intersects(int xMin, int xMax, int yMin, int yMax) {
		// line segments for rectangle region of node
		// 4 points: A: (xMin, yMin), B: (xMin, yMax), C: (xMax, yMax), D: (xMax, yMin)
		Line2D rectLine1 = new Line2D.Double(xMin, xMin, yMin, yMax); // rectangle line segment from (xMin, yMin) to (xMin, yMax)
		Line2D rectLine2 = new Line2D.Double(xMin, xMax, yMax, yMax); // rectangle line segment from (xMin, yMax) to (xMax, yMax)
		Line2D rectLine3 = new Line2D.Double(xMax, xMax, yMax, yMin); // rectangle line segment from (xMax, yMax) to (xMax, yMin)
		Line2D rectLine4 = new Line2D.Double(xMax, xMin, yMin, yMin); // rectangle line segment from (xMax, yMin) to (xMin, yMin)
		
		// if the node rectangle region intersects with the query region
		if (rectLine1.intersectsLine(left) || rectLine1.intersectsLine(top) 
		   || rectLine1.intersectsLine(right) || rectLine1.intersectsLine(bottom)) {
			return true;
		}
		if (rectLine2.intersectsLine(left) || rectLine2.intersectsLine(top) 
		   || rectLine2.intersectsLine(right) || rectLine2.intersectsLine(bottom)) {
			return true;
		}
		if (rectLine3.intersectsLine(left) || rectLine3.intersectsLine(top) 
		   || rectLine3.intersectsLine(right) || rectLine3.intersectsLine(bottom)) {
			return true;
		}
		if (rectLine4.intersectsLine(left) || rectLine4.intersectsLine(top) 
		   || rectLine4.intersectsLine(right) || rectLine4.intersectsLine(bottom)) {
			return true;
		}
		
		// if the query region is wholly contained within the node rectangle region
		double nodeRegionArea = Math.abs(xMax - xMin) * Math.abs(yMax - yMin);
		double nodeArea1 = triangleArea(xMin, yMin, x1, y1, xMax, yMin);
		double nodeArea2 = triangleArea(xMax, yMin, x1, y1, xMax, yMax);
		double nodeArea3 = triangleArea(xMax, yMax, x1, y1, xMin, yMax);
		double nodeArea4 = triangleArea(xMin, yMax, x1, y1, xMin, yMin);
		double nodeAreaResult = nodeArea1+ nodeArea2 + nodeArea3 + nodeArea4;
		
		if (Math.abs(nodeRegionArea - nodeAreaResult) <= 0.000001)
			return true;
		
		// if the node rectangle region is wholly contained within the query region
		double queryRegionArea = Math.abs(x2 - x1) * Math.abs(y2 - y1);
		double queryArea1 = triangleArea(x1, y1, xMin, yMin, x2, y1);
		double queryArea2 = triangleArea(x2, y1, xMin, yMin, x2, y2);
		double queryArea3 = triangleArea(x2, y2, xMin, yMin, x1, y2);
		double queryArea4 = triangleArea(x1, y2, xMin, yMin, x1, y1);
		double queryAreaResult = queryArea1 + queryArea2 + queryArea3 + queryArea4;
		
		if (Math.abs(queryRegionArea - queryAreaResult) <= 0.000001)
			return true;
		
		return false;
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
