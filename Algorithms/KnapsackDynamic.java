import java.util.*;

/**
 * Title: Knapsack.java
 * Abstract:Dynamically solve Knapsack Problem
 * <p>
 * Author: Matt Wall
 * ID: 1399 Date: 11/16/2015
 */
public class KnapsackDynamic{
    public static int[][] solutionArray = null;
    public static Stack<Integer> winners = new Stack<Integer>();

    public static int numItems;
    public static int capacity;

    public static int[] valueArray;
    public static int[] weightArray;

    public static void main(String args[]) {

        //Get input from user
        Scanner in = new Scanner(System.in);
        System.out.print("Enter number of items: ");
        numItems = in.nextInt();

        valueArray = new int[numItems + 1];
        weightArray = new int[numItems + 1];

        System.out.print("Enter knapsack capacity: ");
        capacity = in.nextInt();

        System.out.println("Enter weights and values of " + numItems + " items: ");
        for (int i = 1; i <= numItems; i++) {
            System.out.print("Item " + i + ":");
            weightArray[i] = in.nextInt();
            valueArray[i] = in.nextInt();
        }
        System.out.println();
        in.close();


        //Initialize array and set to 0 for all weights where item == 0
        solutionArray = new int[numItems + 1][];
        solutionArray[0] = new int[capacity + 1];
        for (int i = 0; i <= capacity; i++) {
            solutionArray[0][i] = 0;
        }

        //find max value for each item
        for (int i = 1; i <= numItems; i++) {
            solutionArray[i] = new int[capacity + 1];
            for (int j = 0; j <= capacity; j++) {
                solutionArray[i][j] = returnMax(valueArray, weightArray, i, j);
            }
        }

        //find # of digits in capacity for formatting
        int capacityDigits = 0;
        int tempCapacity = capacity;
        while (tempCapacity > 0) {
            capacityDigits++;
            tempCapacity /= 10;
        }

        //find # of digits in numItems for formatting
        int itemDigits = 0;
        int tempItems = numItems;
        while (tempItems > 0) {
            itemDigits++;
            tempItems /= 10;
        }

        //print header for table
        System.out.print("i\\j:");
        for (int i = 0; i <= capacity; i++) {
            System.out.printf("%" + (capacityDigits + 2) + "d", i);
        }
        System.out.println();

        //print value table
        for (int i = 0; i <= numItems; i++) {
            System.out.printf(i + "%3s", ":");
            for (int j = 0; j <= capacity; j++) {
                System.out.printf("%" + (capacityDigits + 2) + "d", solutionArray[i][j]);
            }
            System.out.println();
        }

        backTrace();

        //print best set and max value
        System.out.print("\nBest set: ");

        int wSize = winners.size();
        for (int i = 0; i < wSize; i++) {
            System.out.print(winners.pop());
            if (i != wSize - 1) System.out.print(", ");
        }

        System.out.println("\nMax value: " + solutionArray[numItems][capacity]);

    }

    //return higher value between A[i-1] and A[i-1] + weight[i] that is < capacity
    public static int returnMax(int[] value, int[] weight, int i, int j) {
        int weightI = weight[i];
        if (weight[i] > j) {
            return solutionArray[i - 1][j];
        }
        int valueI = value[i];
        int lastMax = solutionArray[i - 1][j];
        int nextTry = solutionArray[i - 1][j - weightI] + valueI;
        return Math.max(lastMax, nextTry);
    }

    //find items used from solutionArray
    public static void backTrace() {
        int i = numItems;
        int j = capacity;

        while (i > 0) {
            while (solutionArray[i][j] == solutionArray[i - 1][j] && j > 0) {
                i--;
            }
            winners.push(i);
            j -= weightArray[i];
            i--;
        }
    }

}



