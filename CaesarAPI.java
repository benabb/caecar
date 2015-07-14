package benabb;

import java.util.Arrays;

public class CaesarAPI {
	
	/****************************************************************************************************
	 * @author Ben Abbenes - https://benabb.com
	 * CaesarAPI - A class to encrypt and decrypt text using the Caesar Cipher aka Shift Cipher, 
	 * with eencrypt() and decrypt() respectively. Also capable of returning all possible texts 
	 * and estimating the correct key based on English letter frequency using cryptanalyse().
	 * 
	 *****************************************************************************************************/

	//This holds my alphabet and is used in lookups
	public static char[] alphabet = 
			new char[]{'a','b','c','d','e','f','g','h','i','j','k','l','m','n','o','p','q','r','s','t','u','v','w','x','y','z'}; 
	
	public static String encrypt(String plaintext, Integer shiftkey){
		//Input text and ciphered text variables
		String text = plaintext;
		StringBuffer ciphertext = new StringBuffer(text.length());
				
		//Loop over each character in the input string
		for(int i = 0; i < text.length(); i++){				
			//char from plain text at index i			
			char c = text.charAt(i);			
			//only attempt to convert if alpha
			if(Character.isAlphabetic(c)){
				//case check
				boolean upper = Character.isUpperCase(c);				
				//convert to lower for lookup
				c = Character.toLowerCase(c);
				//lookup the plaintext char's index
				Integer plainIndex = Arrays.binarySearch(alphabet, c);				
				//perform the shift to find the encrypted char's index				
				Integer shiftIndex = plainIndex + shiftkey;					
				if(shiftIndex > 25){					
					//we have an overflow problem
					Integer overflow = shiftIndex - 26;
					shiftIndex = overflow;
				}				
				//get shifted character
				char e = alphabet[shiftIndex];
				//return case
				if(upper){					
					e = Character.toUpperCase(e);
				}				
				//add the shifter character to the ciphertext
				ciphertext.append(e);	
			}
			else{
				ciphertext.append(c);				
			}	
		}
		return ciphertext.toString();
	}
	
	public static String decrypt(String ciphertext, Integer key){
		//Input text and Ciphered text variables
		String text = ciphertext;
		StringBuffer plaintext = new StringBuffer(text.length());
				
		//Loop over each character in the input string
		for(int i = 0; i < text.length(); i++){				
			//char from plain text at index i			
			char c = text.charAt(i);			
			//only attempt to convert if alpha
			if(Character.isAlphabetic(c)){
				//case check
				boolean upper = Character.isUpperCase(c);				
				//convert to lower for lookup
				c = Character.toLowerCase(c);				
				//lookup the hidden char's index
				Integer plainIndex = Arrays.binarySearch(alphabet, c);				
				//perform the shift to find the corresponding plain char			
				Integer shiftIndex = plainIndex - key;	
				// reverse check for array overflow...
				if(shiftIndex < 0){
					//we have an overflow problem
					Integer overflow = shiftIndex + 26;
					shiftIndex = overflow;
				}				
				//get shifted character
				char e = alphabet[shiftIndex];
				//return case
				if(upper){					
					e = Character.toUpperCase(e);
				}				
				//add the shifted character to the plaintext
				plaintext.append(e);
			}
			else{
				plaintext.append(c);				
			}	
		}
		return plaintext.toString();
	}

	public static String cryptanalyse(String input) {
		//Array to hold all possible plaintexts
		String[] plaintexts = new String[26];
			
		//decrypt input with every possible key and output result		
		for(int key = 0; key < 26; key++){
			plaintexts[key] = decrypt(input, key);			
		}
		
		//guesses the encryption key based on letter frequency and returns the key and plaintext[] index
		Integer probableKey = evaluateFrequency(input);
		
		StringBuffer output = new StringBuffer();
		output.append("Plaintext based on Letter Frequency (English): Key "+(probableKey)+"\n"+plaintexts[probableKey]+"\n");
		output.append("\n-------------------------------------------------------------------------------\n");
		output.append("All possible plaintexts: \n\n");
		
		for(int i = 1; i < 26; i++){
			
			output.append("Key: "+ (i) +"\n" + plaintexts[i]+"\n\n");

		}
		return output.toString();
	}
	
	/**
	 * evaluateFrequency()
	 * 
	 * Counts the frequency of each letter in the input string, and the most likely character to represent the
	 * 'E' after encryption. The distance of the shifted character from the real E is used to calculate an 
	 * estimate of a key to return.	 
	 */
	
	public static Integer evaluateFrequency(String input){
		//an array to hold counts for each letter
		Integer[] counts = new Integer[26];		
		
		//initialise the array
		for(int s = 0; s < counts.length; s++){			
			counts[s] = 0;			
		}
		
		//process every character in the input string
		for(int s = 0; s < input.length(); s++){
			char c = input.charAt(s);
			
			//if the char is a letter
			if(Character.isAlphabetic(c)){
				c = Character.toLowerCase(c);				
				//lookup the hidden char's index
				Integer letter = Arrays.binarySearch(alphabet, c);				

				if(counts[letter] != 0){
				Integer addto = counts[letter];			
				addto++;
				counts[letter] = addto;
				} 
				else{
					counts[letter] = 1;
				}
			}			
			
		}
		
		//calculate the highest 
		Integer highest = counts[0];
		
		for(int s = 0; s < counts.length; s++){
			if(counts[s] > highest){				
				highest	= counts[s];
			}					
		}
		
		Integer fakeIndex = 0;
		for(int s = 0; s < counts.length; s++){
			if(counts[s] == highest){				
				fakeIndex = s;
			}					
		}
		
		//assuming highest occuring letter is e..
		Integer frequencyKey = 0;
		
		//position of fake .vs position of real e.
		if(fakeIndex < 4){			
			Integer diff = 4 - fakeIndex;
			frequencyKey = 26 - diff;			
		}
		else{			
			frequencyKey = fakeIndex - 4; 			
		}	
		
		//the most likely key based on letter frequency
		return frequencyKey;
	}	
}
