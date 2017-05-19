import java.sql.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

public class ConnectionTest {
	
	private JFrame mainFrame;
	private static JLabel status;
	private JButton abutton;
	private JButton button;
	
	
	public ConnectionTest() {
		drawGUI();
	}

	private void drawGUI() {
		mainFrame = new JFrame();
		mainFrame.setSize(400,300);
		mainFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		status = new JLabel("",JLabel.CENTER);
		
		abutton = new JButton("aa");
		button = new JButton("bb");
		
		mainFrame.getContentPane().add(BorderLayout.SOUTH, button);
		mainFrame.add(status);
		mainFrame.getContentPane().add(BorderLayout.EAST, abutton);
		
		abutton.setActionCommand("a");
		button.setActionCommand("b");
		abutton.addActionListener(new ActionListener() {

			@Override
			public void actionPerformed(ActionEvent e) {
				status.setText("a is pressed");
				
			}
			
		});
		//abutton.addActionListener(new ButtonClickListener());
		button.addActionListener(new ButtonClickListener());
		
		mainFrame.setVisible(true);

	}
	
	
	public static void main(String[] args) {
		try {
			DriverManager.registerDriver(new oracle.jdbc.driver.OracleDriver());
			Connection con = DriverManager.getConnection("jdbc:oracle:thin:@localhost:1522:ug", "ora_v7y8","a49865132");
			Statement st = con.createStatement();
			ResultSet rs = st.executeQuery("select * from generaluser");
			while(rs.next()) {
				System.out.println(rs.getString(1));
				
			}
			con.close();
		}
		catch(Exception e) {
			System.out.println(e);
		}
		//SportTicketSelling1 sportTicketSelling = new SportTicketSelling1();
		
		
		
	} 
	
	private class ButtonClickListener implements ActionListener {

		@Override
		public void actionPerformed(ActionEvent e) {
			String command = e.getActionCommand();
			if (command.equals("a")) {
				status.setText("a is pressed");
			} else if (command.equals("b")) {
				status.setText("b is pressed");
			}
		}
		
	}

}
