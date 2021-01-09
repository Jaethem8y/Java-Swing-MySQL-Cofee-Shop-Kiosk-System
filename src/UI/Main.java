package UI;

import javax.swing.*;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.PlainDocument;

import UI.Signup.JPassWordFieldLimit;

import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;


public class Main extends JFrame{
	JLabel starbox = new JLabel("STARBOX");
	JButton login = new JButton("Login");
	JButton signup = new JButton("SignUp");
	JButton exit = new JButton("Exit");
	JLabel id = new JLabel("ID:");
	JLabel pw = new JLabel("PW:");
	JTextField ide = new JTextField(10);
	JPasswordField pwe = new JPasswordField(10);
	Vector<Vector<String>> user = new Vector<Vector<String>>(); 
	public Main() {
		setTitle("Log In");
		setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		setComponent();
		JPanel top = new JPanel();
		top.add(starbox);
		JPanel left = new JPanel();
		left.setLayout(new BorderLayout());
		JPanel left1 = new JPanel();
		left1.add(id);
		left1.add(ide);
		JPanel left2 = new JPanel();
		left2.add(pw);
		left2.add(pwe);
		left.add(left1,BorderLayout.NORTH);
		left.add(left2,BorderLayout.SOUTH);
		JPanel center = new JPanel();
		center.add(left);
		center.add(login);
		login.setPreferredSize(new Dimension(75,75));
		JPanel south = new JPanel();
		south.add(signup);
		south.add(exit);
		add(top,BorderLayout.NORTH);
		add(center,BorderLayout.CENTER);
		add(south,BorderLayout.SOUTH);
		setSize(300,200);
		setVisible(true);
		

		
	}
	public void setComponent() {
		starbox.setFont(new Font("Ariel",Font.BOLD,40));
		login.addActionListener(new MyActionListener());
		signup.addActionListener(new MyActionListener());
		exit.addActionListener(new MyActionListener());
		pwe.setDocument(new JPassWordFieldLimit(4));
		Connection conn = DB.Connect.makeConnection("coffee");
		Statement st;
		try {
			st = conn.createStatement();
			st.executeUpdate("delete from shopping");
			st.executeUpdate("alter table shopping auto_increment = 1");

		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	class MyActionListener implements ActionListener{
		
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Exit")) {
				dispose();
			}else if(e.getActionCommand().equals("SignUp")) {
				new Signup();
			}else {
				String id = ide.getText();
				String pw = new String(pwe.getPassword());
				if(id.equals("") || pw.equals("")) {
					JOptionPane.showMessageDialog(null, "Fill all the blocks please!","Message",JOptionPane.ERROR_MESSAGE);
				}else if(id.equals("admin")&&pw.equals("1234")) {
					dispose();
					new Admin();
				}
				else {
					Connection conn = DB.Connect.makeConnection("coffee");
					int login = -1;
					try {
						Statement st = conn.createStatement();
						ResultSet re = st.executeQuery("select * from user");
						while(re.next()) {
							Vector<String>a = new Vector<String>();
							a.add(re.getString
									("u_no"));
							a.add(re.getString("u_id"));
							a.add(re.getString("u_pw"));
							a.add(re.getString("u_name"));
							a.add(re.getString("u_bd"));
							a.add(re.getString("u_point"));
							a.add(re.getString("u_grade"));
							user.add(a);
						}
						for(int i=0;i<user.size();i++) {
							if(user.elementAt(i).elementAt(1).equals(id)&&user.elementAt(i).elementAt(2).equals(pw)) {
								System.out.println("okay");
								login = 1;
								dispose();
								String idee = ide.getText();
								new Starbox(idee);
							}
						}
						if(login==-1) {
							JOptionPane.showMessageDialog(null, "Wrong Information Try Again!","Message",JOptionPane.ERROR_MESSAGE);
						}
					} catch (SQLException e1) {
						// TODO Auto-generated catch block
						e1.printStackTrace();
					}
				}
				
			}
			
		}
		
	}class JPassWordFieldLimit extends PlainDocument{
		private int limit;
		JPassWordFieldLimit(int limit){
			super();
			this.limit = limit;
		}
		JPassWordFieldLimit(int limit, boolean upper){
			super();
			this.limit = limit;
		}
		public void insertString(int offset, String str, AttributeSet attr) {
			if(str == null) return;
			if((getLength()+str.length())<=limit) {
				try {
					super.insertString(offset, str, attr);
				} catch (BadLocationException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
	}

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		DB.Connect.insertTable();
		new Main();
	}

}
