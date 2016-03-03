/* Title: SubstitutionCipher.java
 * Abstract:  Provides ability to analyze cipher text
 *  			Letter, digram and trigram frequencies are available
 * Author: Matt Wall
 * ID: 1399
 * Date: 4/15/2015
 */
package substitutionCipher;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

public class SubstitutionCipher {
	// hasmap to hold characters and the number of occurrences
	public static HashMap<Character, Integer> charFrequencyMap = new HashMap<Character, Integer>();

	// hashmap to hold value of characters and the number of occurrences
	public static HashMap<Integer, Integer> charValueMap = new HashMap<Integer, Integer>();

	// hashmap to hold digrams and frequencies
	public static HashMap<String, Integer> digrams = new HashMap<String, Integer>();

	// hashmap to hold trigrams and frequencies
	public static HashMap<String, Integer> trigrams = new HashMap<String, Integer>();
	
	//counts each letter of cipher text and places it in a Hashmap for counting purposes
	//sorts and outputs each letter with its frequency and percentage
	public static void analyzeCipher(String x) {
		int charValx = 0;			//hex value of each character
		final int OFFSET = 97;		//value difference between hex and ASCII character
		char tempChar;				//individual character from cipher text
		StringBuilder sb = new StringBuilder();
		
		//parse cipher text for letters and store to HashMap for counting
		for (int i = 0; i < x.length(); i++) {
			tempChar = x.charAt(i);
			charValx = (Character.getNumericValue(tempChar) + OFFSET);

			if (!letterExists(tempChar)) {
				charFrequencyMap.put(tempChar, 1);
				charValueMap.put(charValx, 1);
			} else {
				Integer n = charFrequencyMap.get(tempChar);
				n++;
				charFrequencyMap.put(tempChar, n);
				charValueMap.put(charValx, n);
			}
		}
		
		//sort frequency by values
		HashMap<Character, Integer> sortedMap = sortByValues(charFrequencyMap);		
		
		NumberFormat formatter = new DecimalFormat("#0.00");
		double count, percent, sum = 0;
		
		//print character, frequency and percentage
		System.out.println("Character\t  Occurrences\t Percentage\n");
		for (Entry<Character, Integer> e : sortedMap.entrySet()) {
			count = e.getValue();
			percent = count / x.length();
			sum += percent;
			System.out.println("    " + e.getKey() + "\t\t    " + e.getValue()
					+ "\t\t  " + formatter.format(percent * 100));
		}

		// System.out.println("Total percentage: " + sum);
	}
	
	//check if letter exists in charFrequency Map
	public static boolean letterExists(Character c) {
		return (charFrequencyMap.get(c) != null);
	}
	
	//sort Hashmap by values
	private static HashMap sortByValues(HashMap map) {
		List list = new LinkedList(map.entrySet());
		// Defined Custom Comparator here
		Collections.sort(list, new Comparator() {
			public int compare(Object o1, Object o2) {
				return ((Comparable) ((Map.Entry) (o1)).getValue())
						.compareTo(((Map.Entry) (o2)).getValue());
			}
		});

		// Here I am copying the sorted list in HashMap
		// using LinkedHashMap to preserve the insertion order
		HashMap sortedHashMap = new LinkedHashMap();
		for (Iterator it = list.iterator(); it.hasNext();) {
			Map.Entry entry = (Map.Entry) it.next();
			sortedHashMap.put(entry.getKey(), entry.getValue());
		}
		return sortedHashMap;
	}
	
	//Replace cipher text characters with plaintext character
	public static void decrypt(String y) {
		char tempChar;
		StringBuilder sb = new StringBuilder();

		for (int i = 0; i < y.length(); i++) {
			tempChar = y.charAt(i);
			if 		  (tempChar == 'F') { sb.append("w");
			} else if (tempChar == 'G') { sb.append("a");
			} else if (tempChar == 'Z') { sb.append("h");
			} else if (tempChar == 'C') { sb.append("e");
			} else if (tempChar == 'S') { sb.append("o");
			} else if (tempChar == 'D') { sb.append("b");
			} else if (tempChar == 'Y') { sb.append("r");
			} else if (tempChar == 'O') { sb.append("n");
			} else if (tempChar == 'E') { sb.append("i");
			} else if (tempChar == 'N') { sb.append("l");
			} else if (tempChar == 'U') { sb.append("t");
			} else if (tempChar == 'L') { sb.append("y");
			} else if (tempChar == 'M') { sb.append("m");
			} else if (tempChar == 'W') { sb.append("g");
			} else if (tempChar == 'H') { sb.append("f");
			} else if (tempChar == 'K') { sb.append("s");
			} else if (tempChar == 'P') { sb.append("u");
			} else if (tempChar == 'I') { sb.append("d");
			} else if (tempChar == 'X') { sb.append("p");
			} else if (tempChar == 'J') { sb.append("c");
			} else if (tempChar == 'Q') { sb.append("j");
			} else if (tempChar == 'A') { sb.append("v");
			} else {
				sb.append(tempChar);
				//sb.append("-");
			}

		}
		System.out.println(sb.toString());
	}
	
