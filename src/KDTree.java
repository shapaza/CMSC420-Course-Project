import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

/**
 * This is the KDTree class, which will effectively function as a 3D tree for the sake of this project. 
 * It is a binary tree in which every node is a 3-D node of the class recordNode, which itself contains an XLOC, YLOC, and time variable.
 * 
 * @author Brent Wang
 * @UID	   112860066
 */
public class KDTree {
	
	private recordNode root;
	
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
	 */
	public void printTree() {
		printHelper(root);
	}
	
	/**
	 * The printHelper method recursively prints out the nodes of the tree in preorder traversal
	 * Printed in the format "[PARENT mobile ID] LCHILD/RCHILD [CHILD mobile ID]" for all children
	 * If the current node does not have children, then it does not get printed.
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
	 * @param filePath
	 */
	public void writeToFile(String filePath) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			writeToFileHelper(root, writer);
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * @param curr
	 * @param writer
	 */
	private void writeToFileHelper(recordNode curr, BufferedWriter writer) {
		if (curr == null)
			return;
		
		if (curr.getLeft() != null) {
			try {
				writer.append(curr.getPhoneID() + " LCHILD " + curr.getLeft().getPhoneID());
				writer.append("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		if (curr.getRight() != null) {
			try {
				writer.append(curr.getPhoneID() + " RCHILD " + curr.getRight().getPhoneID());
				writer.append("\n");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		
		writeToFileHelper(curr.getLeft(), writer);
		writeToFileHelper(curr.getRight(), writer);
	}
	
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
	 * The getLevel method calls getLevelHelper to return the level of recordNode r
	 * 
	 * @param r	  the recordNode which we are trying to find the level of
	 * @return an integer representing the level of recordNode r in the current tree or -1 if it's not in the tree
	 */
	public int getLevel(recordNode r) {
		return getLevelHelper(root, r, 0);
	}
	
	/**
	 * 
	 * @param curr   the current recordNode we are at, for the sake of recursion
	 * @param r		 the recordNode which we are trying to find the level of
	 * @param level  the int representing the current level we are at, which we will return if we find recordNode r
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
