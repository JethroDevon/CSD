/*
 *
 *   This protocol class manages the simulations and will contain scripts for the
 *   the three agents, the steps demonstrated will rely on user input.
 *
 */

import javax.swing.*;
import javax.swing.ButtonGroup;
import javax.swing.JFormattedTextField;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;


//this class like the gui class manages GUI components
//however this class is seperate from the gui class because it manages the way in which the program
//is used in conjunction with the cryptographic functions and options
public class Protocol extends JPanel implements ItemListener{

    JRadioButton authpad, shift, transport, fourway, rsaInsecure, rsaSecure, random;
    ButtonGroup crypt_methods;
    JFormattedTextField shift_val, rsaHybridKey;
    JTextField transkey;
    JPanel methpanel, shiftpanel, transpanel, securepanel;
    JLabel shiftlabel, keylabel;
    Cryptography cryptography = new Cryptography();

    //many bool variables for all the options, some default true to match button
    boolean bauth, brsaInsecure, brsaSecure, bshift, btransport, brandom, bfourway;


    public Protocol(){

	//Many swing options to control the way in which the program will work
	//Gui displays rbuttons when initialised in the Gui class
	methpanel = new JPanel();
	transpanel = new JPanel();
	shiftpanel = new JPanel( new BorderLayout());
	securepanel = new JPanel( new BorderLayout());

	//set the borders for the two button groups
	methpanel.setBorder(BorderFactory.createTitledBorder( "Cryptographic Method"));
	shiftpanel.setBorder(BorderFactory.createTitledBorder( "Shift Cipher Options"));
	transpanel.setBorder(BorderFactory.createTitledBorder( "Transposition Key"));
	securepanel.setBorder(BorderFactory.createTitledBorder( "Input Unique Key"));

	//initialise all the radio buttons to be used
	authpad = new JRadioButton( "Authenication (CW2)");
	shift = new JRadioButton( "Shift/Substitution");
	transport = new JRadioButton( "Transposition");
	fourway = new JRadioButton( "Four Way Handshake");
	rsaInsecure = new JRadioButton( "RSA - Insecure");
	rsaSecure = new JRadioButton( "RSA - Secure (CW1)");
	random = new JRadioButton( "Random");
	shiftlabel = new JLabel( "Shift Value ");
	keylabel = new JLabel( "Secret Key");
	rsaHybridKey = new JFormattedTextField( new Integer( 2345));
	transkey = new JFormattedTextField( new Integer( 1));
	shift_val = new JFormattedTextField( new Integer( 1));

	//individual radio button logic
	shift.addItemListener( this);
	transport.addItemListener( this);
	rsaInsecure.addItemListener( this);
	rsaSecure.addItemListener( this);
	fourway.addItemListener( this);
	authpad.addItemListener( this);

	//adding components to appropriate panels
	methpanel.add( authpad);
	methpanel.add( shift);
	methpanel.add( transport);
	methpanel.add( fourway);
	methpanel.add( rsaInsecure);
	methpanel.add( rsaSecure);
	shiftpanel.add( shiftlabel, BorderLayout.EAST);
	shiftpanel.add( random, BorderLayout.SOUTH);
	shiftpanel.add( shift_val, BorderLayout.CENTER);
	transpanel.add( transkey);
	securepanel.add( rsaHybridKey, BorderLayout.SOUTH);

	shiftpanel.setVisible( false);
	transpanel.setVisible( false);
	securepanel.setVisible( false);

	crypt_methods = new ButtonGroup();
	crypt_methods.add( authpad);
	crypt_methods.add( shift);
	crypt_methods.add( transport);
	crypt_methods.add( fourway);
	crypt_methods.add( rsaInsecure);
	crypt_methods.add( rsaSecure);

	//adding panels to this functions main JPanel
	add( methpanel);
	add( shiftpanel);
	add( transpanel);
	add( securepanel);
    }

