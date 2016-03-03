/* Title: AffineCipher.java
 * Abstract: Provides all possible combinations of plaintext from a ciphertext
 *  			encrypted using Affine Cipher
 * Author: Matt Wall
 * ID: 1399
 * Date: 4/15/2015
 */

public class AffineCipher {
	
	public static void encrypt(String x, int a, int b) {
		int charValx;			//hex value of each character
		int offset = 97;		//value difference between hex and ASCII character
		char tempChar;			//individual character from cipher text
		StringBuilder sb = new StringBuilder();  //holds new string
		
		/* affine cipher formula: a * value of character + b (mod 26)
		 * read character, get value, 
		 * apply cipher formula with provided values (string x, int a, int b)
		 */
		for (int i = 0; i < x.length(); i++) {
			tempChar = x.charAt(i);
			charValx =  (Character.getNumericValue(tempChar) - 10);
			tempChar = (char) (((a * charValx + b) % 26) + offset);	
			sb.append(tempChar);
		}
		System.out.println(sb.toString());		
	}
	
	//prints all possible values for plaintext for cipher text input
	public static void decrypt(String y) {
		//possible values for a: gcd(num, 26) = 1
		int[] invArray = new int[] {1,3,5,7,9,11,15,17,19,21,23,25};
		
		int charValx;										//int value of character
		int product, modProduct, modProdwOffset, minusB;	
		int offset = 97;									//difference between int and ASCII
		char tempChar;										//current character
		StringBuilder sb = new StringBuilder();
		String tempString;
		int count = 1;										//track # of characters in cipher text string
		
		/* Character.getNumericValue returns a value between 10(a) and 35(z) for each character
		 * 10 is subtracted from this value to allow mod 26 (%26) to work: values are then 0(a) - 25(z)		 
		 * parse string and apply affine cipher decryption formula: a^-1 * (y - b)
		 */
		for (int a = 0; a < invArray.length; a++) {							//all a^-1 values
			for (int b = 0; b < 26; b++) {									//all b values (0 - 25)
				for (int k = 0; k < y.length(); k++) {					
					tempChar = y.charAt(k);									//current character
					charValx =  (Character.getNumericValue(tempChar) - 10); //set value to 0 - 25
					minusB = charValx - b;									//subtract b
					if (minusB < 0) { minusB += 26; }						//make positive number
					product = (invArray[a] * minusB);						//multiply (y-b) by a
					modProduct = product % 26;								//mod26
					modProdwOffset = modProduct + offset;					//add offset for ASCII output
					
					tempChar = (char) modProdwOffset;						//convert value to char	
					sb.append(tempChar);									//add to string
					}		
					tempString = sb.toString();
					System.out.println(count + " a^-1 = " + invArray[a] + " b = " + b + " " + tempString + "\n");
					count++;
					sb = new StringBuilder();
			}
		}
	}
	
	public static void main(String[] args) {
		String x = "KQEREJEBCPPCJCRKIEACUZBKRVPKRBCIBQCARBJCVFCUPKRIOFKPACUZQEPBKRXPEIIEABDKPBCPFCDCCAFIEABDKPBCPFEQ"
				 + "PKAZBKRHAIBKAPCCIBURCCDKDCCJCIDFUIXPAFFERBICZDFKABICBBENEFCUPJCVKABPCYDCCDPKBCOCPERKIVKSCPICBRKIJPKABI";
		String x2 = "ocanadaterredenosaieuxtonfrontestceintdefleuronsglorieuxcartonbrassaitporterlepeeilsaitporterlacroixtonhistoireestuneepopeedesplusbrillantsexploitsettavaleurdefoitrempeeprotegeranosfoyersetnosdroits";
		//decrypt(x);
		encrypt(x2, 19, 4);
	}

}


