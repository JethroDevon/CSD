import javax.swing.UIManager;
import javax.swing.*;
import javax.swing.text.*;
 
import java.awt.*;              //for layout managers and more
import java.awt.event.*;        //for action events
 
import java.net.URL;
import java.io.IOException;

public class Menu{

    public static void main(String args[]) {

	try{
	    
	    //Schedule a job for the event dispatching thread:
	    //creating and showing this application's GUI.
	    SwingUtilities.invokeLater(new Runnable() {
		
		    public void run() {
		
			//Turn off metal's use of bold fonts
			UIManager.put("swing.boldMetal", Boolean.FALSE);
		

			new Gui();
		    }
		});
	} catch (Exception e) {

	    
	}

    }
}
