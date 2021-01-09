package UI;
import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.sql.*;
import java.util.*;
public class Signup extends JFrame{
	JLabel name = new JLabel("name");
	JLabel id = new JLabel("ID");
	JLabel pw = new JLabel("PW");
	JTextField namee = new JTextField(20);
	JTextField ide = new JTextField(20);
	JPasswordField pwe = new JPasswordField(20);
	JLabel birth = new JLabel("Birth Date");
	JComboBox<Integer>year;
	JComboBox<Integer>month;
	JComboBox<Integer>day;
	Vector<Integer>yearr = new Vector<Integer>();
	Vector<Integer>monthh = new Vector<Integer>();
	Vector<Integer>dayy = new Vector<Integer>();
	JButton signup = new JButton("Sign Up");
	JButton cancel = new JButton("Cancel");
	String yea;
	String mon;
	public Signup() {
		setTitle("Sign Up");
		setComponent();
		setLayout(new GridLayout(5,1));
		JPanel a = new JPanel();
		a.add(name);a.add(namee);
		JPanel b = new JPanel();
		b.add(id);b.add(ide);
		JPanel c= new JPanel();
		c.add(pw);c.add(pwe);
		JPanel d = new JPanel();
		d.add(birth);d.add(year);d.add(month);d.add(day);
		JPanel e = new JPanel();
		e.add(signup);e.add(cancel);
		add(a);add(b);add(c);add(d);add(e);
		setSize(430,250);
		setVisible(true);
	}
	class JPassWordFieldLimit extends PlainDocument{
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
	public void setComponent() {
		pwe.setDocument(new JPassWordFieldLimit(4));
		int yearInt = Calendar.getInstance().get(Calendar.YEAR);
		for(int i=1900;i<=yearInt;i++) {
			yearr.add(i);
		}
		for(int i=1;i<13;i++) {
			monthh.add(i);
		}
		signup.addActionListener(new MyActionListener());
		cancel.addActionListener(new MyActionListener());
		year = new JComboBox<Integer>(yearr);
		month = new JComboBox<Integer>(monthh);
		day = new JComboBox<Integer>(dayy);
		
		month.addActionListener(new ActionListener() {
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				setDay();
			}
		});
	}
	public void setDay() {
		int ye = (int) year.getSelectedItem();
		int mo = (int) month.getSelectedItem();
		dayy.clear();
		revalidate();
		if(((ye%4==0 && (ye%100 !=0 || ye%400==0)))&&mo==2) {
			for(int i=1;i<30;i++) {
				dayy.add(i);
			}
		}else if(mo == 1 || mo == 3 || mo == 5|| mo ==7 || mo ==8 || mo ==10 || mo ==12) {
			for(int i=1;i<32;i++) {
				dayy.add(i);
			}
		}else {
			for(int i=1;i<31;i++) {
				dayy.add(i);
			}
		}
		revalidate();
		System.out.println("revalidate");
	}
	
	class MyActionListener implements ActionListener{

		@Override
		public void actionPerformed(ActionEvent e) {
			if(e.getActionCommand().equals("Cancel")) {
				dispose();
			}else {
				Vector<String>confi = new Vector<String>();
				Connection conn = DB.Connect.makeConnection("coffee");
				try {
					Statement st = conn.createStatement();
					ResultSet re = st.executeQuery("select u_id from user");
					while(re.next()) {
						confi.add(re.getString("u_id"));
					}
				} catch (SQLException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
				if(namee.getText().equals("")||ide.getText().equals("")||pwe.getPassword().length==0||day.getSelectedItem()==null) {
					JOptionPane.showMessageDialog(null, "Missing Something","Message",JOptionPane.ERROR_MESSAGE);
				}else {
					int con = -1;
					for(int i=0;i<confi.size();i++) {
						if(ide.getText().equals(confi.elementAt(i))) {
							con =1;
						}
					}
					if(con == 1) {
						JOptionPane.showMessageDialog(null, "ID exists","Message",JOptionPane.ERROR_MESSAGE);
					}else {
						Connection co = DB.Connect.makeConnection("coffee");
						try {
							PreparedStatement psmt = co.prepareStatement("insert into user(u_id,u_pw,u_name,u_bd,u_point,u_grade) values(?,?,?,?,?,?)");
							psmt.setString(1, ide.getText());psmt.setString(2,new String(pwe.getPassword()));psmt.setString(3, namee.getText());
							int y = (int) year.getSelectedItem();
							int m = (int) month.getSelectedItem();
							int d = (int) day.getSelectedItem();
							psmt.setString(4, y+"-"+m+"-"+d);
							psmt.setString(5,"0");
							psmt.setString(6, "일반");
							psmt.executeUpdate();
							dispose();
							JOptionPane.showMessageDialog(null, "Registration Complete","Message",JOptionPane.INFORMATION_MESSAGE);
						} catch (SQLException e1) {
							// TODO Auto-generated catch block
							e1.printStackTrace();
						}
						
					}
				}
			}
			
		}
		
	}

//	public static void main(String[] args) {
//		// TODO Auto-generated method stub
//		new Signup();
//	}

}
