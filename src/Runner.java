import java.util.ArrayList;
import java.util.List;


public class Runner {

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir") + "\\";
		String inputFileName = "points";
		String outputFileName = "output1";
		String triangleInputFileName = "triangle";
		String triangleOutputFileName = "output2";
		
		KDTree MyTree = new KDTree();
		
		// Task 1
		MyTree.insertFile(workingDir + inputFileName);
		MyTree.writeToFile(outputFileName);
		
		// Task 2
		ArrayList<List<Long>> triangleMobileIDs = MyTree.processTriangleQueries(triangleInputFileName);
		MyTree.writeToTriangleOutput(triangleOutputFileName, triangleMobileIDs);
	}

}
