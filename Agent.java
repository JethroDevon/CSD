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
    ArrayList<String> agentlog = new ArrayList<String>();
    int arrayPos = 0;
    
    public Agent( String _agentname){

	setLayout(new BoxLayout( this, BoxLayout.PAGE_AXIS));
	agentname = _agentname;
	agentField = new JTextArea( 20, 20);
	agentField.setLineWrap(true);
	agentField.setWrapStyleWord(true);
	label = new TitledBorder( _agentname);
	agentField.setBorder( label);
	this.setBorder(BorderFactory
			.createEmptyBorder(10,10,10,10));
	JScrollPane scrollpane = new JScrollPane( agentField);
     	add( scrollpane);
        this.setBorder( this.getBorder());
        this.setBorder(BorderFactory.createMatteBorder(1, 1, 1, 1, Color.BLACK));

    }

    //add log entry to stack
    public void addLogEntry( String _ale){

	agentlog.add( _ale);
    }

    //show entry in log and iterate by one
    public void nextLog(){

	
	agentField.setText( agentField.getText() + agentlog.get(arrayPos) + "\r\n");

	if( arrayPos < agentlog.size() - 1)
	    arrayPos++;
    }
}



