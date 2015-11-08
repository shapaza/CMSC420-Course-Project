import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.awt.geom.Line2D;
import java.awt.geom.Line2D.Double;

/**
 * This is the KDTree class, which will effectively function as a 3D tree for the sake of this project. 
 * It is a binary tree in which every node is a 3-D node of the class recordNode, which itself contains an XLOC, YLOC, and time variable.
 * 
 * @author Brent Wang
 * @UID	   112860066
 */
public class KDTree {
	
	private recordNode root;
	private static final int XLOC_MIN = 0;
	private static final int XLOC_MAX = 1023;
	private static final int YLOC_MIN = 0;
	private static final int YLOC_MAX = 1023;
	private static final int TIME_MIN = 0;
	private static final int TIME_MAX = 1023;
	
	/**
	 * Constructor
	 *  
	 */
	public KDTree() {
		root = null;
	}
	
	/**
	 * Calls printHelper, which then prints out the nodes of the tree in preorder traversal
	 * Mainly used as a personal test method to see if the output is correct
	 * 
	 */
	public void printTree() {
		printHelper(root);
	}
	
	/**
	 * The printHelper method recursively prints out the nodes of the tree in preorder traversal
	 * Printed in the format "[PARENT mobile ID] LCHILD/RCHILD [CHILD mobile ID]" for all children
	 * If the current node does not have children, then it does not get printed.
	 * Again, this is mainly used as a personal test method to see if the output is correct
	 * 
	 * @param curr	the current recordNode we are at, for the sake of recursion
	 */
	private void printHelper(recordNode curr) {
		if (curr == null)
			return;
		
		if (curr.getLeft() != null) {
			System.out.print(curr.getPhoneID());
			System.out.println(" LCHILD "+ curr.getLeft().getPhoneID());
		}
		
		if (curr.getRight() != null) {
			System.out.print(curr.getPhoneID());
			System.out.println(" RCHILD " + curr.getRight().getPhoneID());
		}
		
		printHelper(curr.getLeft());
		printHelper(curr.getRight());
	}
	
	/**
	 * This private method will be used to help with writing output to the CSV files.
	 * It will do a preorder traversal of the tree to get all the nodes (and their children), and add them to an ArrayList in the desired format for output.
	 * That is, each element of the ArrayList will be a String like "[PARENT mobile ID] LCHILD/RCHILD [CHILD mobile ID]".
	 * 
	 * @return An ArrayList containing all the nodes in the tree in preorder, according to the desired output format
	 */
	private ArrayList<String> allNodesPreOrder() {
		ArrayList<String> allNodes = new ArrayList<String>();
		allNodesPreOrderHelper(root, allNodes);
		
		return allNodes;
	}
	
	/**
	 * Recursive helper method for allNodesPreOrder, which actually does all the necessary work.
	 * 
	 * @param curr      the current recordNode we are at, for the sake of recursion
	 * @param allNodes  the ArrayList to which we will add all the output strings
	 */
	private void allNodesPreOrderHelper(recordNode curr, ArrayList<String> allNodes) {
		if (curr == null)
			return;
		if (curr.getLeft() != null)
			allNodes.add(curr.getPhoneID() + " LCHILD " + curr.getLeft().getPhoneID());
		if (curr.getRight() != null)
			allNodes.add(curr.getPhoneID() + " RCHILD " + curr.getRight().getPhoneID());
		
		allNodesPreOrderHelper(curr.getLeft(), allNodes);
		allNodesPreOrderHelper(curr.getRight(), allNodes);
	}
	
	/**
	 * This method will write all the nodes of the tree to an output file, according to the format specified in the project description.
	 * That is, each line will follow the format: "[PARENT mobile ID] LCHILD/RCHILD [CHILD mobile ID]" in preorder traversal style.
	 * 
	 * @param filePath   A string containing the file path of the output file, which we are writing to
	 */
	public void writeToFile(String filePath) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			
			ArrayList<String> allNodesPreOrder = allNodesPreOrder();
			
