import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;
import java.util.Stack;
import java.util.StringTokenizer;

/**
 * ask a user for an input file to read a 2-D array of a sequence of numbers. 
 In the array, number one indicates that there’s a coin on the cell, 
 while zero means no coin. Displays maximum number of coins and path to collect them. 
 Assumption that the board size is less than or equal to 50 x 50.
 */
public class CoinCollection {
    public static int numRows;
    public static int numColumns;
    public static int[][] twoDGameBoard;
    public static int[][] solutionArray;
    public static Stack<Pair> winners = new Stack<Pair>();

    public static void main(String args[]) throws IOException {
        Scanner in = new Scanner(System.in);
        String inFile;

        System.out.print("Enter a file name: ");
        inFile = in.next();

        String delimiters = ",";
        StringTokenizer st;

        FileInputStream fStream = new FileInputStream(inFile);
        BufferedReader br = new BufferedReader(new InputStreamReader(fStream));

        String strLine = br.readLine();
        st = new StringTokenizer(strLine, delimiters);
        numRows = Integer.parseInt(st.nextToken());
        numColumns = Integer.parseInt(st.nextToken());
        twoDGameBoard = new int[numRows + 1][numColumns + 1];
        solutionArray = new int[numRows + 1][numColumns + 1];

        //set 0th row and 0th column to 0
        for (int i = 0; i <= numRows; i++) {
            twoDGameBoard[i][0] = 0;
            solutionArray[i][0] = 0;
        }
        for (int j = 0; j <= numColumns; j++) {
            twoDGameBoard[0][j] = 0;
            solutionArray[0][j] = 0;
        }

        //read info from text file
        for (int i = 1; i <= numRows; i++) {
            strLine = br.readLine();
            st = new StringTokenizer(strLine, delimiters);
            for (int j = 1; j <= numColumns; j++) {
                twoDGameBoard[i][j] = Integer.parseInt(st.nextToken());
            }
        }

        collectCoins();
        System.out.println("Max Coins: " + solutionArray[numRows][numColumns]);
        backTrace();
        printPath();
    }

    private static void collectCoins() {
        solutionArray[1][1] = twoDGameBoard[1][1];

        //set values for row 1 -> previous column row 1 + current column row 1
        for (int j = 2; j <= numColumns; j++) {
            int prevVal = solutionArray[1][j - 1];
            int curVal = twoDGameBoard[1][j];
            solutionArray[1][j] = prevVal + curVal;
        }
        //compute rows 2 through numRows
        for (int i = 2; i <= numRows; i++) {
            solutionArray[i][1] = solutionArray[i - 1][1] + twoDGameBoard[i][1];
            int newVal = solutionArray[i][1];
            for (int j = 2; j <= numColumns; j++) {
                solutionArray[i][j] = returnMax(i, j);
            }
        }

    }

    private static void printPath() {
        int winSize = winners.size();
        System.out.print("Path: ");
        for (int i = 1; i <= winSize; i++) {
            Pair tempPair = winners.pop();
            System.out.print("(" + tempPair.getI() + ", "
                    + tempPair.getJ() + ")");
            if (i != winSize) {
                System.out.print("->");
            }
        }
    }

    public static int returnMax(int i, int j) {
        int currentPos = twoDGameBoard[i][j];
        int fromLeft = solutionArray[i][j - 1] + currentPos;
        int fromAbove = solutionArray[i - 1][j] + currentPos;
        //if (weight[i] > j || j - weightI < 0) { return lastMax; }
        return Math.max(fromAbove, fromLeft);
    }

    public static void backTrace() {
        int i = numRows;
        int j = numColumns;
        int coinCount = solutionArray[numRows][numColumns];
        Pair tempPair = new Pair(i, j);
        winners.push(tempPair);

        while (i >= 1 && j >= 1) {
            if (solutionArray[i - 1][j] > solutionArray[i][j - 1]) {
                i--;
            } else if (j > 1) {
                j--;
            } else {
                i--;
            }
            tempPair = new Pair(i, j);
            winners.push(tempPair);
            if (j == 1 && i == 1) {
                break;
            }
        }
    }

    private static class Pair {
        int i;
        int j;

        Pair(int i, int j) {
            this.i = i;
            this.j = j;
        }

        public int getI() {
            return i;
        }

        public int getJ() {
            return j;
        }
    }
}