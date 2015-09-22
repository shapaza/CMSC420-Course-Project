
/**
 * 
 * The recordNode class serves as the "record" for the 3-D tree we are to implement, in which we will track mobile phones of a set of users on a 1024x1024 grid. 
 * Each record contains a mobile phone ID, an x-coordinate, a y-coordinate, a time, as well as a left and right child (since a K-D tree is still a binary tree).
 * 
 * For example, a record in the form (123456789, 60, 70, 33) says that during hour 33, the mobile phone number 123456789 was at location (60,70)   
 * 
 * @author Brent Wang
 * @UID	   112860066
 */
public class recordNode {
	private long phoneID;
	private int xloc;
	private int yloc;
	private int time;
	
	private recordNode left;
	private recordNode right;
	
	/**
	 * Constructor
	 * 
	 * @param phoneID	the mobile phone ID, an integer in the closed interval [0,9999999999]
	 * @param xloc		the x-coordinate of the location, an integer in the closed interval [0,1023]
	 * @param yloc		the y-coordinate of the location, an integer in the closed interval [0,1023]
	 * @param time		the hour, an integer in the closed interval [0,1023]
	 */
	public recordNode(long phoneID, int xloc, int yloc, int time) {
		this.phoneID = phoneID;
		this.xloc = xloc;
		this.yloc = yloc;
		this.time = time;
		
		left = null;
		right = null;
	}

	public long getPhoneID() {
		return phoneID;
	}

	public void setPhoneID(long phoneID) {
		this.phoneID = phoneID;
	}

	public int getXloc() {
		return xloc;
	}

	public void setXloc(int xloc) {
		this.xloc = xloc;
	}

	public int getYloc() {
		return yloc;
	}

	public void setYloc(int yloc) {
		this.yloc = yloc;
	}

	public int getTime() {
		return time;
	}

	public void setTime(int time) {
		this.time = time;
	}

	public recordNode getLeft() {
		return left;
	}

	public void setLeft(recordNode left) {
		this.left = left;
	}

	public recordNode getRight() {
		return right;
	}

	public void setRight(recordNode right) {
		this.right = right;
	}
}
