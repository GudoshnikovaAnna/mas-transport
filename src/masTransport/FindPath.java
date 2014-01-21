package masTransport;

import java.io.*;
import java.util.*;

import javax.*;

public class FindPath {
	Parse parse = new Parse();
	int[][] graph;
	 
	String s = parse.readFromFile();
	public FindPath(){

	}
	
	private void ParseGraph() {
		
		int k = 0;
		int u = 0;
		while (s.charAt(k) != ('\r') && s.charAt(k) != ('\n')) {
			if (s.charAt(k++) == (',')) {
				u++;
			}
		}

		k = u;

		String s2 = s.replaceAll("\r\n", "");
		
		graph = new int[k][k];
		int[] list = new int[k * k];
		int h = 0;
		
		for (int i = 0; i < s2.length(); i++) {
			StringBuilder gS = new StringBuilder();
			
			while (s2.charAt(i) != (',')) {
				gS.append(s2.charAt(i++));
			}	
			list[h++] = Integer.parseInt(gS.toString());
		}
		
		//System.out.println();

		for (int i = 0; i < k; i++) {
			for (int j = 0; j < k; j++) {
				graph[i][j] = list[j + i * k];
			}
		}
	}
	
	private Path DFS(int start, int end, boolean[] visited)
    {
		//System.out.println("We are in DFS function");
        if (start == end)
        {
        	ArrayList<Integer> newPath = new ArrayList<Integer>();
        	newPath.add(start);
        	return new Path(newPath, 0);
        }

        ArrayList<Integer> minPath = null;
        int minPathLength = Integer.MAX_VALUE;

        for (int i = 0; i < graph.length; i++)
        {
            if (!visited[i] && graph[start][i] != 0)
            {
                visited[i] = true;
                Path result = DFS(i, end, visited);
                visited[i] = false;
                
                if (result.Item1() != null)
                {
                    if (result.Item2() + graph[start][i] < minPathLength)
                    {
                    	minPathLength = result.Item2() + graph[start][i];
                    	minPath = result.Item1();
                    }
                }
            }
        }

        if (minPath == null)
        {
        	return new Path(null, 0);
        }
        else
        {
            ArrayList<Integer> path = new ArrayList<Integer>();
            path.add(start);
            path.addAll(minPath);
            return new Path(path, minPathLength);
        }
    }

    public Path findShortestPath(int from, int to)
    {
    	ParseGraph();
    	//System.out.println("We are here");
        return DFS(from, to, new boolean[graph.length]);
    }
	
}