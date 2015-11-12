import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class Runner {
	
	public static void writeOutput(String filePath, ArrayList<List<Long>> mobileIDs) {	
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(filePath));
			
			for (int i = 0; i < mobileIDs.size(); i++) {
				for (int j = 0; j < mobileIDs.get(i).size(); j++) {
					writer.append(mobileIDs.get(i).get(j).toString() + ",");
					
					if (j < mobileIDs.get(i).size() - 1)
						writer.append(" ");
				}
				
				writer.append("\n");
			}
			
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static void main(String[] args) {
		String workingDir = System.getProperty("user.dir") + "\\";
		String inputFileName = "points";
		String outputFileName = "output1";
		String triangleInputFileName = "triangle";
		String triangleOutputFileName = "output2";
		String timeIntInput = "time_int";
		String timeIntOutput = "output3";
		String timeAllIntInput = "time_all_int";
		String timeAllIntOutput = "output4";
		
		KDTree MyTree = new KDTree();
		
		// Task 1
		MyTree.insertFile(workingDir + inputFileName);
		MyTree.writeToFile(outputFileName);
		System.out.println("Done with Task 1!");
		
		// Task 2
		ArrayList<List<Long>> triangleMobileIDs = MyTree.processTriangleQueries(triangleInputFileName);
		MyTree.writeToTriangleOutput(triangleOutputFileName, triangleMobileIDs);
		System.out.println("Done with Task 2!");
		
		// Task 3
		ArrayList<List<Long>> timeIntMobileIDs = MyTree.processTimeIntQueries(timeIntInput);
		writeOutput(timeIntOutput, timeIntMobileIDs);
		System.out.println("Done with Task 3!");
		
		// Task 4
		ArrayList<List<Long>> timeIntAllMobileIDs = MyTree.processTimeAllIntQueries(timeAllIntInput);
		writeOutput(timeAllIntOutput, timeIntAllMobileIDs);
		System.out.println("Done with Task 4!");
	}
}
