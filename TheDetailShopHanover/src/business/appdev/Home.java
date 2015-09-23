package business.appdev;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;

import business.databaseconn.SQLConn;

public class Home extends JFrame implements ActionListener
{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private SQLConn conn;

		// Creates the Application Home Page
	    public Home()
	    {
	  
	    	// Create menubar
	        JMenuBar menuBar = new JMenuBar();
	        setJMenuBar(menuBar);
	        
	        // Create menus
	        JMenu Connect = new JMenu("Connect");
	        JMenu Edit = new JMenu("Edit");
	        JMenu Remove = new JMenu("Remove");
	        JMenu Add = new JMenu("Add");
	        
	        // Add menus
	        menuBar.add(Connect);
	        menuBar.add(Add);
	        menuBar.add(Remove);
	        menuBar.add(Edit);
	    	
	        // Add actionListener on Connect Button
	        Connect.addActionListener(this);
	   
	       
	    }
	    
	    public static void main(String[] args) 
	 	{    
	        Home h = new Home();
	        //make sure the program exits when the frame closes
	        h.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
	        h.setTitle("The Detail Shop Hanover, LLC.");
	        h.setSize(800,600);
	        h.setVisible(true);
	    }

		@Override
		public void actionPerformed(ActionEvent e) {
			conn = new SQLConn();
			// Print the connection
			System.out.println("The connection " + conn + " has been established");
		}
}