			for (int i = 0; i < allNodesPreOrder.size(); i++) {
				writer.append(allNodesPreOrder.get(i));
				
				// making sure that we don't have an extra newline at the end of the file
				if (i < allNodesPreOrder.size() - 1)
					writer.append("\n");
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
//	/**
//	 * @param curr
//	 * @param writer
//	 */
//	private void writeToFileHelper(recordNode curr, BufferedWriter writer) {
//		if (curr == null)
//			return;
//		
//		if (curr.getLeft() != null) {
//			try {
//				writer.append(curr.getPhoneID() + " LCHILD " + curr.getLeft().getPhoneID());
//				writer.append("\n");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		if (curr.getRight() != null) {
//			try {
//				writer.append(curr.getPhoneID() + " RCHILD " + curr.getRight().getPhoneID());
//				writer.append("\n");
//			} catch (IOException e) {
//				e.printStackTrace();
//			}
//		}
//		
//		writeToFileHelper(curr.getLeft(), writer);
//		writeToFileHelper(curr.getRight(), writer);
//	}
	
	/**
	 * Insert function
	 * If the tree is empty, make recordNode r the root node
	 * Else, call insertHelper to insert r into the tree
	 * 
	 * @param r		the recordNode that we want to insert into the tree
	 */
	public void insert(recordNode r) {
		if (root == null)
			root = r;
		else
			insertHelper(root, r);
	}
	
	/**
	 * The insertHelper method recursively inserts recordNode r into the tree
	 * It discriminates on the XLOC, YLOC, or time variable, depending on the level of the node we are currently at
	 * 
	 * @param curr     the current recordNode we are at, for the sake of recursion
	 * @param r		   the recordNode that we are trying to insert into the tree
	 */
	private void insertHelper(recordNode curr, recordNode r) {
		// since we are making a 3-D tree, the dimension we discriminate on will be determined by 
		// the following formula: (level of current node) mod 3 
		int discriminator = getLevel(curr) % 3;
		
		// discriminate on X
		if (discriminator == 0) {
			if (r.getXloc() < curr.getXloc()) {
				if (curr.getLeft() == null) {
					curr.setLeft(r);
					return;
				}
				else {
					insertHelper(curr.getLeft(), r);
				}
			}
			else if (r.getXloc() >= curr.getXloc()) {
				if (curr.getRight() == null) {
					curr.setRight(r);
					return;
				}
				else {
					insertHelper(curr.getRight(), r);
				}
			}
		}
		// discriminate on Y
		else if (discriminator == 1) {
			if (r.getYloc() < curr.getYloc()) {
				if (curr.getLeft() == null) {
					curr.setLeft(r);
					return;
				}
				else {
					insertHelper(curr.getLeft(), r);
				}
			}
			else if (r.getYloc() >= curr.getYloc()) {
				if (curr.getRight() == null) {
					curr.setRight(r);
					return;
				}
				else {
					insertHelper(curr.getRight(), r);
				}
			}
		}
		// discriminate on Z (time)
		else if (discriminator == 2) {
			if (r.getTime() < curr.getTime()) {
				if (curr.getLeft() == null) {
					curr.setLeft(r);
					return;
				}
				else {
					insertHelper(curr.getLeft(), r);
				}
			}
			else if (r.getTime() >= curr.getTime()) {
				if (curr.getRight() == null) {
					curr.setRight(r);
					return;
				}
				else {
					insertHelper(curr.getRight(), r);
				}
			}
		}
		
	}
	
	/**
	 * The insertFile method takes in a String filePath containing the filePath of an input file.
	 * This file will contain data corresponding to new recordNodes that we will create and insert into the tree
	 * 
	 * @param filePath	A string containing the filepath of a CSV file that we will use for input, which contains lines of record data to insert into the tree
	 */
	public void insertFile(String filePath) {
		try {
			BufferedReader br = new BufferedReader(new FileReader(filePath));
			String currLine = "";
			
			while ((currLine = br.readLine()) != null) {
				String data[] = currLine.split(",");
				
				long mobileID = Long.parseLong(data[0].trim());
				int xloc = Integer.parseInt(data[1].trim());
				int yloc = Integer.parseInt(data[2].trim());
				int time = Integer.parseInt(data[3].trim());
				
				recordNode newRecord = new recordNode(mobileID, xloc, yloc, time);
				
				insert(newRecord);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method processes the data contained in a file containing triangle queries, and returns a list of lists of longs containing the mobileIDs that are contained within
	 * the triangle query region.
	 * 
	 * @param triangleFile
	 * @return An ArrayList of Lists of Longs, corresponding to the mobileIDs that are contained in the triangle query regions
	 */
	public ArrayList<List<Long>> processTriangleQueries(String triangleFile) {
		ArrayList<List<Long>> outputLines = new ArrayList<List<Long>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(triangleFile));
			String currLine = "";	
			
			while ((currLine = br.readLine()) != null) {
				String triangleQuery[] = currLine.split(",");
				
				int time = Integer.parseInt(triangleQuery[0].trim());
				int x1 = Integer.parseInt(triangleQuery[1].trim());
				int y1 = Integer.parseInt(triangleQuery[2].trim());
				int x2 = Integer.parseInt(triangleQuery[3].trim());
				int y2 = Integer.parseInt(triangleQuery[4].trim());
				int x3 = Integer.parseInt(triangleQuery[5].trim());
				int y3 = Integer.parseInt(triangleQuery[6].trim());
				
				List<Long> mobileIDs = triangle(time, x1, y1, x2, y2, x3, y3);
				outputLines.add(mobileIDs);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputLines;
	}
	
	/**
	 * This method writes the mobileIDs found by the triangle method to an output file, as specified in the project description.
	 * 
	 * @param filePath
	 * @param mobileIDs
	 */
	public void writeToTriangleOutput(String filePath, ArrayList<List<Long>> mobileIDs) {	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			
			for (int i = 0; i < mobileIDs.size(); i++) {
				for (int j = 0; j < mobileIDs.get(i).size(); j++) {
					writer.append(mobileIDs.get(i).get(j).toString() + ", ");
				}
				
				if (i < mobileIDs.size() - 1)
					writer.append("\n");
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * This method takes in a triangle query region represented by vertices (x1, y1), (x2, y2), and (x3, y3)
	 * and a time value. It will return a list of mobileIDs that are within the triangle at the specified time value. 
	 * 
	 * @param time
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @param x3
	 * @param y3
	 * @return a list of mobileIDs that correspond to recordNodes that intersect a triangle query region
	 */
	public List<Long> triangle(int time, int x1, int y1, int x2, int y2, int x3, int y3) {
		List<Long> mobileIDs = new ArrayList<Long>();
		triangleHelper(root, mobileIDs, time, x1, y1, x2, y2, x3, y3, XLOC_MIN, XLOC_MAX, YLOC_MIN, YLOC_MAX, TIME_MIN, TIME_MAX);
		
		return mobileIDs;
	}
	
	/**
	 * This is a helper method to the above "triangle" method that actually does all the work.
	 * To determine whether any particular mobileID lies inside the triangle query region, this method calculates the area of the triangle query region represented by vertices
	 * (x1, y1), (x2, y2), and (x3, y3), which I will refer to as points A, B, and C, respectively. I will also refer to the point in the current node as R.
	 * The method then calculates the area of triangles RAB, RBC, and RAC (not necessarily in the order listed here), and adds them up. 
	 * If the sum of these areas equals the area of the the triangle query region, then that means the point lies inside 
	 * the triangle query region, and the method adds the corresponding mobileID to the list. 
	 * 
	 * @param r			the current recordNode we are at
	 * @param list		the list of Longs that we are adding mobileIDs to (if they lie within the triangle query region)
	 * @param time		the specified time at which mobileIDs must be
	 * @param x1		the x-coordinate of the first vertex of the triangle query region
	 * @param y1		the y-coordinate of the first vertex of the triangle query region
	 * @param x2		the x-coordinate of the second vertex of the triangle query region
	 * @param y2		the y-coordinate of the second vertex of the triangle query region
	 * @param x3		the x-coordinate of the third vertex of the triangle query region
	 * @param y3		the y-coordinate of the third vertex of the triangle query region
	 * @param xMin		the minimum possible x value of the current region
	 * @param xMax		the maximum possible x value of the current region
	 * @param yMin		the minimum possible y value of the current region
	 * @param yMax		the maximum possible y value of the current region
	 * @param timeMin	the minimum possible time value of the current region
	 * @param timeMax	the maximum possible time value of the current region
	 */
	private void triangleHelper(recordNode r, List<Long> list, int time,
			int x1, int y1, int x2, int y2, int x3, int y3, int xMin, int xMax,
			int yMin, int yMax, int timeMin, int timeMax) {
		if (r == null)
			return;
		
		// if the time is not within bounds
		if (time < timeMin || time > timeMax) {
			// System.out.println("Pruning at Node " + r.getPhoneID() + " based on time");
			return; // prune
		}
		// if the triangle query region does not intersect the current node's rectangular region
		if(!intersects(x1, y1, x2, y2, x3, y3, xMin, xMax, yMin, yMax)) {
			// System.out.println("Pruning at Node " + r.getPhoneID() + " based on lack of intersection");
			return; // prune
		}
		// if the time of the current node matches the time specified
		if (r.getTime() == time) {
			double wholeArea = triangleArea(x1, y1, x2, y2, x3, y3);
			double triangleArea1 = triangleArea(r.getXloc(), r.getYloc(), x2, y2, x3, y3);
			double triangleArea2 = triangleArea(x1, y1, r.getXloc(), r.getYloc(), x3, y3);
			double triangleArea3 = triangleArea(x1, y1, x2, y2, r.getXloc(), r.getYloc());
			double resultingArea = triangleArea1 + triangleArea2 + triangleArea3;

			// if the point is within the triangle query region, add it to the list of solutions
			if (Math.abs(wholeArea - resultingArea) <= 0.000001) {
				list.add(r.getPhoneID());
			}
		}
		
		int discriminator = getLevel(r) % 3;
		
		// if we're discriminating on the X value, update xMin and xMax values for recursive calls
		if (discriminator == 0) {
			triangleHelper(r.getLeft(), list, time, x1, y1, x2, y2, x3, y3, 
					xMin, r.getXloc() - 1, yMin, yMax, timeMin, timeMax);
			triangleHelper(r.getRight(), list, time, x1, y1, x2, y2, x3, y3, 
					r.getXloc(), xMax, yMin, yMax, timeMin, timeMax);
		}
		// if we're discriminating on the Y value, update yMin and yMax values for recursive calls
		else if (discriminator == 1) {
			triangleHelper(r.getLeft(), list, time, x1, y1, x2, y2, x3, y3, 
					xMin, xMax, yMin, r.getYloc() - 1, timeMin, timeMax);
			triangleHelper(r.getRight(), list, time, x1, y1, x2, y2, x3, y3, 
					xMin, xMax, r.getYloc(), yMax, timeMin, timeMax);
		}
		// if we're discriminating on the time value, update timeMin and timeMax values for recursive calls
		else if (discriminator == 2) {
			triangleHelper(r.getLeft(), list, time, x1, y1, x2, y2, x3, y3, 
					xMin, xMax, yMin, yMax, timeMin, r.getTime() - 1);
			triangleHelper(r.getRight(), list, time, x1, y1, x2, y2, x3, y3, 
					xMin, xMax, yMin, yMax, r.getTime(), timeMax);
		}
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
	
	/**
	 * This method checks if the triangle represented by vertices (x1, y1), (x2, y2), and (x3, y3)
	 * intersects with the rectangle region represented by the points (xMin, yMin), (xMin, yMax), (xMax, yMax), and (xMax, yMin).
	 * 
	 * @param x1		the x-coordinate of the first vertex of the triangle query region
	 * @param y1		the y-coordinate of the first vertex of the triangle query region
	 * @param x2		the x-coordinate of the second vertex of the triangle query region
	 * @param y2		the y-coordinate of the second vertex of the triangle query region
	 * @param x3		the x-coordinate of the third vertex of the triangle query region
	 * @param y3		the y-coordinate of the third vertex of the triangle query region
	 * @param xMin		the minimum possible x value of the current region
	 * @param xMax		the maximum possible x value of the current region
	 * @param yMin		the minimum possible y value of the current region
	 * @param yMax		the maximum possible y value of the current region
	 * 
	 * @return a boolean indicating whether the triangle intersects with the rectangular region
	 */
	private boolean intersects(int x1, int y1, int x2, int y2, int x3, int y3,
			int xMin, int xMax, int yMin, int yMax) {
		// line segments for triangle query region
		Line2D triLine1 = new Line2D.Double(x1, x2, y1, y2); // triangle line segment from (x1, y1) to (x2, y2)
		Line2D triLine2 = new Line2D.Double(x1, x3, y1, y3); // triangle line segment from (x1, y1) to (x3, y3)
		Line2D triLine3 = new Line2D.Double(x2, x3, y2, y3); // triangle line segment from (x2, y2) to (x3, y3)
		
		// line segments for rectangle region of node
		// 4 points: A: (xMin, yMin), B: (xMin, yMax), C: (xMax, yMax), D: (xMax, yMin)
		Line2D rectLine1 = new Line2D.Double(xMin, xMin, yMin, yMax); // rectangle line segment from (xMin, yMin) to (xMin, yMax)
		Line2D rectLine2 = new Line2D.Double(xMin, xMax, yMax, yMax); // rectangle line segment from (xMin, yMax) to (xMax, yMax)
		Line2D rectLine3 = new Line2D.Double(xMax, xMax, yMax, yMin); // rectangle line segment from (xMax, yMax) to (xMax, yMin)
		Line2D rectLine4 = new Line2D.Double(xMax, xMin, yMin, yMin); // rectangle line segment from (xMax, yMin) to (xMin, yMin)
		
		double wholeArea = triangleArea(x1, y1, x2, y2, x3, y3);
		double triangleArea1 = triangleArea(xMin, yMin, x2, y2, x3, y3);
		double triangleArea2 = triangleArea(x1, y1, xMin, yMin, x3, y3);
		double triangleArea3 = triangleArea(x1, y1, x2, y2, xMin, yMin);
		double resultingArea = triangleArea1 + triangleArea2 + triangleArea3;

		// if the rectangle is wholly contained inside the triangle query region
		if (Math.abs(wholeArea - resultingArea) <= 0.000001)
			return true;
		
		double wholeRecArea = Math.abs(xMax - xMin) * Math.abs(yMax - yMin);
		double area1 = triangleArea(xMin, yMin, x1, y1, xMax, yMin);
		double area2 = triangleArea(xMax, yMin, x1, y1, xMax, yMax);
		double area3 = triangleArea(xMax, yMax, x1, y1, xMin, yMax);
		double area4 = triangleArea(xMin, yMax, x1, y1, xMin, yMin);
		double result = area1+ area2 + area3 + area4;
		
		// if triangle is wholly contained inside the rectangle region
		if (Math.abs(wholeRecArea - result) <= 0.000001)
			return true;
		
		// if any of the triangle's line segments intersects with the rectangular region
		if (triLine1.intersectsLine(rectLine1) || triLine1.intersectsLine(rectLine2)
		 || triLine1.intersectsLine(rectLine3) || triLine1.intersectsLine(rectLine4))
			return true;
		if (triLine2.intersectsLine(rectLine1) || triLine2.intersectsLine(rectLine2)
		 || triLine2.intersectsLine(rectLine3) || triLine2.intersectsLine(rectLine4))
			return true;
		if (triLine3.intersectsLine(rectLine1) || triLine3.intersectsLine(rectLine2)
		 || triLine3.intersectsLine(rectLine3) || triLine3.intersectsLine(rectLine4))
			return true;
		
		return false;
	}
	
	/**
	 * This method processes the data contained in a file containing timeInt queries, and returns a list of lists of longs containing the mobileIDs that are contained within
	 * the rectangle query region at some time point in the interval [S, E].
	 * 
	 * @param timeIntFile
	 * @return An ArrayList of Lists of Longs, corresponding to the mobileIDs that are contained in the rectangle query region at some time point in [S, E]
	 */
	public ArrayList<List<Long>> processTimeIntQueries(String timeIntFile) {
		ArrayList<List<Long>> outputLines = new ArrayList<List<Long>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(timeIntFile));
			String currLine = "";	
			
			while ((currLine = br.readLine()) != null) {
				String[] timeIntQuery = currLine.split(",");
				
				int startTime = Integer.parseInt(timeIntQuery[0].trim());
				int endTime = Integer.parseInt(timeIntQuery[1].trim());
				int x1 = Integer.parseInt(timeIntQuery[2].trim());
				int y1 = Integer.parseInt(timeIntQuery[3].trim());
				int x2 = Integer.parseInt(timeIntQuery[4].trim());
				int y2 = Integer.parseInt(timeIntQuery[5].trim());
				
				List<Long> mobileIDs = timeInt(startTime, endTime, x1, y1, x2, y2);
				outputLines.add(mobileIDs);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputLines;
	}
	
	/**
	 * This method takes in a rectangle query region whose lower-left corner is represented by (x1, y1) and whose upper-right is represented by (x2, y2)
	 * and a time interval [start, end]. 
	 * It will return a list of mobileIDs that are within the rectangle at some point in the time interval. 
	 * 
	 * @param start	 int representing the start of the time interval
	 * @param end	 int representing the end of the time interval
	 * @param x1	 int representing the x-coordinate of the lower-left corner of the rectangle query region
	 * @param y1	 int representing the y-coordinate of the lower-left corner of the rectangle query region
	 * @param x2	 int representing the x-coordinate of the upper-right corner of the rectangle query region
	 * @param y2	 int representing the u-coordinate of the upper-right corner of the rectangle query region
	 * 
	 * @return a list of mobileIDs that correspond to recordNodes that intersect the query region and time interval
	 */
	public List<Long> timeInt(int start, int end, int x1, int y1, int x2, int y2) {
		List<Long> mobileIDs = new ArrayList<Long>();
		Line2D left = new Line2D.Double(x1, x1, y1, y2);    // line going from (x1, y1) to (x1, y2)
		Line2D top = new Line2D.Double(x1, x2, y2, y2);     // line going from (x1, y2) to (x2, y2)
		Line2D right = new Line2D.Double(x2, x2, y2, y1);   // line going from (x2, y2) to (x2, y1)
		Line2D bottom = new Line2D.Double(x2, x1, y1, y1);  // line going from (x2, y1) to (x1, y1)
		Rectangle queryRegion = new Rectangle(x1, y1, x2, y2, left, top, right, bottom);
		
		timeIntHelper(root, mobileIDs, start, end, queryRegion, XLOC_MIN, XLOC_MAX, YLOC_MIN, YLOC_MAX, TIME_MIN, TIME_MAX);
		
		return mobileIDs;
	}
	
	/**
	 * This is a helper method to the above "timeInt" method that actually does all the work.
	 * 
	 * @param r 	    the current recordNode in the KD-Tree that we are processing, for the sake of recursion
	 * @param list      the list of mobileIDs that lie within the rectangle query region at some point in the time interval [start, end]
	 * @param start     the start of the time interval
	 * @param end       the end of the time interval
	 * @param qr        the rectangle query region
	 * @param xMin		the minimum possible x value of the current region
	 * @param xMax		the maximum possible x value of the current region
	 * @param yMin		the minimum possible y value of the current region
	 * @param yMax		the maximum possible y value of the current region
	 * @param timeMin	the minimum possible time value of the current region
	 * @param timeMax	the maximum possible time value of the current region
	 */
	private void timeIntHelper(recordNode r, List<Long> list, int start, int end, Rectangle qr, int xMin, int xMax, int yMin, int yMax, int timeMin, int timeMax) {
		if (r == null)
			return;
		
		// if the time intervals don't overlap
		if (end < timeMin || start > timeMax) {
			// System.out.println("Pruning at Node " + r.getPhoneID() + " based on time");
			return; // pruning
		}
		// if the query region does not intersect with the node region
		if (!qr.intersects(xMin, xMax, yMin, yMax)) {
			// System.out.println("Pruning at Node " + r.getPhoneID() + " based on lack of intersection");
			return; // pruning
		}
		
		// if the time is within the interval specified
		if (start <= r.getTime() && r.getTime() <= end) {
			double wholeRecArea = Math.abs(qr.getX2() - qr.getX1()) * Math.abs(qr.getY2() - qr.getY1());
			double area1 = triangleArea(qr.getX1(), qr.getY1(), r.getXloc(), r.getYloc(), qr.getX2(), qr.getY1());
			double area2 = triangleArea(qr.getX1(), qr.getY1(), r.getXloc(), r.getYloc(), qr.getX1(), qr.getY2());
			double area3 = triangleArea(qr.getX1(), qr.getY2(), r.getXloc(), r.getYloc(), qr.getX2(), qr.getY2());
			double area4 = triangleArea(qr.getX2(), qr.getY2(), r.getXloc(), r.getYloc(), qr.getX2(), qr.getY1());
			double result = area1 + area2 + area3 + area4;
			
			// if the point lies within the rectangle query region
			if (Math.abs(wholeRecArea - result) <= 0.000001)
				list.add(r.getPhoneID());
		}
		
		int discriminator = getLevel(r) % 3;
		
		// if we're discriminating on the X value, update xMin and xMax values for recursive calls
		if (discriminator == 0 ) {
			timeIntHelper(r.getLeft(), list, start, end, qr, xMin, r.getXloc() - 1, yMin, yMax, timeMin, timeMax);
			timeIntHelper(r.getRight(), list, start, end, qr, r.getXloc(), xMax, yMin, yMax, timeMin, timeMax);
		}
		// if we're discriminating on the Y value, update yMin and yMax values for recursive calls
		else if (discriminator == 1) {
			timeIntHelper(r.getLeft(), list, start, end, qr, xMin, xMax, yMin, r.getYloc() - 1, timeMin, timeMax);
			timeIntHelper(r.getRight(), list, start, end, qr, xMin, xMax, r.getYloc(), yMax, timeMin, timeMax);
		}
		// if we're discriminating on the time value, update timeMin and timeMax values for recursive calls
		else if (discriminator == 2) {
			timeIntHelper(r.getLeft(), list, start, end, qr, xMin, xMax, yMin, yMax, timeMin, r.getTime() - 1);
			timeIntHelper(r.getRight(), list, start, end, qr, xMin, xMax, yMin, yMax, r.getTime(), timeMax);
		}
	}
	
	/**
	 * This method processes the data contained in a file containing timeInt queries, and returns a list of lists of longs containing the mobileIDs that are contained within
	 * the rectangle query region at ALL time points in the interval [S, E].
	 * 
	 * @param timeIntFile
	 * @return An ArrayList of Lists of Longs, corresponding to the mobileIDs that are contained in the rectangle query region at ALL time points in [S, E]
	 */
	public ArrayList<List<Long>> processTimeAllIntQueries(String timeIntFile) {
		ArrayList<List<Long>> outputLines = new ArrayList<List<Long>>();
		
		try {
			BufferedReader br = new BufferedReader(new FileReader(timeIntFile));
			String currLine = "";	
			
			while ((currLine = br.readLine()) != null) {
				String[] timeIntQuery = currLine.split(",");
				
				int startTime = Integer.parseInt(timeIntQuery[0].trim());
				int endTime = Integer.parseInt(timeIntQuery[1].trim());
				int x1 = Integer.parseInt(timeIntQuery[2].trim());
				int y1 = Integer.parseInt(timeIntQuery[3].trim());
				int x2 = Integer.parseInt(timeIntQuery[4].trim());
				int y2 = Integer.parseInt(timeIntQuery[5].trim());
				
				List<Long> mobileIDs = timeAllInt(startTime, endTime, x1, y1, x2, y2);
				outputLines.add(mobileIDs);
			}
			
			br.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		
		return outputLines;
	}
	
	/**
	 * 
	 * @param start
	 * @param end
	 * @param x1
	 * @param y1
	 * @param x2
	 * @param y2
	 * @return
	 */
	public List<Long> timeAllInt(int start, int end, int x1, int y1, int x2, int y2) {
		List<Long> mobileIDs = new ArrayList<Long>();
		HashMap<Long, ArrayList<Integer>> mobileIDTimes = new HashMap<Long, ArrayList<Integer>>();
		
		Line2D left = new Line2D.Double(x1, x1, y1, y2);    // line going from (x1, y1) to (x1, y2)
		Line2D top = new Line2D.Double(x1, x2, y2, y2);     // line going from (x1, y2) to (x2, y2)
		Line2D right = new Line2D.Double(x2, x2, y2, y1);   // line going from (x2, y2) to (x2, y1)
		Line2D bottom = new Line2D.Double(x2, x1, y1, y1);  // line going from (x2, y1) to (x1, y1)
		Rectangle queryRegion = new Rectangle(x1, y1, x2, y2, left, top, right, bottom);
		
		timeAllIntHelper(root, mobileIDTimes, start, end, queryRegion, XLOC_MIN, XLOC_MAX, YLOC_MIN, YLOC_MAX, TIME_MIN, TIME_MAX);
		
		// going through all the mobileIDs to check if they appear at all points in time interval [start, end]
		for (long mobileID : mobileIDTimes.keySet()) {
			boolean atAllTimes = true;
			
			// we will only add the mobileID to the List being returned if it appears at every point in the interval 
			// (and if they lie in the specified region, of course)
			for (int currTime = start; currTime <= end; currTime++) {
				if (!mobileIDTimes.get(mobileID).contains(currTime)) {
					atAllTimes = false;
				}
			}
			
			if (atAllTimes)
				mobileIDs.add(mobileID);
		}
		
		return mobileIDs;
	}
	
	/**
	 * 
	 * @param r
	 * @param hm
	 * @param start
	 * @param end
	 * @param qr
	 * @param xMin
	 * @param xMax
	 * @param yMin
	 * @param yMax
	 * @param timeMin
	 * @param timeMax
	 */
	private void timeAllIntHelper(recordNode r, HashMap<Long, ArrayList<Integer>> hm, int start, int end, Rectangle qr, int xMin, int xMax, int yMin, int yMax, int timeMin, int timeMax) {
		if (r == null)
			return;
		
		// if the time intervals don't overlap
		if (end < timeMin || start > timeMax) {
			// System.out.println("Pruning at Node " + r.getPhoneID() +
			// " based on time");
			return; // pruning
		}
		// if the query region does not intersect with the node region
		if (!qr.intersects(xMin, xMax, yMin, yMax)) {
			// System.out.println("Pruning at Node " + r.getPhoneID() +
			// " based on lack of intersection");
			return; // pruning
		}
		
		// if the time is within the interval specified
		if (start <= r.getTime() && r.getTime() <= end) {
			double wholeRecArea = Math.abs(qr.getX2() - qr.getX1()) * Math.abs(qr.getY2() - qr.getY1());
			double area1 = triangleArea(qr.getX1(), qr.getY1(), r.getXloc(), r.getYloc(), qr.getX2(), qr.getY1());
			double area2 = triangleArea(qr.getX1(), qr.getY1(), r.getXloc(), r.getYloc(), qr.getX1(), qr.getY2());
			double area3 = triangleArea(qr.getX1(), qr.getY2(), r.getXloc(), r.getYloc(), qr.getX2(), qr.getY2());
			double area4 = triangleArea(qr.getX2(), qr.getY2(), r.getXloc(), r.getYloc(), qr.getX2(), qr.getY1());
			double result = area1 + area2 + area3 + area4;

			// if the point lies within the rectangle query region
			if (Math.abs(wholeRecArea - result) <= 0.000001) {
				if (!hm.containsKey(r.getPhoneID()))
					hm.put(r.getPhoneID(), new ArrayList<Integer>());
				
				hm.get(r.getPhoneID()).add(r.getTime());
			}
		}
		
		int discriminator = getLevel(r) % 3;

		// if we're discriminating on the X value, update xMin and xMax values
		// for recursive calls
		if (discriminator == 0) {
			timeAllIntHelper(r.getLeft(), hm, start, end, qr, xMin, r.getXloc() - 1, yMin, yMax, timeMin, timeMax);
			timeAllIntHelper(r.getRight(), hm, start, end, qr, r.getXloc(), xMax, yMin, yMax, timeMin, timeMax);
		}
		// if we're discriminating on the Y value, update yMin and yMax values
		// for recursive calls
		else if (discriminator == 1) {
			timeAllIntHelper(r.getLeft(), hm, start, end, qr, xMin, xMax, yMin, r.getYloc() - 1, timeMin, timeMax);
			timeAllIntHelper(r.getRight(), hm, start, end, qr, xMin, xMax, r.getYloc(), yMax, timeMin, timeMax);
		}
		// if we're discriminating on the time value, update timeMin and timeMax
		// values for recursive calls
		else if (discriminator == 2) {
			timeAllIntHelper(r.getLeft(), hm, start, end, qr, xMin, xMax, yMin, yMax, timeMin, r.getTime() - 1);
			timeAllIntHelper(r.getRight(), hm, start, end, qr, xMin, xMax, yMin, yMax, r.getTime(), timeMax);
		}
		
	}
	
	/**
	 * The getLevel method calls getLevelHelper to return the level of recordNode r
	 * 
	 * @param r	  the recordNode which we are trying to find the level of
	 * 
	 * @return an integer representing the level of recordNode r in the current tree or -1 if it's not in the tree
	 */
	public int getLevel(recordNode r) {
		return getLevelHelper(root, r, 0);
	}
	
	/**
	 * Helper method for getLevel that actually does all the work.
	 * 
	 * @param curr   the current recordNode we are at, for the sake of recursion
	 * @param r		 the recordNode which we are trying to find the level of
	 * @param level  the int representing the current level we are at, which we will return if we find recordNode r
	 * 
	 * @return an integer representing the level of recordNode r in the current tree or -1 if it's not in the tree
	 */
	private int getLevelHelper(recordNode curr, recordNode r, int level) {
		if (curr == null)
			return -1;
		
		if (curr.getPhoneID() == r.getPhoneID())
			return level;
		
		int left = getLevelHelper(curr.getLeft(), r, level + 1);
		int right = getLevelHelper(curr.getRight(), r, level + 1);
		
		if (left != -1)
			return left;
		else if (right != -1)
			return right;
		else
			return -1;
	}
}
