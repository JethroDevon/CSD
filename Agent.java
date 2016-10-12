import javax.swing.*;
import javax.swing.border.TitledBorder;
import java.awt.event.*;
import javax.imageio.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;
import java.io.IOException;



public class Agent extends JPanel{

    String agentname;
    JTextArea agentField;
    TitledBorder label;
  
    public Agent( String _agentname){

	setLayout(new BoxLayout( this, BoxLayout.PAGE_AXIS));
	agentname = _agentname;
	agentField = new JTextArea( 20, 10);
	label = new TitledBorder( _agentname);
	agentField.setBorder( label);
	this.setBorder(BorderFactory
			.createEmptyBorder(10,10,10,10));
     	add( agentField);
        this.setBorder( this.getBorder());
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

    }
}



