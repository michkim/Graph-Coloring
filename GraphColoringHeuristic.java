/***Graph Coloring

 * This class prints out the Chromatic Number of a randomly generated graph with a given number of a vertices
 * Author: Michelle Kim
 * ***/


import java.util.Random;
import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;


public class GraphColoringHeuristic {

	private int numVertices;
	private int prob;
	private int seed;
	private int maxEdges; 
	private int maxprob;
	private long startTime;

	private int[] randomEdges;
	private int[][] matrix;
	private String colors;

	private ArrayList<Character> usedColorsArray;
	private HashMap<Integer, Character> map;

	private int chromaticColor;


	public GraphColoringHeuristic(int numVertices, int seed, int probability){
		
		this.maxprob = 100;
		this.prob = probability;
		this.numVertices = numVertices;
		this.maxEdges = (numVertices*(numVertices-1))/2; //num edges if connected graph
		//System.out.println("max num of edges:" + maxEdges); 
		this.randomEdges =  new int[maxEdges];
		this.seed = seed;
		this.matrix = new int[maxEdges][maxEdges];
		this.chromaticColor = 0;
		this.usedColorsArray = new ArrayList<Character>();
		this.colors = "0123456789abcdefghijklmonpqrstuvwxyz"; //each char is a color option
		
		this.map = new HashMap<Integer, Character>();

		this.generateAdjacencyMatrix();
		this.colorVertices();
		this.getChromaticNum();
		System.out.println("Chromatic Color: " + chromaticColor);
		
		long endTime = System.currentTimeMillis();
		long totalTime = endTime - startTime;
		System.out.println("time: " + totalTime);

	}
	
	
	/***Generates ColorString of 0-n with the length as number of Vertices***/
	public String getColorString(int numVertices) {
		String colorstring = "";
		for(int i = 0; i < numVertices; i++){
			colorstring = colorstring + this.colors.charAt(i);
		}
		//System.out.println(colorstring);
		return colorstring;
	}


	/***Generates random int that stays consistent with given seed***/
	public void generateRandomEdges(int seed) {
		Random random = new Random(seed);
		for(int e = 0; e < this.maxEdges; e++){
			int randInt = random.nextInt(maxprob); //restrict b/w 0 and seed
			randomEdges[e] = randInt;
				//System.out.print(randInt);
		}
	}

	
	/***Generates Adjacency Matrix of random ints based on seed*/
	public void generateAdjacencyMatrix() {
		this.generateRandomEdges(seed);
		
		int count = 0;
		for(int i = 0; i<numVertices; i++) { 
			for(int j = i; j<numVertices;j++) { //j=i, 

				if(i==j) { //bc we cannot have edge to same vertex, or else graph coloring does not work (with self loops)
					matrix[i][j] = 0;
					matrix[j][i] = 0;
				}
				else {
					matrix[i][j] = randomEdges[count]; //assigns random int to each edge
					matrix[j][i] = randomEdges[count];
					count++;
					//	System.out.print(count);
				}
				//System.out.print(matrix[i][j]);
			}
		}
		for(int x=0; x<numVertices; x++) {
			for(int y=0; y<numVertices; y++) {
				//System.out.print(matrix[x][y]);
			}
			//System.out.print("\n");
		}
	}
	
	/***Greedy grab of best available coloring for each vertex***/
	public void colorVertices() {
		this.startTime = System.currentTimeMillis();
		map.put(0, colors.charAt(0)); //init first color
		for(int v=1; v<numVertices; v++) {
			this.usedColorsArray = new ArrayList<Character>();
			for(int w=0; w<numVertices; w++) {
				if((matrix[v][w]>prob) && (map.containsKey(w))) {//edges exists, adj vertex has been colored
					this.usedColorsArray.add(map.get(w));
					//System.out.println("added color " + map.get(w) +" to used colors");
				}
			}
			map.put(v, this.getFirstUnusedColor()); //sets vertex w first unique color
			//System.out.println("set vertex " + v + " to color " + this.getFirstUnusedColor());
		}
		
	}
	
	/***returns first unique color option***/
	public Character getFirstUnusedColor() {
		for(int c=0; c<this.colors.length(); c++) {
			if(!this.usedColorsArray.contains(colors.charAt(c)))  {
				return colors.charAt(c);
			}
		}
		return 'Z';
	}

	
	
	
	/***Gets Number of Unique Colors in Possible Coloring option***/
	public int getChromaticNum(){
		
		//System.out.println("possibleColoring length: " + temp.length );
		ArrayList<Character> uniqueColors = new ArrayList<Character>();
		
		    Iterator it = map.entrySet().iterator();
		    while (it.hasNext()) {
		        Map.Entry<Integer, Character> pair = (Map.Entry<Integer, Character>)it.next();
		        if (!uniqueColors.contains(pair.getValue())){
					uniqueColors.add( pair.getValue());
					chromaticColor++;
					//System.out.println("added " + pair.getValue() + " to array");
				}
		      //  System.out.println(pair.getKey() + " = " + pair.getValue());
		        it.remove(); 
		    }
		

	//	System.out.println("num unique colors " + chromaticColor );
		return chromaticColor;
	}
	
	

	
	

	public static void main(String [] args) {
		int numVertices = Integer.parseInt(args[0]);
		int seed = Integer.parseInt(args[1]);
		int probability = Integer.parseInt(args[2]);
		GraphColoringHeuristic gc = new GraphColoringHeuristic(numVertices, seed, probability);
		
	}
}
