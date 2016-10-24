/*
 *
 * This class contains each cryptographic function and supporting functions
 * to be used by the rest of the program.
 * It will also contain a random number generator.
 *
 */

import java.math.BigInteger;


public class Cryptography{

    //P and Q will make up to primes and N will be the product of the too
    //phi is the two primes with 1 taken away and multiplied
    public int P, Q, N, publicKey, phi;
    private int privateKey;

    Cryptography(){

	
    }

    //must add error checking to allow P and Q arent
    //too small otherwise the chances of being the same
    //will be too large and the program will hang
    Cryptography( String _rsa, int _min, int _max){

	//get two random numbers
	P = randomPrimeGen( _min, _max);
	Q = randomPrimeGen( _min, _max);

	//ensures P != Q
	while( P == Q){

	    Q = randomPrimeGen( _min, _max);
	}

	//find a composite of the primes
	N = P * Q;
	phi = ((P-1)*(Q-1));

	//select the privateKey so that its
	//lowest common devisor with phi is 1  
	privateKey = gcdisone( phi);

	System.out.println( _rsa + " keys are");
	System.out.println("private key ->" + privateKey);

	//get the publicKey by selecting an integer that
	//times with the public key and mod phi returns 1
	publicKey = makePublicKey( phi);

	System.out.println("public key ->" + publicKey);
	System.out.println("N = " + N + "\n");
    }

    

    //this function simply shifts the alphabet by the given amount
    //in args and is also known as the Ceasar cipher
    String shiftcipherEncrypt( String _plaintext, int _shiftkey){

	_shiftkey = _shiftkey % 26 + 26;
        StringBuilder encoded = new StringBuilder();
        for (char i : _plaintext.toCharArray()) {

	    if (Character.isLetter(i)) {

		if (Character.isUpperCase(i)) {

		    encoded.append((char) ('A' + (i - 'A' + _shiftkey) % 26 ));
                } else {
                    encoded.append((char) ('a' + (i - 'a' + _shiftkey) % 26 ));
                }
            } else {
                encoded.append(i);
            }
        }
        return encoded.toString();
    }
    

    //deciphers with negative value of same key that was shifted
    String shiftcipherDecrypt( String _plaintext, int _shiftkey){

	return shiftcipherEncrypt( _plaintext, 26 - _shiftkey);
    }

    //this method will encrypt each ascii value of each char in a string
    //using the RSA encryption method - this is not so secure as each ascii
    //value will use the entire encryption method exposing a lot of examples
    //of the same cipher in use to charlie
    int[] asciiEncryptRSA( String _plaintext, int _publicKey, int _N){

	//first each argument must be converted to a big integer for operations
	//in order to use the modPow()
	char[] charArray = _plaintext.toCharArray();
	int csize = charArray.length;
	BigInteger[] bigArray = new BigInteger[ csize]; 
		
	for (int i = 0; i < csize ; i++)
	    bigArray[i] = BigInteger.valueOf(charArray[i]);

	BigInteger b_publicKey = BigInteger.valueOf( _publicKey); 
	BigInteger b_N = BigInteger.valueOf( _N);

	int[] encryptedChars = new int[csize];
	
        //now loop through each element of the plaintext char
	//array and perform the modPow() to apply the encryption method
	for( int i = 0; i < csize; i++){

	    BigInteger temp = bigArray[i].modPow( b_publicKey, b_N);
	    encryptedChars[i] = temp.intValue();
	}

	return encryptedChars;
    }

    //decrypts using converesions to big integer in the same way as above
    String asciiDecryptRSA( int[] _encryptedchars, int _privateKey, int _N){

	//first each argument must be converted to a big integer for operations
	//in order to use the modPow()
	int csize = _encryptedchars.length;
	BigInteger[] bigArray = new BigInteger[ csize]; 
		
	for (int i = 0; i < csize ; i++)
	    bigArray[i] = BigInteger.valueOf( _encryptedchars[i]);

	BigInteger b_privateKey = BigInteger.valueOf( _privateKey); 
	BigInteger b_N = BigInteger.valueOf( _N);

	String s = "";
	
        //now loop through each element of the plaintext char
	//array and perform the modPow() to apply the encryption method
	for( int i = 0; i < csize; i++){

	    BigInteger temp = bigArray[i].modPow( b_privateKey, b_N);
	    s += (char)temp.intValue();
	}

	return s;
    }

    //unlike other function with same name this verstion does not use
    //the big integer data type, however it works just the same way
    int[] asciiEncryptRSA2( String _plaintext, int _publicKey, int _N){
	             
	char[] echars = _plaintext.toCharArray();
	int csize = _plaintext.length();
	int[] encryptedChars = new int[csize];
	
        //now loop through each element of the plaintext char
	//array and perform the modPow() to apply the encryption method
	for( int i = 0; i < csize; i++){

	    encryptedChars[i] = (int)Math.pow( echars[i], _publicKey % _N) % _N;
	}

	return encryptedChars;
    }
    

    //function checks the primality of the input number
    private boolean checkPrimal( int _num){

	//checks if even
	if( _num % 2 == 0)
	    return false;

	int tmp = 3;

	//while number being checked is smaller than the test
	//number squared, no non even number will have a factor
	//greater than a quarter of its size
	while ((tmp * tmp) <= _num) {
	    if ( _num % tmp == 0) {
		return false;
	    } else {
	        tmp += 2;
	    }
	}
 
	return true;
    }

    //generates a random prime number within input range -1 is
    //an error value
    private int randomPrimeGen( int _min, int _max){

	int randomNum = _min + (int)(Math.random() * _max);

	for( int tmp = randomNum; tmp > 0; tmp--){

	    if( checkPrimal( tmp)){

		return tmp;
	    }
	}

	System.out.println( "Warning: random number generator failed");
	return -1;
    }

    //finds relationship between two numbers where the
    //greatest common devisor is 1 and the number is odd
    private int gcdisone( int _phi){

        for( int e = _phi-=3; e > 0; e --){

	    if( gcd( e, phi) == 1){

		return e;
	    }
	}

	return -1;
    }

    //makes the public key
    //only to be used in constructot returns -1 in error
    int makePublicKey( int d){

	for( int i = d-=3; i > 0; i--){

	    if(( i * privateKey) % phi == 1){  //checking for a relative prime

		return i;
	    }
	}
	
	//returns -1 if there was an error
	return -1;
    }

    //finds greatest common devisor
    int gcd( int a, int b ){
	
	BigInteger b1 = BigInteger.valueOf(a);
	BigInteger b2 = BigInteger.valueOf(b);
	BigInteger gcd = b1.gcd(b2);
	return gcd.intValue();
    }

    //returns the private key
    public int getPrivateKey(){

	return privateKey;
    }

    public int getN(){

	return N;
    }

    public int getP(){

	return P;
    }

    public int getQ(){

	return Q;
    }
   
}






//http://stackoverflow.com/questions/21412148/simple-caesar-cipher-in-java for help realising about ascii ofset value
//http://www.rosettacode.org/wiki/Caesar_cipher#Java better code than my ceasar cipher or one in link above
//for reminging of steps to RSA algorithm http://flylib.com/books/en/3.190.1.84/1/
//for help with gcd http://stackoverflow.com/questions/4009198/java-get-greatest-common-divisor
