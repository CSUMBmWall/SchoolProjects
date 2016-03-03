/**
 * Title: Assignment.java 
 * Abstract: Assignment problem
 * 				Asks the user for number of people and costs of different jobs
 * 				if user enters 2 people, they may have 2 different job values
 * 				All permutations are calculated for total job costs
 * 				These are printed out, along with the lowest value
 * 
 * Author: Matt Wall 
 * ID: 1399 Date: 10/4/2015
 */

package edu.csumb.cst370;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

public class Assignment {
	
	public static int index = 0;

	public static int permutation = 1;
	public static int numJobs;

	// holds all 1's, 2's 3's, etc -- when multiplied by binaryMatrix, give job
	// chosen
	public static int[][] genericJobMatrix;

	// holds job input from user
	public static int[][] costMatrix;

	/* holds 001, 010, 100 for a user's numJobs of 3 when multiplied by generic job
	matrix, gives chosen job when multiplied by jobMatrix, chooses correct
	job from each row */
	public static int[][] binaryMatrix;

	// holds chosen assignment
	public static ArrayList<Integer> costArray = new ArrayList<Integer>();

	// holds which job was chosen
	public static ArrayList<Integer> chosenJobArray = new ArrayList<Integer>();
	
	//hash map to hold total cost and individual costs
	public static Map<Integer, ArrayList<Integer>> costMap = new HashMap<Integer, ArrayList<Integer>>();

	public static void main(String[] args) {
		
		Scanner in = new Scanner(System.in);

		// get info from user
		System.out.print("Number of jobs: ");
		numJobs = in.nextInt();		

		// initialize array to hold assignment costs
		costMatrix = new int[numJobs][numJobs];

		// populate array for user entered assignment costs
		System.out.println("Enter assignment costs of " + numJobs + " persons:");
		for (int i = 0; i < numJobs; i++) {			
			System.out.print("Person " + (i + 1) + ": ");
			for (int j = 0; j < numJobs; j++) {
				costMatrix[i][j] = in.nextInt();				
			}
		}
		System.out.println();

		in.close();				

	    /*populate binary matrix for permutations
		for 3 jobs -> 3 rows -> 100 010 001
		for 2 jobs -> 2 rows -> 10 01 */
		binaryMatrix = new int[numJobs][];

		for (int i = 0; i < numJobs; i++) {
			binaryMatrix[i] = new int[numJobs];
			for (int j = 0; j < numJobs; j++) {
				if (j == i)
					binaryMatrix[i][j] = 1;
				else {
					binaryMatrix[i][j] = 0;
				}
			}
		}				

		/* populate matrix to hold rows of jobs to find permutation
		will be multiplied by binary matrix
		3 jobs -> 3 rows -> 111 222 333
		2 jobs -> 2 rows -> 11 22 */

		populateJobsMatrix();
		
		//call recursive function to compute permutations of jobs
		recursiveAssignment(numJobs);		
		printBigWinner();
	}
	
	//no explanation needed
	private static void printBigWinner() {
		int minKey = Collections.min(costMap.keySet());
		ArrayList<Integer> minValues = new ArrayList<Integer>();
		minValues = costMap.get(minKey);
		 System.out.print("\nSolution: < ");
		 
		 for (int i = 0; i < minValues.size(); i++) {
			 System.out.print(minValues.get(i));
			 if (i == minValues.size() - 1) {
				 System.out.print("> => total cost: " + minKey);
			 }
			 else {
				 System.out.print(", ");
			 }
		 }		
	}
	
	//recursive method to compute permutation
	public static void recursiveAssignment(int n) {
		if (n == 0) {
			multiplyMatrices(binaryMatrix, genericJobMatrix, chosenJobArray);
			multiplyMatrices(binaryMatrix, costMatrix, costArray);
			printPerms();
		}
		
		else {
			for (int i = 0; i < n; i++) {
				recursiveAssignment(n - 1);
				if (n % 2 == 1) {
					swapMatricesOdd(n,genericJobMatrix);
					swapMatricesOdd(n, binaryMatrix);					
						
				}
				//if (n % 2 == 0) {
				else {
					swapMatricesEven(i, n, genericJobMatrix);					
					swapMatricesEven(i, n, binaryMatrix);
				}
			} 
		}
		return;
	}
	