	//parse ciphertext for digrams
	public static void digramAnalysis(String y) {

		StringBuilder sb = new StringBuilder();
		Character temp1, temp2, temp3;
		String tempDigram, tempTrigram;

		for (int i = 0; i < y.length() - 2; i++) {
			// loop positions for last trigram, special case is needed for last
			// two characters

			// get chars at position 0, 1, 2
			temp1 = y.charAt(i);
			temp2 = y.charAt(i + 1);
			temp3 = y.charAt(i + 2);

			sb.append(temp1);
			sb.append(temp2);				// add first two chars to string
			tempDigram = sb.toString();		
			putDigram(tempDigram);			//put digram in hashmap

			sb.append(temp3); 				// add third character for trigram
			tempTrigram = sb.toString();
			putTrigram(tempTrigram);  		//add trigram to hashmap
			
			//special case for last two letters
			if (i == y.length() - 3) {		
				temp2 = y.charAt(i + 1);
				temp3 = y.charAt(i + 2);
				sb = new StringBuilder();
				sb.append(temp2);
				sb.append(temp3);
				tempDigram = sb.toString();
				putDigram(tempDigram);
			}

			sb = new StringBuilder();
		}
		
		//sort digram and trigrams by values
		HashMap<String, Integer> sortedDigrams = sortByValues(digrams);
		HashMap<String, Integer> sortedTrigrams = sortByValues(trigrams);
		
		//print all digrams and frequency
		for (Entry<String, Integer> d : sortedDigrams.entrySet()) {
			System.out.println("    " + d.getKey() + "\t\t    " + d.getValue());
		}

		System.out.println("\n");
		
		//print all trigrams and frequency
		for (Entry<String, Integer> t : sortedTrigrams.entrySet()) {
			System.out.println("    " + t.getKey() + "\t\t    " + t.getValue());
		}
	}
	
	// put digram in Hashmap with frequency
	public static void putDigram(String d) {
		
		int num1, num2;
		if (!digramExists(d)) {
			digrams.put(d, 1);
		} else {
			num1 = digrams.get(d);
			num1++;
			digrams.put(d, num1);
		}
	}
	
	
	// put trigram in hashmap with frequency
	public static void putTrigram(String t) {
		int num1, num2;

		if (!trigramExists(t)) {
			trigrams.put(t, 1);
		} else {
			num2 = trigrams.get(t);
			num2++;
			trigrams.put(t, num2);
		}
	}
	
	//returns true if digram exists in hashmap, false otherwise
	public static boolean digramExists(String s) {
		return digrams.get(s) != null;
	}
	
	//returns true if trigram exists in hashmap, false otherwise
	public static boolean trigramExists(String s) {
		return trigrams.get(s) != null;
	}
	
	
	public static void main(String[] args) {
		String y = "EMGLOSUDCGDNCUSWYSFHNSFCYKDPUMLWGYICOXYSIPJCKQPKUGKMGO"
				+ "LICGINCGACKSNISACYKZSCKXECJCKSHYSXCGOIDPKZCNKSHICGIWYGK"
				+ "KGKGOLDSILKGOIUSIGLEDSPWZUGFZCCNDGYYSFUSZCNXEOJNCGYEOWE"
				+ "UPXEZGACGNFGLKNSACIGOIYCKXCJUCIUZCFZCCNDGYYSFEUEKUZCSOC"
				+ "FZCCNCIACZEJNCSHFZEJZEGMXCYHCJUMGKUCY";

		// analyzeCipher(y);
		decrypt(y);
		// digramAnalysis(y);
	}
}
