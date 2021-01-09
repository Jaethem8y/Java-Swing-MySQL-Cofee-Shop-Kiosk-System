package UI;
import javax.swing.*;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.text.DateFormat;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.Date;
public class Admin extends JFrame {
	JButton register = new JButton("Register");
	JButton manage = new JButton("Manage");
	JButton logout = new JButton("LogOut");
	public Admin() {
		setTitle("Admin Menu");
		setLayout(new GridLayout(3,1));
		add(register);
		add(manage);
		add(logout);
		register.addActionListener(new MyActionListener());
		manage.addActionListener(new MyActionListener());
		logout.addActionListener(new MyActionListener());

		setSize(300,300);
		setVisible(true);
		
	}
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			// TODO Auto-generated method stub
			if(e.getActionCommand().equals("Register")) {
				dispose();
				new Register();
			}else if(e.getActionCommand().equals("Manage")) {
				dispose();
				new Manage();
			}else {
				dispose();
				new Main();
			}
		}
		
	}
	
//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Admin();
//		
//	}

}
