/**
 * Title: Anagram.java 
 * Abstract: asks the user for two words, then checks to see if they are anagrams
 * 
 * Author: Matt Wall 
 * ID: 1399 Date: 09/22/2015
 */

import java.util.Arrays;
import java.util.Scanner;

public class Anagram {

	public static void main(String[] args) {
		String firstWord, secondWord;     	//input from user
		char[] fwArray;						//to hold input words as char arrays
		char[] swArray;
		
		Scanner in = new Scanner(System.in);
				
		System.out.print("Enter first word: ");
		firstWord = in.next();
		System.out.print("Enter second word: ");
		secondWord = in.next();
		
		//if lengths are different, it is not an anagram
		if (firstWord.length() != secondWord.length()) {
			noAnagram();
			System.exit(0);
		}
		
		//convert string to char array
		fwArray = firstWord.toCharArray();
		swArray = secondWord.toCharArray();		
		
		//sort both character arrays
		Arrays.sort(fwArray);
		Arrays.sort(swArray);
		
		//check one by one -- since it is sorted, if the letters are different, no anagram
		for (int i = 0; i < firstWord.length(); i++ ) {
			if(fwArray[i] != swArray[i]) {
				noAnagram();
				System.exit(0);
			}
		}
		//all letters matched it is an anagram
		anagram();		
		
	}
	
	static void noAnagram() {
		System.out.println("No anagram.\nDone.");
	}
	
	static void anagram() {
		System.out.println("Anagram.\nDone.");
	}

}