    //goes though appropriate action based on the options selected by the user then store this
    //data for appropriate demonstration steps by outputing log data and agent data to
    //the appropriate stacks this way each step is stored in the stack and displayed with "next step"
    public String startDemo( String plaintext, Agent alice, Agent charlie, Agent bob){

	//Demonstration always starts like this
	String logtext = "Starting demo : \r\n\n";

	try{

	    /*
	     *
	     *   SHIFT CIPHER METHOD WITH PRE SHARED KEYS
	     *
	     */

	    //if the shift cipher has been selected and its not random substitution and theres no
	    //crypto system present then a message is just being sent between two parties
	    if( bshift){

		  String tmp = "0: Alice wants to send the message: " + plaintext + " to Bob";
	    logtext += tmp + "\r\n\n\n";

		logtext += "1: Alice and Bob shared a predecided number between 1 and 26\n before using the shift cipher, alice then sends it to bob and charlie intercepts it. \r\n";

		String cipherText = cryptography.shiftcipherEncrypt( plaintext, (int) shift_val.getValue());
		alice.addLogEntry("1: " +  cipherText);
		bob.addLogEntry( cipherText);
		charlie.addLogEntry( cipherText);

		logtext += "2: Now that Bob has the message he will decrypt it using the reverse shift key.";
		bob.addLogEntry( "2:" + cryptography.shiftcipherDecrypt( cipherText, (int) shift_val.getValue()));
		alice.addLogEntry( " ");
		charlie.addLogEntry( "");

		logtext += "\r\n The message is passed on, however this is not very secure\r\n - END -";

		bob.addLogEntry( "ENDOFLOG");
		alice.addLogEntry("ENDOFLOG");
		charlie.addLogEntry( "ENDOFLOG");

	    }




	    /*
	     *
	     *      BAD RSA METHOD WHERE EACH UTF CHARACTER HAS BEEN ENCRYPTED - THAT IS STUPID (made it to test rsa maths tho)
	     *
	     */

	     if( brsaInsecure){

		   String tmp = "0: Alice wants to send the message: " + plaintext + " to Bob";
	    logtext += tmp + "\r\n\n\n";

		logtext += " THIS METHOD IS FLAWED, THE CIPHER TEXT IS JUST SUBSTITUTED PLAIN TEXT \r\n";
		logtext += "1: Alice first makes a request from Bob for his public key, this is public so it is okay if Charlie sees it\r\n";
		alice.addLogEntry( "1: public key please ;" + bob.getPublicKey() + ". mod value ;" + bob.getPublicKeyMod());
		charlie.addLogEntry( bob.getPublicKey() + "mod value " + bob.getPublicKeyMod());
		bob.addLogEntry("");

		logtext += "2: Alice then encrypts the message she wishes to send\r\n";
		int[] cipherintarray = cryptography.utfEncryptRSA( plaintext, bob.getPublicKey(), bob.getPublicKeyMod());
		String ciphertext = "";

		for( int i: cipherintarray)
		    ciphertext += (char) i;

		alice.addLogEntry("2: "+ ciphertext);
		charlie.addLogEntry("");
		bob.addLogEntry("");

		logtext += "3: Alice now sends that encrypted message to Bob, Charlie can also see it\r\n";
		charlie.addLogEntry( ciphertext);
		bob.addLogEntry("3: " + ciphertext);
		alice.addLogEntry("");

		logtext += "4: now all Bob has to do is decrypt the message with his private key";
		bob.addLogEntry("4: " + cryptography.utfDecryptRSA( cipherintarray, bob.getPrivateKey(), bob.getPrivateKeyMod()));
		charlie.addLogEntry("");
		alice.addLogEntry("");

		bob.addLogEntry( "ENDOFLOG");
		alice.addLogEntry("ENDOFLOG");
		charlie.addLogEntry( "ENDOFLOG");
	    }

	     /*  RSA MESSAGE - CW PART I - THIS METHOD USES RSA ALGORITHM TO SEND MASSEGES SECURLEY
	     *
	     *
	     *    GOOD RSA METHOD THAT IS SIMILAR TO HYBRID RSA & AES - A SEED FOR CRYPTO RANDOM NUMBER IS RSA ENCRYPTED AND SENT
	     *    AND THAT RANDOM NUMBER WILL XOR TRANSPOSE THE WHOLE MESSAGE.
	     *    THE SEED WILL ALSO BE A PASSWORD FOR OTHER ENCRYPTION METHODS.
	     *
	     */

	    if( brsaSecure){

		  String tmp = "0: Alice wants to send the message: " + plaintext + " to Bob";
	    logtext += tmp + "\r\n\n\n";

		logtext += "1: Alice requests Bobs public key, this is public so it is okay if charlie sees it\r\n";

		int rsakey = (int) rsaHybridKey.getValue();
		int bobskey = bob.getPublicKey();
		int bobsmod =  bob.getPublicKeyMod();
		int encryptedkey = cryptography.rsaNumber( rsakey, bobskey, bobsmod);

		alice.addLogEntry( "1: public key please bob;" + bobskey  + ". and your mod value: " + bobsmod);
		charlie.addLogEntry( "1: bobs public key is: " + bobskey);
		bob.addLogEntry("");

		logtext += "2: Alice chooses a key, in this case it is: "+ rsakey +" and encrypts it with Bobs public key, then sends it\r\n";

		alice.addLogEntry( "2: sending encrypted key: " + String.valueOf(encryptedkey));
		bob.addLogEntry( String.valueOf(encryptedkey) );
		charlie.addLogEntry( "2: intercepted: " + String.valueOf(encryptedkey));

		logtext += "3: Alice also encrypts the message she wishes to send with that same key value (before it was encrypted)\r\n";

		String ciphertext = cryptography.xorBytes( rsakey, plaintext);

		logtext += "in this case it is overlayed with a random number sequence generated by that same key value";
		logtext += "\r\n this method also adds some diffusion to that cipher text also by adding a transport encryption cipher\r\n";
		logtext += "Charlie sees this as well but it is okay because Charlie cannot decrypt it\r\n";


		alice.addLogEntry("3: sending encryped message: " + ciphertext);
		charlie.addLogEntry( "3: also intercepts the message: " + ciphertext);
		bob.addLogEntry( "3: " + ciphertext);

		logtext += "4: When Bob recieves the encrypted key he decrypts it.";

		int decryptedkey = cryptography.rsaNumber( encryptedkey, bob.getPrivateKey(), bob.getPrivateKeyMod());

		bob.addLogEntry( "decrypting key: " + encryptedkey + " back to " + decryptedkey);
		alice.addLogEntry("");
		charlie.addLogEntry( "");

		logtext += "5: lastly, bob uses that key he decrypted to also decrypt the other message he was sent\r\n";
		alice.addLogEntry("");
		charlie.addLogEntry( "");

		String plaintext2 = cryptography.xorBytes( decryptedkey, ciphertext);

		bob.addLogEntry( "5: using key to decrypt encrypted messages : " + plaintext2);

		bob.addLogEntry( "ENDOFLOG");
		alice.addLogEntry("ENDOFLOG");
		charlie.addLogEntry( "ENDOFLOG");

	    }


	    /*
	     *
	     *  EL GEMAL VERSION OF THE RSA MESSAGING METHOD
	     *
	    */
	    //if(el_gamal)){}


	    /*
	     *
	     *  This just demonstrates the transport cipher - it could demonstrate confusion
	     *  and diffusion in a cipher, only that keys would have to be pre shared so it is
	     *  not practical, and it can be decoded still with brute force and or analysis
	     *
	     */
	    if(btransport){

		  String tmp = "0: Alice wants to send the message: " + plaintext + " to Bob";
	    logtext += tmp + "\r\n\n\n";

		logtext += "1: Assuming Alice and bob have allready shared keys alice encrypts\r\n";
		logtext += "her message and sends it, charlie intercepts and gets to work decodingit";

		String cipher = cryptography.transportEncrypt( "The_Key", plaintext);

		bob.addLogEntry( "1: "+cipher);
		alice.addLogEntry( "1: "+cipher);
		charlie.addLogEntry( "1: "+cipher);

		logtext += "2: Bob then decrypts the data";

		plaintext = cryptography.transportDecrypt( "The_Key", cipher);

		bob.addLogEntry( "2: " + plaintext);
		alice.addLogEntry("");
		charlie.addLogEntry( "");

		bob.addLogEntry("ENDOFLOG");
		alice.addLogEntry("ENDOFLOG");
		charlie.addLogEntry( "ENDOFLOG");

	    }

	    /*
	     *
	     *    AUTHORISATION - CW PART II - THIS METHOD IS FOR SAFLEY EXCHANGING KEYS WITH A TRUSTED SERVER
	     *
	     */
	    if(bauth){

		logtext += " This method will exchange keys between alice and bob using a trusted server\r\n";
		logtext += " so that Alice and Bob can prove they are who they say they are at the same time\r\n";
		logtext += " In this example Charlie is the trusted server, the irony is, that if the trusted\r\n";
		logtext += " server is compromised then the trusted server it would most definatley be Charlie\r\n\r\n\r\n";
		logtext += "1: Alice asks the trusted server for Bobs publickey, the trusted server duly sends it but encrypted\r\n";
		logtext += " with its own private key, the server has 'signed' it\r\n\n\n";

		int BobspublicKey = cryptography.rsaNumber( bob.getPublicKey(), charlie.getPrivateKey(), charlie.getPrivateKeyMod());

		bob.addLogEntry("");
		alice.addLogEntry("1: Bobs public key signed by the trusted server - " + BobspublicKey);
		charlie.addLogEntry("1: request from Alice for public key" );

		logtext += "2: Alice decrypts that key with the trusted servers public key, this way it is\r\n";
		logtext += " definatley from the trusted server, Alice then sends a 'nonce' encrypted with that decrypted publickey\r\n";
		logtext += " to Bob data about Alice, in this case it is her name but it could also contain things\r\n";
		logtext += " like a timestamp or details about Alice, it also contains her public keyn this is a nonce,\r\n";
		logtext += " a symetric key has to also be sent in order to decrypt the encrypted nonce\r\n\n\n";

		BobspublicKey = cryptography.rsaNumber( BobspublicKey, charlie.getPublicKey(), charlie.getPublicKeyMod());

		int aliceSecret = 6321;
		String alicenonce = cryptography.xorBytes( aliceSecret, "-n-" + alice.agentname + "-K-" + alice.getPublicKey()+"-M-"+alice.getPublicKeyMod());
		aliceSecret = cryptography.rsaNumber( aliceSecret, BobspublicKey, bob.getPublicKeyMod());

		bob.addLogEntry("2: Alice symetric cipher key - " + aliceSecret + " Alice nonce - " + alicenonce);
		alice.addLogEntry("2: Bobs decrypted public key: " + BobspublicKey);
		charlie.addLogEntry("");

		logtext += "3: Bob decrypts these messages\r\n\n\n";
		
		aliceSecret = cryptography.rsaNumber( aliceSecret, bob.getPrivateKey(), bob.getPrivateKeyMod());
		alicenonce = cryptography.xorBytes( aliceSecret, alicenonce);
		String alicespublicKey = cryptography.xorBytes( aliceSecret, alicenonce);
		
		bob.addLogEntry( "3: decrypted Alice's key " + aliceSecret + " and Alice's nonce " + alicenonce);
		alice.addLogEntry("");
		charlie.addLogEntry( "");
	       
		logtext += "4: Bob requests the servers signed copy of Alice's nonce, the server sends it to B\r\n\n\n";

		int alicespublicKeyfromserver = cryptography.rsaNumber( alice.getPublicKey(), charlie.getPrivateKey(), charlie.getPrivateKeyMod());
		
		bob.addLogEntry( "4: Alice's nonce containing public key signed by the trusted server - " + alicespublicKeyfromserver);
		alice.addLogEntry("");
		charlie.addLogEntry( "4: request from Bob for public key");

		logtext += "5: Bob uses the servers public key to decrypt the signed version of Alice's nonce, he compares\r\n";
		logtext += " the one from the server and the one from Alice, once satisfied that they are the same\r\n";
		logtext += "Bob then uses Alice's decrypted pubklickey to both encrypt the secret key to allow for the symetric\r\n";
		logtext += " encryption algorithm and the nonce that he has stored for alice to check hers against, and sends to alice\r\n\n\n";

	        alicespublicKeyfromserver = cryptography.rsaNumber( alicespublicKeyfromserver, charlie.getPublicKey(), charlie.getPublicKeyMod());//
		
		int bobsecret = 3612;
		String bobnonce = cryptography.xorBytes( bobsecret, "-n-"+ bob.agentname + "-K-" + bob.getPublicKey() + "-M-" + bob.getPublicKeyMod());
		bobsecret = cryptography.rsaNumber( bobsecret, alice.getPublicKey(), alice.getPublicKeyMod());
	
		bob.addLogEntry("5: Alice's decrypted nonce from the server: -n-Alice-K-" + alicespublicKeyfromserver +"-M-" + alice.getPublicKeyMod() + " compared from Alice" + alicenonce);
		alice.addLogEntry("5: encrypted with alice's public key is Bob's symetric cipher key - " + bobsecret + " and his nonce " +  bobnonce);
		charlie.addLogEntry("");

		logtext += "6: Alice recieves the message from Bob and decrypts the data with her privatekey, she then compares Bob's nonce with hers\r\n";
		logtext += " once she is satisfied that they are the same she sends his nonce back proving she decrypted it\r\n";
		logtext += "encrypting it with his publickey\r\n\n\n";   
		
		bobsecret = cryptography.rsaNumber( bobsecret, alice.getPrivateKey(), alice.getPrivateKeyMod());
		bobnonce = cryptography.xorBytes( bobsecret, bobnonce);
	       
		alice.addLogEntry("6: Bobs nonce and symetric key from bob - " + bobnonce + " - " + bobsecret + " Bobs public key from the server " + BobspublicKey);

		String bobnonceAlice = cryptography.xorBytes( bobsecret, bobnonce);
		
	        bob.addLogEntry("6: Bobs nonce encrypted and sent back by Alice: " + bobnonceAlice );
		charlie.addLogEntry("");

		logtext += "7: Bob recieves the encrypted nonce from Alice and decrypts it, this proves that Alice has decrypted it and the\r\n";
		logtext += " autherentication process has been completed";

	        alice.addLogEntry("7: sending encrypted version of Bob's nonse:" + bobnonceAlice);
		
		bobnonceAlice = cryptography.xorBytes( bobsecret, bobnonceAlice);

		bob.addLogEntry("7: Bob's nonce sent by Alice: " + bobnonceAlice );
		charlie.addLogEntry( "");

		bob.addLogEntry("ENDOFLOG");
		alice.addLogEntry("ENDOFLOG");
		charlie.addLogEntry( "ENDOFLOG");
	    }

	     /*
	     *
	     *    THIS METHOD DEMONSTRATES THE FOURWAY HANDSHAKE, THIS WOULD BE A GOOD METHOD ALTHOUGH IT IS PRONE TO A MAN IN THE MIDDLE ATTACK
	     *
	     */
	    if(bfourway){

		  String tmp = "0: Alice wants to send the message: " + plaintext + " to Bob";
	    logtext += tmp + "\r\n\n\n";

		logtext += "The fourway handshake seems to be flawless but it does have one\r\n vulnerability";
		logtext += "without a special protocol its prone to man in the middle attacks\r\n";
		logtext += "this is how it should work\r\n";
		logtext += "  1:  Alice uses a shiftstream transposition to send her message" + plaintext +" to bob, charlie intercpts it";

		String message = cryptography.shiftStream( alice.secretNum + (100/2), plaintext);

		bob.addLogEntry("1: " + message);
		alice.addLogEntry("1: " + message);
		charlie.addLogEntry("1: " + message);

		logtext += "2: When bob gets the message he doesnt understand he simply encrypts it with the shiftstream and his secret key as well\r\n";
		logtext += " then sends it back";

		message = cryptography.shiftStream( bob.secretNum + (100/2), message);

		bob.addLogEntry("2: " + message);
		alice.addLogEntry("2: " + message);
		charlie.addLogEntry("2: " + message);

		logtext += "3: Alice gets the encrypted message and takes her encryption off before sending it to back to Bob\r\n";

		message = cryptography.shiftStream( alice.secretNum + (100/2), message);

		bob.addLogEntry("3: " + message);
		alice.addLogEntry("3: " + message);
		charlie.addLogEntry("3: " + message);

		logtext += "4: Now all bob has to do is take his own encryption off";

		message = cryptography.shiftStream( bob.secretNum + (100/2), message);

		bob.addLogEntry("4: " + message);
		alice.addLogEntry("");
		charlie.addLogEntry("");
;


		bob.addLogEntry( "ENDOFLOG");
		alice.addLogEntry("ENDOFLOG");
		charlie.addLogEntry( "ENDOFLOG");
	    }

	}catch( Exception e){

	    return "error in protocol.startDemo(), make sure options are correct";
	}

	return logtext;
    }


