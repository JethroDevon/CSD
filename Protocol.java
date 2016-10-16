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



public class Protocol extends JPanel implements ItemListener{

    JRadioButton onepad, shift, transport, fourway, rsa, none, random; 
    ButtonGroup crypt_methods, crypt_sys;
    JFormattedTextField shift_val;
    JTextField transkey;
    JPanel methpanel, syspanel, shiftpanel, transpanel;
    JLabel shiftlabel;
    Cryptography cryptography = new Cryptography();

    //many bool variables for all the options, some default true to match button
    boolean bonepad, bshift = true, btransport, bfourway, brsa, bnone = true, brandom = false;


    
    public Protocol(){
	
	//Many swing options to control the way in which the program will work
	//Gui displays rbuttons when initialised in the Gui class
	methpanel = new JPanel();
	syspanel = new JPanel();
	transpanel = new JPanel();
	shiftpanel = new JPanel( new BorderLayout());
	
	//set the borders for the two button groups
	methpanel.setBorder(BorderFactory.createTitledBorder( "Cryptographic Method"));
	syspanel.setBorder(BorderFactory.createTitledBorder( "Cryptographic System"));
	shiftpanel.setBorder(BorderFactory.createTitledBorder( "Shift Cipher Options"));
	transpanel.setBorder(BorderFactory.createTitledBorder( "Transposition Key"));

	//initialise all the radio buttons to be used
        onepad = new JRadioButton("One Time Pad");
        shift = new JRadioButton("Shift/Substitution");
        transport = new JRadioButton("Transposition");
        fourway = new JRadioButton("Four Way Handshake");
        rsa = new JRadioButton("RSA");
	none = new JRadioButton("none");
	random = new JRadioButton("Random");
	shiftlabel = new JLabel("Shift Value ");
	transkey = new JTextField("Default");
	shift_val = new JFormattedTextField( new Integer( 1));

	//individual radio button logic
	shift.addItemListener( this);
	transport.addItemListener( this);
	transpanel.setVisible(false);
	shift.setSelected( true);
	none.setSelected( true);

	//adding components to appropriate panels
	methpanel.add( onepad);
	methpanel.add( shift);
        methpanel.add( transport);
        syspanel.add( fourway);
        syspanel.add( rsa);
	syspanel.add( none);
	shiftpanel.add( shiftlabel, BorderLayout.EAST);
	shiftpanel.add( random, BorderLayout.SOUTH);
	shiftpanel.add( shift_val, BorderLayout.CENTER);
	transpanel.add( transkey);
	
	crypt_methods = new ButtonGroup();
	crypt_methods.add( onepad);
	crypt_methods.add( shift);
	crypt_methods.add( transport);

	crypt_sys = new ButtonGroup();
	crypt_sys.add( fourway);
	crypt_sys.add( rsa);
	crypt_sys.add( none);

	//adding panels to this functions main JPanel
	add( methpanel);
	add( syspanel);
	add( shiftpanel);
	add( transpanel);
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
	    if( bshift && !brandom){

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
	     *      RSA METHOD WHERE EACH ASCII CHARACTER HAS BEEN ENCRYPTED
	     *
	     */

	    if( brsa){
	    
		logtext += "1: Alice first makes a request from Bob for his public key, this is public so it is okay if Charlie sees it\r\n";
		alice.addLogEntry( "1: public key please ;" + bob.getPublicKey() + ". mod value ;" + bob.getPublicKeyMod());
		charlie.addLogEntry( bob.getPublicKey() + "mod value " + bob.getPublicKeyMod());
		bob.addLogEntry("");
		
		logtext += "2: Alice then encrypts the message she wishes to send\r\n";
		int[] cipherintarray = cryptography.asciiEncryptRSA( plaintext, bob.getPublicKey(), bob.getPublicKeyMod());
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
		bob.addLogEntry("4: " + cryptography.asciiDecryptRSA( cipherintarray, bob.getPrivateKey(), bob.getPrivateKeyMod()));
		charlie.addLogEntry("");
		alice.addLogEntry("");
	    }

	}catch( Exception e){

	    return "error in protocol.startDemo(), make sure options are correct";
	}
	
	return logtext;
    }


    //listener for radio button selection, shows appopriate
    //options boxes
    public void itemStateChanged(ItemEvent e) {
	if (e.getStateChange() == ItemEvent.SELECTED) {

	    if( none.isSelected()){
		if( transport.isSelected()){
		
		    transpanel.setVisible(true);
		    btransport = true;	
		}
		if( shift.isSelected()){

		    shiftpanel.setVisible( true);
		    bshift = true;	
		}
	    }else {

		transpanel.setVisible(false);
		shiftpanel.setVisible( false);
		methpanel.setVisible(true);
		btransport = false;
		bshift = false;
		brsa = false;
	    }

	    if( rsa.isSelected()){

		brsa = true;
		System.out.println("rsa true");
	    }
	}
    }

}
