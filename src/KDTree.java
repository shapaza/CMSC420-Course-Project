import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

/**
 * 
 * 
 * 
 * @author Brent Wang
 * @UID	   112860066
 */
public class KDTree {
	private recordNode root;
	
	public KDTree() {
		root = null;
	}
	
	/**
	 * 
	 */
	public void printTree() {
		printHelper(root);
	}
	
	/**
	 * 
	 * 
	 * @param curr
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
	 * 
	 * 
	 * @param r        the recordNode that we want to insert into the tree
	 */
	public void insert(recordNode r) {
		if (root == null)
			root = r;
		else
			insertHelper(root, r);
	}
	
	/**
	 * 
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
	 * 
	 * 
	 * @param filePath	A string containing the filepath of a CSV file, which contains lines of record data to insert into the tree
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
	 * @param r
	 * @return an integer representing the level of recordNode r in the current tree
	 */
	public int getLevel(recordNode r) {
		return getLevelHelper(root, r, 0);
	}
	
	/**
	 * @param curr
	 * @param r
	 * @param level
	 * @return
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
