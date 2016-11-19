import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;


//this class deals with input and displaying swing components,
//communication and approach to running the program with cryptography
//functions can be found in the protocol class
public class Gui extends JPanel implements ActionListener{

    TitledBorder plaintextTitle, consoleTitle;
    JTextArea plaintext, console;
    JButton send;
    JFrame frame;
    JPanel fitconsole, fitinput;
    static Agent alice = new Agent( "Alice");
    static Agent bob = new Agent( "Bob");
    static Agent charlie = new Agent( "Charlie");
    Protocol protocol = new Protocol();
    boolean sent = false;
        
    public Gui(){

	//creating a lot of swing components this
	//includes the agent objects that are initialised
	//here
	JPanel agents = new JPanel();
	JPanel protocolbuttons = new JPanel();

	frame = new JFrame();
	plaintext = new JTextArea();
	console = new JTextArea();	
	send = new JButton( "Start Demo");
	setLayout( new BoxLayout( this, BoxLayout.PAGE_AXIS));

	send.addActionListener( this);

	frame.setContentPane( this);
	
	agents.add( alice);
	agents.add( charlie);
	agents.add( bob);
	protocolbuttons.add( protocol);

	plaintextTitle = new TitledBorder( "Input Plain Text");
	consoleTitle = new TitledBorder( "Explanation Log");

	fitconsole = new JPanel();
        fitconsole.setLayout( new BoxLayout( fitconsole, BoxLayout.X_AXIS));
	fitconsole.setPreferredSize( new Dimension( 800, 400));
        fitconsole.setMaximumSize( new Dimension( 800, 600));
	JScrollPane scrollpane1 = new JScrollPane( console);
	fitconsole.add( scrollpane1);

	fitinput = new JPanel();
	fitinput.setLayout( new BoxLayout( fitinput, BoxLayout.X_AXIS));
	fitinput.setPreferredSize( new Dimension( 750, 80));
        fitinput.setMaximumSize( new Dimension( 750, 100));
	JScrollPane scrollpane = new JScrollPane( plaintext);
	fitinput.add( scrollpane);
	fitinput.add( send);
	
        fitinput.setBorder( plaintextTitle);
        fitconsole.setBorder( consoleTitle);

	frame.setSize( new Dimension( 1012, 954));
	frame.setResizable( false);
	frame.add( fitinput);
	frame.add( agents);
	frame.add( fitconsole);
	frame.add( protocolbuttons);
	frame.pack();
	frame.setVisible( true);


    }

    //listener for button presses
    public void actionPerformed(ActionEvent e) {

	if( e.getSource() == send){

	    try{

	    if(!send.getText().equals("quit")){
		if( sent == false){
		
		    console.setText( protocol.startDemo( plaintext.getText(), alice, charlie, bob));
		    alice.nextLog();
		    charlie.nextLog();
		    bob.nextLog();
		    send.setText( "click next");
		    sent = true;
		}else{

		    alice.nextLog();
		    charlie.nextLog();
		    bob.nextLog();

		    if( alice.endofLog() && charlie.endofLog() && bob.endofLog()){

			send.setText( "quit");
		    }
		}
		
	    //then reset program
	    }else{

	        System.exit(0);
	    }
	    }catch( Exception q){

		System.out.println( " Select a method");
	    }
	}
	        	
    }

}

