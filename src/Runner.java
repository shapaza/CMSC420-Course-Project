
public class Runner {

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir") + "\\";
		String inputFileName = "points";
		String outputFileName = "output1";
		String inputFilePath = workingDir + inputFileName;
		
		KDTree MyTree = new KDTree();
		MyTree.insertFile(inputFilePath);
		
		MyTree.printTree();
	}

}
