/*
 *
 * This class contains each cryptographic function and supporting functions
 * to be used by the rest of the program.
 * It will also contain a random number generator.
 *
 */

import java.util.*;
import java.lang.*;
import java.math.*;
import java.util.Arrays;
import java.nio.charset.StandardCharsets;
import java.nio.ByteBuffer;

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

	//somethimes a parallel prime is not found and private or public
	//key is initialised with a -1, if this happens the constructor
	//calls itself again
	boolean done = false;
	while( !done){
	    
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

	    //get the publicKey by selecting an integer that
	    //times with the public key and mod phi returns 1
	    publicKey = makePublicKey( phi);

	    //make sure nothing went wrong
	    if( privateKey != -1 && publicKey != -1){

		done = true;
	    }
	}

	System.out.println( _rsa + " keys are");
	System.out.println("private key ->" + privateKey);
	System.out.println("public key ->" + publicKey);
	System.out.println("N = " + N + "\n");
    }

    ///////////////////// Transport Cipher - Diffusion \\\\\\\\\\\\\\\\\\\\\\\////
    //this is a transport cipher-it will take a key and a string to shuffle up\\\\
    // it is taking advantage of utf values again but still demonstrates      ////
    // how plain text may be broken up and confused by using a couple of loops\\\\
    ///////////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\////
    String transportEncrypt( String _key, String _plaintext){

	String plaintext = shiftStream( _key.length(), _plaintext + "~~~~~~~~");

	//pad out the _plaintext to fit in the tables correctly
        int add =  plaintext.length()/_key.length();
	for (int i = 0; i < add; i++) {

	    plaintext += "~";
	}
	
	String[][] temp = stringToArr( plaintext.length()/_key.length(), _key.length(), plaintext);
      	
	//must transpose twice otherwise a dimenstion is shifted and transposed
	String[][] temp2 = transposeMatrix(temp);
	return stringArrToString(temp2);
    }

    String transportDecrypt( String _key, String _cipher){

	//only is messed up by transpose when its transposed then sent through this
        String[][] temp = stringToArr( _key.length(), _cipher.length()/_key.length(), _cipher);
	String[][] temp2 = mirrorCols(temp);
	showMatrix(temp2);
	temp = transposeMatrix( temp2);
	showMatrix( temp);
	temp2 = mirrorCols( temp);
	showMatrix( temp2);

	return shiftStream( _key.length(), stringArrToString(temp2));
    }

    //returns an array based on a string in args, number of
    //columns is dependent on first argument
    String[][] stringToArr( int rows, int cols, String _string){

	///TO_DO make sure that this is does not interfere with the dimensionality
	int size = _string.length();
	String[][] sarr = new String[rows][cols];
	int index = 0;
	for (int r = 0; r < rows; r++) {
	    for (int c = 0; c < cols; c++) {

		if( index < size){
		    
		    sarr[r][c] = _string.substring( index, index + 1);
		    index++;
		}else{

		    sarr[c][r] = "~";
		}
	    }
	}

	//returns the array
	return sarr;
    }

    String stringArrToString( String[][] _string){

        int M = _string.length;
	int N = _string[0].length;
	
	String returnString = "";

	for (int i = 0; i < M; i++) {
	    for (int c = 0; c < N; c++) {

		returnString += _string[i][c];
	    }
       
        }
 
	return returnString;
    }

    //make a few of these
    String[][] transposeMatrix( String[][] _string){

	int M = _string.length;
	int N = _string[0].length;
	
	String[][] returnString = new String[N][M];

	for (int i = 0; i < M; i++) {
	    for (int c = 0; c < N; c++) {

		returnString[c][i] = _string[M - i - 1][c];
	    }
       
        }
 
	return returnString;
    }

    void showMatrix( String[][] _string){

	int M = _string.length;
	int N = _string[0].length;
	
	for (int i = 0; i < M; i++) {
	    for (int c = 0; c < N; c++) {

		System.out.print( _string[i][c]);
	    }
	    System.out.println();
        }
	
	System.out.println();
    }

    String[][] mirrorCols( String[][] _temp){

	//reverse each row for added confusion
        for (int r = 0; r < ( _temp.length/2); r++) {

	    String[] s = _temp[r];
            _temp[r] = _temp[ _temp.length - r - 1];
            _temp[ _temp.length - r - 1] = s;
        }

	return _temp;
    }

    String[][] reverseRows( String[][] _string){

	int M = _string.length;
	int N = _string[0].length;
		
	String[][] newarray = new String[N][M];

	for (int i = 0; i < M; i++){
	    for (int c =0; c < N; c++){
		
		newarray[c][i] = _string[M-1-i][c];
	    }
	}   

	return newarray;
    }
    
    ////////////// Ceasar Cipher - Confusion \\\\\\\\\\\\\\\\\\\\\\\
    //this function simply shifts the alphabet by the given amount\\
    //in args and is also known as the Ceasar cipher\\\\\\\\\\\\\\\\
    ///////////////////////////////\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\\
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
    int[] utfEncryptRSA( String _plaintext, int _publicKey, int _N){

	//first each argument must be converted to a big integer for operations
	//in order to use the modPow()
	char[] charArray = _plaintext.toCharArray();
	int csize = charArray.length;
	BigInteger[] bigArray = new BigInteger[ csize]; 

	//turns the char array into an array of big integers
	for (int i = 0; i < csize ; i++)
	    bigArray[i] = BigInteger.valueOf(charArray[i]);

	//converts the number N and the public key into big integer versions
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

    //uses the rsa algorithm to encrypt and decrypt a number, just use public or private key appropriatley
    int rsaNumber( int number, int _Key, int _N){

	BigInteger _k = BigInteger.valueOf( _Key);
	BigInteger b_N = BigInteger.valueOf( _N);
	BigInteger bnum = BigInteger.valueOf( number);
        BigInteger temp = bnum.modPow( _k, b_N);

	return temp.intValue();
    }

    //decrypts using converesions to big integer in the same way as above
    String utfDecryptRSA( int[] _encryptedchars, int _privateKey, int _N){

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

	int randomNum = ( _min + 100) + (int)(Math.random() * _max + ( _min + 100));

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

    //this function overlays a block of random data over the cipher text using the xor
    //shift 
    String xorBytes( int _seed, String _input){

	//get length of string
	int n = _input.length();

	//create psuedo random numbers with as many digits in as n, use that to
	//create a big integer object
	Random generator = new Random(_seed);
	BigInteger tenPown = new BigInteger("10").pow(n);
	BigInteger num = new BigInteger(100, generator).remainder(tenPown);
	
	//convert that big integer to a string, this is so I can turn
	//chars at a specific index as bytes, theres probaly a more
	//elegand way to do this however it still works
	String pad = num.toString();
	byte[] plaintext = _input.getBytes(StandardCharsets.UTF_8);
	byte[] byterandom = pad.getBytes(StandardCharsets.UTF_8);
	byte[] ciphertext = new byte[ plaintext.length];
	
	String _output = "";

	for (int i = 0; i < byterandom.length; i++) {

	   
	    ciphertext[i] =  (byte) ( byterandom[i] ^ plaintext[i]);
	}

	//convert byte array back into utf characters and append to a string
	try{
	    
	    _output = new String( ciphertext, "UTF-8");
	}catch( Exception e){

	    System.out.println( e.toString());
	}

	return _output;
    }

    
    //Uses the first arg to transpose a shift on the second args -
    //returns a string after it has been converted into bytes, transposed with another byte array
    //turned back into a string and returned 
    String shiftStream( int _K, String _M){

	String temo="-";
	byte[] shiftstream = _M.getBytes(StandardCharsets.UTF_8);
	byte[] temp = new byte[ shiftstream.length];

	//create a single byte buffer to overlay string _K
	byte[] bytes = ByteBuffer.allocate(4).putInt(_K).array(); 

	try{

	    //check data is equal or less that the _stream in bits
	    if( bytes.length <= shiftstream.length){

		for( int i = 0; i < shiftstream.length; i++){
		    for( int c = 0; c < bytes.length; c++){
		    
			temp[i] = (byte) ( shiftstream[i] ^ bytes[c] );
		    }
		}       		
	    }

	    //impose a xor shift all the way across the stream
	    temo = new String( temp, "UTF-8");

	}catch( Exception e){

	    System.out.println( e.toString());
	}
	
	return temo;
    }
   
}






//http://stackoverflow.com/questions/21412148/simple-caesar-cipher-in-java for help realising about ascii ofset value
//http://www.rosettacode.org/wiki/Caesar_cipher#Java better code than my ceasar cipher or one in link above
//for reminging of steps to RSA algorithm http://flylib.com/books/en/3.190.1.84/1/
//for help with gcd http://stackoverflow.com/questions/4009198/java-get-greatest-common-divisor
//help from C_sto @ #cryptography - not sure where he got it though http://ideone.com/mLyRC
