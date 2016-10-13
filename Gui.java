import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;
import java.awt.Dimension;
import javax.swing.border.TitledBorder;

public class Gui extends JPanel{

    TitledBorder plaintextTitle, consoleTitle;
    JTextArea plaintext, console;
    JButton send, next;
    JFrame frame;
    Agent alice = new Agent( "Alice");
    Agent bob = new Agent( "Bob");
    Agent charlie = new Agent( "Charlie");
    // Protocol protocol = new Protocol();
    
    public Gui() {

	//creating a lot of swing components this
	//includes the agent objects that are initialised
	//here
	JPanel agents = new JPanel();
	//	JPanel protocolbuttons = new JPanel();

	frame = new JFrame();
	plaintext = new JTextArea();
	console = new JTextArea();	
	next = new JButton( "Next Step");
	send = new JButton( "Send");
	setLayout(new BoxLayout( this, BoxLayout.PAGE_AXIS));

	        
	frame.setContentPane( this);
	
	agents.add( alice);
	agents.add( charlie);
	agents.add( bob);
	//protocolbuttons.add( protocol);

	plaintextTitle = new TitledBorder( "Input Plain Text");
	consoleTitle = new TitledBorder( "Console Log");

        plaintext.setBorder( plaintextTitle);
        console.setBorder( consoleTitle);

	frame.setSize( new Dimension( 812, 584));
	frame.setResizable( false);
	JScrollPane scrollpane = new JScrollPane( plaintext);
	frame.add( scrollpane);
	frame.add( send);
	frame.add( agents);
	frame.add( next);
	JScrollPane scrollpane1 = new JScrollPane( console);
	frame.add( scrollpane1);
	//	frame.add( protocolbuttons);
	frame.pack();
	frame.setVisible( true);
	
    }
}


