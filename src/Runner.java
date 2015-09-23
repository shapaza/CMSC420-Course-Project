
public class Runner {

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir") + "\\";
		String inputFileName = "points";
		String outputFileName = "output1.csv";
		
		KDTree MyTree = new KDTree();
		MyTree.insertFile(workingDir + inputFileName);
		
		MyTree.printTree();
		MyTree.writeToFile(outputFileName);
	}

}