	//print list of permutations and job values
	public static void printPerms() {
		int hashKey = 0;
		ArrayList<Integer> hashArray = new ArrayList<>(); 
		int arrSize = chosenJobArray.size();
		System.out.print("Permutation " + permutation + ": <");
		for (int i = 0; i < arrSize; i++) {
			System.out.print(chosenJobArray.get(i));
			hashArray.add(chosenJobArray.get(i));
			if (i != arrSize - 1) {
				System.out.print(", ");
			} else {
				System.out.print("> => total cost: " + totalJobCost());
				hashKey = totalJobCost();
			}
		}
		permutation++;
		costMap.put(hashKey, hashArray);
		System.out.println();
	}
	
	//print of of the matrixes for binary values, job costs, or generic job matrix
	public static void printMatrix(int[][] m) {
		for (int i = 0; i < m.length; i++) {
			for (int j = 0; j < m[i].length; j++) {
				System.out.print(m[i][j] + " ");
			}
			System.out.println();
		}
	}
	
	//multiply two matrices and store in an Arraylist
	public static void multiplyMatrices(int[][] matrixArray, int[][] nums, ArrayList<Integer> array) {
		int sum;
		int product = 0;
		int iterate = 0;

		array.clear();
		for (int i = 0; i < numJobs; i++) {
			sum = 0;

			for (int j = 0; j < numJobs; j++) {
				int tempnum1 = matrixArray[i][j];
				int tempnum2 = nums[i][j];
				product = tempnum1 * tempnum2;
				sum += product;
			}
			array.add(iterate, sum);
			iterate++;
		}
	}

	//for instances of recursive algorithm where n is odd swap matrixArray A[0] with A[n]
	public static void swapMatricesOdd(int n, int[][] matrix) {
		int[] lineOneArray = new int[numJobs];
		int[] lineNArray = new int[numJobs];

		pullLineMatrix(lineOneArray, 0, matrix);
		pullLineMatrix(lineNArray, n-1, matrix);

		putLineMatrix(lineOneArray, n-1, matrix);
		putLineMatrix(lineNArray, 0, matrix);
	}

	//for instances of recursive algorithm where n is even swap matrixArray A[i] and A[n]
	public static void swapMatricesEven(int i, int n, int[][] matrix) {
		int[] lineOneArray = new int[numJobs];
		int[] lineNArray = new int[numJobs];

		pullLineMatrix(lineOneArray, n-1, matrix);
		pullLineMatrix(lineNArray, i, matrix);

		putLineMatrix(lineOneArray, i, matrix);
		putLineMatrix(lineNArray, n-1, matrix);
	}
	
	//pull row from Matrix
	public static void pullLineMatrix(int[] array, int x, int[][] matrix) {
		for (int i = 0; i < numJobs; i++) {
			array[i] = matrix[x][i];
		}
	}
	
	//replace matrix row contents 
	public static void putLineMatrix(int[] array, int x, int[][] matrix) {
		for (int i = 0; i < numJobs; i++) {
			matrix[x][i] = array[i];
		}
	}
	
	//populate generic job matrix to find which position was used in cost Matrix
	public static void populateJobsMatrix() {
		genericJobMatrix = new int[numJobs][numJobs];
		for (int i = 0; i < numJobs; i++) {
			for (int j = 0; j < numJobs; j++) {
				genericJobMatrix[i][j] = i + 1;
			}
		}
	}
	
	//add all sums of costArray to find job cost total
	public static int totalJobCost() {
		int sum = 0;
		for (int nums : costArray) {
			sum += nums;
		}
		return sum;
	}

}
