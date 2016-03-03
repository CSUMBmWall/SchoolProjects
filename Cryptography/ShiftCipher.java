
public class ShiftCipher {

	public static void main(String[] args) {
		String y = "apwnrmqwqrckq";		

		char temp = 0;    	//store character from y
		int charVal;		//store value of character -> a = 10, z = 35
		int offset = 97;	//what I need to add to get from value - 10 to Unicode character
		
		//loop once for every possible shift 
		for (int i = 0; i < 26; i++) { 
			System.out.print("Shift = + " + i + " or - " + (26 - i) + ": \t");
			
			//loop through the letters of the string
			for (int j = 0; j < y.length(); j++) {
				temp = y.charAt(j);								//get character from cipher string
				charVal = (Character.getNumericValue(temp));	//get value from character
				charVal = charVal - 10;							//set a = 0 and z = 25
				charVal = (charVal + i) % 26;					//add shift and mod 26
				temp = (char) (charVal + offset);				//convert to Unicode character
				
				System.out.print(temp);							//print shifted character
			}					
			System.out.println();	
		}
			
	}

	

}
