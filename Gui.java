import javax.swing.*;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;


public class Gui extends JPanel{

   
    JTextArea aliceField, bobField, charlieField;
    JButton send;
  

    public Gui() {
      
	aliceField = new JTextArea("Alice");
	bobField = new JTextArea("Bob");
	charlieField = new JTextArea("Charlie");
	send = new JButton("SEND");
	add( aliceField);
	add( bobField);
	add( charlieField);
	add( send);
    
	JFrame frame = new JFrame("If I can be seen then well done");
	frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	frame.setContentPane(this);
	frame.pack();
	frame.setVisible(true);

   
    }
  

 

    public static void main(String args[]) {
      
	new Gui();
    }
}
