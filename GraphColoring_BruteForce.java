/***Graph Coloring: Brute Force
 * This class prints out the Chromatic Number of a randomly generated graph with a given number of a vertices
 * Author: Michelle Kim
 * ***/


import java.util.Random;
import java.io.*;
import java.util.ArrayList;


public class GraphColoring {

	private int numVertices;
	private int prob;
	private int seed;
	private int maxEdges; 
	private int maxprob;

	private int[] randomEdges;
	private int[][] matrix;
	private String colors;

	private ArrayList<String> colorPossibilities;
	private long startTime;

	private int chromaticColor;


	public GraphColoring(int numVertices, int seed, int probability){
		
		
		this.maxprob = 100;
		this.prob = probability;
		this.numVertices = numVertices;
		this.maxEdges = (numVertices*(numVertices-1))/2; //num edges if connected graph
		//System.out.println("max num of edges:" + maxEdges); 
		this.randomEdges =  new int[maxEdges];
		this.seed = seed;
		this.matrix = new int[maxEdges][maxEdges];
		this.chromaticColor = 0;
		this.colorPossibilities = new ArrayList<String>();
		this.colors = "0123456789abcdefghijklmonpqrstuvwxyz"; //each char is a color option

		this.generateAdjacencyMatrix();
		this.updatePossibleColoringArray(this.getColorString(numVertices));
		this.updatesLowestChromaticNumber();
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


	/***Appends all color schemes to arraylist & Tests all possible permutations***/
	public void updatePossibleColoringArray(String color) {

		//adds all colorings into the arraylist
		for (int s = 0; s < color.length(); s++) {
			this.colorPossibilities.add(Character.toString(color.charAt(s)));
			//System.out.println(Character.toString(color.charAt(s)));
		}
		this.startTime = System.currentTimeMillis();
		//tests all color possibilities with length less than number of vertices
		while(this.colorPossibilities.get(0).toString().length() < this.numVertices) {
			for(int i = 0; i < color.length(); i++){
				String newColorString = this.colorPossibilities.get(0).toString() + color.charAt(i);
				this.colorPossibilities.add(newColorString.toString());
		//		System.out.println("added color string: " + newColorString.toString());
			}
		//	System.out.println("removed color string: " + this.colorPossibilities.get(0));
			this.colorPossibilities.remove(0); 
		}
		//System.out.println(this.colorPossibilities.toString());
	}
	
	
	/**Checks if no adjacent vertices share the same color**/
	public boolean isColorable(String input){

		for(int currVertex = 0; currVertex < input.length(); currVertex++) {
			for(int adjacentVertex = 0; adjacentVertex < numVertices; adjacentVertex++) {

				if(matrix[currVertex][adjacentVertex] > prob) { //if random int > prob, an edge exists

					char currVertexColor = input.charAt(currVertex); 
					char adjacentColor = input.charAt(adjacentVertex); 
					
					if (currVertexColor == adjacentColor) {
						return false; //return false if colors are matching on adjacent vertices
					}
				}
			}
		}
		return true; //if no colors are matching on adjacent vertices, this color scheme works!
	}
	
	
	/***Gets Number of Unique Colors in Possible Coloring option***/
	public int getChromaticPossibility(String possibleColoring){
		
		int potentialChromatic = 0;
		char[] temp = possibleColoring.toCharArray();
		//System.out.println("possibleColoring length: " + temp.length );
		ArrayList<Character> uniqueColors = new ArrayList<Character>();

		for(int v = 0; v < temp.length; v++){
			if (!uniqueColors.contains(temp[v])){
				uniqueColors.add(temp[v]);
				potentialChromatic++;
				//System.out.println("added " + temp[v] + " to array");
			}
		}
		//System.out.println("num unique colors " + potentialChromatic );
		return potentialChromatic;
	}
	
	
	/**Checks for lowest number of colors in all valid coloring possibilities**/
	public void updatesLowestChromaticNumber() {

		for(int c = 0; c < colorPossibilities.size(); c++){
			String possibleColoring = colorPossibilities.get(c);

			if (this.isColorable(possibleColoring)) { //if coloring is valid 
				int currChromatic = getChromaticPossibility(possibleColoring);

				if(chromaticColor > 0) {
					if(currChromatic < this.chromaticColor) {
						this.chromaticColor = currChromatic;
					}
				}
				else { //chromaticColor has not been initialized 
					this.chromaticColor = currChromatic;
				}
			}
		}
	}
	
	

	public static void main(String [] args) {
		int numVertices = Integer.parseInt(args[0]);
		int seed = Integer.parseInt(args[1]);
		int probability = Integer.parseInt(args[2]);
		GraphColoring gc = new GraphColoring(numVertices, seed, probability);
		
	}
}
