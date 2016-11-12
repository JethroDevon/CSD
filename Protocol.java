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

    JRadioButton onepad, shift, transport, fourway, rsaInsecure, rsaSecure, random; 
    ButtonGroup crypt_methods;
    JFormattedTextField shift_val, rsaHybridKey;
    JTextField transkey;
    JPanel methpanel, shiftpanel, transpanel, securepanel;
    JLabel shiftlabel, keylabel;
    Cryptography cryptography = new Cryptography();

    //many bool variables for all the options, some default true to match button
    boolean bone, brsaInsecure, brsaSecure, bshift, btransport, brandom;
    
    
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

	//initialise all the radio buttons to be used
        onepad = new JRadioButton("One Time Pad");
        shift = new JRadioButton("Shift/Substitution");
        transport = new JRadioButton("Transposition");
        fourway = new JRadioButton("Four Way Handshake");
        rsaInsecure = new JRadioButton("RSA - Insecure");
	rsaSecure = new JRadioButton("RSA - Secure");
	random = new JRadioButton("Random");
	shiftlabel = new JLabel("Shift Value ");
	keylabel = new JLabel( "Secret Key");
	rsaHybridKey = new JFormattedTextField( new Integer( 1));
	transkey = new JFormattedTextField( new Integer( 1));
	shift_val = new JFormattedTextField( new Integer( 1));

	//individual radio button logic
	shift.addItemListener( this);
	transport.addItemListener( this);
	rsaInsecure.addItemListener( this);
	rsaSecure.addItemListener( this);
	
	//adding components to appropriate panels
	methpanel.add( onepad);
	methpanel.add( shift);
        methpanel.add( transport);
        methpanel.add( fourway);
        methpanel.add( rsaInsecure);
	methpanel.add( rsaSecure);
	shiftpanel.add( shiftlabel, BorderLayout.EAST);
	shiftpanel.add( random, BorderLayout.SOUTH);
	shiftpanel.add( shift_val, BorderLayout.CENTER);
	transpanel.add( transkey);
	securepanel.add( keylabel, BorderLayout.CENTER);
	securepanel.add( rsaHybridKey, BorderLayout.SOUTH);

	shiftpanel.setVisible( false);
	transpanel.setVisible( false);
	securepanel.setVisible( false);

	crypt_methods = new ButtonGroup();
	crypt_methods.add( onepad);
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
	String logtext = "Starting demo : \r\n";
	  
	try{
	  
	    String tmp = "0: Alice wants to send the message: " + plaintext + " to Bob";
	    logtext += tmp + "\r\n";



	    /*
	     *
	     *   SHIFT CIPHER METHOD WITH PRE SHARED KEYS
	     *
	     */
	 
	    //if the shift cipher has been selected and its not random substitution and theres no
	    //crypto system present then a message is just being sent between two parties
	    if( bshift){

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
	    }
	



	    /*
	     *
	     *      BAD RSA METHOD WHERE EACH UTF CHARACTER HAS BEEN ENCRYPTED - THAT IS STUPID (made it to test rsa maths tho)
	     *
	     */

	     if( brsaInsecure){

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
	    }

	    if( brsaSecure){
	  
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

		logtext += "5: lastly, bob uses that key he decrypted to also decrypt the other message he was sent";
		alice.addLogEntry("");
		charlie.addLogEntry( "");
		
		String plaintext2 = cryptography.xorBytes( decryptedkey, ciphertext);

		bob.addLogEntry( "5: using key to decrypt encrypted messages : " + plaintext2);

		
	    }

       

	    //if(el_gamal))

	    //if(fourway)

	}catch( Exception e){

	    return "error in protocol.startDemo(), make sure options are correct";
	}
	
	return logtext;
    }


    //listener for radio button selection, shows appopriate
    //options boxes and manages logic for selections, very simple
    //and goes against the dry principle - but thats swing
    public void itemStateChanged(ItemEvent e) {

	//
	if (e.getStateChange() == ItemEvent.SELECTED) {

	    if( rsaInsecure.isSelected()){

		System.out.println( "weak utf rsa method selected");
		brsaInsecure = true;
		brsaSecure = false;
		shiftpanel.setVisible( true);
		
		bshift = false;
		securepanel.setVisible( false);
		shiftpanel.setVisible( false);
	    }

	    if( rsaSecure.isSelected()){

		System.out.println( "Strong rsa method selected");
		brsaSecure = true;
		securepanel.setVisible( true);
		
		brsaInsecure = false;
		bshift = false;
		shiftpanel.setVisible( false);

	    }
	    
	    if( shift.isSelected()){

		System.out.println( "shift cipher method");
		bshift = true;
		
		
		brsaInsecure = false;
		brsaSecure = false;
		securepanel.setVisible( false);
		shiftpanel.setVisible( true);
	    }

	}
    }
}
