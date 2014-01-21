package masTransport;
import java.util.ArrayList;

public class Path {
	ArrayList<Integer> shortestPath;
	int pathValue;
	
	public Path (ArrayList<Integer> arr, int i){
		this.shortestPath = arr;		
		this.pathValue = i;
	}
	
	public ArrayList<Integer> Item1(){
		return this.shortestPath;
	}
	
	public int Item2(){
		return this.pathValue;
	}
}
