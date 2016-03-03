import java.util.ArrayList;
import java.util.Scanner;

/**
 * Title: Partition.java Abstract: This program asks the user for a quantity,
 * then a list of numbers The numbers are then compared against each other to
 * create a partition between numbers that have an equal sum for example: user
 * enters 4 number, 1, 3, 5 and 7 output is Partition 1 7 vs 3 8 Author: Matt
 * Wall ID: 1399 Date: 09/11/2015
 */

public class Partition {

	public static void main(String[] args) {
		int numInputs = 0;
		Scanner in = new Scanner(System.in);

		System.out.print("Number of inputs:  ");
		numInputs = in.nextInt();

		int[] nums = new int[numInputs];
		System.out.print("Enter " + numInputs + " numbers: ");
		for (int i = 0; i < numInputs; i++) {
			nums[i] = in.nextInt();
		}

		// permutation arrays -> permleft == binary counting from 0 to 2^n
		// permright is the complement of permleft.

		int arrayHeight = (int) Math.pow(2, numInputs);
		int[][] permLeft = new int[arrayHeight][numInputs];
		int[][] permRight = new int[arrayHeight][numInputs];
		int column = numInputs;

		// outer loop to move across columns
		for (int i = 0; i < numInputs; i++) {
			int duration = (int) Math.pow(2, column) / 2; // how many 0s or 1s to oscillate in column
			int row = 0;

			/*
			 * when 3 numbers are entered, we will need 8 rows of numbers -> 2^n 
			 * each row oscillates 0's and 1s for a duration of 2^column/2 
			 * column 3 (third from right) [3][2][1] oscillates (2^3)/2 or every 4 numbers so,
			 *  0 for four rows, then 1s for four rows. Row 2 (second from right rows [3][2][1] oscillates 
			 *  (2^2)/2 or every 2 numbers. 0s for 2 rows, then 1s for 2 rows. k oscillates 0's in left Permutation Array
			 *  array and 1s in right Permutation array. l(L) is the complement to k
			 */

			while (row < arrayHeight) {
				for (int k = 0; k < duration; k++) {
					permLeft[row][i] = 0;
					permRight[row][i] = 1;
					row++;
				}
				for (int l = 0; l < duration; l++) {
					permLeft[row][i] = 1;
					permRight[row][i] = 0;
					row++;
				}

			}
			column--;
		}

		/*
		 * Multiply permutation arrays by number inputs and check for equality multiplying permleft 
		 * by list of numbers will give number times either 1 or 0. The set of numbers [1][3][5][7] will be
		 * multiplied by leftPerm row 1, which == 1 in binary -> [0][0][0][1] This will give the sum of 0 + 0 + 0 + 7 -> 7
		 * rightPerm row 1, which is the complement of 1 in binary -> [1][1][1][0] multiplied by the set of numbers will give the sum
		 * of 1 + 3 + 5 + 0 -> 9 the comparison is then made of the two sums 7 != to 9, so this is not a partition
		 */
		int leftSum, rightSum;
		int tempNum;
		ArrayList<Integer> leftArray = new ArrayList<Integer>();
		ArrayList<Integer> rightArray = new ArrayList<Integer>();
		int leftIterate, rightIterate;
		for (int i = 1; i < arrayHeight; i++) {
			leftIterate = rightIterate = 0;
			leftArray.clear();
			rightArray.clear();
			leftSum = rightSum = 0;

	

			for (int j = 0; j < numInputs; j++) {
				tempNum = nums[j] * permLeft[i][j];
				leftSum += tempNum;
				if (tempNum != 0) {
					leftArray.add(leftIterate, tempNum);
					leftIterate++;
				}
				tempNum = nums[j] * permRight[i][j];
				rightSum += tempNum;
				if (tempNum != 0) {
					rightArray.add(rightIterate, tempNum);
					rightIterate++;

				}

			}
			// if the sums are equal, print the partition
			if (leftSum == rightSum) {
				System.out.print("Partition: ");

				for (int k = 0; k < leftArray.size(); k++) {
					System.out.print(leftArray.get(k) + " ");
				}
				System.out.print("vs ");
				for (int l = 0; l < rightArray.size(); l++) {
					System.out.print(rightArray.get(l) + " ");
				}
				return;
			}

		}
		System.out.println("No Partition");
	}

}
