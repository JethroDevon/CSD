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

    TitledBorder title;
    JTextField plaintext, console;
    JButton send;
    JFrame frame;
    Agent alice = new Agent( "alice");
    Agent bob = new Agent( "bob");
    Agent charlie = new Agent( "charlie");
    
    public Gui() {
   
	JPanel agents = new JPanel();
	frame = new JFrame();
	plaintext = new JTextField();
	plaintext.setPreferredSize( new Dimension( 100,50));
	send = new JButton( "SEND");	 
	setLayout(new BoxLayout( this, BoxLayout.PAGE_AXIS));

        title = new TitledBorder( "Input plain text");
	plaintext.setBorder( title);	
	frame.setContentPane(this);
	
	agents.add( alice);
	agents.add( charlie);
	agents.add( bob);
	
	plaintext.setColumns(10);
	frame.setSize( 512, 384);
	frame.setResizable( false);
	frame.add( plaintext);
	frame.add( send);	
	frame.add( agents);	
	frame.pack();
	frame.setVisible( true);
	
    }
}