    //listener for radio button selection, shows appopriate
    //options boxes and manages logic for selections, very simple
    //and goes against the dry principle - but thats swing
    public void itemStateChanged(ItemEvent e) {

	if (e.getStateChange() == ItemEvent.SELECTED) {

	    if( rsaInsecure.isSelected()){

		System.out.println( "weak utf rsa method selected");
		brsaInsecure = true;
		brsaSecure = false;
		shiftpanel.setVisible( true);

		bfourway = false;
		bshift = false;
		securepanel.setVisible( false);
		shiftpanel.setVisible( false);
		btransport = false;
		bauth = false;
	    }

	    if( rsaSecure.isSelected()){

		System.out.println( "Strong rsa method selected");
		brsaSecure = true;
		securepanel.setVisible( true);

		brsaInsecure = false;
		bshift = false;
		shiftpanel.setVisible( false);
		bfourway = false;
		btransport = false;
		bauth = false;
	    }

	    if( shift.isSelected()){

		System.out.println( "shift cipher method");
		bshift = true;

		brsaInsecure = false;
		brsaSecure = false;
		securepanel.setVisible( false);
		shiftpanel.setVisible( true);
		bfourway = false;
		btransport = false;
		bauth = false;
	    }

	    if( fourway.isSelected()){

		System.out.println( "The fourway handshake");
		bfourway = true;

		brsaInsecure = false;
		brsaSecure = false;
		securepanel.setVisible( false);
		shiftpanel.setVisible( false);
		btransport = false;
		bshift = false;
		bauth = false;
	    }

	     if( transport.isSelected()){

		System.out.println( "The Transposition Cipher\n");
		btransport = true;

		bfourway = false;
		brsaInsecure = false;
		brsaSecure = false;
		securepanel.setVisible( false);
		shiftpanel.setVisible( false);
		bshift = false;
		bauth = false;
	    }

	     //something like the pki protocol anyway
	     if( authpad.isSelected()){

		System.out.println( "OSCP AUTHENTICATION METHOD");
		bauth = true;

		bfourway = false;
		brsaInsecure = false;
		brsaSecure = false;
		securepanel.setVisible( false);
		shiftpanel.setVisible( false);
		bshift = false;
	     }
	}
    }
}
