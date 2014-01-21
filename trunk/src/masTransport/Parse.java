package masTransport;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

public class Parse {
	String s = "";
	//int[][] graph;
	
	public Parse(){}

	public String readFromFile() {
		File fileName = new File("test.txt");
		Scanner in;
		try {
			in = new Scanner(fileName);
			while (in.hasNext()) {
				s += in.nextLine() + "\r\n";
			}
			in.close();
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return s;
	}
}
