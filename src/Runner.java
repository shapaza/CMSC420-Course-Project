
public class Runner {

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir") + "\\";
		String fileName = "points";
		String filePath = workingDir + fileName;
		
		KDTree MyTree = new KDTree();
		MyTree.insertFile(filePath);
		
		MyTree.printTree();
	}

}
