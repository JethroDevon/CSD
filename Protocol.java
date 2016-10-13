/*
*
* This protocol class manages the simulations and will contain scripts for the 
* the three agents, the steps demonstrated will rely on user input.
*  
*/

import javax.swing.*;
import javax.swing.ButtonGroup;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;



public class Protocol extends JPanel{

    JButton onepad, shift, transport, fourway, rsa; 
    ButtonGroup crypt_methods, crypt_sys;
    JPanel methpanel, syspanel;
    
    public Protocol(){
	
	//a few radio buttons will be displayed in the GUI and added to the frame in
	//Gui class
	setLayout( new BorderLayout());
    

	methpanel = new JPanel();
	syspanel = new JPanel();
	
	crypt_methods = new ButtonGroup();
	crypt_methods.add( onepad);
	crypt_methods.add( shift);
	crypt_methods.add( transport);

	crypt_sys = new ButtonGroup();
	crypt_sys.add( fourway);
	crypt_sys.add( rsa);

	//set the borders for the two button groups
	methpanel.setBorder(BorderFactory.createTitledBorder( "Cryptographic Method"));
	syspanel.setBorder(BorderFactory.createTitledBorder( "Cryptographic System"));
	methpanel.add( onepad);
	methpanel.add( shift);
	methpanel.add( transport);
	syspanel.add( fourway);
	syspanel.add( rsa);

	this.add( methpanel);
	this.add( syspanel);
    }
}

